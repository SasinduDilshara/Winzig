package code_generator;

import java.util.HashMap;

public class DclnTable {
    private HashMap<String, DclnRow> rows;

    public DclnTable() {
        this.rows = new HashMap<>();
    }

    public void enter(String name, int location, String type) {
        this.rows.put(name, new DclnRow(
            name,
            location,
            type
        ));
    }

    public DclnRow lookup(String name) {
        return this.rows.get(name);
    }
}
