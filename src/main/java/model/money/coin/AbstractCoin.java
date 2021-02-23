package model.money.coin;

import model.money.AbstractMoney;

public abstract class AbstractCoin extends AbstractMoney {
    public static final String MATERIAL = "metal";
    public static final String UNIT = "cent";

    public String getMaterial(){
        return MATERIAL;
    }

    public String getUnit(){
        return UNIT;
    }

    public float toDollar(){
        return getValue()/100.0f;
    }
}
