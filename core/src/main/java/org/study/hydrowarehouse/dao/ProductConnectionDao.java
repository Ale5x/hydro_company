package org.study.hydrowarehouse.dao;

import org.study.hydrowarehouse.entity.ProductConnection;

import java.util.List;
import java.util.Optional;

/**
 * Interface {@link ProductConnectionDao} provides operation with data of database table 'products_connection'.
 *
 * @author Aliaksandr Pishchala
 */
public interface ProductConnectionDao {

    /**
     * Persists the given {@link ProductConnection} entity in the database.
     * <p>
     * This method saves the {@code productConnection} entity using the current Hibernate session.
     * It explicitly flushes the session to ensure that the insert operation is executed immediately,
     * and then returns the generated ID of the saved productConnection.
     *
     * @param productConnection the {@link ProductConnection} entity to be persisted in the database
     * @return the generated ID of the saved productConnection
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
