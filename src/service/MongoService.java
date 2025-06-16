package service;

import model.Employee;
import model.User;
import util.MongoUtil;
import util.HashUtil;

import java.util.List;

public class MongoService {

    // ======== Employee =========
    public static void saveEmployee(Employee emp) {
        MongoUtil.insertEmployee(emp.getName(), emp.getAge(), emp.getDepartment());
    }

    public static List<Employee> getAllEmployees() {
        return MongoUtil.getAllEmployees();
    }

    public static void deleteAllEmployees() {
        MongoUtil.deleteAllEmployees();
    }

    // ======== User Register/Login =========
    public static boolean registerUser(User user) {
        if (MongoUtil.isUserExist(user.getUsername())) {
            return false; // username sudah terpakai
        }
        String hashedPassword = HashUtil.hashBCrypt(user.getPassword());
        MongoUtil.insertUser(user.getUsername(), hashedPassword);
        return true;
    }

    public static boolean loginUser(String username, String passwordPlain) {
        String storedHash = MongoUtil.getUserPasswordHash(username);
        if (storedHash == null) {
            return false;
        }
        return HashUtil.checkBCrypt(passwordPlain, storedHash);
    }

    public static List<User> getAllUsers() {
        return MongoUtil.getAllUsers();
    }

}
