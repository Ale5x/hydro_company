package org.study.hydrowarehouse.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.study.hydrowarehouse.dao.*;
import org.study.hydrowarehouse.entity.*;

import java.util.List;
import java.util.Optional;

/**
 * Mediator class that facilitates communication between the ProductService and PictureService between each other.
 *
 * @author Aliaksandr Pishchala
 */
@Component
public class ServiceMediator {

    private final ProductDao productDao;
    private final PictureDao pictureDao;
    private final CountryDao countryDao;
    private final ProductTypeDao productTypeDao;
    private final ProductCompanyDao productCompanyDao;
    private final ProductConnectionDao productConnectionDao;
    private final StorageRackDao storageRackDao;
    private final UserCompanyDao userCompanyDao;

    @Autowired
    public ServiceMediator(ProductDao productDao, PictureDao pictureDao, CountryDao countryDao,
                           ProductTypeDao productTypeDao, ProductCompanyDao productCompanyDao,
                           ProductConnectionDao productConnectionDao, StorageRackDao storageRackDao,
                           UserCompanyDao userCompanyDao) {
        this.productDao = productDao;
        this.pictureDao = pictureDao;
        this.countryDao = countryDao;
        this.productTypeDao = productTypeDao;
        this.productCompanyDao = productCompanyDao;
        this.productConnectionDao = productConnectionDao;
        this.storageRackDao = storageRackDao;
        this.userCompanyDao = userCompanyDao;
    }

    /**
     * Finds a product by its ID.
     *
     * @param id The ID of the product to be found
     * @return An Optional containing the product if found, or empty if not
     */
    public Optional<Product> findProductById(int id) {
        return productDao.getProductById(id);
    }

    /**
     * Finds all pictures associated with a specific product.
     *
     * @param productId The ID of the product for which pictures are to be retrieved
     * @return A list of pictures associated with the given product ID
     */
    public List<Picture> findPicturesByProduct(int productId) {
        return pictureDao.getPicturesByProductId(productId);
    }

    /**
     * Retrieves a {@link Country} entity by its ID.
     * <p>
     * This method delegates the retrieval of the {@link Country} to the underlying DAO layer.
     * It returns an {@link Optional} containing the country if found, or an empty {@link Optional} if no country
     * with the given ID exists in the database.
     *
     * @param id the ID of the country to retrieve
     * @return an {@link Optional} containing the country if found, or an empty {@link Optional} if not
     * @throws IllegalArgumentException if the provided ID is invalid (e.g., negative or zero)
     */
    public Optional<Country> countryById(int id) {
        return countryDao.countryById(id);
    }

    /**
     * Retrieves a {@link ProductType} by its unique identifier.
     *
     * @param id the ID of the product type to retrieve
     * @return an {@link Optional} containing the found {@link ProductType},
     *         or an empty Optional if no product type with the given ID exists
     */
    public Optional<ProductType> findProductTypeById(Integer id) {
        return productTypeDao.getProductTypeById(id);
    }

    /**
     * Retrieves a {@link ProductCompany} entity by its unique identifier.
     *
     * @param id the ID of the product company to retrieve
     * @return an {@link Optional} containing the found {@link ProductCompany},
     *         or an empty Optional if no entity with the given ID exists
     */
    public Optional<ProductCompany> findProductCompanyById(Integer id) {
        return productCompanyDao.getById(id);
    }

    public Optional<UserCompany> findUserCompanyById(Integer id) {
        return userCompanyDao.companyById(id);
    }

    /**
     * Retrieves a {@link ProductConnection} entity by its unique identifier.
     *
     * @param id the ID of the product connection to retrieve
     * @return an {@link Optional} containing the found {@link ProductConnection},
     *         or an empty Optional if no such entity exists
     */
    public Optional<ProductConnection> findProductConnectionById(Integer id) {
        return productConnectionDao.getProductConnectionById(id);
    }

    /**
     * Retrieves a {@link StorageRack} entity by its unique identifier.
     *
     * @param id the ID of the storage rack to retrieve
     * @return an {@link Optional} containing the found {@link StorageRack},
     *         or an empty Optional if no such entity exists
     */
    public Optional<StorageRack> findStorageRackById(Integer id) {
        return storageRackDao.getStorageRackById(id);
    }
}
