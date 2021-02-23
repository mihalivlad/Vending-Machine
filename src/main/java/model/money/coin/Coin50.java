package model.money.coin;

public class Coin50 extends AbstractCoin{
    public static final int VALUE = 50;

    public Coin50(){

    }

    public Coin50(int id){
        setMid(id);
    }

    public int getValue() {
        return VALUE;
    }
}
