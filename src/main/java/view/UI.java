package view;

import controller.IVendingMachine;

import java.lang.reflect.Array;
import java.util.*;

public class UI {
    private IVendingMachine vendingMachine;
    private Scanner input;
    public UI(IVendingMachine vendingMachine){
        this.vendingMachine = vendingMachine;
    }

    public void execute(){
        vendingMachine.load();
        boolean isAdmin = false;
        String str;
        input = new Scanner(System.in);
        int cmd = -1;
        System.out.println("Vending Machine (by Vlad Mihali and Vlad Corocea)");
        while(cmd!=0){
            System.out.println("Please Enter to select another option");
            input.nextLine();
            if(!isAdmin) {
                vendingMachine.getProductToStringList().forEach(System.out::println);
                System.out.println("Select one of the following option: ");
                printUserOptions();
                System.out.println(vendingMachine.getBalance());
                str = input.nextLine();
                try {
                    cmd = Integer.parseInt(str);
                }catch (NumberFormatException ex){
                    System.out.println("Wrong command, please select: 0,1,2 or 3");
                    continue;
                }
                switch (cmd){
                    case 0:
                        System.out.println("Goodbye!!!");
                        break;
                    case 1:
                        insertMoneyCommand();
                        break;
                    case 2:
                        selectItemCommand();
                        break;
                    case 3:
                        withdrawCommand();
                        break;
                    case 1234:
                        isAdmin = true;
                        break;
                    default:
                        System.out.println("Wrong command, please select: 0,1,2 or 3");
                }
            }else{
                System.out.println("Admin Mode");
                vendingMachine.getProductToStringList().forEach(System.out::println);
                System.out.println("Select one of the following option: ");
                printAdminOptions();
                str = input.nextLine();
                try {
                    cmd = Integer.parseInt(str);
                }catch (NumberFormatException ex){
                    System.out.println("Wrong command, please select: 0,1,2 or 3");
                    continue;
                }
                switch (cmd){
                    case 0:
                        System.out.println("Goodbye!!!");
                        break;
                    case 1:
                        refillCommand();
                        break;
                    case 2:
                        setPriceCommand();
                        break;
                    case 3:
                        isAdmin = false;
                        break;
                    default:
                        System.out.println("Wrong command, please select: 0,1,2 or 3");
                }
            }
        }
    }
    private void refillCommand() {
        String str;
        System.out.println("Refilling the vending machine");
        System.out.println("Are you sure?(y/n)");
        str = input.nextLine();
        if(str.toLowerCase().equals("y")) {
            ArrayList<String> items = new ArrayList<>(Arrays.asList("borsec", "redbull", "coca-cola", "boromir covrigi", "sevendays", "snickers"));
            HashMap<String, Float> newItems = new HashMap<>();
            String in;
            ArrayList<String> refillEmpty= new ArrayList<>();
            ArrayList<String> refillResponse;
            refillResponse = vendingMachine.refill();
            for (String s: refillResponse) {
                System.out.println(vendingMachine.getItemsPrice());
                if (newItems.size() > 0){
                    System.out.println("newly inserted items price: ");
                    System.out.println(newItems+"\n");
                }
                refillEmpty.add(s);
                System.out.println("Found key " + s + " empty. What item should be inserted? : ");
                in = input.nextLine();
                while(!items.contains(in)){
                    System.out.println("item not found! Try again :");
                    in = input.nextLine();
                }
                refillEmpty.add(in);
                if (!vendingMachine.getItemPrice(in).equals("Item is sold out")){
                    System.out.println("Found the price for " + in);
                    refillEmpty.add(vendingMachine.getItemPrice(in));
                }else if(newItems.get(in)!= null) {
                    System.out.println("Found the price for " + in);
                    refillEmpty.add(String.valueOf(newItems.get(in)));
                }
                else{
                    String newItem = in;
                    System.out.println("What price should "+ in + " be? : ");
                    in = String.valueOf(input.nextFloat());
                    refillEmpty.add(in);
                    newItems.putIfAbsent(newItem,Float.parseFloat(in));
                    in = input.nextLine();
                }
            }
            System.out.println(vendingMachine.refillEmpty(refillEmpty));
        }
    }

    private void setPriceCommand() {
        String str;
        System.out.println(vendingMachine.getItemsPrice());
        System.out.println("select product (case sensitive!): ");
        String name = input.nextLine();
        System.out.println("Are you sure?(y/n)");
        str = input.nextLine();
        if(str.toLowerCase().equals("y")) {
            System.out.println("Insert price: ");
            float price = Float.parseFloat(input.nextLine());
            System.out.println(vendingMachine.setPrice(name, price));
        }
    }

    private void withdrawCommand() {
        String str;
        System.out.println("Withdraw change ");
        System.out.println("Are you sure?(y/n)");
        str = input.nextLine();
        if(str.toLowerCase().equals("y")) {
            System.out.println(vendingMachine.withdraw());
        }
    }

    private void selectItemCommand() {
        String str;
        System.out.println("Select a product");
        String command = input.nextLine();
        System.out.println("Are you sure?(y/n)");
        str = input.nextLine();
        if(str.toLowerCase().equals("y")) {
            System.out.println(vendingMachine.selectItem(command));
        }
    }

    private void insertMoneyCommand() {
        String str;
        System.out.println("Insert money");
        str = input.nextLine();
        System.out.println(vendingMachine.insertMoney(str));
    }

    private void printUserOptions(){
        System.out.println("0.  Exit");
        System.out.println("1.  Insert money (5,10,50 cents or 1,5,10 dollar(s))");
        System.out.println("2.  Select product");
        System.out.println("3.  withdraw money");
    }

    private void printAdminOptions(){
        System.out.println("0.  Exit");
        System.out.println("1.  refill");
        System.out.println("2.  set price for products, format (code price)");
        System.out.println("3.  switch to user");
    }
}
