package repository.bankStorageRepo;

import exception.CustomMySQLException;
import jdbc.ConnectionFactory;
import model.money.AbstractMoney;
import model.money.bill.Bill1;
import model.money.bill.Bill10;
import model.money.bill.Bill5;
import model.money.coin.Coin10;
import model.money.coin.Coin5;
import model.money.coin.Coin50;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

class BankStorageDB {
    int insert(AbstractMoney abstractMoney){
        StringBuilder stringBuilder = new StringBuilder("INSERT INTO `money` (bill, valoare) VALUES (");
        if(abstractMoney.getUnit().equals("dollar")){
            stringBuilder.append("1, ");
        }else{
            stringBuilder.append("0, ");
        }
        stringBuilder.append(abstractMoney.getValue()).append(")");

        Connection connection = null;
        Statement statement = null;
        ResultSet resultSet = null;
        try{
            connection = ConnectionFactory.getConnection();
            statement = connection.createStatement();
            statement.executeUpdate(stringBuilder.toString(), Statement.RETURN_GENERATED_KEYS);
            resultSet = statement.getGeneratedKeys();
            if(resultSet.next()){
                return resultSet.getInt(1);
            }
        }catch (Exception e){
            System.out.println("MySQL server error for inserting money");
            throw new CustomMySQLException();
        }finally {
            ConnectionFactory.close(statement);
            ConnectionFactory.close(resultSet);
            ConnectionFactory.close(connection);
        }
        return -1;
    }

    void delete(AbstractMoney abstractMoney){
        StringBuilder stringBuilder = new StringBuilder("DELETE FROM `money` WHERE ");
        stringBuilder.append("mid = ").append(abstractMoney.getMid());
        if(abstractMoney.getUnit().equals("dollar")) {
            stringBuilder.append(" AND bill = 1");
        }else{
            stringBuilder.append(" AND bill = 0");
        }
        stringBuilder.append(" AND valoare = ").append(abstractMoney.getValue());

        try(Connection connection = ConnectionFactory.getConnection();
            Statement statement = connection.createStatement()){
            statement.execute(stringBuilder.toString());
        }catch (Exception e){
            System.out.println("MySQL server error for removing money");
            throw new CustomMySQLException();
        }
    }

    List<Stack<AbstractMoney>> load(){
        List<Stack<AbstractMoney>> abstractMoneyList = new ArrayList<>();
        for(int index = 0; index < BankStorage.MoneyType.values().length; index++) {
            abstractMoneyList.add(new Stack<>());
        }
        String sqlString = "SELECT * FROM `money`";
        try(Connection connection = ConnectionFactory.getConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(sqlString)){
            while(resultSet.next()){
                int value = resultSet.getInt("valoare");
                int id = resultSet.getInt("mid");
                boolean isBill = resultSet.getBoolean("bill");
                addMoneyObject(abstractMoneyList,value, id, isBill);
            }
            return abstractMoneyList;
        }catch (Exception e){
            System.out.println("MySQL server error for loading money");
            throw new CustomMySQLException();
        }
    }

    /*private void addMoneyObject(List<Stack<AbstractMoney>> abstractMoneyList, int value, int id, boolean isBill) {
        if(isBill){
            if(value == 1){
                abstractMoneyList.get(BankStorage.MoneyType.BILL_1.ordinal()).push(new Bill1(id));
            }else if(value == 5){
                abstractMoneyList.get(BankStorage.MoneyType.BILL_5.ordinal()).push(new Bill5(id));
            }else if(value == 10){
                abstractMoneyList.get(BankStorage.MoneyType.BILL_10.ordinal()).push(new Bill10(id));
            }
        }else{
            if(value == 5){
                abstractMoneyList.get(BankStorage.MoneyType.COIN_5.ordinal()).push(new Coin5(id));
            }else if(value == 10){
                abstractMoneyList.get(BankStorage.MoneyType.COIN_10.ordinal()).push(new Coin10(id));
            }else if(value == 50){
                abstractMoneyList.get(BankStorage.MoneyType.COIN_50.ordinal()).push(new Coin50(id));
            }
        }
    }*/

    private void addMoneyObject(List<Stack<AbstractMoney>> abstractMoneyList, int value, int id, boolean isBill) {
        switch (value){
            case 1:
                abstractMoneyList.get(BankStorage.MoneyType.BILL_1.ordinal()).push(new Bill1(id));
            case 5:
                abstractMoneyList.get((isBill?BankStorage.MoneyType.BILL_5:BankStorage.MoneyType.COIN_5).ordinal()).
                        push(isBill?new Bill5(id):new Coin5(id));
            case 10:
                abstractMoneyList.get((isBill?BankStorage.MoneyType.BILL_10:BankStorage.MoneyType.COIN_10).ordinal()).
                        push(isBill?new Bill10(id):new Coin10(id));
            case 50:
                abstractMoneyList.get(BankStorage.MoneyType.COIN_50.ordinal()).push(new Coin50(id));
            }
    }
}
