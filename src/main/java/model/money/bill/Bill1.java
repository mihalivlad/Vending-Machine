package model.money.bill;

public class Bill1 extends AbstractBill{
    public static final int VALUE = 1;

    public Bill1(){

    }

    public Bill1(int id){
        setMid(id);
    }

    public int getValue() {
        return VALUE;
    }
}
