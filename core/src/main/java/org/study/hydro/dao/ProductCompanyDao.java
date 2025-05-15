package org.study.hydro.dao;

import org.study.hydro.entity.ProductCompany;

import java.util.List;
import java.util.Optional;

/**
 * Interface {@link ProductCompanyDao} provides operation with data of database table 'product_companies'.
 *
 * @author Aliaksandr Pishchala
 */
public interface ProductCompanyDao {

    /**
     * Persists the given {@link ProductCompany} entity in the database.
     * <p>
     * This method saves the {@code productCompany} entity using the current Hibernate session.
     * It explicitly flushes the session to ensure that the insert operation is executed immediately,
     * and then returns the generated ID of the saved productCompany.
     *
     * @param productCompany the {@link ProductCompany} entity to be persisted in the database
     * @return the generated ID of the saved productCompany
     */
    int create(ProductCompany productCompany);

    /**
     * The method returns the specified ProductCompany by id.
     *
     * @param id is the ProductCompany's id.
     *
     * @return the specified Optional ProductCompany by id.
     */
    Optional<ProductCompany> getById(Integer id);

    /**
     * The method will return list of productCompanies.
     * @param limit the maximum number of products to include in the list.
     * @param offset the starting position of the product list.
     * @return the specified list of the productCompanies.
     */
    List<ProductCompany> getProductCompanies(int offset, int limit);

    /**
     * Updates the given {@link ProductCompany} entity in the data store.
     * <p>
     * This method attempts to update an existing product company record with the data
     * provided in the {@link ProductCompany} entity. If the entity does not exist or the update
     * operation fails (e.g., due to validation or persistence issues), the method returns {@code false}.
     *
     * @param productCompany the {@link ProductCompany} entity containing updated information
     * @return {@code true} if the update was successful; {@code false} if the entity was not found or the update failed
     */
    boolean update(ProductCompany productCompany);

    /**
     * The method returns the specified list of the productCompanies by the name or the same name.
     *
     * @param name is the productCompany's name.
     *
     * @return the specified list of productCompanies by the name or the same name.
     */
    List<ProductCompany> getProductCompaniesByName(String name);
}
