package controller;

import model.money.AbstractMoney;
import model.money.bill.Bill1;
import model.money.bill.Bill10;
import model.money.bill.Bill5;
import model.money.coin.Coin10;
import model.money.coin.Coin5;
import model.money.coin.Coin50;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import repository.bankStorageRepo.BankStorage;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class VendingMachineImplTest {

    private VendingMachineImpl vendingMachine = new VendingMachineImpl();

    @BeforeEach
    public void tearDown() {
        vendingMachine.load();
    }

    @ParameterizedTest
    @MethodSource
    public void insertMoneyTest(String input, String expected) {
        //vendingMachine.bankStorageAdmin.refillToEmptyAll();
        assertEquals(expected, vendingMachine.insertMoney(input));
    }

    private static Stream<Arguments> insertMoneyTest() {
        return Stream.of(
                Arguments.of("5 cents", "5 cents inserted"),
                Arguments.of("10 cents", "10 cents inserted"),
                Arguments.of("50 cents", "50 cents inserted"),
                Arguments.of("1 dollar", "1 dollar inserted"),
                Arguments.of("5 dollars", "5 dollars inserted"),
                Arguments.of("10 dollars", "10 dollars inserted"),
                Arguments.of("5 sents", "Money denied")
        );
    }

    @ParameterizedTest
    @MethodSource
    public void withdrawTest(AbstractMoney abstractMoney1, AbstractMoney abstractMoney2, AbstractMoney abstractMoney3,
                             float price, String expected) {
        //vendingMachine.bankStorageAdmin.refillAll(10);
        vendingMachine.refill();
        vendingMachine.insertMoney(abstractMoney1.toString());
        vendingMachine.insertMoney(abstractMoney2.toString());
        vendingMachine.insertMoney(abstractMoney3.toString());
        if (price != 0) {
            vendingMachine.setPrice("Borsec",price);
            vendingMachine.selectItem("11");
        }
        assertEquals(expected, vendingMachine.withdraw());
    }

    private static Stream<Arguments> withdrawTest() {
        return Stream.of(
                Arguments.of(new Bill5(), new Bill5(), new Bill1(), 0.0f, "[1 dollar, 5 dollars, 5 dollars]"),
                Arguments.of(new Bill5(), new Bill5(), new Bill1(), 1.0f, "[10 dollars]"),
                Arguments.of(new Coin50(), new Bill10(), new Coin5(), 5.0f, "[5 cents, 50 cents, 5 dollars]"),
                Arguments.of(new Bill10(), new Coin10(), new Coin5(), 1.0f, "[5 cents, 10 cents, 1 dollar, 1 dollar, 1 dollar, 1 dollar, 5 dollars]"),
                Arguments.of(new Bill1(), new Coin10(), new Coin5(), 0.25f, "[10 cents, 10 cents, 10 cents, 10 cents, 50 cents]")
        );
    }

   /* @ParameterizedTest
    @MethodSource
    public void itemPriceTest(String name, float price, String expected) {
        vendingMachine.refill();
        vendingMachine.setPrice(name,price);
        assertEquals(expected, vendingMachine.getItemPrice(name));
    }

    private static Stream<Arguments> itemPriceTest() {
        return Stream.of(
                Arguments.of("Borsec", 1.0f, "1.0"),
                Arguments.of("Snickers", 1.0f, "1.0"),
                Arguments.of("Coca-Cola", 1.0f, "1.0"),
                Arguments.of("RedBull", 1.0f, "1.0"),
                Arguments.of("SevenDays", 1.0f, "1.0"),
                Arguments.of("Covrigi", 1.0f, "1.0"),
                Arguments.of("Borse", 1.0f, "1.0")
                );
    }*/

//    @Test
//    void selectItem() {
//    }
//
//    @Test
//    void withdraw() {
//    }
//
//    @Test
//    void refill() {
//    }
//
//    @Test
//    void setPrice() {
//    }
//
//    @Test
//    void getBalance() {
//    }
//
//    @Test
//    void getProductToStringList() {
//    }
}