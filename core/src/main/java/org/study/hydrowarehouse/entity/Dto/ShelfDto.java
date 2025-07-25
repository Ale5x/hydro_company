package org.study.hydrowarehouse.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class ShelfDto extends RepresentationModel<ShelfDto> {

    private Integer shelfDtoId;
    private String name;

    private StorageRackDto storageRackDto;

    public ShelfDto(){}

    public ShelfDto(Integer id, String name, StorageRackDto storageRackDto) {
        this.shelfDtoId = id;
        this.name = name;
        this.storageRackDto = storageRackDto;
    }

    public Integer getShelfDtoId() {
        return shelfDtoId;
    }

    public void setShelfDtoId(Integer shelfDtoId) {
        this.shelfDtoId = shelfDtoId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StorageRackDto getStorageRackDto() {
        return storageRackDto;
    }

    public void setStorageRackDto(StorageRackDto storageRackDto) {
        this.storageRackDto = storageRackDto;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        ShelfDto shelfDto = (ShelfDto) o;

        if (!Objects.equals(shelfDtoId, shelfDto.shelfDtoId)) return false;
        if (!Objects.equals(name, shelfDto.name)) return false;
        return Objects.equals(storageRackDto, shelfDto.storageRackDto);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (shelfDtoId != null ? shelfDtoId.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (storageRackDto != null ? storageRackDto.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ShelfDto{" +
                "shelfDtoId=" + shelfDtoId +
                ", name='" + name + '\'' +
                ", storageRackDto=" + storageRackDto +
                '}';
    }
}
