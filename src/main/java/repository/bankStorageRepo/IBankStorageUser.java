package repository.bankStorageRepo;

import exception.NotSufficientChangeException;
import model.money.AbstractMoney;
import java.util.List;

public interface IBankStorageUser {
    void init();
    float getBalance();
    void subtractBalance(float subtractValue);
    void addMoneyUser(AbstractMoney abstractMoney);
    List<AbstractMoney> withdraw();

}
