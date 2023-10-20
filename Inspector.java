import com.sun.nio.sctp.IllegalReceiveException;

import javax.management.RuntimeErrorException;
import java.util.*;
import java.lang.reflect.*;

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
    HashSet<Class> HashSuperclassAndinterface = new HashSet<>();

    public void inspect(Object obj, boolean recursive) {
        this.recursive = recursive;
        System.out.println("inside inspector: " + obj + " (recursive = " + recursive + ")");

        InspectBasicInfo(obj);
    }

    public void InspectBasicInfo(Object obj)
    {
        Class immediateSuperClass;
        Class[] theInterface;
        HashSuperclassAndinterface.add(obj.getClass());
        superclassAndinterface = new Vector<>(HashSuperclassAndinterface);
        for (int index = 0; index < superclassAndinterface.size(); index++)
        {
            Class objClassToInspect = superclassAndinterface.elementAt(index);
            // Update superclass
            try
            {
                System.out.println("==============CLASS==============");
                System.out.println("ーーーーclass name: " + objClassToInspect.getName() + "ーーーー");
                immediateSuperClass = objClassToInspect.getSuperclass();
                theInterface = objClassToInspect.getInterfaces();
                System.out.println("Superclass = " + immediateSuperClass);
                System.out.println("Interface = " + Arrays.toString(theInterface));
                if (immediateSuperClass != null && !HashSuperclassAndinterface.contains(immediateSuperClass))
                {
                    updateList(immediateSuperClass);
                }
                for (Class i : theInterface)
                {
                    if (!HashSuperclassAndinterface.contains(i))
                    {
                        updateList(i);
                    }
                }

                if(objClassToInspect.isArray())
                {
                    ArrayInfo info = new ArrayInfo();
                    InspectArray(obj, info);
                }
                InspectMethod(objClassToInspect);
                InspectConstructor(objClassToInspect);
                if (Modifier.isAbstract(objClassToInspect.getModifiers()))
                {
                    InspectField(objClassToInspect, obj);
                }
                else
                {
                    InspectField(objClassToInspect, objClassToInspect.newInstance());

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
        for(Method method : methods)
        {
            System.out.println("ーーーー method name: " + method.getName() + "ーーーー");
            method_exceptionTypes = method.getExceptionTypes();
            method_parameterTypes = method.getParameterTypes();
            method_returnType = method.getReturnType();
            method_modifier = method.getModifiers();

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

        for(Constructor cons : constructors)
        {
            System.out.println("ーーーー constructor name: " + cons.getName() + "ーーーー");
            constructor_parameterTypes = cons.getParameterTypes();
            constructor_modifier = cons.getModifiers();
            System.out.println("|ーparameter types = " + Arrays.toString(constructor_parameterTypes));
            System.out.println("|ーthe modifier = " + Modifier.toString(constructor_modifier));
        }

        return constructors;
    }

    private void updateList(Class clsToUpdate)
    {
        HashSuperclassAndinterface.add(clsToUpdate);
        superclassAndinterface.addElement(clsToUpdate);
    }

    public Field[] InspectField(Class ObjClass, Object obj) throws IllegalAccessException {
        System.out.println("==============FIELD==============");
        int modifier = ObjClass.getModifiers();

        Field[] fields = ObjClass.getDeclaredFields();
        for(Field field : fields)
        {
            Class field_type = field.getType();
            int field_modifier = field.getModifiers();
            System.out.println("ーーーー field name: " + field.getName() + "ーーーー");
            System.out.println("|ーField type = " + field_type);
            System.out.println("|ーField modifier = " + Modifier.toString(field_modifier));

            try
            {
                field.setAccessible(true);
            }
            catch (InaccessibleObjectException e)
            {
                System.out.println("Field is not accessible");
                continue;
            }

            Boolean isAbtract = Modifier.isAbstract(modifier);
            // Skip abstract classes
            if (!isAbtract){
                try
                {
                    if(!field_type.isPrimitive())
                    {
                        if(field_type.isArray())
                        {
                            ArrayInfo arrayInfo = new ArrayInfo();
                            InspectArray(field.get(obj), arrayInfo);
                        }
                        else if(this.recursive)
                        {
                            if(!HashSuperclassAndinterface.contains(field_type)) {
                                updateList(field_type);
                                System.out.println(field_type + " is added to be inspect");
                            }
                        }
                        else
                        {
                            System.out.println("Object reference " + field.getClass() + " " + field.getName() + " with hashcode: " + field.hashCode());
                        }
                    }
                    else
                    {
                        Object value = field.get(obj);
                        System.out.println("|ーValue = " + value);
                    }
                }
                catch (IllegalAccessException | IllegalArgumentException e)
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
        for (Class cls : info.ComponentType.keySet())
        {
            if(!HashSuperclassAndinterface.contains(cls) && this.recursive)
            {
                updateList(cls);
            }
        }
    }

    private void InspectArrayContent(Object obj, ArrayInfo info) {
        Class ObjClass = obj.getClass();
        Class componentType = ObjClass.getComponentType();
        int arrayLen = Array.getLength(obj);
        try
        {
            for (int index = 0; index < arrayLen; index++) {
                Object arrobj = Array.get(obj, index);
                if (componentType.isArray()) {
                    InspectArrayContent(arrobj, info);
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
