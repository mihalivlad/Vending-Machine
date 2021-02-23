package model.item;

public abstract class AbstractItem {
    private int id;

    public abstract float getPrice();
    public abstract void setPrice(float price);
    public abstract String getName();

    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String toString(){
        return "Name: "+ this.getName() +", Price: "+ this.getPrice();
    }
}
