package org.study.hydro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.study.hydro.dao.PictureDao;
import org.study.hydro.dao.ProductDao;
import org.study.hydro.entity.Picture;
import org.study.hydro.entity.Product;

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

    @Autowired
    public ServiceMediator(ProductDao productDao, PictureDao pictureDao) {
        this.productDao = productDao;
        this.pictureDao = pictureDao;
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
}
