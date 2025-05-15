package org.study.hydro.service;

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
     * The method converts a list of entity objects into a list of entity DTO objects.
     * @param objectsList the list of the entities objects.
     * @return the list of DTO objects.
     */
    public abstract List<T> mapToListObjectsDto(List<K> objectsList);

    /**
     * The method converts the entity into the of entity DTO object.
     * @param object the entity object.
     * @return the DTO object.
     */
    public abstract T mapToObjectDto(K object);

//    /**
//     * The method converts the DTO entity into the of entity object.
//     * @param objectDto the DTO object.
//     * @param isUpdate the flag that provides additional logic if this entity will participate in the update.
//     * @return the entity object.
//     */
//    public abstract K mapToEntityFromDto(T objectDto, boolean isUpdate);
}
