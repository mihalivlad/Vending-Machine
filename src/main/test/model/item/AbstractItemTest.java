package model.item;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class AbstractItemTest {

    @ParameterizedTest
    @MethodSource
    public void getNameTest(AbstractItem input, String expected) { assertEquals(expected, input.getName()); }
    private static Stream<Arguments> getNameTest() {
        return Stream.of(
                Arguments.of(new Borsec(0.0f), "Borsec"),
                Arguments.of(new RedBull(0.0f), "RedBull"),
                Arguments.of(new CocaCola(0.0f), "Coca-Cola"),
                Arguments.of(new Covrigi(0.0f), "Boromir Covrigi"),
                Arguments.of(new SevenDays(0.0f), "SevenDays"),
                Arguments.of(new Snickers(0.0f), "Snickers")
        );
    }

    @ParameterizedTest
    @MethodSource
    public void getPriceTest(AbstractItem input, float expected) { assertEquals(expected, input.getPrice()); }
    private static Stream<Arguments> getPriceTest() {
        return Stream.of(
                Arguments.of(new Borsec(1.0f), 1.0f),
                Arguments.of(new RedBull(1.0f), 1.0f),
                Arguments.of(new CocaCola(1.0f), 1.0f),
                Arguments.of(new Covrigi(1.0f), 1.0f),
                Arguments.of(new SevenDays(1.0f), 1.0f),
                Arguments.of(new Snickers(1.0f), 1.0f)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void setPriceTest(AbstractItem input, float price, float expected) {
        input.setPrice(price);
        assertEquals(expected, input.getPrice());
    }
    private static Stream<Arguments> setPriceTest() {
        return Stream.of(
                Arguments.of(new Borsec(0.0f), 1.0f, 1.0f),
                Arguments.of(new RedBull(0.0f), 1.0f, 1.0f),
                Arguments.of(new CocaCola(0.0f), 1.0f, 1.0f),
                Arguments.of(new Covrigi(0.0f), 1.0f, 1.0f),
                Arguments.of(new SevenDays(0.0f), 1.0f, 1.0f),
                Arguments.of(new Snickers(0.0f), 1.0f, 1.0f)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void toStringTest(AbstractItem input, String expected) {
        assertEquals(expected, input.toString());
    }
    private static Stream<Arguments> toStringTest() {
        return Stream.of(
                Arguments.of(new Borsec(0.0f),"Name: Borsec, Price: 0.0"),
                Arguments.of(new RedBull(0.0f), "Name: RedBull, Price: 0.0"),
                Arguments.of(new CocaCola(0.0f), "Name: Coca-Cola, Price: 0.0"),
                Arguments.of(new Covrigi(0.0f), "Name: Boromir Covrigi, Price: 0.0"),
                Arguments.of(new SevenDays(0.0f), "Name: SevenDays, Price: 0.0"),
                Arguments.of(new Snickers(0.0f), "Name: Snickers, Price: 0.0")
        );
    }

    @ParameterizedTest
    @MethodSource
    public void getIdTest(AbstractItem input, int expected) {
        assertEquals(expected, input.getId());
    }
    private static Stream<Arguments> getIdTest() {
        return Stream.of(
                Arguments.of(new Borsec(0.0f),-1),
                Arguments.of(new RedBull(0.0f),-1),
                Arguments.of(new CocaCola(0.0f),-1),
                Arguments.of(new Covrigi(0.0f),-1),
                Arguments.of(new SevenDays(0.0f),-1),
                Arguments.of(new Snickers(0.0f),-1)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void setIdTest(AbstractItem input, int id, int expected) {
        input.setId(id);
        assertEquals(expected, input.getId());
    }
    private static Stream<Arguments> setIdTest() {
        return Stream.of(
                Arguments.of(new Borsec(0.0f),1,1),
                Arguments.of(new RedBull(0.0f),2,2),
                Arguments.of(new CocaCola(0.0f),3,3),
                Arguments.of(new Covrigi(0.0f),4,4),
                Arguments.of(new SevenDays(0.0f),5,5),
                Arguments.of(new Snickers(0.0f),6,6)
        );
    }

    @ParameterizedTest
    @MethodSource
    public void constructorTest(AbstractItem input, int expected) {
        assertEquals(expected, input.getId());
    }
    private static Stream<Arguments> constructorTest() {
        return Stream.of(
                Arguments.of(new Borsec(1,0.0f),1),
                Arguments.of(new RedBull(2,0.0f),2),
                Arguments.of(new CocaCola(3,0.0f),3),
                Arguments.of(new Covrigi(4,0.0f),4),
                Arguments.of(new SevenDays(5,0.0f),5),
                Arguments.of(new Snickers(6,0.0f),6)
        );
    }
}