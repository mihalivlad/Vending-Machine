package repository.bankStorageRepo;

import exception.NotSufficientChangeException;
import model.money.AbstractMoney;
import model.money.bill.Bill1;
import model.money.bill.Bill10;
import model.money.bill.Bill5;
import model.money.coin.Coin10;
import model.money.coin.Coin5;
import model.money.coin.Coin50;

import java.util.*;

public class ChangeMechanism {
    private BankStorage bankStorage;
    private List<Integer> frequencyInitialMoney;
    private boolean refund;

    public ChangeMechanism(BankStorage bankStorage){
        this.bankStorage = bankStorage;
    }

    public void init(){
        refund = true;
        setFreqToZero();
    }

    public List<Integer> getFrequencyInitialMoney() {
        return frequencyInitialMoney;
    }

    public void setFrequencyInitialMoney(List<Integer> frequencyInitialMoney) {
        this.frequencyInitialMoney = frequencyInitialMoney;
    }

    public boolean isRefund() {
        return refund;
    }

    public void setRefund(boolean refund) {
        this.refund = refund;
    }

    public void increment(BankStorage.MoneyType moneyType){
        int incrementedValue = frequencyInitialMoney.get(moneyType.ordinal()) + 1;
        frequencyInitialMoney.set(moneyType.ordinal(), incrementedValue);
    }

    void setFreqToZero() {
        frequencyInitialMoney = new LinkedList<>();
        for(int index = 0; index < BankStorage.MoneyType.values().length; index++) {
            frequencyInitialMoney.add(0);
        }
    }
    public List<AbstractMoney> refunding() {
        List<AbstractMoney> refundList = new LinkedList<>();
        int index = 0;
        for(BankStorage.MoneyType moneyType : BankStorage.MoneyType.values()){
            int frequency = frequencyInitialMoney.get(moneyType.ordinal());
            for(index = 0; index < frequency; index++){
                refundList.add(bankStorage.removeMoneyUser(moneyType));
            }
        }
        setFreqToZero();
        refund = true;
        return refundList;
    }

    public void withDrawAlgorithm(List<Stack<AbstractMoney>> bankStorageList){
        ArrayList<Float> result = new ArrayList<>();
        List<Float> numbers = toValueList(bankStorageList);
        calculateWithdraw(numbers, result);
        if(result.isEmpty()){
            throw new NotSufficientChangeException();
        }
        List<Float> list = result;
        frequencyInitialMoney.set(BankStorage.MoneyType.COIN_5.ordinal(), Collections.frequency(list, 0.05f));
        frequencyInitialMoney.set(BankStorage.MoneyType.COIN_10.ordinal(), Collections.frequency(list, 0.10f));
        frequencyInitialMoney.set(BankStorage.MoneyType.COIN_50.ordinal(), Collections.frequency(list, 0.50f));
        frequencyInitialMoney.set(BankStorage.MoneyType.BILL_1.ordinal(), Collections.frequency(list, 1.0f));
        frequencyInitialMoney.set(BankStorage.MoneyType.BILL_5.ordinal(), Collections.frequency(list, 5.0f));
        frequencyInitialMoney.set(BankStorage.MoneyType.BILL_10.ordinal(), Collections.frequency(list, 10.0f));

    }

    private List<Float> toValueList(List<Stack<AbstractMoney>> bankStorageList){
        List<Float> numbers = new ArrayList<>();
        for (int i = 0; i < bankStorageList.get(BankStorage.MoneyType.COIN_5.ordinal()).size(); i++) {
            numbers.add(Coin5.VALUE/100.0f);
        }
        for (int i = 0; i < bankStorageList.get(BankStorage.MoneyType.COIN_10.ordinal()).size(); i++) {
            numbers.add(Coin10.VALUE/100.0f);
        }
        for (int i = 0; i < bankStorageList.get(BankStorage.MoneyType.COIN_50.ordinal()).size(); i++) {
            numbers.add(Coin50.VALUE/100.0f);
        }
        for (int i = 0; i < bankStorageList.get(BankStorage.MoneyType.BILL_1.ordinal()).size(); i++) {
            numbers.add((float) Bill1.VALUE);
        }
        for (int i = 0; i < bankStorageList.get(BankStorage.MoneyType.BILL_5.ordinal()).size(); i++) {
            numbers.add((float) Bill5.VALUE);
        }
        for (int i = 0; i < bankStorageList.get(BankStorage.MoneyType.BILL_10.ordinal()).size(); i++) {
            numbers.add((float) Bill10.VALUE);
        }
        return numbers;
    }

    private void calculateWithdraw(List<Float> numbers, ArrayList<Float> partial) {
        float s = 0.0f;
        for(int i=numbers.size()-1;i>=0;i--) {
            float n = numbers.get(i);
            if(s+n-bankStorage.getBalance()>0.01){
                continue;
            }
            partial.add(n);
            s+=n;
            if(Math.abs(s - bankStorage.getBalance()) < 0.01) {
                return;
            }
        }
        throw new NotSufficientChangeException();
    }

}
