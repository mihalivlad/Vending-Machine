package model.money.coin;

public class Coin5 extends AbstractCoin{
    public static final int VALUE = 5;

    public Coin5(){

    }

    public Coin5(int id){
        setMid(id);
    }

    public int getValue() {
        return VALUE;
    }


}
