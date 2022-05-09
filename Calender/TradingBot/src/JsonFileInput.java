import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;
import java.util.List;

public class JsonFileInput {

    public static void main(String[] args) {
        List<Object> ergebnis = new ArrayList<>();
        Object object;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("JsonCalender2.txt"));) {
            while (true) {         
            object = ois.readObject();
                if (object != null) {
                ergebnis.add(object);
                } 
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}