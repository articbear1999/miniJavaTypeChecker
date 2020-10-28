import java.util.ArrayList;

public class PairIdMap {
    public PairIdMap(){}
    ArrayList<Param> firstMap = new ArrayList<Param>();
    IdMap secondMap = new IdMap();
    int index = 0;
    public ArrayList<Param> getKey()
    {
        return firstMap;
    }
    public IdMap getValue()
    {
        return secondMap;
    }
    public boolean pairIdEqual(PairIdMap a, PairIdMap b)
    {
        int lengthA = a.firstMap.size();
        int lengthB = b.firstMap.size();
        if(lengthA != lengthB)
            return false;
        else
        {
            for(int i = 0; i< lengthA; i++)
            {
                if(!a.firstMap.get(i).type.equals(b.firstMap.get(i).type))
                    return false;
            }
        }
        return true;
    }
}
