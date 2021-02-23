package repository.vendingStorageRepo;

import model.item.AbstractItem;

public interface IVendingStorageUser {
    AbstractItem remove(String key) ;
    void populate();
}
