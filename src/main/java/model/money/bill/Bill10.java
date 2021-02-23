package model.money.bill;

public class Bill10 extends AbstractBill{
    public static final int VALUE = 10;

    public Bill10(){

    }

    public Bill10(int id){
        setMid(id);
    }

    public int getValue() {
        return VALUE;
    }
}
