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
     * The method creates new record in the database table.
     *
     * @param picture entity that specifies creation of the new record in the database table.
     *
     * @return returns boolean's result if the row creates.
     */
    boolean create(Picture picture);

    /**
     * The method removes the current record in the database table.
     *
     * @param id is the id of the picture that will be removed.
     *
     * @return returns boolean's result if the row removes.
     */
    boolean remove(int id);

    /**
     * The method returns the specified picture by id.
     *
     * @param id is the picture's id.
     *
     * @return the specified Optional picture by id.
     */
    Optional<Picture> findById(int id);

    /**
     * The method returns specified list of the pictures by the id product.
     *
     * @param productId is the id of the product for which the list of pictures will be returned.
     *
     * @return the specified list of pictures with the same search id product.
     */
    List<Picture> getPicturesByProductId(int productId);

    /**
     * The method will return list of the pictures.
     * @param limit the number of rows to get at one time.
     * @param offset the value of the element from which the countdown starts.
     *
     * @return the specified list of the pictures.
     */
    List<Picture> getPictures(int limit, int offset);
}
