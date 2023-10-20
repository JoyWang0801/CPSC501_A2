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
        System.out.println("inside inspector: " + obj + " (recursive = " + recursive + ")");

        InspectBasicInfo(obj);
//        InspectMethod(ObjClass);
//        InspectConstructor(ObjClass);
//        InspectField(obj, ObjClass);

        Enumeration e = this.objectsToInspect.elements();

        //while(e.hasMoreElements())
        //{
          //  Object o = e.nextElement();
           // System.out.println("==============" + o + "==============");
            //InspectMethod(o);
            //InspectConstructor(o);
            //InspectField(o);
        //}

        if(recursive)
        {

        }
          //inspectFieldClasses( obj, ObjClass, objectsToInspect, recursive);
        System.out.println(this.objectsToInspect.toString());

    }

    public void InspectBasicInfo(Object obj)
    {
        String className = obj.getClass().getName();
        Class immediateSuperClass = obj.getClass().getSuperclass();
        //System.out.println(className + " Superclass = " + immediateSuperClass);
        Class[] theInterface = obj.getClass().getInterfaces();
       // System.out.println(className + " Interface = " + Arrays.toString(theInterface));

        Vector<Class> superclassAndinterface = new Vector<>();
        superclassAndinterface.addElement(obj.getClass());
        //if (immediateSuperClass != null) superclassAndinterface.addElement(immediateSuperClass);
        //for (Class i : theInterface) superclassAndinterface.addElement(i);

        for (int index = 0; index < superclassAndinterface.size(); index++)
        {
            Class c = superclassAndinterface.elementAt(index);

            // Update superclass
            try
            {
                className = c.getName();
                System.out.println("==============" + className + "==============");
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
                    InspectField(c, c.getModifiers());

                }
            }
            catch (NullPointerException e){;}

        }
    }

    public Method[] InspectMethod(Class ObjClass)
    {
        Method[] methods = ObjClass.getDeclaredMethods();
        System.out.println("==============METHOD==============");
        for(Method m : methods)
        {
            System.out.println("ーーーー" + m.getName() + "ーーーー");
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
            System.out.println("ーーーー" + c.getName() + "ーーーー");
            constructor_parameterTypes = c.getParameterTypes();
            constructor_modifier = c.getModifiers();
            System.out.println("|ーparameter types = " + Arrays.toString(constructor_parameterTypes));
            System.out.println("|ーthe modifier = " + Modifier.toString(constructor_modifier));
        }

        return constructors;
    }

    public Field[] InspectField(Class ObjClass, Object obj) {
        System.out.println("==============FIELD==============");
        int modifier = ObjClass.getModifiers();
        Boolean isAbtract = Modifier.isAbstract(modifier);
        Field[] fields = ObjClass.getDeclaredFields();

        for(Field f : fields)
        {
            Class field_type = f.getType();
            int field_modifier = f.getModifiers();
            System.out.println("ーーーー" + f.getName() + "ーーーー");
            System.out.println("|ー" + field_type);
            System.out.println("|ー" + Modifier.toString(field_modifier));

            // Access private
            try
            {
                f.setAccessible(true);

            }
            catch (InaccessibleObjectException e)
            {
                System.out.println("Not accessible");
            }


            // Skip abstract classes
            if (!isAbtract){
                try
                {
                    if(!field_type.isPrimitive())
                    {
                        //System.out.println("Object references, arrays or interfaces");
                        if(field_type.isArray())
                        {
                            ArrayInfo arrayInfo = new ArrayInfo();
                            InspectArray(f.get(obj), arrayInfo);
                            System.out.println("Name = " + arrayInfo.name);
                            System.out.println("Component Type = " + arrayInfo.ComponentType.keySet());
                            System.out.println("Len = " + arrayInfo.Length);
                        }
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
