import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class TestInspector {
    Inspector isp;
    @Before
    public void setup()
    {
        isp = new Inspector();
    }

    @Test
    public void TestInspectArray()
    {
        ArrayInfo info1 = new ArrayInfo();
        ArrayInfo info2 = new ArrayInfo();
        ArrayInfo info3 = new ArrayInfo();
        int[] testList = {1,2,3,4,5};
        char[] testList2 = {'a', 'b', 'c', 'f'};
        String[] testList3 = {"a", "b", "c", "f"};

        isp.InspectArray(testList, info1);
        isp.InspectArray(testList2, info2);
        isp.InspectArray(testList3, info3);

        assertEquals("[I", info1.name);
        assertEquals("[int]", Arrays.toString(info1.ComponentType.keySet().toArray()));
        assertEquals(5, info1.Length);
        assertEquals("[C", info2.name);
        assertEquals("[char]", Arrays.toString(info2.ComponentType.keySet().toArray()));
        assertEquals(4, info2.Length);
        assertEquals("[Ljava.lang.String;", info3.name);
        assertEquals("[class java.lang.String]", Arrays.toString(info3.ComponentType.keySet().toArray()));
        assertEquals(4, info3.Length);
    }

    @Test
    public void TestClassBArray()
    {
        ArrayInfo info = new ArrayInfo();

        isp.InspectArray(new ClassB[10], info);
        assertEquals("[LClassB;", info.name);
        assertEquals("[class ClassB]", Arrays.toString(info.ComponentType.keySet().toArray()));
        assertEquals(10, info.Length);
    }
    @Test
    public void TestClassB2DArray()
    {
        ArrayInfo info = new ArrayInfo();

        isp.InspectArray(new ClassB[10][5], info);
        assertEquals("[[LClassB;", info.name);
        assertEquals("[class ClassB]", Arrays.toString(info.ComponentType.keySet().toArray()));
        assertEquals(10, info.Length);
    }

    @Test
    public void TestInspect2DArray()
    {
        ArrayInfo info1 = new ArrayInfo();
        int[][] testList = {{1,2},{3,4,5,6}};

        isp.InspectArray(testList, info1);

        assertEquals("[[I", info1.name);
        assertEquals("[int]", Arrays.toString(info1.ComponentType.keySet().toArray()));
        assertEquals(testList.length, info1.Length);

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

        isp.InspectArray(testList, info);

        assertEquals(testList.length, info.Length);
        System.out.println( info.name);
        System.out.println(Arrays.toString(info.ComponentType.keySet().toArray()));
    }

    @Test
    public void TestField() throws Exception {

        ClassA a = new ClassA();
        isp.InspectField(a.getClass(), a);

        assertEquals(3, a.getVal());
        a.setVal(10);
        isp.InspectField(a.getClass(), a);
    }

    @Test
    public void TestFieldD() throws Exception {

        ClassD d = new ClassD();
        isp.InspectField(d.getClass(), d);
    }

    @Test
    public void TestStringField() throws Exception {

        //ClassC c = new ClassC();
        String s = "Karin Winter";
        isp.InspectField(s.getClass(), s);
    }

    @Test
    public void TestBasicInfo() throws Exception {

        ClassB a = new ClassB();
        isp.inspect(a, false);
    }

    @Test
    public void TestInspect() throws Exception {

        isp.inspect(new ClassB(), false);
    }

}