import java.util.*;
import java.lang.reflect.*;

/*
The name of the declaring class
The name of the immediate superclass
The name of the interfaces the class implements

The methods the class declares. For each, also find the following:
    The exceptions thrown
    The parameter types
    The return type
    The modifiers

The constructors the class declares. For each, also find the following:
    The parameter types
    The modifiers

The fields the class declares. For each, also find the following:
    The type
    The modifiers

The current value of each field. If the field is an object reference,
    and recursive is set to false, simply print out the “reference value”
    directly (this will be the name of the object’s class plus the object’s “identity hash code”).
 */


public class Inspector {
    Class[] method_exceptionTypes;
    Class[] method_parameterTypes;
    Class method_returnType;
    int method_modifier;
    Class[] constructor_parameterTypes;
    int constructor_modifier;
    Class field_type;
    int field_modifier;

    public void inspect(Object obj, boolean recursive) {
        Vector objectsToInspect = new Vector();
        Class ObjClass = obj.getClass();
        System.out.println("inside inspector: " + obj + " (recursive = " + recursive + ")");

        //inspect the current class
        //inspectFields(obj, ObjClass,objectsToInspect);
//        InspectBasicInfo(ObjClass);
//        InspectMethod(ObjClass);
//        InspectConstructor(ObjClass);
//        InspectField(obj, ObjClass);
        ArrayInfo arrayInfo = new ArrayInfo();

        if(ObjClass.isArray())
        {
            InspectArray(obj, arrayInfo);
        }

        if(recursive)
        {

        }
          //inspectFieldClasses( obj, ObjClass, objectsToInspect, recursive);

    }

    public String InspectClassName(Object obj)
    {
        Class ObjClass = obj.getClass();
        String className = "";
        if(ObjClass.isArray())
        {
            className = ObjClass.getName();
        }
        else
        {
            className = ObjClass.getName();
        }

        return className;
    }

    public void InspectBasicInfo(Class ObjClass)
    {
        String className = ObjClass.getName();
        Class immediateSuperClass = ObjClass.getSuperclass();
        Class[] theInternface = ObjClass.getInterfaces();

        System.out.println("name = " + className);
//        System.out.println("The name of the immediate superclass: " + immediateSuperClass);
//        System.out.println("The name of the immediate superclass: " + Arrays.toString(theTnternface));
    }

    public Method[] InspectMethod(Class ObjClass)
    {
        Method[] methods = ObjClass.getDeclaredMethods();

        for(Method m : methods)
        {
            method_exceptionTypes = m.getExceptionTypes();
            method_parameterTypes = m.getParameterTypes();
            method_returnType = m.getReturnType();
            method_modifier = m.getModifiers();

//            System.out.println("exceptions thrown in function " + m.getName() + " : " + Arrays.toString(exceptionTypes));
//            System.out.println("parameter type in function " + m.getName() + " : " + Arrays.toString(parameterTypes));
//            System.out.println("return type in function " + m.getName() + " : " + returnType);
//            System.out.println("the modifier in function " + m.getName() + " : " + Modifier.toString(modifier));
        }

        return methods;
    }

    public Constructor[] InspectConstructor(Class ObjClass)
    {
        Constructor[] constructors = ObjClass.getConstructors();

        for(Constructor c : constructors)
        {
            constructor_parameterTypes = c.getParameterTypes();
            constructor_modifier = c.getModifiers();
            //System.out.println("parameter types in function " + c.getName() + " : " + Arrays.toString(parameterTypes));
            //System.out.println("the modifier in function " + c.getName() + " : " + Modifier.toString(modifier));

        }

        return constructors;
    }

    public Field[] InspectField(Object obj, Class ObjClass)
    {
        Field[] fields = ObjClass.getDeclaredFields();
        for(Field f : fields)
        {
            Class field_type = f.getType();
            int field_modifier = f.getModifiers();

            try
            {
                if(!field_type.isPrimitive())
                {
                    //System.out.println("Object references, arrays or interfaces");
                    if(field_type.isArray())
                    {

                    }
                    else if(field_type.isInterface())
                    {
                        System.out.println("Interface");
                    }
                }
                else
                {
                    try
                    {
                        f.setAccessible(true);

                    }
                    catch (InaccessibleObjectException e)
                    {
                        System.out.println("Not accessible");
                    }
                    Object value = f.get(obj);
                    System.out.println(value);
                }
            }
            catch (IllegalAccessException e)
            {
                System.out.println(e);
            }
            catch (IllegalArgumentException e)
            {
                System.out.println(e);
            }
//            System.out.println("the type in the field " + f.getName() + " : " + type.getName());
//            System.out.println("the modifier in field " + f.getName() + " : " + Modifier.toString(modifier));

        }

        return fields;
    }

    public void InspectArray(Object obj, ArrayInfo info) {
        Class ObjClass = obj.getClass();
        Class componentType = ObjClass.getComponentType();
        int arrayLen = Array.getLength(obj);
        try
        {
            //System.out.println("Component type: " + componentType);
            for (int index = 0; index < arrayLen; index++) {
                Object arrobj = Array.get(obj, index);
                if (arrobj.getClass().isArray()) {
                    InspectArray(arrobj, info);
                } else {
                    //System.out.print(arrobj + " ");
                    info.ComponentType.put(componentType, 1);
                    info.Length++;
                }
            }
            //System.out.println();
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e)
        {
            System.out.println(e);
        }
    }
}
