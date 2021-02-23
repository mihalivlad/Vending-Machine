package model.item;

public class SevenDays extends AbstractItem {
    private final String name = "SevenDays";
    private float price;

    public SevenDays(float price){
        super.setId(-1);
        this.price = price;
    }
    public SevenDays(int id, float price){
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
