import java.util.HashMap;

public class ArrayInfo {
    public int Length;
    public HashMap<Class, Integer> ComponentType;
    public int Dimension;

    ArrayInfo()
    {
        Dimension = 0;
        Length = 0;
        ComponentType = new HashMap<>();
    }
}
