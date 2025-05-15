package org.study.hydro.dao;

import org.study.hydro.entity.Picture;

import java.util.List;
import java.util.Optional;

/**
 * Interface {@link PictureDao} provides operation with data of database table 'pictures'.
 *
 * @author Aliaksandr Pishchala
 */
public interface PictureDao {

    /**
     * Persists the given {@link Picture} entity in the database.
     * <p>
     * This method saves the {@code picture} entity using the current Hibernate session.
     * It explicitly flushes the session to ensure that the insert operation is executed immediately,
     * and then returns the generated ID of the saved picture.
     *
     * @param picture the {@link Picture} entity to be persisted in the database
     * @return the generated ID of the saved picture
     */
    int create(Picture picture);

    /**
     * Updates the given {@link Picture} entity in the data store.
     * <p>
     * This method attempts to update the existing picture with the new data provided.
     * If the picture does not exist or the update fails due to validation or persistence issues,
     * the method returns {@code false}.
     *
     * @param picture the {@link Picture} entity containing updated information
     * @return {@code true} if the update was successful; {@code false} if the picture was not found or not updated
     */
    boolean update (Picture picture);

    /**
     * The method creates new records in the database table from the list of the pictures.
     *
     * @param pictures the list of Picture entities to be saved.
     *
     * @return {@code true} if all pictures were saved successfully, {@code false} otherwise
     */
    boolean createList(List<Picture> pictures);

    /**
     * The method removes the current record in the database table.
     *
     * @param id is the id of the picture that will be removed.
     *
     * @return returns boolean's result if the row removes.
     */
    boolean remove(Integer id);

    /**
     * The method returns the specified picture by id.
     *
     * @param id is the picture's id.
     *
     * @return the specified Optional picture by id.
     */
    Optional<Picture> findById(Integer id);

    /**
     * The method returns specified list of the pictures by the id product.
     *
     * @param productId is the id of the product for which the list of pictures will be returned.
     *
     * @return the specified list of pictures with the same search id product.
     */
    List<Picture> getPicturesByProductId(Integer productId);

    /**
     * The method will return list of the pictures.
     * @param limit the number of rows to get at one time.
     * @param offset the value of the element from which the countdown starts.
     *
     * @return the specified list of the pictures.
     */
    List<Picture> getPictures(int limit, int offset);
}
