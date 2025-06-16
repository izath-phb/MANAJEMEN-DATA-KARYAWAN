package util;
import java.util.List;

public class GenericUtil {
    public static <T> void printList(List<? extends T> list) {
        for (T item : list) {
            System.out.println(item);
        }
    }
}
