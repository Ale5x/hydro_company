package org.study.hydro.dao;

import org.study.hydro.entity.ProductConnection;

import java.util.List;
import java.util.Optional;

/**
 * Interface {@link ProductConnectionDao} provides operation with data of database table 'products_connection'.
 *
 * @author Aliaksandr Pishchala
 */
public interface ProductConnectionDao {

    /**
     * The method creates the new record in the database table.
     *
     * @param productConnection entity that specifies the creation of new records in the database table.
     *
     * @return the ID of the newly created record in the table.
     */
    int save(ProductConnection productConnection);

    /**
     * The method returns the specified ProductConnection by id.
     *
     * @param id is the ProductConnection's id.
     *
     * @return the specified Optional ProductConnection by id.
     */
    Optional<ProductConnection> getProductConnectionById(Integer id);

    /**
     * The method will return list of the productConnections.
     *
     * @return the specified list of the productConnections.
     */
    List<ProductConnection> getListProductsConnection();

    /**
     * The method updates an existing table record in the database.
     *
     * @param productConnection entity having new data to update an existing table record.
     *
     * @return returns always a true result.
     */
    boolean updateProductConnection(ProductConnection productConnection);
}
