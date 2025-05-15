package org.study.hydrowarehouse.dao;

import org.study.hydrowarehouse.entity.StorageRack;

import java.util.List;
import java.util.Optional;

/**
 * Interface {@link StorageRackDao} provides operation with data of database table 'storage_racks'.
 *
 * @author Aliaksandr Pishchala
 */
public interface StorageRackDao {

    /**
     * Persists the given {@link StorageRack} entity in the database.
     * <p>
     * This method saves the {@code storageRack} entity using the current Hibernate session.
     * It explicitly flushes the session to ensure that the insert operation is executed immediately,
     * and then returns the generated ID of the saved storageRack.
     *
     * @param storageRack the {@link StorageRack} entity to be persisted in the database
     * @return the generated ID of the saved storageRack
     */
    int create(StorageRack storageRack);

    /**
     * The method removes the current record in the database table.
     *
     * @param id is the id of the StorageRack that will be removed.
     *
     * @return returns boolean's result if the row removes.
     */
    boolean remove(Integer id);

    /**
     * The method updates an existing table record in the database.
     *
     * @param storageRack entity having new data to update an existing table record.
     *
     * @return returns boolean's result true.
     */
    boolean update(StorageRack storageRack);

    /**
     * The method returns the specified StorageRack by id.
     *
     * @param id is the StorageRack's id.
     *
     * @return the specified Optional StorageRack by id.
     */
    Optional<StorageRack> getStorageRackById(Integer id);

    /**
     * The method will return list of the storageRacks.
     *
     * @param limit the number of rows to get at one time.
     * @param offset the value of the element from which the countdown starts.
     *
     * @return the specified list of the storageRacks.
     */
    List<StorageRack> getStorageRacksList(int limit, int offset);
}
