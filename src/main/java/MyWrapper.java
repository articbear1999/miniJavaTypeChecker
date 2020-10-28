import java.util.HashMap;
import java.util.Map;

public class MyWrapper {
    //class has variables and methods
    //variables are simple enough
    //methods have names, parameter list, vars and statements and return an expression
    //hash<class, (hash<var,type>, hash<method, <hash<paramlist,type>, type>>)>
    ClassMap myClass = new ClassMap();
    String className = "";
    String methodName = "";
    String methodType = "";
    String typeName = "";

    String nonScopeClassName = "";
    String nonScopeMethodName = "";
    String nonScopeMethodType = "";
    public MyWrapper(){
    }

    public void printCurScope()
    {
        System.out.println("class: " + className + " method: " + methodName + " type: " + methodType);
    }

    public void printNonCurScope()
    {
        System.out.println("class: " + nonScopeClassName + " method: " + nonScopeMethodName + " type: " + nonScopeMethodType);
    }

    public void resetNonCurScope()
    {
        nonScopeClassName = "";
        nonScopeMethodName = "";
        nonScopeMethodType = "";
    }

    public void changeCurScope(String classN, String methodN, String methoT)
    {
        className = classN;
        methodName = methodN;
        methodType = methoT;
    }

    public void changeNonCurScope(String classN, String methodN, String methoT)
    {
        nonScopeClassName = classN;
        nonScopeMethodName = methodN;
        nonScopeMethodType = methoT;
    }
    public void print()
    {
        System.out.println(myClass.print());
    }

    public void addClass(String name)
    {
        myClass.addClass(name);
        className = name;
    }

    public void setType(String name)
    {
        typeName = name;
    }

    public void addId(String id, String type)
    {
        if (methodName == "")
            myClass.myMap.get(className).getKey().addId(id,type);
        else
            myClass.myMap.get(className).getValue().addId(methodName, methodType, id, type);
    }

    public void addFormal(String id, String type)
    {
        myClass.myMap.get(className).getValue().addFormal(methodName, methodType, id, type);
    }

    public void addMethod(String methodName, String type)
    {
        myClass.myMap.get(className).getValue().addMethod(methodName,type);
    }

    public void addExtendClass(String current, String parent)
    {
        myClass.addExtendClass(current, parent);
    }

    public boolean classExists(String classId)
    {
        return myClass.contains(classId);
    }

    //check if method exists in current class, then superclass
    public boolean methodExists(String className, String methodName)
    {
        //In currentClass then check super class
        boolean inCurrent = myClass.myMap.get(className).getValue().methodExists(methodName);
        return inCurrent;
        //this will get the PairSub
    }
    public boolean methodExistsSuper(String className, String methodName)
    {
        boolean inSuper = myClass.methodExists(className,methodName);
        return inSuper;
    }

    //set Index of formal params
    public void setIndex(int index)
    {
        myClass.myMap.get(nonScopeClassName).getValue().setIndex(index, nonScopeMethodName, nonScopeMethodType);
    }
    public int getIndex(){
        return myClass.myMap.get(nonScopeClassName).getValue().getIndex(nonScopeMethodName,nonScopeMethodType);
    }

    public void iterateIndex()
    {
        myClass.myMap.get(nonScopeClassName).getValue().iterateIndex(nonScopeMethodName, nonScopeMethodType);
    }

    //get method type
    public String getMethodType(String className, String methodName)
    {
        /*String type = myClass.myMap.get(className).getValue().getType(methodName);
        if(type != "")
            return type;
        else*/
        return myClass.retMType(className,methodName);
    }
    //get method class for supers
    public String getMethodClass(String className, String methodName)
    {
        String classVal= myClass.retMClass(className,methodName);
        //System.err.println(classVal);
        return classVal;
    }

    //get method parameter length
    public int getParamLength(String className, String methodName, String methodType)
    {
        return myClass.myMap.get(className).getValue().paramLength(methodName, methodType);
    }

    //check if an identifier exists, must search through the method one, then the classone
    //must check super classes as well
    public boolean idExistsCurScope(String id)
    {
        boolean inMethod = false;
        if(!methodName.equals("")) {
            //check if its in method variables/parameters
            inMethod = myClass.myMap.get(className).getValue().idExists(methodName, methodType, id);
        }
        //check if its in class variables
        boolean inClass = myClass.myMap.get(className).getKey().idExists(id);
        boolean inSuperClass = checkSCV(className,id);
        return inMethod || inClass || inSuperClass;
    }

    public String idTypeCurScope(String id)
    {
        //check if its in method variables/parameters
        //printNonCurScope();
        String inMethod = "";
        String inClass = "";
        String inSuper = "";
        if(!methodName.equals("")) {
            inMethod = myClass.myMap.get(className).getValue().idType(methodName, methodType, id);
        }
        if(!(inMethod.equals("")))
            return inMethod;
        //check if its in class variables
        inClass = myClass.myMap.get(className).getKey().idType(id);
        if(!(inClass.equals("")))
            return inClass;
        //check if its in super variables
        inSuper = getSCV(className, id);
        return inSuper;
    }

    public boolean paramValid(String subclass)
    {
        //printNonCurScope();
        return myClass.paramValid(nonScopeClassName, nonScopeMethodName, nonScopeMethodType, subclass);
    }

    public boolean isSubType(String classA, String classB)
    {
        return myClass.isSubtype(classA,classB);
    }

    public boolean checkSCV(String classN,String id)
    {
        return myClass.checkSCVWrapper(classN, id);
    }

    public String getSCV(String classN,String id)
    {
        return myClass.getSCVWrapper(classN, id);
    }

}
