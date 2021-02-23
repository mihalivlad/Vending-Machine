package repository.vendingStorageRepo;

import exception.SoldOutException;
import model.item.AbstractItem;
import model.item.Borsec;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class VendingStorageTest {

    @ParameterizedTest
    @MethodSource
    public void vendingStorageTest(String key, String name, float price, AbstractItem item) {
        VendingStorage vendingStorage = new VendingStorage();
        assertEquals(vendingStorage.getColumns(), 6);
        assertEquals(vendingStorage.getLines(), 6);
        assertEquals(vendingStorage.getMaxQueue(), 6);
        assertEquals(vendingStorage.getItemsPrice(), "Prices for each item: \nBorsec: 0.0\nRedBull: 0.0\n" +
                "Coca-Cola: 0.0\nBoromir Covrigi: 0.0\nSevenDays: 0.0\nSnickers: 0.0\n");
        vendingStorage.updateItemsPrice();
        assertEquals(vendingStorage.getItemsPrice(), "Prices for each item: \nBorsec: 0.0\nRedBull: 0.0\n" +
                "Coca-Cola: 0.0\nBoromir Covrigi: 0.0\nSevenDays: 0.0\nSnickers: 0.0\n");
        assertEquals(vendingStorage.getItemsToList().toString(), "[ 11                None  0.0  12                None  0.0  13                None  0.0  14                None  0.0  15                None  0.0  16                None  0.0 ,  21                None  0.0  22                None  0.0  23                None  0.0  24                None  0.0  25                None  0.0  26                None  0.0 ,  31                None  0.0  32                None  0.0  33                None  0.0  34                None  0.0  35                None  0.0  36                None  0.0 ,  41                None  0.0  42                None  0.0  43                None  0.0  44                None  0.0  45                None  0.0  46                None  0.0 ,  51                None  0.0  52                None  0.0  53                None  0.0  54                None  0.0  55                None  0.0  56                None  0.0 ,  61                None  0.0  62                None  0.0  63                None  0.0  64                None  0.0  65                None  0.0  66                None  0.0 ]");

        SoldOutException exception = assertThrows(SoldOutException.class , () -> {vendingStorage.getPrice(name);});
        vendingStorage.add(key,item);
        vendingStorage.add(key,item);
        assertEquals(vendingStorage.getPrice(item.getName()),item.getPrice());
        vendingStorage.updatePrice(name,3.0f);
        assertEquals(vendingStorage.getPrice(item.getName()),3.0f);
        assertEquals(vendingStorage.check(key), name);

        item.setId(2);
        assertEquals(vendingStorage.remove(key), item);
        item.setId(1);
        assertEquals(vendingStorage.remove(key), item);
        SoldOutException exception2 = assertThrows(SoldOutException.class , () -> {vendingStorage.remove(key);});
        assertEquals(vendingStorage.check(key), "empty");

    }
    private static Stream<Arguments> vendingStorageTest(){
        return Stream.of(
                Arguments.of("11", "Borsec", 1.0f, new Borsec(1.0f))
        );
    }
}