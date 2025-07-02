package app;

import model.Employee;
import model.User;
import service.MongoService;
import util.FileUtil;
import util.GenericUtil;
import util.HashUtil;

import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        // Hash password
        String pass = "mypassword";
        String hashed = HashUtil.hashBCrypt(pass);
        System.out.println("Password asli: " + pass);
        System.out.println("Password hashed: " + hashed);

        // Tambahkan employee ke DB
        Employee emp = new Employee("Budi", 30, "HRD");
        MongoService.saveEmployee(emp);

        // Ambil semua employee dari DB
        List<Employee> empList = MongoService.getAllEmployees();
        System.out.println("=== Daftar Employee dari DB ===");
        GenericUtil.printList(empList);

        // Simpan ke file
        FileUtil.saveToFile(empList, "employees.dat");

        // Load dari file
        List<Employee> loadedEmp = FileUtil.loadFromFile("employees.dat");
        System.out.println("=== Data Employee hasil Load dari File ===");
        if (loadedEmp != null) {
            GenericUtil.printList(loadedEmp);
        } else {
            System.out.println("Data gagal dimuat.");
        }

        // Cetak semua user
        List<User> userList = MongoService.getAllUsers();
        System.out.println("=== Daftar User ===");
        GenericUtil.printList(userList);

        // ===== Contoh Multi-threading =====
        System.out.println("\n=== Contoh Multi-threading ===");

        DataProcessorThread thread1 = new DataProcessorThread("Proses Simulasi 1");
        DataProcessorThread thread2 = new DataProcessorThread("Proses Simulasi 2");

        thread1.start(); // Memulai thread pertama
        thread2.start(); // Memulai thread kedua

        System.out.println("Main thread selesai.");
    }
}
