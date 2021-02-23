package model.money;

import model.money.bill.Bill1;
import model.money.bill.Bill10;
import model.money.bill.Bill5;
import model.money.coin.Coin10;
import model.money.coin.Coin5;
import model.money.coin.Coin50;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AbstractMoneyTest {


    @ParameterizedTest
    @MethodSource
    public void getMaterialTest(AbstractMoney input, String expected){
        assertEquals(expected, input.getMaterial());
    }
    private static Stream<Arguments> getMaterialTest() {
        return Stream.of(
                Arguments.of(new Coin5(),"metal"),
                Arguments.of(new Coin10(),"metal"),
                Arguments.of(new Coin50(),"metal"),
                Arguments.of(new Bill1(),"paper"),
                Arguments.of(new Bill5(),"paper"),
                Arguments.of(new Bill10(),"paper")
        );
    }

    @ParameterizedTest
    @MethodSource
    public void toStringTest(AbstractMoney input, String expected){
        assertEquals(expected, input.toString());
    }
    private static Stream<Arguments> toStringTest() {
        return Stream.of(
                Arguments.of(new Coin5(),"5 cents"),
                Arguments.of(new Coin10(),"10 cents"),
                Arguments.of(new Coin50(),"50 cents"),
                Arguments.of(new Bill1(),"1 dollar"),
                Arguments.of(new Bill5(),"5 dollars"),
                Arguments.of(new Bill10(),"10 dollars")
        );
    }

    @ParameterizedTest
    @MethodSource
    public void toDollarTest(AbstractMoney input, float expected){
        assertEquals(expected, input.toDollar());
    }
    private static Stream<Arguments> toDollarTest() {
        return Stream.of(
                Arguments.of(new Coin5(),0.05f),
                Arguments.of(new Coin10(),0.1f),
                Arguments.of(new Coin50(),0.5f),
                Arguments.of(new Bill1(),1.0f),
                Arguments.of(new Bill5(),5.0f),
                Arguments.of(new Bill10(),10.0f)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void idTest(AbstractMoney input, int expected){
        assertEquals(expected, input.getMid());
    }
    private static Stream<Arguments> idTest() {
        return Stream.of(
                Arguments.of(new Coin5(),-1),
                Arguments.of(new Coin10(),-1),
                Arguments.of(new Coin50(),-1),
                Arguments.of(new Bill1(),-1),
                Arguments.of(new Bill5(),-1),
                Arguments.of(new Bill10(),-1),
                Arguments.of(new Coin5(1),1),
                Arguments.of(new Coin10(2),2),
                Arguments.of(new Coin50(3),3),
                Arguments.of(new Bill1(4),4),
                Arguments.of(new Bill5(5),5),
                Arguments.of(new Bill10(6),6)
        );
    }

}