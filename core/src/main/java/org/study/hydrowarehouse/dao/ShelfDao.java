package org.study.hydrowarehouse.dao;

import org.study.hydrowarehouse.entity.Shelf;

import java.util.List;
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
    Optional<Shelf> findById(Integer id);

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
    boolean remove (Integer id);

    /**
     * The method will return list of the shelf.
     *
     * @return the specified list of the shelves.
     */
    List<Shelf> getAllShelf();

    /**
     * Persists the given {@link Shelf} entity in the database.
     * <p>
     * This method saves the {@code shelf} entity using the current Hibernate session.
     * It explicitly flushes the session to ensure that the insert operation is executed immediately,
     * and then returns the generated ID of the saved shelf.
     *
     * @param shelf the {@link Shelf} entity to be persisted in the database
     * @return the generated ID of the saved shelf
     */
    int create(Shelf shelf);
}
