import org.junit.Test;

import static org.junit.Assert.*;

public class TestInspector {
    @Test
    public void TestInspectClassName() {
        String s;
        Inspector isp = new Inspector();
        s = isp.InspectClassName(new ClassA());
        assertEquals(s, "ClassA");
    }


}