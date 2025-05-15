package org.study.hydrowarehouse.dao;

import org.study.hydrowarehouse.entity.*;

import java.util.List;
import java.util.Optional;

/**
 * Interface {@link ProductDao} provides operation with data of database table 'products'.
 *
 * @author Aliaksandr Pishchala
 */
public interface ProductDao {

    /**
     * Persists the given {@link Product} entity in the database.
     * <p>
     * This method saves the {@code product} entity using the current Hibernate session.
     * It explicitly flushes the session to ensure that the insert operation is executed immediately,
     * and then returns the generated ID of the saved product.
     *
     * @param product the {@link Product} entity to be persisted in the database
     * @return the generated ID of the saved product
     */
    int create(Product product);

    /**
     * The method updates an existing table record in the database.
     *
     * @param product entity having new data to update an existing table record.
     *
     * @return returns boolean's result true.
     */
    boolean update(Product product);

    /**
     * The method removes the current record in the database table.
     *
     * @param id is the id of the product that will be removed.
     *
     * @return returns boolean's result if the row removes.
     */
    boolean remove(Integer id);

    /**
     * The method returns the specified Product by id.
     *
     * @param id is the id of the product that will be returned.
     *
     * @return the specified Optional Product by id.
     */
    Optional<Product> getProductById(Integer id);

    /**
     * The method will return list of the products.
     *
     * @param limit the number of rows to get at one time.
     * @param offset the value of the element from which the countdown starts.
     *
     * @return the specified list of the products.
     */
    List<Product> getProductsList(int limit, int offset);

    /**
     * The method will return list of the products by the pressure.
     *
     * @param limit the number of rows to get at one time.
     * @param offset the value of the element from which the countdown starts.
     * @param pressure is the value of the elements of the products that correspond
     * to a given pressure value.
     *
     * @return the specified list of the products with the same search pressure.
     */
    List<Product> getProductsByPressure(int limit, int offset, Integer pressure);

    /**
     * The method will return list of the products by the flow rate.
     *
     * @param limit the number of rows to get at one time.
     * @param offset the value of the element from which the countdown starts.
     * @param flowRate is the value of the elements of the products that correspond
     * to a given flowRate value.
     *
     * @return the specified list of the products with the same search flow rate.
     */
    List<Product> getProductsByFlowRate(int limit, int offset, Integer flowRate);

    /**
     * The method will return list of the products by the pressure.
     *
     * @param limit the number of rows to get at one time.
     * @param offset the value of the element from which the countdown starts.
     * @param productTypeId is the productType's id that match the similar value of the type.
     *
     * @return the specified list of the products with the same search type.
     */
    List<Product> getProductsByTypeId(int limit, int offset, Integer productTypeId);

    /**
     * The method will return list of the products by the product company.
     *
     * @param limit the number of rows to get at one time.
     * @param offset the value of the element from which the countdown starts.
     * @param productCompanyId is the productCompany's id that match the similar value of the company.
     *
     * @return the specified list of the products with the same company.
     */
    List<Product> getProductsByCompanyId(int limit, int offset, Integer productCompanyId);

    /**
     * The method will return list of the products by the product company.
     *
     * @param limit the number of rows to get at one time.
     * @param offset the value of the element from which the countdown starts.
     * @param storageRackName is the storageRack's name that match the similar value of the storageRack's name.
     *
     * @return the specified list of the products with the same storageRack.
     */
    List<Product> getProductsByStorageRackName(int limit, int offset, String storageRackName);
}
