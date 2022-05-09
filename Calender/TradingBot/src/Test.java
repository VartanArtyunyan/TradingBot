import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class Test {
    

    public static void main(String[] args) {
        try{
            ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("person.dat"));
            Person p = new Person("John", "Doe");
            oos.writeObject(p);
            oos.close();
        } catch (Exception e){
             e.printStackTrace();
        }   
    }
        
}
