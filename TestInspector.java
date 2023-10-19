import org.junit.Test;

import java.util.Arrays;

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
        ArrayInfo info1 = new ArrayInfo();
        int[][] testList = {{1,2},{3,4,5,6}};
        Inspector isp = new Inspector();
        isp.InspectArray(testList, info1);

        System.out.println(info1.Length);
        System.out.println(info1.Dimension);
        System.out.println(Arrays.toString(info1.ComponentType.keySet().toArray()));
    }

    @Test
    public void TestMixArray()
    {
        ArrayInfo info = new ArrayInfo();
        Object[][] testList = {
                {1, "two", 3},
                {"four", 5},
                {6, "seven"}
        };
        Inspector isp = new Inspector();
        isp.InspectArray(testList, info);

        System.out.println(info.Length);
        System.out.println(info.Dimension);
        System.out.println(Arrays.toString(info.ComponentType.keySet().toArray()));


    }


}