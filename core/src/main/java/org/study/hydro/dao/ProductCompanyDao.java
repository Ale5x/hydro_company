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
     * The method creates the new record in the database table.
     *
     * @param productCompany entity that specifies the creation of the new record in the database table.
     *
     * @return returns boolean's result if the row creates.
     */
    boolean create(ProductCompany productCompany);

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
     * The method updates an existing table record in the database.
     *
     * @param productCompany entity having new data to update an existing table record.
     *
     * @return returns always a true result.
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
