package symboltable;

import java.util.Dictionary;
import java.util.Hashtable;

public class Symbol {

    private String id;
    private static Dictionary<String, Symbol> dict = new Hashtable();
    private Symbol(String id) {
        this.id = id;
    }

    public String toString() {
        return id;
    }

    public static Symbol symbol(String id) {
        String u = id.intern();
        Symbol symbol = (Symbol) dict.get(u);
        if (symbol == null) {
            symbol = new Symbol(u);
            dict.put(u, symbol);
        }
        return symbol;
    }
}
