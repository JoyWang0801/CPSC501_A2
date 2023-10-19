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

    @Test
    public void TestClassBArray()
    {
        Inspector isp = new Inspector();
        isp.inspect(new ClassB[10], false);
    }

    @Test
    public void TestInspectArray()
    {
        ArrayInfo info1 = new ArrayInfo();
        ArrayInfo info2 = new ArrayInfo();
        int[] testList = {1,2,3,4,5};
        String[] testList2 = {"Hi", "This", "Is", "Karina"};
        Inspector isp = new Inspector();
        isp.InspectArray(testList, info1);
        isp.InspectArray(testList2, info2);
    }

    @Test
    public void TestInspect2DArray()
    {
        int[][] testList = {{1,2},{3,4}};
        Inspector isp = new Inspector();
        isp.InspectMultipleDimensionalArray(testList);
    }



}