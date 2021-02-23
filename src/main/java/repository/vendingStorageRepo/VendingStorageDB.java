package repository.vendingStorageRepo;

import com.mysql.cj.conf.ConnectionUrlParser.Pair;
import exception.CustomMySQLException;
import jdbc.ConnectionFactory;
import model.item.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class VendingStorageDB {

    public int add(String key, AbstractItem item) {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement("INSERT INTO Item (`name`,`price`,`key`) VALUES (?,?,?);");
             PreparedStatement lastID = con.prepareStatement("SELECT LAST_INSERT_ID();")) {
            stmt.setString(1, item.getName());
            stmt.setFloat(2, item.getPrice());
            stmt.setString(3, key);
            stmt.executeUpdate();
            try (ResultSet rs = lastID.executeQuery()) {
                rs.next();
                return rs.getInt(1);
            }
        } catch (SQLException ignored) {
            throw new CustomMySQLException();
        }
    }

    public void remove(String key) {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM Item I WHERE I.key = ? AND I.iid IN (SELECT MIN(It.iid) FROM Item It WHERE It.key = ? );");
             PreparedStatement delete = con.prepareStatement("DELETE FROM Item WHERE iid = ? AND `name` = ? AND price = ? AND `key` = ?;")) {
            stmt.setString(1, key);
            stmt.setString(2, key);
            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                delete.setInt(1, rs.getInt(1));
                delete.setString(2, rs.getString(2));
                delete.setFloat(3, rs.getFloat(3));
                delete.setString(4, rs.getString(4));
                delete.executeUpdate();
            }
        } catch (SQLException ignored) {
            throw new CustomMySQLException();
        }
    }

    public ArrayList<Pair<String, AbstractItem>> populate() {

        ArrayList<Pair<String, AbstractItem>> items = new ArrayList<>();
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement("SELECT * FROM Item;");
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                String name = rs.getString(2);
                AbstractItem item;
                Pair<String, AbstractItem> pair;
                switch (name) {
                    case "Borsec":
                        item = new Borsec(rs.getInt(1), rs.getFloat(3));
                        pair = new Pair<>(rs.getString(4), item);
                        items.add(pair);
                        break;
                    case "Coca-Cola":
                        item = new CocaCola(rs.getInt(1), rs.getFloat(3));
                        pair = new Pair<>(rs.getString(4), item);
                        items.add(pair);
                        break;
                    case "Boromir Covrigi":
                        item = new Covrigi(rs.getInt(1), rs.getFloat(3));
                        pair = new Pair<>(rs.getString(4), item);
                        items.add(pair);
                        break;
                    case "RedBull":
                        item = new RedBull(rs.getInt(1), rs.getFloat(3));
                        pair = new Pair<>(rs.getString(4), item);
                        items.add(pair);
                        break;
                    case "SevenDays":
                        item = new SevenDays(rs.getInt(1), rs.getFloat(3));
                        pair = new Pair<>(rs.getString(4), item);
                        items.add(pair);
                        break;
                    case "Snickers":
                        item = new Snickers(rs.getInt(1), rs.getFloat(3));
                        pair = new Pair<>(rs.getString(4), item);
                        items.add(pair);
                        break;
                }
            }
            return items;
        } catch (SQLException ignored) {
            throw new CustomMySQLException();
        }
    }

    public void updatePrice(String name, float price) {
        try (Connection con = ConnectionFactory.getConnection();
             PreparedStatement stmt = con.prepareStatement("UPDATE Item I SET I.`price` = ? WHERE I.`name` = ?;")){
            stmt.setFloat(1, price);
            stmt.setString(2, name);
            stmt.executeUpdate();
        } catch (SQLException ignored) {
            throw new CustomMySQLException();
        }
    }
}
