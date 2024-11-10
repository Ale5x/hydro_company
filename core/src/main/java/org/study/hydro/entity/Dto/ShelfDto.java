package org.study.hydro.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class ShelfDto extends RepresentationModel<ShelfDto> {

    private int shelfDtoId;
    private String name;

    public int getShelfDtoId() {
        return shelfDtoId;
    }

    public void setShelfDtoId(int shelfDtoId) {
        this.shelfDtoId = shelfDtoId;
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

        ShelfDto shelfDto = (ShelfDto) o;

        if (shelfDtoId != shelfDto.shelfDtoId) return false;
        return Objects.equals(name, shelfDto.name);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + shelfDtoId;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "ShelfDto{" +
                "shelfDtoId=" + shelfDtoId +
                ", name='" + name + '\'' +
                '}';
    }
}
