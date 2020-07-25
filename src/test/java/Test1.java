import com.tuxdave.JChess.extras.Vector2;

public class Test1 {
    public static void main(String[] args) {
        Vector2 a = new Vector2(1,1);
        Vector2 b = new Vector2(1,3);
        a = Vector2.add(a,b);
        System.out.println(a);
    }
}
