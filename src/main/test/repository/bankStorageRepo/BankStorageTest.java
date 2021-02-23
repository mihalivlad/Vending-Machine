package repository.bankStorageRepo;

import exception.FullStackMoneyException;
import exception.NotFullPaidException;
import exception.NotSufficientChangeException;
import model.money.AbstractMoney;
import model.money.bill.Bill1;
import model.money.bill.Bill10;
import model.money.bill.Bill5;
import model.money.coin.Coin10;
import model.money.coin.Coin5;
import model.money.coin.Coin50;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class BankStorageTest {
    //private BankStorage bankStorage = new BankStorage();
    @ParameterizedTest
    @MethodSource
    public void bankStorageTest(int refillLevel,float price, AbstractMoney abstractMoney, String expected){
        BankStorage bankStorage = new BankStorage();
        bankStorage.init();
        bankStorage.refillAll(refillLevel);
        try {
            bankStorage.addMoneyUser(abstractMoney);
        }catch (FullStackMoneyException ex){
            assertEquals(BankStorage.CAPACITY, refillLevel);
            assertEquals(expected, "FullStackMoney");
            return;
        }
        try {
            bankStorage.subtractBalance(price);
            assertEquals(expected, Arrays.toString(bankStorage.withdraw().toArray()));
        }catch (NotFullPaidException ex){
            assertEquals(1.0f, price);
            assertEquals(expected,"NotFullPaid");
        }
        catch (NotSufficientChangeException ex){
            assertEquals(0, refillLevel);
            assertEquals(expected, "NotSufficientChange");
        }
    }

    private static Stream<Arguments> bankStorageTest() {
        return Stream.of(
                Arguments.of(BankStorage.CAPACITY/2,0.05f,new Coin5(),"[]"),
                Arguments.of(BankStorage.CAPACITY/2,0.05f,new Coin10(),"[5 cents]"),
                Arguments.of(BankStorage.CAPACITY/2,0.05f,new Coin50(),"[5 cents, 10 cents, 10 cents, 10 cents, 10 cents]"),
                Arguments.of(BankStorage.CAPACITY/2,0.05f,new Bill1(),"[5 cents, 10 cents, 10 cents, 10 cents, 10 cents, 50 cents]"),
                Arguments.of(BankStorage.CAPACITY/2,0.05f,new Bill5(),"[5 cents, 10 cents, 10 cents, 10 cents, 10 cents, 50 cents, 1 dollar, 1 dollar, 1 dollar, 1 dollar]"),
                Arguments.of(BankStorage.CAPACITY/2,0.05f,new Bill10(),"[5 cents, 10 cents, 10 cents, 10 cents, 10 cents, 50 cents, 1 dollar, 1 dollar, 1 dollar, 1 dollar, 5 dollars]"),
                Arguments.of(BankStorage.CAPACITY/2,1.0f,new Coin5(),"NotFullPaid"),
                Arguments.of(BankStorage.CAPACITY,0.05f,new Bill10(),"FullStackMoney"),
                Arguments.of(0,0.05f,new Bill10(),"NotSufficientChange")
        );
    }
}