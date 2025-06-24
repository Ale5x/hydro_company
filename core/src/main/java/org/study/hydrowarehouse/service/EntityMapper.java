package org.study.hydrowarehouse.service;

import org.study.hydrowarehouse.exception.CoreException;

import java.util.List;

/**
 * The class helps create entities from DTO entities and vice versa.
 * @param <T> the DTO entity.
 * @param <K> the entity.
 *
 * @author Aliaksandr Pishchala
 */
public abstract class EntityMapper<T, K> {

    protected EntityMapper() {
    }

    /**
     * Converts a list of entity objects of type {@code K} into a list of corresponding DTOs of type {@code T}.
     *
     * <p>Each object in the input list is mapped to its DTO representation using the defined mapping logic.</p>
     *
     * @param objectsList the list of entity objects to convert
     * @return a list of DTOs corresponding to the input entity list
     * @throws CoreException if an error occurs during the mapping process
     */
    public abstract List<T> mapToListObjectsDto(List<K> objectsList)  throws CoreException;

    /**
     * Converts the given entity object of type {@code K} into its corresponding DTO representation of type {@code T}.
     *
     * <p>This method is intended to be implemented by subclasses to define how a specific entity should be mapped
     * to its Data Transfer Object (DTO) counterpart.</p>
     *
     * @param object the entity object to be converted to DTO
     * @return the DTO representation of the provided entity
     * @throws CoreException if mapping fails due to invalid data or internal errors
     */
    public abstract T mapToObjectDto(K object)  throws CoreException;
}
