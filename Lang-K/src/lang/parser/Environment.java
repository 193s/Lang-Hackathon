package lang.parser;

import java.util.HashMap;

public class Environment {
    Environment outer;
	public HashMap<String, Object> map = new HashMap<>();

    public Environment(Environment outer) {
        this.outer = outer;
    }

    public boolean find(String key) {
        return map.containsKey(key)
               || ((outer != null) && outer.find(key));
    }

    public Object get(String key) {
        return map.containsKey(key)? map.get(key)
               : outer != null? outer.get(key)
               : null;
    }

    public void put(String key, Object o) {
        if (find(key)) {
            if (map.containsKey(key))
                map.put(key, o);
            else
                outer.put(key, o);
        }
        else {
            map.put(key, o);
        }
    }
}
