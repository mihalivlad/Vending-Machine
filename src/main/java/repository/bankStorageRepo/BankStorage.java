package repository.bankStorageRepo;

import exception.EmptyStackMoneyException;
import exception.FullStackMoneyException;
import exception.NotFullPaidException;
import exception.NotSufficientChangeException;
import model.money.AbstractMoney;
import model.money.bill.Bill1;
import model.money.bill.Bill10;
import model.money.bill.Bill5;
import model.money.coin.Coin10;
import model.money.coin.Coin5;
import model.money.coin.Coin50;

import java.util.*;

public class BankStorage implements IBankStorageUser, IBankStorageAdmin {
    //static constants
    public static final int CAPACITY = 100;

    //private variables
    private float balance;
    private BankStorageDB bankStorageDB;
    private List<Stack<AbstractMoney>> bankStorageList;
    private  ChangeMechanism changeMechanism;

    enum MoneyType {
        COIN_5,
        COIN_10,
        COIN_50,
        BILL_1,
        BILL_5,
        BILL_10
    }

    public void init(){
        balance = 0.0f;

        bankStorageDB = new BankStorageDB();
        bankStorageList = new ArrayList<>();
        for(int index = 0; index < MoneyType.values().length; index++) {
            bankStorageList.add(new Stack<>());
        }
        changeMechanism = new ChangeMechanism(this);
        changeMechanism.init();
        bankStorageList = bankStorageDB.load();

    }

    //implementation for UserBankStorage methods
    public float getBalance() {
        return balance;
    }

    public void subtractBalance(float subtractValue) {
        float newBalance = balance - subtractValue;
        if(newBalance < 0){
            throw new NotFullPaidException();
        }
        balance = newBalance;
        if(newBalance == 0){
            changeMechanism.init();
        }else{
            List<Integer> copy = new ArrayList<>(changeMechanism.getFrequencyInitialMoney());
            try {
                changeMechanism.withDrawAlgorithm(bankStorageList);
            }catch (NotSufficientChangeException ex){
                balance+=subtractValue;
                throw ex;
            }
            changeMechanism.setFrequencyInitialMoney(copy);
            changeMechanism.setRefund(false);
        }

    }

    public void addMoneyUser(AbstractMoney abstractMoney) {
        addMoney(abstractMoney);
        changeMechanism.increment(matchMoney(abstractMoney));
        balance += abstractMoney.toDollar();
    }

    public List<AbstractMoney> withdraw(){
        if(!changeMechanism.isRefund()){
            changeMechanism.withDrawAlgorithm(bankStorageList);
        }
        return changeMechanism.refunding();
    }

    //implementation for AdminBankStorage methods
    public void refill(int refillLevel, AbstractMoney abstractMoney) {
        if(refillLevel < 0 || refillLevel > CAPACITY){
            throw new IllegalArgumentException();
        }
        List<AbstractMoney> moneyTypeList = bankStorageList.get(matchMoney(abstractMoney).ordinal());
        int size = moneyTypeList.size();
        for (int i = size; i < refillLevel; i++) {
            try {
                addMoney(abstractMoney.getClass().getConstructor().newInstance());
            }catch (ReflectiveOperationException ex){
                System.out.println("this should never happen");
                System.out.println(ex.getMessage());
                System.exit(-1);
            }
        }
        for (int i = refillLevel; i < size; i++) {
            removeMoney(matchMoney(abstractMoney));
        }
    }

    public void refillAll(int refillLevel)  {
        refill(refillLevel, new Coin5());
        refill(refillLevel, new Coin10());
        refill(refillLevel, new Coin50());
        refill(refillLevel, new Bill1());
        refill(refillLevel, new Bill5());
        refill(refillLevel, new Bill10());
    }

    public void refillToFull(AbstractMoney abstractMoney) {
        refill(CAPACITY, abstractMoney);
    }

    public void refillToEmpty(AbstractMoney abstractMoney) {
        refill(0, abstractMoney);
    }

    public void refillToFullAll() {
        refillAll(CAPACITY);
    }

    public void refillToEmptyAll() {
        refillAll(0);
    }

    //private utilities functions
    private void addMoney(AbstractMoney abstractMoney) {
        MoneyType moneyType = matchMoney(abstractMoney);
        if(isFull(moneyType)){
            throw new FullStackMoneyException();
        }
        Stack<AbstractMoney> currentStack = bankStorageList.get(moneyType.ordinal());
        int id = bankStorageDB.insert(abstractMoney);
        abstractMoney.setMid(id);
        currentStack.push(abstractMoney);

    }

    private AbstractMoney removeMoney(MoneyType moneyType){
        if(isEmpty(moneyType)){
            throw new EmptyStackMoneyException();
        }
        Stack<AbstractMoney> currentStack = bankStorageList.get(moneyType.ordinal());
        AbstractMoney abstractMoney = currentStack.pop();
        bankStorageDB.delete(abstractMoney);
        return abstractMoney;

    }

    AbstractMoney removeMoneyUser(MoneyType moneyType){
        AbstractMoney abstractMoney = removeMoney(moneyType);
        balance-=abstractMoney.toDollar();
        return abstractMoney;
    }

    private boolean isEmpty(MoneyType moneyType){
        return bankStorageList.get(moneyType.ordinal()).isEmpty();
    }

    private boolean isFull(MoneyType moneyType){
        return bankStorageList.get(moneyType.ordinal()).size() == CAPACITY;
    }

    private MoneyType matchMoney(AbstractMoney abstractMoney){
        int value = abstractMoney.getValue();
        if(abstractMoney.getUnit().equals("dollar")) {
            if (value == 1) {
                return MoneyType.BILL_1;
            }
            if (value == 5) {
                return MoneyType.BILL_5;
            }
            if (value == 10) {
                return MoneyType.BILL_10;
            }
        }
        if(value == 5){
            return MoneyType.COIN_5;
        }
        if(value == 10){
            return MoneyType.COIN_10;
        }
        return MoneyType.COIN_50;
    }
}
