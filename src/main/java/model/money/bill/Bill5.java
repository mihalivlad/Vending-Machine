package model.money.bill;

public class Bill5 extends AbstractBill{
    public static final int VALUE = 5;

    public Bill5(){

    }

    public Bill5(int id){
        setMid(id);
    }

    public int getValue() {
        return VALUE;
    }
}
