package org.study.hydro.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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

    private final ProductService productService;
    private final PictureService pictureService;

    @Autowired
    public ServiceMediator(ProductService productService, PictureService pictureService) {
        this.productService = productService;
        this.pictureService = pictureService;
    }

    /**
     * Finds a product by its ID.
     *
     * @param id The ID of the product to be found
     * @return An Optional containing the product if found, or empty if not
     */
    public Optional<Product> findProductById(int id) {
        return productService.findProductById(id);
    }

    /**
     * Finds all pictures associated with a specific product.
     *
     * @param productId The ID of the product for which pictures are to be retrieved
     * @return A list of pictures associated with the given product ID
     */
    public List<Picture> findPicturesByProduct(int productId) {
        return pictureService.findAllPicturesByProductId(productId);
    }
}
