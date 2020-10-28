import java.util.HashMap;
import java.util.Map;
import java.util.ArrayList;

//For formal parameters and also identifiers within classes
//so from class to id
//from method to formal params
public class IdMap {
    HashMap<String, String> idMap = new HashMap<>();
    ArrayList<HashMap<String,String>> idArray = new ArrayList<HashMap<String,String>>();
    public IdMap(){}
    //figure out someway to get distinct formal parameters and class variables
    public void addId(String id, String type)
    {
        if(idMap.containsKey(id))
        {
            System.out.println("Type error");
            System.exit(0);
        }
        idMap.put(id,type);
        HashMap<String,String> temp = new HashMap<>();
        temp.put(id,type);
        idArray.add(temp);
    }

    public String print()
    {
        String retVal = "";
        for (int i = 0; i < idArray.size(); i++) {
            for (Map.Entry<String, String> e : idArray.get(i).entrySet()) {
                retVal = retVal.concat("Identifier: " + e.getKey() + " Type: " + e.getValue() + "\n");
            }
        }
        return retVal;
    }

    public boolean idExists(String id)
    {
        for(int i = idArray.size() - 1; i >=0; i--)
        {
            if(idArray.get(i).containsKey(id))
                return true;
        }
        return false;
    }

    public String idType(String id)
    {
        for(int i = idArray.size() - 1; i >=0; i--)
        {
            if(idArray.get(i).containsKey(id))
                return idArray.get(i).get(id);
        }
        return "";
    }

}
