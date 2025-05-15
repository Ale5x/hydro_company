package org.study.hydro.service;

import org.study.hydro.entity.Dto.PictureDto;
import org.study.hydro.entity.Picture;
import org.study.hydro.exception.CoreException;

import java.util.List;
import java.util.Optional;

/**
 * The PictureService interface {@link PictureService} provides methods for managing pictures.
 * It defines basic operations for creating, removing and etc. pictures.
 *
 * @author Aliaksandr Pishchala
 */
public interface PictureService {

    String PHOTO_LIMIT_EXCEEDED_ERROR = "Photo limit reached for product.";

    /**
     * Creates a new pictures based on the provided data.
     * @param pictureDto a {@link PictureDto} object containing the new picture data.
     * @return true if the picture was successfully created; false otherwise.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    boolean create(PictureDto pictureDto) throws CoreException;

    /**
     * Updates an existing picture entity using the data provided in the given {@link PictureDto}.
     * <p>
     * This method performs validation and business logic before delegating the update operation
     * to the underlying persistence layer. If the picture with the given ID does not exist,
     * or if any validation fails, a {@link CoreException} is thrown.
     *
     * @param pictureDto the data transfer object containing updated picture information
     * @return {@code true} if the update was successful, {@code false} otherwise
     * @throws CoreException if the picture cannot be updated due to business or system constraints
     */
    boolean update (PictureDto pictureDto) throws CoreException;

    /**
     * Deletes the data of an existing picture.
     * @param id the unique identifier of the picture to be deleted.
     * @return true if the picture was successfully deleted; false otherwise.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    boolean remove(int id) throws CoreException;

    /**
     * Retrieves a list of all picturesDto filtered by the unique identifier of the product.
     * @return a list of all picturesDto as {@link PictureDto} objects.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<PictureDto> findPicturesByProductId(int productId) throws CoreException;

    /**
     * Retrieves a pictureDto filtered by the own unique identifier.
     * @return a pictureDto  {@link PictureDto} object.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    Optional<PictureDto> findPictureById(int productId) throws CoreException;

    /**
     * Retrieves a list of all pictures filtered by the unique identifier of the product.
     * @return a list of all pictures as {@link Picture} objects.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<Picture> findAllPicturesByProductId(int productId) throws CoreException;

    /**
     * Retrieves a list of all picturesDto.
     * @param limit the maximum number of picturesDto to include in the list.
     * @param offset the starting position of the pictureDto list.
     * @return a list of all picturesDto as {@link PictureDto} objects.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<PictureDto> findAll(int offset, int limit) throws CoreException;

    /**
     * Validates that the number of photos associated with a product does not exceed the allowed limit. This method
     * retrieves all pictures for the given product ID and checks if the number of pictures exceeds the maximum
     * allowed photo limit. If the limit is exceeded, a {@link CoreException} is thrown.
     *
     * @param productId the ID of the product whose photos are being validated
     * @throws CoreException if the number of photos exceeds the configured limit
     */
    default void validatePhotoCountLimit(int productId) throws CoreException {
        List<Picture> pictures = findAllPicturesByProductId(productId);
        if (pictures.size() >= getMaxPhotoLimit()) {
            throw new CoreException(PHOTO_LIMIT_EXCEEDED_ERROR);
        }
    }

    /**
     * The method returns the max number of photos allowed per product for saving.
     * @return the maximum allowed number of photos for a product.
     */
    int getMaxPhotoLimit();
}
