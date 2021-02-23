package controller;

import exception.FullStackMoneyException;
import exception.NotFullPaidException;
import exception.NotSufficientChangeException;
import exception.SoldOutException;
import model.item.*;
import model.money.AbstractMoney;
import model.money.bill.Bill1;
import model.money.bill.Bill10;
import model.money.bill.Bill5;
import model.money.coin.Coin10;
import model.money.coin.Coin5;
import model.money.coin.Coin50;
import repository.bankStorageRepo.BankStorage;
import repository.bankStorageRepo.IBankStorageAdmin;
import repository.bankStorageRepo.IBankStorageUser;
import repository.vendingStorageRepo.IVendingStorageAdmin;
import repository.vendingStorageRepo.IVendingStorageUser;
import repository.vendingStorageRepo.VendingStorage;
import vendingNotification.SendNotification;

import java.lang.reflect.Array;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class VendingMachineImpl implements IVendingMachine {
    private IBankStorageUser bankStorageUser;
    private IBankStorageAdmin bankStorageAdmin;
    private IVendingStorageAdmin vendingStorageAdmin;
    private IVendingStorageUser vendingStorageUser;
    private SendNotification sendNotification;


    public VendingMachineImpl() {
        bankStorageUser = new BankStorage();
        bankStorageAdmin = (IBankStorageAdmin) bankStorageUser;

        vendingStorageAdmin = new VendingStorage();
        vendingStorageUser = (IVendingStorageUser) vendingStorageAdmin;
        sendNotification = new SendNotification();
    }

    @Override
    public void load() {
        bankStorageUser.init();
        vendingStorageAdmin.populate();
    }

    @Override
    public String insertMoney(String str) {
        str = str.toLowerCase().trim();
        AbstractMoney abstractMoney = MoneyFactory.parseMoney(str);
        if (abstractMoney == null) {
            return "Money denied";
        }
        try {
            bankStorageUser.addMoneyUser(abstractMoney);
        } catch (FullStackMoneyException ex) {
            Thread thread = new Thread(sendNotification);
            thread.start();
            return "Money denied";
        }
        return abstractMoney.toString() + " inserted";
    }

    @Override
    public String selectItem(String key) {
        key = key.toLowerCase().trim();
        AbstractItem abstractItem;
        try {
            float price = vendingStorageAdmin.getPrice(vendingStorageAdmin.check(key));
            try {
                bankStorageUser.subtractBalance(price);
                abstractItem = vendingStorageUser.remove(key);
            } catch (NotFullPaidException ex) {
                return "insufficient funds, you need to add "
                        + (-bankStorageUser.getBalance() + price) + " dollars";
            } catch (NotSufficientChangeException ex) {
                Thread thread = new Thread(sendNotification);
                thread.start();
                return "insufficient change";
            }
            return "Please take your " + abstractItem.getName();

        } catch (SoldOutException e) {
            return "The item is sold out";
        }
    }

    @Override
    public String withdraw() {
        List<AbstractMoney> changeList;
        try {
            changeList = bankStorageUser.withdraw();
            return Arrays.toString(changeList.toArray());
        } catch (NotSufficientChangeException ex) {
            System.out.println("This should not happen, sorry");
            System.exit(-1);
        }
        return null;
    }

    @Override
    public ArrayList<String> refill() {
        sendNotification.setAlreadySend(new AtomicBoolean());
        bankStorageAdmin.refillAll(BankStorage.CAPACITY / 2);
        //check + addAll
        ArrayList<String> empty = new ArrayList<>();
        StringBuilder sb = new StringBuilder("00");
        for (int i = 1; i <= vendingStorageAdmin.getColumns(); i++)
            for (int j = 1; j <= vendingStorageAdmin.getColumns(); j++) {
                sb.setCharAt(0, (char) (i + '0'));
                sb.setCharAt(1, (char) (j + '0'));
                if (vendingStorageAdmin.check(sb.toString()).equals("empty")) {
                    empty.add(sb.toString());
                } else {
                    String name = vendingStorageAdmin.check(sb.toString());
                    AbstractItem item;
                    switch (name) {
                        case "Borsec":
                            item = new Borsec(vendingStorageAdmin.getPrice(name));
                            vendingStorageAdmin.addAll(sb.toString(), item);
                            break;
                        case "Coca-Cola":
                            item = new CocaCola(vendingStorageAdmin.getPrice(name));
                            vendingStorageAdmin.addAll(sb.toString(), item);
                            break;
                        case "Boromir Covrigi":
                            item = new Covrigi(vendingStorageAdmin.getPrice(name));
                            vendingStorageAdmin.addAll(sb.toString(), item);
                            break;
                        case "RedBull":
                            item = new RedBull(vendingStorageAdmin.getPrice(name));
                            vendingStorageAdmin.addAll(sb.toString(), item);
                            break;
                        case "SevenDays":
                            item = new SevenDays(vendingStorageAdmin.getPrice(name));
                            vendingStorageAdmin.addAll(sb.toString(), item);
                            break;
                        case "Snickers":
                            item = new Snickers(vendingStorageAdmin.getPrice(name));
                            vendingStorageAdmin.addAll(sb.toString(), item);
                            break;
                    }
                }
            }
        return empty;
        //in view will check if this list is empty, if it is not, will give a list of key, element and price
    }

    @Override
    public String refillEmpty(ArrayList<String> empty) {
        //wrongiteminserted exception
        for (int i = 0; i < empty.size(); i += 3) {
            AbstractItem item;
            String name = empty.get(i + 1).toLowerCase();
            switch (name) {
                case "borsec":
                    item = new Borsec(Float.parseFloat(empty.get(i + 2)));
                    vendingStorageAdmin.addAll(empty.get(i), item);
                    break;
                case "coca-cola":
                    item = new CocaCola(Float.parseFloat(empty.get(i + 2)));
                    vendingStorageAdmin.addAll(empty.get(i), item);
                    break;
                case "boromir covrigi":
                    item = new Covrigi(Float.parseFloat(empty.get(i + 2)));
                    vendingStorageAdmin.addAll(empty.get(i), item);
                    break;
                case "redbull":
                    item = new RedBull(Float.parseFloat(empty.get(i + 2)));
                    vendingStorageAdmin.addAll(empty.get(i), item);
                    break;
                case "sevendays":
                    item = new SevenDays(Float.parseFloat(empty.get(i + 2)));
                    vendingStorageAdmin.addAll(empty.get(i), item);
                    break;
                case "snickers":
                    item = new Snickers(Float.parseFloat(empty.get(i + 2)));
                    vendingStorageAdmin.addAll(empty.get(i), item);
                    break;
            }
        }
        return "Items added Successfully";
    }


    @Override
    public String setPrice(String name, float price) {
        try {
            vendingStorageAdmin.updatePrice(name, price);
            return "Prices updated successfully";
        } catch (SoldOutException e) {
            return "Items are sold out";
        }
    }

    @Override
    public String getItemsPrice() {
        return vendingStorageAdmin.getItemsPrice();
    }

    @Override
    public String getItemPrice(String name) {
        try {
            switch (name) {
                case "borsec":
                    return String.valueOf(vendingStorageAdmin.getPrice("Borsec"));
                case "coca-cola":
                    return String.valueOf(vendingStorageAdmin.getPrice("Coca-Cola"));
                case "boromir covrigi":
                    return String.valueOf(vendingStorageAdmin.getPrice("Boromir Covrigi"));
                case "redbull":
                    return String.valueOf(vendingStorageAdmin.getPrice("RedBull"));
                case "sevendays":
                    return String.valueOf(vendingStorageAdmin.getPrice("SevenDays"));
                case "snickers":
                    return String.valueOf(vendingStorageAdmin.getPrice("Snickers"));
                default:
                    return "Item is sold out";
            }
        }catch (SoldOutException e){
            return "Item is sold out";
        }
    }

    @Override
    public String getBalance() {
        return bankStorageUser.getBalance() + "";
    }

    @Override
    public List<String> getProductToStringList() {
        return vendingStorageAdmin.getItemsToList();
    }
}
