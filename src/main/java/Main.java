import controller.VendingMachineImpl;
import jdbc.ConnectionFactory;
import view.UI;

import java.sql.Connection;
import java.sql.PreparedStatement;

public class Main {
    public static void main(String[] args){
        System.out.println("Hello world");
        Connection con = ConnectionFactory.getConnection();
        UI view = new UI(new VendingMachineImpl());
        view.execute();
    }
}
