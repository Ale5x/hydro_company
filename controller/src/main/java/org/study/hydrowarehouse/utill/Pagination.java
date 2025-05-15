package org.study.hydrowarehouse.utill;

import org.springframework.stereotype.Component;
import org.study.hydrowarehouse.exception.AppRequestException;

/**
 * Utility component for handling pagination logic such as calculating page numbers and offset values for paginated
 * data queries. All methods are stateless and validate the input before processing.
 *
 * @author Aliaksandr Pishchala
 */
@Component
public class Pagination {

    /**
     * Calculates the previous page number based on the current page. Ensures that the page number does not go below 1.
     *
     * @param page the current page number as a string
     * @return the previous page number as a string (at minimum, "1")
     * @throws AppRequestException if the page is not a valid number
     */
    public static String getPreviousPage(String page) {
        ValidatorParam.isNumber(page);
        int previousPage = Integer.parseInt(page) - 1;
        if(previousPage >= 1) {
            return String.valueOf(previousPage);
        }
        return String.valueOf(1);
    }

    /**
     * Calculates the next page number based on the current page. Ensures that the returned page is always at least "2"
     * if the current page is invalid or less than 1.
     *
     * @param page the current page number as a string
     * @return the next page number as a string
     * @throws AppRequestException if the page is not a valid number
     */
    public static String getNumberNextPage(String page) {
        ValidatorParam.isNumber(page);
        if(Integer.parseInt(page) <= 0) {
            return String.valueOf(2);
        }
        return String.valueOf(Integer.parseInt(page) + 1);
    }

    /**
     * Calculates the offset (zero-based index) for a paginated query based on page number and page size. This value is
     * typically used in SQL queries with LIMIT and OFFSET.
     *
     * @param page the current page number as a string (1-based)
     * @param size the number of items per page as a string
     * @return the offset as an integer
     * @throws AppRequestException if page or size are not valid numbers
     */
    public static int getOffset(String page, String size) {
        ValidatorParam.isNumber(page);
        ValidatorParam.isNumber(size);

        return (Integer.parseInt(page) - 1) * Integer.parseInt(size);
    }
}
