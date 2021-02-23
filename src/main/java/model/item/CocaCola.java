package model.item;

public class CocaCola extends AbstractItem{
    private final String name;
    private float price;

    public CocaCola(int id, float price){
        super.setId(id);
        this.name = "Coca-Cola";
        this.price = price;
    }
    public CocaCola(float price){
        super.setId(-1);
        this.name = "Coca-Cola";
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

