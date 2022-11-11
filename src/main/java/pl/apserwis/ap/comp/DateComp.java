package pl.apserwis.ap.comp;

import pl.apserwis.ap.entity.Calendar;

import java.util.Comparator;

public class DateComp implements Comparator<Calendar> {

    @Override
    public int compare(Calendar o1, Calendar o2) {
        String[] date1 = o1.getDate().split("-");
        String[] date2 = o2.getDate().split("-");

        if (!date1[2].equals(date2[2]))
            return compString(date1[2], date2[2]);
        if (!date1[1].equals(date2[1]))
            return compString(date1[1], date2[1]);
        else
            return compString(date1[0], date2[0]);
    }

    private int compString(String s1, String s2) {
        int i1 = Integer.parseInt(s1);
        int i2 = Integer.parseInt(s2);

        return Integer.compare(i1, i2);
    }
}
