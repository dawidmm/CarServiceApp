package pl.apserwis.ap.service;

import pl.apserwis.ap.entity.Work;

import java.util.Comparator;

public class SortComp implements Comparator<Work> {

    String sortValue = "";
    int column = 0;
    String sortChar = "";

    private SortComp() {
    }

    public SortComp(String sortValue) {
        this.sortValue = sortValue;
        getColumnAndChar();
    }

    @Override
    public int compare(Work o1, Work o2) {
        switch (column) {
            case 1:
                if (sortChar.equals("+"))
                    return (int)(o1.getId() - o2.getId());
                return (int)(o2.getId() - o1.getId());
            case 2:
                if (sortChar.equals("+"))
                    return compareString(o1.getCars().getPeople().getPhone(), o2.getCars().getPeople().getPhone());
                return compareString(o2.getCars().getPeople().getPhone(), o1.getCars().getPeople().getPhone());
            case 3:
                if (sortChar.equals("+"))
                    return compareString(o1.getCars().getPlateNumber(), o2.getCars().getPlateNumber());
                return compareString(o2.getCars().getPlateNumber(), o1.getCars().getPlateNumber());
            case 4:
                if (sortChar.equals("+"))
                    return compareString(o1.getDescription(), o2.getDescription());
                return compareString(o2.getDescription(), o1.getDescription());
            case 5:
                if (sortChar.equals("+"))
                    return Integer.parseInt(o1.getPrice()) - Integer.parseInt(o2.getPrice());
                return Integer.parseInt(o2.getPrice()) - Integer.parseInt(o1.getPrice());
            case 6:
                if (sortChar.equals("+"))
                    return compareString(o1.getDate(), o2.getDate());
                return compareString(o2.getDate(), o1.getDate());
        }
        return 0;
    }

    private void getColumnAndChar() {
        if (sortValue == null || sortValue.isEmpty())
            return;
        column = Integer.parseInt(Character.toString(sortValue.charAt(0)));
        sortChar = Character.toString(sortValue.charAt(1));
    }

    private int compareString(String o1, String o2) {
        return o1.compareToIgnoreCase(o2);
    }
}
