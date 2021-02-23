package repository.bankStorageRepo;

import model.money.AbstractMoney;

public interface IBankStorageAdmin {
    void init();
    void refill(int refillLevel, AbstractMoney abstractMoney);
    void refillAll(int refillLevel);
    void refillToFull(AbstractMoney abstractMoney);
    void refillToEmpty(AbstractMoney abstractMoney);
    void refillToFullAll();
    void refillToEmptyAll();
}
