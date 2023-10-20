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
    Vector objectsToInspect = new Vector();
    public void inspect(Object obj, boolean recursive) {
        this.objectsToInspect.addElement(obj);
        this.recursive = recursive;

        Class ObjClass = obj.getClass();
        this.objectsToInspect.addElement(obj);
        System.out.println("inside inspector: " + obj + " (recursive = " + recursive + ")");

        //inspect the current class
        //inspectFields(obj, ObjClass,objectsToInspect);

//        InspectBasicInfo(ObjClass);
//        InspectMethod(ObjClass);
//        InspectConstructor(ObjClass);
//        InspectField(obj, ObjClass);

        Enumeration e = this.objectsToInspect.elements();
        Object o = e.nextElement();
        while(e.hasMoreElements())
        {
            System.out.println("==============" + o + "==============");
            //InspectSupers(o);
            InspectBasicInfo(o);
            //InspectMethod(o);
            //InspectConstructor(o);
            //InspectField(o);
            o = e.nextElement();
        }

        if(recursive)
        {

        }
          //inspectFieldClasses( obj, ObjClass, objectsToInspect, recursive);

    }

    public void InspectSupers(Object obj)
    {
        Class immediateSuperClass = obj.getClass().getSuperclass();
        Class[] theInterface = obj.getClass().getInterfaces();

        while(immediateSuperClass != null || theInterface != null)
        {
            System.out.println("Superclass = " + immediateSuperClass);
            System.out.println("Interface = " + Arrays.toString(theInterface));

            immediateSuperClass = immediateSuperClass.getSuperclass();
            theInterface = immediateSuperClass.getInterfaces();
        }

        while(theInterface != null)
        {
        }
        //System.out.println("Interface = " + theInterface.toString());
    }

    public void InspectBasicInfo(Object obj)
    {
        String className = obj.getClass().getName();
        Class immediateSuperClass = obj.getClass().getSuperclass();
        Class[] theInterface = obj.getClass().getInterfaces();

        while(immediateSuperClass != null || theInterface.length > 0)
        {
            System.out.println("Superclass = " + immediateSuperClass);
            System.out.println("Interface = " + Arrays.toString(theInterface));

            this.objectsToInspect.addElement(immediateSuperClass);

            try
            {
                immediateSuperClass = immediateSuperClass.getSuperclass();

                theInterface = immediateSuperClass.getInterfaces();
            }
            catch (NullPointerException e){break;}
        }

        /*if(theInterface.length > 0)
        {
            for(Class i : theInterface)
            {
                if(!this.objectsToInspect.contains(i))
                {
                    System.out.println("Adding interface " + i);
                    this.objectsToInspect.addElement(i);
                }
            }
        }*/

        System.out.println("name = " + className);
        System.out.println("Immediate superclass: " + immediateSuperClass);
        System.out.println("Immediate interfaces: " + Arrays.toString(theInterface));

        System.out.println(this.objectsToInspect.toString());
    }

    public Method[] InspectMethod(Object obj)
    {
        Class ObjClass = obj.getClass();
        Method[] methods = ObjClass.getDeclaredMethods();

        for(Method m : methods)
        {
            method_exceptionTypes = m.getExceptionTypes();
            method_parameterTypes = m.getParameterTypes();
            method_returnType = m.getReturnType();
            method_modifier = m.getModifiers();

            System.out.println("exceptions thrown in function " + m.getName() + " : " + Arrays.toString(method_exceptionTypes));
            System.out.println("parameter type in function " + m.getName() + " : " + Arrays.toString(method_parameterTypes));
            System.out.println("return type in function " + m.getName() + " : " + method_returnType);
            System.out.println("the modifier in function " + m.getName() + " : " + Modifier.toString(method_modifier));
        }

        return methods;
    }

    public Constructor[] InspectConstructor(Object obj)
    {
        Class ObjClass = obj.getClass();
        Constructor[] constructors = ObjClass.getConstructors();

        for(Constructor c : constructors)
        {
            constructor_parameterTypes = c.getParameterTypes();
            constructor_modifier = c.getModifiers();
            System.out.println("parameter types in function " + c.getName() + " : " + Arrays.toString(constructor_parameterTypes));
            System.out.println("the modifier in function " + c.getName() + " : " + Modifier.toString(constructor_modifier));
        }

        return constructors;
    }

    public Field[] InspectField(Object obj)
    {
        Class ObjClass = obj.getClass();
        Field[] fields = ObjClass.getDeclaredFields();
        System.out.println(Arrays.toString(fields));

        for(Field f : fields)
        {
            Class field_type = f.getType();
            System.out.println(f.getName() + " " + field_type);
            int field_modifier = f.getModifiers();

            try
            {
                f.setAccessible(true);

            }
            catch (InaccessibleObjectException e)
            {
                System.out.println("Not accessible");
            }

            try
            {
                if(!field_type.isPrimitive())
                {
                    //System.out.println("Object references, arrays or interfaces");
                    if(field_type.isArray())
                    {
                        ArrayInfo arrayInfo = new ArrayInfo();
                        InspectArray(f.get(obj), arrayInfo);
/*                        System.out.println("Name = " + arrayInfo.name);
                        System.out.println("Component Type = " + arrayInfo.ComponentType.keySet());
                        System.out.println("Len = " + arrayInfo.Length);*/
                    }
/*                    else if(field_type.isInterface())
                    {
                        System.out.println("Interface");
                    }*/
                    else
                    {
                        System.out.println("Object reference " + ObjClass + " " + f.getName() + " " + f.hashCode());
                        //objectsToInspect.add(f);
                        this.objectsToInspect.addElement(field_type);
                    }
                }
                else
                {
                    Object value = f.get(obj);
                    System.out.println(value);
                }
            }
            catch (IllegalAccessException | IllegalArgumentException e)
            {
                System.out.println(e);
            }
//            System.out.println("the type in the field " + f.getName() + " : " + type.getName());
//            System.out.println("the modifier in field " + f.getName() + " : " + Modifier.toString(modifier));
        }
        return fields;
    }

    public void InspectArray(Object obj, ArrayInfo info)
    {
        Class ObjClass = obj.getClass();
        int arrayLen = Array.getLength(obj);
        info.Length = arrayLen;
        info.name = ObjClass.getName();

        InspectArrayContent(obj, info);
    }

    private void InspectArrayContent(Object obj, ArrayInfo info) {
        Class ObjClass = obj.getClass();
        Class componentType = ObjClass.getComponentType();
        int arrayLen = Array.getLength(obj);
        System.out.println("Component type = " + componentType + " name = " + info.name + " len = " + arrayLen + " objc = " + ObjClass);
        try
        {
            //System.out.println("Component type: " + componentType);
            for (int index = 0; index < arrayLen; index++) {
                Object arrobj = Array.get(obj, index);
                if (componentType.isArray()) {
                    InspectArrayContent(arrobj, info);
                } else {
                    System.out.print(arrobj + " ");
                    info.ComponentType.put(componentType, 1);
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
