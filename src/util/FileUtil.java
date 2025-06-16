package util;

import java.io.*;
import java.util.List;

public class FileUtil {
    public static <T extends Serializable> void saveToFile(List<T> data, String filename) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(filename))) {
            out.writeObject(data);
            System.out.println("Data berhasil disimpan ke file: " + filename);
        } catch (IOException e) {
            System.err.println("Gagal menyimpan file: " + e.getMessage());
        }
    }

    public static <T> List<T> loadFromFile(String filename) {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(filename))) {
            Object obj = in.readObject();
            if (obj instanceof List<?>) {
                return (List<T>) obj;
            }
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Gagal membaca file: " + e.getMessage());
        }
        return null;
    }
}
