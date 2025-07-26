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
     * Returns a list of products with pagination and optional status filtering.
     *
     * @param limit  the maximum number of products to return.
     * @param offset the starting position of the first product to retrieve.
     * @param status the status of the products to filter by (e.g. "ACTIVE", "INACTIVE");
     *               if null or empty, all statuses are included.
     *
     * @return the list of products matching the criteria.
     */
    List<Product> getProductsList(int limit, int offset, String status);

    /**
     * Retrieves a list of products filtered by pressure and product SKU status, with pagination.
     *
     * @param limit    the maximum number of products to return.
     * @param offset   the index of the first product to retrieve.
     * @param pressure the pressure value to filter products by.
     * @param status   the status of associated product SKUs to filter by;
     *                 only products having at least one SKU with the given status will be included.
     *
     * @return a list of products matching the specified pressure and status criteria.
     */
    List<Product> getProductsByPressure(int limit, int offset, Integer pressure, String status);

    /**
     * Retrieves a list of products filtered by flow rate and product SKU status, with pagination.
     *
     * @param limit     the maximum number of products to return.
     * @param offset    the index of the first product to retrieve.
     * @param flowRate  the flow rate value to filter products by.
     * @param status    the status of associated product SKUs to filter by;
     *                  only products having at least one SKU with the given status will be included.
     *                  If {@code null} or empty, products are not filtered by status.
     *
     * @return a list of products matching the specified flow rate and status criteria.
     */
    List<Product> getProductsByFlowRate(int limit, int offset, Integer flowRate, String status);

    /**
     * Retrieves a list of products filtered by product type ID and product SKU status, with pagination.
     *
     * @param limit          the maximum number of products to return.
     * @param offset         the index of the first product to retrieve.
     * @param productTypeId  the ID of the product type to filter by.
     * @param status         the status of associated product SKUs to filter by;
     *                       only products having at least one SKU with the given status will be included.
     *                       If {@code null} or empty, products are not filtered by status.
     *
     * @return a list of products matching the specified product type and status criteria.
     */
    List<Product> getProductsByTypeId(int limit, int offset, Integer productTypeId, String status);

    /**
     * Retrieves a list of products filtered by product company ID and product SKU status, with pagination.
     *
     * @param limit             the maximum number of products to return.
     * @param offset            the index of the first product to retrieve.
     * @param productCompanyId  the ID of the product company to filter products by.
     * @param status            the status of associated product SKUs to filter by;
     *                          only products having at least one SKU with the given status will be included.
     *                          If {@code null} or empty, products are not filtered by status.
     *
     * @return a list of products matching the specified company and status criteria.
     */
    List<Product> getProductsByCompanyId(int limit, int offset, Integer productCompanyId, String status);

    /**
     * Retrieves a list of products filtered by storage rack name and product SKU status, with pagination.
     *
     * @param limit             the maximum number of products to return.
     * @param offset            the index of the first product to retrieve.
     * @param storageRackName   the name of the storage rack to filter products by.
     * @param status            the status of associated product SKUs to filter by;
     *                          only products having at least one SKU with the given status will be included.
     *                          If {@code null} or empty, products are not filtered by status.
     *
     * @return a list of products matching the specified storage rack name and status criteria.
     */
    List<Product> getProductsByStorageRackName(int limit, int offset, String storageRackName, String status);
}
