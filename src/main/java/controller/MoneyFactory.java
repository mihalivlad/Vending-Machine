package controller;

import model.money.AbstractMoney;
import model.money.bill.Bill1;
import model.money.bill.Bill10;
import model.money.bill.Bill5;
import model.money.coin.Coin10;
import model.money.coin.Coin5;
import model.money.coin.Coin50;

public class MoneyFactory {
    public static AbstractMoney parseMoney(String str) {
        AbstractMoney abstractMoney;
        switch (str) {
            case "5 cents":
                abstractMoney = new Coin5();
                break;
            case "10 cents":
                abstractMoney = new Coin10();
                break;
            case "50 cents":
                abstractMoney = new Coin50();
                break;
            case "1 dollar":
                abstractMoney = new Bill1();
                break;
            case "5 dollars":
                abstractMoney = new Bill5();
                break;
            case "10 dollars":
                abstractMoney = new Bill10();
                break;
            default:
                return null;
        }
        return abstractMoney;
    }
}
