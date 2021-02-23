package model.money;

public abstract class AbstractMoney {
    private int mid = -1;
    public final void setMid(int id){
        mid=id;
    }
    public final int getMid(){
        return mid;
    }

    public abstract String getMaterial();
    public abstract String getUnit();
    public abstract int getValue();
    public abstract float toDollar();
    public String toString(){
        if(getValue() == 1) {
            return getValue() + " " + getUnit();
        } else {
            return getValue() + " " + getUnit() + "s";
        }

    }
}
