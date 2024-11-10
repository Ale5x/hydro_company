package org.study.hydro.dao;

import org.study.hydro.entity.Shelf;

import java.util.Optional;

/**
 * Interface {@link ShelfDao} provides operation with data of database table 'shelf'.
 *
 * @author Aliaksandr Pishchala
 */
public interface ShelfDao {

    /**
     * The method returns the specified Shelf by id.
     *
     * @param id is the Shelf's id.
     *
     * @return the specified Optional Shelf by id.
     */
    Optional<Shelf> findById(int id);

    /**
     * The method returns the specified Shelf by name.
     *
     * @param name is the Shelf's name.
     *
     * @return the specified Optional Shelf by name.
     */
    Optional<Shelf> findByName(String name);

    /**
     * The method updates an existing table record in the database.
     *
     * @param shelf entity having new data to update an existing table record.
     *
     * @return returns boolean's result true.
     */
    boolean update(Shelf shelf);

    /**
     * The method removes the current record in the database table.
     *
     * @param id is the id of the shelf that will be removed.
     *
     * @return returns boolean's result if the row removes.
     */
    boolean remove (int id);
}
