package util;

import com.mongodb.client.*;
import model.Employee;
import org.bson.Document;
import model.User;
import java.util.ArrayList;
import java.util.List;

public class MongoUtil {

    private static final MongoClient client = MongoClients.create("mongodb://localhost:27017");
    private static final MongoDatabase db = client.getDatabase("company");

    // ====== Employee Collection ======
    private static MongoCollection<Document> getEmployeeCollection() {
        return db.getCollection("employees");
    }

    public static void insertEmployee(String name, int age, String department) {
        Document doc = new Document("name", name)
                .append("age", age)
                .append("department", department);
        getEmployeeCollection().insertOne(doc);
    }

    public static List<Employee> getAllEmployees() {
        List<Employee> list = new ArrayList<>();
        FindIterable<Document> docs = getEmployeeCollection().find();

        for (Document doc : docs) {
            String name = doc.getString("name");
            int age = doc.getInteger("age", 0);
            String dept = doc.getString("department");
            list.add(new Employee(name, age, dept));
        }

        return list;
    }

    public static List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        FindIterable<Document> docs = getUserCollection().find();
        for (Document doc : docs) {
            String username = doc.getString("username");
            String password = doc.getString("password");
            list.add(new User(username, password));
        }
        return list;
    }

    public static void deleteAllEmployees() {
        getEmployeeCollection().deleteMany(new Document());
    }

    // ====== Users Collection ======
    private static MongoCollection<Document> getUserCollection() {
        return db.getCollection("users");
    }

    public static void insertUser(String username, String hashedPassword) {
        Document doc = new Document("username", username)
                .append("password", hashedPassword);
        getUserCollection().insertOne(doc);
    }

    public static boolean isUserExist(String username) {
        Document found = getUserCollection().find(new Document("username", username)).first();
        return found != null;
    }

    public static String getUserPasswordHash(String username) {
        Document doc = getUserCollection().find(new Document("username", username)).first();
        if (doc != null) {
            return doc.getString("password");
        }
        return null;
    }
}
