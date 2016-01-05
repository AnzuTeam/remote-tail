import javafx.scene.paint.Color;
import org.junit.Test;

/**
 * Created by nanashi07 on 16/1/5.
 */
public class TestColor {

    @Test
    public void testValue() {
//            ColorPicker p;
//            p.getValue().toString();


        System.out.println(Color.RED);
        Color c = Color.valueOf("0xff0000ff");
        System.out.println(c);
    }

}
