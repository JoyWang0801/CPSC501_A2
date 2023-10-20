import com.sun.nio.sctp.IllegalReceiveException;

import javax.management.RuntimeErrorException;
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

    boolean recursive = false;
    Vector<Class> superclassAndinterface = new Vector<>();

    public void inspect(Object obj, boolean recursive) {
        this.recursive = recursive;
        System.out.println("inside inspector: " + obj + " (recursive = " + recursive + ")");

        InspectBasicInfo(obj);
          //inspectFieldClasses( obj, ObjClass, objectsToInspect, recursive);
        System.out.println(this.superclassAndinterface.toString());

    }

    public void InspectBasicInfo(Object obj)
    {
        Class immediateSuperClass;
        Class[] theInterface;
        superclassAndinterface.addElement(obj.getClass());

        for (int index = 0; index < superclassAndinterface.size(); index++)
        {
            Class c = superclassAndinterface.elementAt(index);

            // Update superclass
            try
            {
                System.out.println("==============CLASS==============");
                System.out.println("ーーーーclass name: " + c.getName() + "ーーーー");
                immediateSuperClass = c.getSuperclass();
                theInterface = c.getInterfaces();
                System.out.println("Superclass = " + immediateSuperClass);
                System.out.println("Interface = " + Arrays.toString(theInterface));
                if (immediateSuperClass != null) superclassAndinterface.addElement(immediateSuperClass);
                for (Class i : theInterface) superclassAndinterface.addElement(i);

                //InspectMethod(c);
                //InspectConstructor(c);
                if (Modifier.isAbstract(c.getModifiers()))
                {
                    InspectField(c, obj);
                }
                else
                {
                    InspectField(c, c.newInstance());

                }
            }
            catch (NullPointerException | IllegalAccessException | InstantiationException e)
            {
                throw new RuntimeException(e.getMessage());
            }

        }
    }

    public Method[] InspectMethod(Class ObjClass)
    {
        Method[] methods = ObjClass.getDeclaredMethods();
        System.out.println("==============METHOD==============");
        for(Method m : methods)
        {
            System.out.println("ーーーー method name: " + m.getName() + "ーーーー");
            method_exceptionTypes = m.getExceptionTypes();
            method_parameterTypes = m.getParameterTypes();
            method_returnType = m.getReturnType();
            method_modifier = m.getModifiers();

            System.out.println("|ーThe exception thrown =  " + Arrays.toString(method_exceptionTypes));
            System.out.println("|ーparameter type = " + Arrays.toString(method_parameterTypes));
            System.out.println("|ーreturn type = " + method_returnType);
            System.out.println("|ーthe modifier = " + Modifier.toString(method_modifier));
        }

        return methods;
    }

    public Constructor[] InspectConstructor(Class ObjClass)
    {
        Constructor[] constructors = ObjClass.getConstructors();
        System.out.println("==============Constructor==============");

        for(Constructor c : constructors)
        {
            System.out.println("ーーーー constructor name: " + c.getName() + "ーーーー");
            constructor_parameterTypes = c.getParameterTypes();
            constructor_modifier = c.getModifiers();
            System.out.println("|ーparameter types = " + Arrays.toString(constructor_parameterTypes));
            System.out.println("|ーthe modifier = " + Modifier.toString(constructor_modifier));
        }

        return constructors;
    }

    public Field[] InspectField(Class ObjClass, Object obj) throws IllegalAccessException {
        System.out.println("==============FIELD==============");
        int modifier = ObjClass.getModifiers();
        Boolean isAbtract = Modifier.isAbstract(modifier);

        Field[] fields = ObjClass.getDeclaredFields();
        for(Field f : fields)
        {
            Class field_type = f.getType();
            int field_modifier = f.getModifiers();
            System.out.println("ーーーー field name: " + f.getName() + "ーーーー");
            System.out.println("|ーField type = " + field_type);
            System.out.println("|ーField modifier = " + Modifier.toString(field_modifier));

            try
            {
                f.setAccessible(true);
            }
            catch (InaccessibleObjectException e)
            {
                System.out.println("Not accessible");
                continue;
            }

            // Skip abstract classes
            if (!isAbtract){
                try
                {
                    if(!field_type.isPrimitive())
                    {
                        if(field_type.isArray())
                        {
                            ArrayInfo arrayInfo = new ArrayInfo();
                            InspectArray(f.get(obj), arrayInfo);
                        }
                        else
                        {
                            System.out.println("Object reference " + f.getClass() + " " + f.getName() + " " + f.hashCode());
                        }

                        if(this.recursive)
                        {
                            superclassAndinterface.addElement(field_type);
                        }
                    }
                    else
                    {
                        Object value = f.get(obj);
                        System.out.println("|ーValue = " + value);
                    }
                }
                catch (IllegalAccessException e)
                {
                    //System.out.println(e);
                    throw new RuntimeException(e.getMessage());
                }
                catch (IllegalArgumentException e)
                {
                    throw new RuntimeException(e.getMessage());
                }
            }
        }
        return fields;
    }

    public void InspectArray(Object obj, ArrayInfo info)
    {
        Class ObjClass = obj.getClass();
        int arrayLen = Array.getLength(obj);
        info.Length = arrayLen;
        info.name = ObjClass.getName();
        System.out.println("------Array Content:-------");
        InspectArrayContent(obj, info);
        System.out.println("---------------------------");
        System.out.println("|ーName = " + info.name);
        System.out.println("|ーComponent Type = " + info.ComponentType.keySet());
        System.out.println("|ーArray Length = " + info.Length);
        System.out.println("---------------------------");
    }

    private void InspectArrayContent(Object obj, ArrayInfo info) {
        Class ObjClass = obj.getClass();
        Class componentType = ObjClass.getComponentType();
        int arrayLen = Array.getLength(obj);
        //System.out.println("Component type = " + componentType + " name = " + info.name + " len = " + arrayLen + " objc = " + ObjClass);
        try
        {
            //System.out.println("Component type: " + componentType);
            for (int index = 0; index < arrayLen; index++) {
                Object arrobj = Array.get(obj, index);
                if (componentType.isArray()) {
                    InspectArrayContent(arrobj, info);
                   // Object[] obj = (Object[]) arrobj;
                } else {
                    System.out.print(arrobj + " ");
                    info.ComponentType.put(componentType, 1);
                }
            }
            System.out.println();
        }
        catch (IllegalArgumentException | ArrayIndexOutOfBoundsException e)
        {
            System.out.println(e);
        }
    }
}
