package org.study.hydrowarehouse.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class StorageRackDto extends RepresentationModel<StorageRackDto> {

    private Integer storageRackDtoId;
    private String name;
    private String shelfName;

    public StorageRackDto(){}

    public StorageRackDto(Integer storageRackDtoId) {
        this.storageRackDtoId = storageRackDtoId;
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

    public String getShelfName() {
        return shelfName;
    }

    public void setShelfName(String shelfName) {
        this.shelfName = shelfName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        StorageRackDto that = (StorageRackDto) o;

        if (!Objects.equals(storageRackDtoId, that.storageRackDtoId))
            return false;
        if (!Objects.equals(name, that.name)) return false;
        return Objects.equals(shelfName, that.shelfName);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (storageRackDtoId != null ? storageRackDtoId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (shelfName != null ? shelfName.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StorageRackDto{" +
                "storageRackDtoId=" + storageRackDtoId +
                ", name='" + name + '\'' +
                ", shelfName='" + shelfName + '\'' +
                '}';
    }
}
