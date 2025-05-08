package org.study.hydro.hateoas;

import org.springframework.hateoas.Link;
import org.springframework.http.HttpStatus;
import org.study.hydro.controller.ControllerConstants;
import org.study.hydro.exception.AppRequestException;
import org.study.hydro.utill.Pagination;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

/**
 * Utility class for creating HATEOAS navigation links (e.g., previous and next page links). This helper is typically
 * used in REST controllers to generate pagination links using
 * Spring HATEOAS {@link org.springframework.hateoas.Link}.
 *
 * @author Aliaksandr Pishchala
 */
public class HateoasLinkHelper {


    private static final String CREATE_LINK_ERROR = "Criteria must come in pairs of key and value.";

    /**
     * Creates a previous page link for pagination.
     *
     * @param controllerClass The controller class to link to.
     * @param basePath       The base path for the link.
     * @param searchCriteria The search criteria, including page and size parameters.
     * @return A Link pointing to the previous page.
     */
    public static Link createPreviousLink(Class<?> controllerClass, String basePath, Map<String, String> searchCriteria) {
        String previousPage = Pagination.getPreviousPage(searchCriteria.get(ControllerConstants.PAGE));

        // Update the page number in search criteria
        Map<String, String> updatedCriteria = new HashMap<>(searchCriteria);
        updatedCriteria.put(ControllerConstants.PAGE, previousPage);

        return linkTo(controllerClass)
                .slash(createLink(basePath, updatedCriteria))
                .withRel(ControllerConstants.PREVIOUS)
                .withType(ControllerConstants.METHOD_GET);
    }

    /**
     * Creates a next page link for pagination.
     *
     * @param controllerClass The controller class to link to.
     * @param basePath       The base path for the link.
     * @param searchCriteria The search criteria, including page and size parameters.
     * @return A Link pointing to the next page.
     */
    public static Link createNextLink(Class<?> controllerClass, String basePath, Map<String, String> searchCriteria) {
        String nextPage = Pagination.getNumberNextPage(searchCriteria.get(ControllerConstants.PAGE));

        // Update the page number in search criteria
        Map<String, String> updatedCriteria = new HashMap<>(searchCriteria);
        updatedCriteria.put(ControllerConstants.PAGE, nextPage);

        return linkTo(controllerClass)
                .slash(createLink(basePath, updatedCriteria))
                .withRel(ControllerConstants.NEXT)
                .withType(ControllerConstants.METHOD_GET);
    }

    /**
     * Creates a URL link by combining the base path with the search criteria as query parameters.
     *
     * @param basePath       The base path of the link.
     * @param searchCriteria The search criteria to be included as query parameters.
     * @return A string representing the full URL with query parameters.
     */
    private static String createLink(String basePath, Map<String, String> searchCriteria) {
        String queryString = buildQueryString(searchCriteria);
        return queryString.isEmpty() ? basePath : basePath + ControllerConstants.CHAIR_PARAM + queryString;
    }

    /**
     * Builds a query string from the search criteria map.
     *
     * @param params The search criteria to be converted into a query string.
     * @return A query string with key-value pairs separated by '&', or an empty string if no parameters exist.
     */
    private static String buildQueryString(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return "";
        }

        StringBuilder query = new StringBuilder();
        boolean first = true;

        for (Map.Entry<String, String> entry : params.entrySet()) {
            if (!first) {
                query.append(ControllerConstants.CHAIR_END);
            }
            query.append(entry.getKey())
                    .append(ControllerConstants.CHAIR_EQUAL)
                    .append(entry.getValue());
            first = false;
        }

        return query.toString();
    }

    /**
     * Utility method to build a Map containing pagination and dynamic search criteria.
     * This method supports an arbitrary number of key-value pairs for search criteria.
     *
     * @param page The page number for pagination.
     * @param size The number of items per page.
     * @param criteria A variable-length argument list of key-value pairs for additional search criteria.
     * @return A Map containing the page, size, and additional search criteria.
     */
    public static Map<String, String> buildSearchCriteria(String page, String size, String... criteria) {
        Map<String, String> searchCriteria = new HashMap<>();

        searchCriteria.put(ControllerConstants.PAGE, page);
        searchCriteria.put(ControllerConstants.SIZE, size);

        if (criteria.length % 2 == 0) {
            for (int i = 0; i < criteria.length; i += 2) {
                searchCriteria.put(criteria[i], criteria[i + 1]);
            }
        } else {
            throw new AppRequestException(CREATE_LINK_ERROR, HttpStatus.BAD_REQUEST);
        }

        return searchCriteria;
    }




    public static Link createPreviousLink(Class<?> controllerClass, String basePath, String page, String size) {
//        String previousPage = Pagination.getPreviousPage(page);
        return null;
    }

    public static Link createNextLink(Class<?> controllerClass, String basePath, String page, String size) {
//        String nextPage = Pagination.getNumberNextPage(page);
        return null;
    }
}

//
//
//
//
//
//    /**
//     * Creates a HATEOAS link to the previous page of a paginated resource.
//     *
//     * @param controllerClass the controller class to link to
//     * @param basePath the base path of the resource (e.g., "/products")
//     * @param page the current page number as a string
//     * @param size the size of the page as a string
//     * @return a {@link Link} to the previous page, with {@code rel="previous"} and {@code type="GET"}
//     */
//    public static Link createPreviousLink(Class<?> controllerClass, String basePath, String page, String size) {
//        String previousPage = Pagination.getPreviousPage(page);
//        return linkTo(controllerClass)
//                .slash(createLink(basePath, size, previousPage))
//                .withRel(ControllerConstants.PREVIOUS)
//                .withType(ControllerConstants.METHOD_GET);
//    }
//
//    /**
//     * Creates a HATEOAS link to the next page of a paginated resource.
//     *
//     * @param controllerClass the controller class to link to
//     * @param basePath the base path of the resource (e.g., "/products")
//     * @param page the current page number as a string
//     * @param size the size of the page as a string
//     * @return a {@link Link} to the next page, with {@code rel="next"} and {@code type="GET"}
//     */
//    public static Link createNextLink(Class<?> controllerClass, String basePath, String page, String size) {
//        String nextPage = Pagination.getNumberNextPage(page);
//        return linkTo(controllerClass)
//                .slash(createLink(basePath, size, nextPage))
//                .withRel(ControllerConstants.NEXT)
//                .withType(ControllerConstants.METHOD_GET);
//    }
//
//    /**
//     * Constructs a relative URL path with pagination query parameters.
//     *
//     * @param basePath the base path of the resource
//     * @param size the size of the page
//     * @param page the page number
//     * @return a relative path with pagination parameters (e.g., "/products?size=10&page=1")
//     */
//    private static String createLink(String basePath, String size, String page) {
//        StringBuilder path = new StringBuilder();
//        path.append(basePath).append(ControllerConstants.CHAIR_PARAM)
//                .append(ControllerConstants.SIZE).append(ControllerConstants.CHAIR_EQUAL).append(size)
//                .append(ControllerConstants.CHAIR_END)
//                .append(ControllerConstants.PAGE).append(ControllerConstants.CHAIR_EQUAL).append(page);
//
//        if (searchCriteria != null && !searchCriteria.isEmpty()) {
//            for (Map.Entry<String, String> entry : searchCriteria.entrySet()) {
//                path.append(ControllerConstants.CHAIR_END)
//                        .append(entry.getKey())
//                        .append(ControllerConstants.CHAIR_EQUAL)
//                        .append(entry.getValue());
//            }
//        }
//        return path.toString();
//    }
//}

/*
.getPreviousPage(searchCriteria.get(ControllerConstants.PAGE));
        Map<String, String> updatedCrit
 */


/*
public class HateoasLinkHelper {


public static Link createPreviousLink(Class<?> controllerClass, String basePath, String page, String size) {
    String previousPage = Pagination.getPreviousPage(page);
    return linkTo(controllerClass)
            .slash(createLink(basePath, size, previousPage))
            .withRel(ControllerConstants.PREVIOUS)
            .withType(ControllerConstants.METHOD_GET);
}


    public static Link createNextLink(Class<?> controllerClass, String basePath, String page, String size) {
        String nextPage = Pagination.getNumberNextPage(page);
        return linkTo(controllerClass)
                .slash(createLink(basePath, size, nextPage))
                .withRel(ControllerConstants.NEXT)
                .withType(ControllerConstants.METHOD_GET);
    }


    private static String createLink(String basePath, String size, String page) {
        StringBuilder path = new StringBuilder();
        path.append(basePath).append(ControllerConstants.CHAIR_PARAM)
                .append(ControllerConstants.SIZE).append(ControllerConstants.CHAIR_EQUAL).append(size)
                .append(ControllerConstants.CHAIR_END)
                .append(ControllerConstants.PAGE).append(ControllerConstants.CHAIR_EQUAL).append(page);

        if (searchCriteria != null && !searchCriteria.isEmpty()) {
            for (Map.Entry<String, String> entry : searchCriteria.entrySet()) {
                path.append(ControllerConstants.CHAIR_END)
                        .append(entry.getKey())
                        .append(ControllerConstants.CHAIR_EQUAL)
                        .append(entry.getValue());
            }
        }
        return path.toString();
    }
}
 */