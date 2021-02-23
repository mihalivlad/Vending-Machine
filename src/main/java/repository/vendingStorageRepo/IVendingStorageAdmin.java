package repository.vendingStorageRepo;

import model.item.AbstractItem;

import java.util.List;

public interface IVendingStorageAdmin {
    void populate();

    void updatePrice(String name, float price);

    void addAll(String key, AbstractItem item);

    String check(String key);

    int add(String key, AbstractItem item);

    int getLines();

    int getMaxQueue();

    int getColumns();

    float getPrice(String item);

    String getItemsPrice();

    List<String> getItemsToList();

    void updateItemsPrice();

}
