package model.money.bill;

import model.money.AbstractMoney;

public abstract class AbstractBill extends AbstractMoney {
    public static final String MATERIAL = "paper";
    public static final String UNIT = "dollar";

    public String getMaterial(){
        return MATERIAL;
    }

    public String getUnit(){
        return UNIT;
    }

    public float toDollar(){
        return getValue();
    }
}
