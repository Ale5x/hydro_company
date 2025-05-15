package org.study.hydro.dao;

import org.study.hydro.entity.ProductType;

import java.util.List;
import java.util.Optional;

/**
 * Interface {@link ProductTypeDao} provides operation with data of database table 'products_type'.
 *
 * @author Aliaksandr Pishchala
 */
public interface ProductTypeDao {

    /**
     * Persists the given {@link ProductType} entity in the database.
     * <p>
     * This method saves the {@code productType} entity using the current Hibernate session.
     * It explicitly flushes the session to ensure that the insert operation is executed immediately,
     * and then returns the generated ID of the saved productType.
     *
     * @param productType the {@link ProductType} entity to be persisted in the database
     * @return the generated ID of the saved productType
     */
    int create(ProductType productType);

    /**
     * The method will return list of the productTypes.
     *
     * @return the specified list of the productTypes.
     */
    List<ProductType> getProductTypes();

    /**
     * The method returns the specified productType by id.
     *
     * @param id is the productType's id.
     *
     * @return the specified Optional productType by id.
     */
    Optional<ProductType> getProductTypeById(Integer id);

    /**
     * The method returns the list of specified productType by name.
     *
     * @param name is the productType's name.
     *
     * @return the specified list of productType by name.
     */
    List<ProductType> getProductTypeByName(String name);

    /**
     * The method updates an existing table record in the database.
     *
     * @param productType entity having new data to update an existing table record.
     *
     * @return returns always a true result.
     */
    boolean update (ProductType productType);

    /**
     * The method removes the current record in the database table.
     *
     * @param id is the id of the productType that will be removed.
     *
     * @return returns boolean's result if the row removes.
     */
    boolean remove (Integer id);
}
