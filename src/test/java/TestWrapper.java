import com.prhythm.app.remotetail.models.DataWrapper;
import com.prhythm.app.remotetail.models.HighLight;
import org.junit.Test;

import javax.xml.bind.JAXBException;
import java.io.File;

/**
 * Created by nanashi07 on 16/1/5.
 */
public class TestWrapper {

    @Test
    public void testSave() throws JAXBException {
        DataWrapper w = new DataWrapper();
//        w.getHighLights().getHighLights().add(new HighLight(false, true, ));
        w.getHighLights().add(new HighLight());

        w.save(new File("test.xml"));
    }

}
