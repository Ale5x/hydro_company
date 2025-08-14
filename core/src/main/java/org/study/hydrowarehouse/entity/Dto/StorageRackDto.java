package org.study.hydrowarehouse.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;
/**
 * Data Transfer Object for {@link org.study.hydrowarehouse.entity.StorageRack} entity.
 * <p>
 * This DTO is used to transfer storage rack data between the service layer and the presentation layer.
 * It extends {@link RepresentationModel} to support HATEOAS links.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
public class StorageRackDto extends RepresentationModel<StorageRackDto> {

    /**
     * Unique identifier of the storage rack.
     */
    private Integer storageRackDtoId;

    /**
     * Name of the storage rack.
     */
    private String name;

    /**
     * Default constructor.
     */
    public StorageRackDto(){}

    /**
     * Constructs a StorageRackDto with the specified id.
     *
     * @param storageRackDtoId unique identifier of the storage rack
     */
    public StorageRackDto(Integer storageRackDtoId) {
        this.storageRackDtoId = storageRackDtoId;
    }

    /**
     * Constructs a StorageRackDto with the specified id and name.
     *
     * @param storageRackDtoId unique identifier of the storage rack
     * @param name             name of the storage rack
     */
    public StorageRackDto(Integer storageRackDtoId, String name) {
        this.storageRackDtoId = storageRackDtoId;
        this.name = name;
    }

    public StorageRackDto(String name) {
        this.name = name;
    }

    public Integer getStorageRackDtoId() {
        return storageRackDtoId;
    }

    public void setStorageRackDtoId(Integer storageRackDtoId) {
        this.storageRackDtoId = storageRackDtoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        StorageRackDto that = (StorageRackDto) o;

        if (!Objects.equals(storageRackDtoId, that.storageRackDtoId))
            return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (storageRackDtoId != null ? storageRackDtoId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StorageRackDto{" +
                "storageRackDtoId=" + storageRackDtoId +
                ", name='" + name + '\'' +
                '}';
    }
}
