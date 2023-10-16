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
    public void inspect(Object obj, boolean recursive) {
        Vector objectsToInspect = new Vector();
        Class ObjClass = obj.getClass();

        String className = ObjClass.getName();
        Class immediateSuperClass = ObjClass.getSuperclass();
        Class[] theTnternface = ObjClass.getInterfaces();
        Method[] methods = ObjClass.getDeclaredMethods();

        System.out.println("inside inspector: " + obj + " (recursive = " + recursive + ")");

//        System.out.println("The name of the ObjClass: " + className);
//        System.out.println("The name of the immediate superclass: " + immediateSuperClass);
//        System.out.println("The name of the immediate superclass: " + Arrays.toString(theTnternface));

        for(Method m : methods)
        {
            Class[] exceptionTypes = m.getExceptionTypes();
            Class[] parameterTypes = m.getParameterTypes();
            Class returnType = m.getReturnType();
            int modifier = m.getModifiers();

//            System.out.println("exceptions thrown in function " + m.getName() + " : " + Arrays.toString(exceptionTypes));
//            System.out.println("parameter type in function " + m.getName() + " : " + Arrays.toString(parameterTypes));
//            System.out.println("return type in function " + m.getName() + " : " + returnType);
//            System.out.println("the modifier in function " + m.getName() + " : " + Modifier.toString(modifier));
        }

        //inspect the current class
        //inspectFields(obj, ObjClass,objectsToInspect);

        //if(recursive)
        //  inspectFieldClasses( obj, ObjClass, objectsToInspect, recursive);

    }

    private String findClassName(){
        String className = "";



        return className;
    }
}
