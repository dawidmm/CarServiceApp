package pl.apserwis.ap.service;

import java.util.Comparator;
import java.util.Map;

public class MapSortForCarsComp implements Comparator<Map<String, String>> {

    String sortValue = "";
    int column = 0;
    String sortChar = "";

    private MapSortForCarsComp() {
    }

    public MapSortForCarsComp(String sortValue) {
        this.sortValue = sortValue;
        getColumnAndChar();
    }

    private void getColumnAndChar() {
        if (sortValue == null || sortValue.isEmpty())
            return;
        column = Integer.parseInt(Character.toString(sortValue.charAt(0)));
        sortChar = Character.toString(sortValue.charAt(1));
    }

    @Override
    public int compare(Map<String, String> o1, Map<String, String> o2) {
        switch (column) {
            case 1:
                if (sortChar.equals("+"))
                    return o1.get("plate").compareToIgnoreCase(o2.get("plate"));
                return o2.get("plate").compareToIgnoreCase(o1.get("plate"));
            case 2:
                if (sortChar.equals("+"))
                    return o1.get("name").compareToIgnoreCase(o2.get("name"));
                return o2.get("name").compareToIgnoreCase(o1.get("name"));
            case 3:
                if (sortChar.equals("+"))
                    return o1.get("phone").compareToIgnoreCase(o2.get("phone"));
                return o2.get("phone").compareToIgnoreCase(o1.get("phone"));
            case 4:
                if (sortChar.equals("+"))
                    return o1.get("works").compareToIgnoreCase(o2.get("works"));
                return o2.get("works").compareToIgnoreCase(o1.get("works"));
        }
        return 0;
    }
}
