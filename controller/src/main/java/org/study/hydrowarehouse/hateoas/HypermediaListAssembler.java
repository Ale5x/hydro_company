package org.study.hydrowarehouse.hateoas;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;

import java.util.List;

/**
 * A generic interface for building HATEOAS-compliant paginated collection models. This interface provides a default
 * method to create a {@link CollectionModel} with optional hypermedia navigation links like "previous" and "next".
 *
 * @param <T> the type of resource in the collection
 *
 * @author Aliaksandr Pishchala
 */
public interface HypermediaListAssembler<T> {

    /**
     * Creates a paginated HATEOAS {@link CollectionModel} from the provided data.
     *
     * @param dataList the current page of data
     * @param nextDataList the next page of data (used to determine if the "next" link should be included)
     * @param previousLink the hypermedia link to the previous page
     * @param nextLink the hypermedia link to the next page (only included if {@code nextDataList} is not empty)
     * @return a HATEOAS-compliant {@code CollectionModel<T>} including appropriate pagination links
     */
    default CollectionModel<T> createPaginatedModel(List<T> dataList, List<T> nextDataList, Link previousLink, Link nextLink) {
        if (dataList.isEmpty()) {
            return CollectionModel.of(dataList);
        }
        return (nextDataList.isEmpty())
                ? CollectionModel.of(dataList, previousLink)
                : CollectionModel.of(dataList, previousLink, nextLink);
    }
}
