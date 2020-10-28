import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

//Method Map, will map from a method name, to the formal params to its own type
public class MethodMap {
    HashMap<HashMap<String,String>, PairIdMap> mMap = new HashMap<>();
    public MethodMap(){}

    public void addMethod(String name, String type)
    {
        HashMap<String,String> temp = new HashMap<>();
        temp.put(name,type);
        //If method already exists
        if(mMap.containsKey(temp))
        {
            //ln("from methods");
            System.out.println("Type error");
            System.exit(0);
        }
        PairIdMap tempId = new PairIdMap();
        mMap.put(temp, tempId);
    }

    public String print()
    {
        String retVal = "";
        for (Map.Entry<HashMap<String, String> , PairIdMap> e : mMap.entrySet()) {
            for (Map.Entry<String,String> k: e.getKey().entrySet())
            {
                retVal = retVal.concat("Method Name: " + k.getKey() + " Type: " + k.getValue() + "\n");
                ArrayList<Param> temp = e.getValue().getKey();
                for (int i = 0; i< temp.size(); i++ )
                {
                    retVal = retVal.concat("Method Formal Param: \n" + "Name: " + temp.get(i).id);
                    retVal = retVal.concat(" Type: " + temp.get(i).type + "\n");
                }
                retVal = retVal.concat("Method Regular Identifiers: \n") + e.getValue().getValue().print();
                retVal = retVal + "\n" + "\n";
            }
        }
        return retVal;
    }

    //add id and type
    public void addId(String methodName, String methodType, String identifier, String type)
    {
        HashMap<String,String> temp = new HashMap<>();
        temp.put(methodName,methodType);
        mMap.get(temp).getValue().addId(identifier,type);
    }

    //add to both formal and regular
    public void addFormal(String methodName, String methodType, String identifier, String type)
    {
        HashMap<String,String> temp = new HashMap<>();
        temp.put(methodName,methodType);
        //add to both formal params and informal params
        Param tempParam = new Param(identifier,type);
        mMap.get(temp).getKey().add(tempParam);
        mMap.get(temp).getValue().addId(identifier,type);
    }

    //check if method exists
    public boolean methodExists(String methodName)
    {
        for (Map.Entry<HashMap<String, String> , PairIdMap> e : mMap.entrySet()) {
            for (Map.Entry<String,String> k: e.getKey().entrySet())
            {
                if(k.getKey().equals(methodName))
                    return true;
            }
        }
        return false;
    }

    //get the method type back
    public String getType(String methodName)
    {
        for (Map.Entry<HashMap<String, String> , PairIdMap> e : mMap.entrySet()) {
            for (Map.Entry<String,String> k: e.getKey().entrySet())
            {
                if(k.getKey().equals(methodName))
                    return k.getValue();
            }
        }
        return "";
    }

    public void setIndex(int index, String method, String methodType)
    {
        HashMap<String,String> temp = new HashMap<>();
        temp.put(method,methodType);
        mMap.get(temp).index = index;
    }

    public void iterateIndex(String method, String methodType)
    {
        HashMap<String,String> temp = new HashMap<>();
        temp.put(method,methodType);
        mMap.get(temp).index += 1;
    }
    public int getIndex(String method, String methodType)
    {
        HashMap<String,String> temp = new HashMap<>();
        temp.put(method,methodType);
        return mMap.get(temp).index;
    }
    public String paramType(String method, String methodType)
    {
        //get the param type
        HashMap<String,String> temp = new HashMap<>();
        temp.put(method,methodType);
        ArrayList<Param> formalParams = mMap.get(temp).getKey();
        return formalParams.get(mMap.get(temp).index).type;
    }
    public int paramLength(String method, String methodType)
    {
        HashMap<String,String> temp = new HashMap<>();
        temp.put(method,methodType);
        return mMap.get(temp).getKey().size();
    }

    public boolean idExists(String methodName, String type, String id)
    {
        HashMap<String,String> temp = new HashMap<>();
        temp.put(methodName,type);
        //get the idList for the methods
        IdMap variableList = mMap.get(temp).getValue();
        return variableList.idExists(id);
    }

    //Get id type
    public String idType(String methodName, String type, String id)
    {
        HashMap<String,String> temp = new HashMap<>();
        temp.put(methodName,type);
        //get the idList for the methods
        IdMap variableList = mMap.get(temp).getValue();
        return variableList.idType(id);
    }
}
