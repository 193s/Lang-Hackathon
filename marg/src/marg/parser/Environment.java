package marg.parser;

import marg.lang.data.IType;
import marg.lang.data.MNull;

import java.util.HashMap;

public class Environment {
    Environment outer;
	public HashMap<String, IType> map = new HashMap<>();

    public Environment(Environment outer) {
        this.outer = outer;
    }

    public boolean find(String key) {
        return map.containsKey(key)
               || ((outer != null) && outer.find(key));
    }

    public IType get(String key) {
        return map.containsKey(key)? map.get(key)
               : outer != null?      outer.get(key)
               : new MNull();
    }

    public void put(String key, IType o) {
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
