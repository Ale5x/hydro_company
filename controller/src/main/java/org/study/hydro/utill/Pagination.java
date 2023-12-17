package org.study.hydro.utill;

import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Pagination {

    public static String getPreviousPage(String page) {
        int previousPage = Integer.parseInt(page) - 1;
        if(previousPage >= 1) {
            return String.valueOf(previousPage);
        }
        return String.valueOf(1);
    }

    public static String getNumberNextPage(String page) {
        if(Integer.parseInt(page) <= 0) {
            return String.valueOf(2);
        }
        return String.valueOf(Integer.parseInt(page) + 1);
    }

    public static boolean isNextListEmpty(List<Object> objects) {
        return objects.isEmpty();
    }
}
