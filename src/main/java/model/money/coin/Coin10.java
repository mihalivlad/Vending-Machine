package model.money.coin;

public class Coin10 extends AbstractCoin{
    public static final int VALUE = 10;

    public Coin10(){

    }

    public Coin10(int id){
        setMid(id);
    }

    public int getValue() {
        return VALUE;
    }

}
