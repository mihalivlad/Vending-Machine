package model.item;

public class RedBull extends AbstractItem{
    private final String name = "RedBull";
    private float price;

    public RedBull(float price){
        super.setId(-1);
        this.price = price;
    }
    public RedBull(int id, float price){
        super.setId(id);
        this.price = price;
    }

    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public String getName() {
        return name;
    }
    public void setId(int id){
        super.setId(id);
    }
    public int getId(){
        return super.getId();
    }
}
