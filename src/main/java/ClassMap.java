import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ClassMap {
    //class has variables and methods
    //variables are simple enough
    //methods have names, parameter list, vars and statements and return an expression
    //hash<class, (hash<var,type>, hash<method, <hash<paramlist,type>, type>>)>
    HashMap<String, PairSub> myMap = new HashMap<>();
    HashMap<String, ArrayList<String>> linkSet = new HashMap<>();
    HashMap<String, ArrayList<String>> subClass = new HashMap<>();
    public ClassMap(){}
    public boolean contains(String className) {
        return myMap.containsKey(className) ;
    }
    public void addClass(String className)
    {
        if(myMap.containsKey(className))
        {
            System.out.println("Type error");
            System.exit(0);
        }
        IdMap tempId = new IdMap();
        MethodMap tempMeth = new MethodMap();
        PairSub tempPair = new PairSub();
        myMap.put(className,tempPair);
    }
    public void addExtendClass(String current, String parent)
    {
        if(linkSet.containsKey(current)) {
            linkSet.get(current).add(parent);
            subClass.get(current).add(parent);
        }
        else {
            ArrayList<String> temp1 = new ArrayList<String>();
            ArrayList<String> temp2 = new ArrayList<String>();
            if(parent.equals("")) {
            }
            else {
                temp1.add(parent);
                temp2.add(parent);
            }
            linkSet.put(current,temp1);
            subClass.put(current,temp2);
        }
    }
    public String print()
    {
        String retString = "";
        for (Map.Entry<String, PairSub> e : myMap.entrySet())
        {
            retString = retString.concat("class: " + e.getKey() + "\n" + "Identifiers/Types :" + "\n" + e.getValue().getKey().print()
                    + "\n" + "Methods: " + e.getValue().getValue().print());
        }
        return retString;
    }
    public void printLinkSet()
    {
        String retVal = "";
        for (Map.Entry<String, ArrayList<String>> e : subClass.entrySet())
        {
            retVal = retVal.concat(e.getKey() + " ");
            ArrayList<String> tem = e.getValue();
            for( int i = 0; i< tem.size(); i ++)
            {
                retVal = retVal.concat("its list: " + tem.get(i));
            }
        }
        System.out.println(retVal);
    }
    public void isAcyclic()
    {
        //HashMap<String, ArrayList<String>> temp = new HashMap<>();
        //temp.putAll(linkSet);
        String key = "";
        boolean shouldBreak = true;
        while (linkSet.size() != 0)
        {
            shouldBreak = true;
            for (Map.Entry<String, ArrayList<String>> e : linkSet.entrySet())
            {
                //find the one with arraylist length one, remove and redo
                if(e.getValue().size() == 0)
                {
                    key = e.getKey();
                    linkSet.remove(key);
                    //remove all entries that contain that set
                    for (Map.Entry<String, ArrayList<String>> k : linkSet.entrySet())
                    {
                        //continue if it is already 0
                        if(k.getValue().size() == 0)
                            continue;
                        if(k.getValue().contains(key))
                        {
                            k.getValue().remove(key);
                        }
                    }
                    shouldBreak = false;
                    break;
                }
            }
            //if it goes through the whole thing and there's still stuff left, then it has a cycle
            if(shouldBreak && linkSet.size()!=0)
            {
                System.out.println("Type Error");
                System.exit(0);
            }
        }
    }

    public boolean isSubtype(String LHS, String RHS)
    {
        //Depth first search all of parents of A to find if it equals subtype
        ArrayList<String> temp = subClass.get(RHS);
        if(LHS.equals(RHS))
            return true;
        if(LHS.equals("int") || LHS.equals("boolean")||LHS.equals("int[]"))
            return false;
        for(int i = 0; i< temp.size(); i++)
        {
            if(temp.get(i).equals(LHS)) //LHS is parent of RHS
                return true;
            else
                isSubtype(temp.get(i),LHS);
        }
        return false;
    }

    public boolean paramValid(String className, String method, String methodType, String type)
    {
        String paraType = myMap.get(className).getValue().paramType(method,methodType);
        if(isSubtype(paraType,type))
            return true;
        else
            return false;
    }

    public boolean checkSCV(String classN, String id)
    {
        //This'll get all the parents of the class
        ArrayList<String> parentList = subClass.get(classN);
        for(int i = 0; i < parentList.size(); i++)
        {
            //get the variables from the parents
            ArrayList<HashMap<String,String>> parentId = myMap.get(parentList.get(i)).getKey().idArray;
            for(int j = 0; j < parentId.size(); j++)
            {
                for(Map.Entry<String, String> e : parentId.get(j).entrySet()) {
                    if (e.getKey().equals(id)) {
                        return true;
                    }
                }
            }
            //recursively go up
            if(checkSCV(parentList.get(i), id))
                return true;
        }
        return false;
    }

    public boolean checkSCVWrapper(String classN, String id)
    {
        for (Map.Entry<String, ArrayList<String>> e : subClass.entrySet())
        {
            //add super class variables to all classes
            if(e.getKey().equals(classN))
            {
                if(checkSCV(classN, id)) {
                    return true;
                }
            }
        }
        return false;
    }

    public String getSCV(String classN, String id)
    {
        //This'll get all the parents of the class
        ArrayList<String> parentList = subClass.get(classN);
        String retType = "";
        for(int i = 0; i < parentList.size(); i++)
        {
            //get the variables from the parents, add to the child
            //append parent to the end of child's
            ArrayList<HashMap<String,String>> parentId = myMap.get(parentList.get(i)).getKey().idArray;
            for(int j = 0; j < parentId.size(); j++)
            {
                for(Map.Entry<String, String> e : parentId.get(j).entrySet())
                    if(e.getKey().equals(id))
                        return e.getValue();
            }
            //recursively go up if not in closest scope
            retType = getSCV(parentList.get(i), id);
            if(!retType.equals(""))
                return retType;
        }
        return retType;
    }

    public String getSCVWrapper(String classN, String id)
    {
        for (Map.Entry<String, ArrayList<String>> e : subClass.entrySet())
        {
            //add super class variables to all classes
            if(e.getKey().equals(classN))
            {
                if(!getSCV(classN,id).equals(""))
                    return (getSCV(classN, id));
            }
        }
        return "";
    }

    public boolean methodExists(String classN, String methodN)
    {
        ArrayList<String> parentList = subClass.get(classN);
        MethodMap cur = myMap.get(classN).getValue();
        if(cur.methodExists(methodN)) {
            return true;
        }
        for(int i = 0; i < parentList.size(); i++)
        {
            //recursively go up
            //check if method exists in the current class
            //if it doesn't check the parents
            if(methodExists(parentList.get(i), methodN))
                return true;
        }
        return false;
    }

    //return the Method type, mainly for super classes, but works on normal ones too.
    public String retMType(String classN, String methodN)
    {
        ArrayList<String> parentList = subClass.get(classN);
        MethodMap cur = myMap.get(classN).getValue();
        if(cur.methodExists(methodN))
            return cur.getType(methodN);
        for(int i = 0; i < parentList.size(); i++)
        {
            //recursively go up
            //check if method exists in the current class
            //if it doesn't check the parents
            String retVal = retMType(parentList.get(i), methodN);
            if(!retVal.equals(""))
                return retVal;
        }
        return "";
    }

    public String retMClass(String classN, String methodN)
    {
        ArrayList<String> parentList = subClass.get(classN);
        MethodMap cur = myMap.get(classN).getValue();
        if(cur.methodExists(methodN)) {
            return classN;
        }
        for(int i = 0; i < parentList.size(); i++)
        {
            //recursively go up
            //check if method exists in the current class
            //if it doesn't check the parents
            String retVal = retMClass(parentList.get(i), methodN);
            if(!retVal.equals("")) {
                return retVal;
            }
        }
        return "";
    }

    public void overLoads()
    {
        //Go through all classes and check that they don't overload their super classes, or their ancestors
        for (Map.Entry<String, PairSub> e : myMap.entrySet())
        {
            //get the class
            String baseClass = e.getKey();
            //check for every method in the class
            MethodMap curMMap = e.getValue().getValue();
            String methodName = "";
            String methodType = "";
            PairIdMap formalP;
            //loops through all methods
            for (Map.Entry<HashMap<String, String> , PairIdMap> k : curMMap.mMap.entrySet()) {
                HashMap<String,String> curMethod = k.getKey();
                //get the method parameters
                formalP = k.getValue();
                for(Map.Entry<String,String> j : curMethod.entrySet())
                {
                    methodName = j.getKey();
                    methodType = j.getValue();
                }
                if(willOverload(baseClass, methodName, methodType, formalP))
                {
                    System.out.println("Type error");
                    System.exit(1);
                }
            }
            //for every method, check that it doesn't overload another
        }
    }


    public boolean willOverload(String classN, String methodN, String methodT, PairIdMap formalP)
    {
        //    HashMap<String, ArrayList<String>> subClass = new HashMap<>();
        //check every ancestor's
        ArrayList<String> parentL = subClass.get(classN);
        String parentClass = "";
        //no parents
        if(parentL.size() == 0)
            return false;
        else
            parentClass = parentL.get(0);
        MethodMap parentMethod = myMap.get(parentClass).getValue();
        for (Map.Entry<HashMap<String, String> , PairIdMap> e : parentMethod.mMap.entrySet()) {
            HashMap<String,String> curMethod = e.getKey();
            for(Map.Entry<String,String> k : curMethod.entrySet())
            {
                //if the current method name and types are the same, do something
                //else continue
                if(!k.getKey().equals(methodN) || !k.getValue().equals(methodT))
                    break;
                //check that there formal parameters are the same
                if(formalP.pairIdEqual(formalP,e.getValue()))
                    return true;
            }
        }
        if(willOverload(parentClass,methodN,methodT,formalP))
            return true;
        return false;
    }


}
