package service;

import com.mongodb.client.*;
import org.bson.Document;
import util.HashUtil;

public class MongoUserService {
    private static final MongoClient client = MongoClients.create("mongodb://localhost:27017");
    private static final MongoCollection<Document> getUserCollection() {
        return client.getDatabase("company").getCollection("user");
    }

    public static boolean register(String username, String plainPassword) {
        try {
            String hashed = HashUtil.hashBCrypt(plainPassword);
            Document user = new Document("username", username)
                                .append("password", hashed);
            getUserCollection().insertOne(user);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean login(String username, String inputPassword) {
        Document user = getUserCollection().find(new Document("username", username)).first();
        if (user == null) return false;

        String storedHashed = user.getString("password");
        return HashUtil.checkBCrypt(inputPassword, storedHashed);
    }
}
