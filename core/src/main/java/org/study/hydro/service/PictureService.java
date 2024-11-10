package org.study.hydro.service;

import org.study.hydro.entity.Dto.PictureDto;
import org.study.hydro.exception.CoreException;

import java.util.List;

/**
 * The PictureService interface {@link PictureService} provides methods for managing pictures.
 * It defines basic operations for creating, removing and etc. pictures.
 *
 * @author Aliaksandr Pishchala
 */
public interface PictureService {

    /**
     * Creates a new pictures based on the provided data.
     * @param pictureDto a {@link PictureDto} object containing the new picture data.
     * @return true if the picture was successfully created; false otherwise.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    boolean create(PictureDto pictureDto) throws CoreException;

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
     * Retrieves a list of all picturesDto.
     * @param limit the maximum number of picturesDto to include in the list.
     * @param offset the starting position of the pictureDto list.
     * @return a list of all picturesDto as {@link PictureDto} objects.
     * @throws CoreException if an error occurs during the retrieval process.
     */
    List<PictureDto> findAll(int limit, int offset) throws CoreException;
}
