import java.util.HashMap;

public class ArrayInfo {
    public String name;
    public int Length;
    public HashMap<Class, Integer> ComponentType;
    public int Dimension;

    ArrayInfo()
    {
        this.name = "";
        this.Dimension = 0;
        this.Length = 0;
        this.ComponentType = new HashMap<>();
    }
}
