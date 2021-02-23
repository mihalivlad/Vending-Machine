package repository.vendingStorageRepo;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;
import exception.SoldOutException;
import exception.WrongItemInsertedInKeyException;
import model.item.AbstractItem;

import java.lang.reflect.Array;
import java.util.*;

public class VendingStorage implements IVendingStorageUser, IVendingStorageAdmin {
    private HashMap<String, Queue<AbstractItem>> storage;
    private HashMap<String, Float> itemsPrice;
    private final int lines, columns, maxQueue;
    private final VendingStorageDB vendingStorageDB;

    public VendingStorage() {
        //The storage is defined as 6x6
        lines = 6;
        columns = 6;
        maxQueue = 6;
        storage = new HashMap<>();
        itemsPrice = new HashMap<>();
        vendingStorageDB = new VendingStorageDB();
        StringBuilder sb = new StringBuilder("00");
        for (int i = 1; i <= columns; i++)
            for (int j = 1; j <= lines; j++) {
                sb.setCharAt(0, (char) (i + '0'));
                sb.setCharAt(1, (char) (j + '0'));
                Queue<AbstractItem> q = new ArrayDeque<>(maxQueue);
                storage.putIfAbsent(sb.toString(), q);
            }
    }

    @Override
    public int getLines() {
        return lines;
    }

    @Override
    public int getColumns() {
        return columns;
    }

    @Override
    public int getMaxQueue() {
        return maxQueue;
    }

    @Override
    public float getPrice(String item) {
        if (itemsPrice.get(item) != null) {
            return itemsPrice.get(item);
        } else {
            throw new SoldOutException();
        }
    }

    @Override
    public String getItemsPrice() {
        String[] items = {"Borsec", "RedBull", "Coca-Cola", "Boromir Covrigi", "SevenDays", "Snickers"};
        StringBuilder result = new StringBuilder("Prices for each item: \n");
        for (String item : items) {
            if (itemsPrice.get(item) == null) {
                result.append(item).append(": ").append("0.0");
            } else {
                result.append(item).append(": ").append(itemsPrice.get(item).toString());
            }
            result.append("\n");
        }
        return result.toString();
    }

    @Override
    public int add(String key, AbstractItem item) {
        Queue<AbstractItem> queue = storage.get(key);
        if (queue.size() != 0) {
            if (queue.element().getName().equals(item.getName())) {
                int id = vendingStorageDB.add(key, item);
                if (id == -1) {
                    return id;
                }
                item.setId(id);

                queue.add(item);
                itemsPrice.putIfAbsent(item.getName(), item.getPrice());
                return id;
            } else {
                throw new WrongItemInsertedInKeyException();
            }
        } else {
            int id = vendingStorageDB.add(key, item);
            if (id == -1) {
                return id;
            }
            item.setId(id);

            queue.add(item);
            storage.put(key, queue);
            return id;
        }
    }


    @Override
    public AbstractItem remove(String key) {
        AbstractItem item;
        Queue<AbstractItem> queue = storage.get(key);
        if (queue.size() != 0) {
            item = queue.poll();
            vendingStorageDB.remove(key);
            storage.put(key, queue);
            this.updateItemsPrice();
            return item;
        } else {
            throw new SoldOutException();
        }
    }

    @Override
    public void populate() {
        storage.clear();
        StringBuilder sb = new StringBuilder("00");
        for (int i = 1; i <= columns; i++)
            for (int j = 1; j <= lines; j++) {
                sb.setCharAt(0, (char) (i + '0'));
                sb.setCharAt(1, (char) (j + '0'));
                Queue<AbstractItem> q = new ArrayDeque<>(maxQueue);
                storage.putIfAbsent(sb.toString(), q);
            }
        for (Pair<String, AbstractItem> pair : vendingStorageDB.populate()) {
            Queue<AbstractItem> queue = storage.get(pair.left);
            queue.add(pair.right);
            itemsPrice.putIfAbsent(pair.right.getName(), pair.right.getPrice());
        }
    }

    @Override
    public void updatePrice(String name, float price) {
        if (itemsPrice.get(name) != null) {
            vendingStorageDB.updatePrice(name, price);
            StringBuilder sb = new StringBuilder("00");
            for (int i = 1; i <= columns; i++) {
                for (int j = 1; j <= lines; j++) {
                    sb.setCharAt(0, (char) (i + '0'));
                    sb.setCharAt(1, (char) (j + '0'));
                    Queue<AbstractItem> q = storage.get(sb.toString());
                    for (AbstractItem ai : q) {
                        if (ai.getName().equals(name)) {
                            ai.setPrice(price);
                            itemsPrice.put(ai.getName(), price);
                        } else {
                            break;
                        }
                    }
                }
            }
            this.updateItemsPrice();
        } else {
            throw new SoldOutException();
        }
    }

    @Override
    public void addAll(String key, AbstractItem item) {
        Queue<AbstractItem> queue = storage.get(key);
        for (int i = queue.size(); i < maxQueue; i++) {
            int id = this.add(key, item);
        }
    }

    @Override
    public String check(String key) {
        Queue<AbstractItem> queue = storage.get(key);
        if (queue.size() > 0) {
            return queue.element().getName();
        } else {
            return "empty";
        }
    }

    @Override
    public void updateItemsPrice() {
        itemsPrice.clear();
        StringBuilder sb = new StringBuilder("00");
        for (int i = 1; i <= columns; i++) {
            for (int j = 1; j <= lines; j++) {
                sb.setCharAt(0, (char) (i + '0'));
                sb.setCharAt(1, (char) (j + '0'));
                Queue<AbstractItem> q = storage.get(sb.toString());
                if (q.size() != 0) {
                    AbstractItem item = q.element();
                    itemsPrice.putIfAbsent(item.getName(), item.getPrice());
                }
            }
        }
    }


    @Override
    public List<String> getItemsToList() {
        ArrayList<String> result = new ArrayList<>();
        StringBuilder sb = new StringBuilder("00");
        for (int i = 1; i <= columns; i++) {
            StringBuilder item = new StringBuilder();
            for (int j = 1; j <= lines; j++) {
                sb.setCharAt(0, (char) (i + '0'));
                sb.setCharAt(1, (char) (j + '0'));
                Queue<AbstractItem> q = storage.get(sb.toString());
                if (q.size() != 0) {
                    AbstractItem it = q.element();
                    item.append(String.format(" %2s  %18s %.2f ", sb.toString(), it.getName(), it.getPrice()));
                } else {
                    item.append(String.format(" %2s  %18s %4s ", sb.toString(), "None", "0.0"));
                }
            }
            result.add(item.toString());
        }
        return result;
    }
}
