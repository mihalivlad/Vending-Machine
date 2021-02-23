package controller;

import java.util.ArrayList;
import java.util.List;

public interface IVendingMachine {
    void load();// 2 private

    String insertMoney(String str);// eu
    String selectItem(String key);//2 private
    String withdraw();//eu

    ArrayList<String> refill();//2 private
    String refillEmpty(ArrayList<String> empty);
    String setPrice(String name, float price);// tu
    String getItemPrice(String name);

    //String swap(String str);

    String getBalance();//eu
    List<String> getProductToStringList();//tu

    //mockito
    //coverage test 75%
    //observable
    //send mail
}
