package org.study.hydro.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class PictureDto extends RepresentationModel<PictureDto> {

    private int pictureDtoId;
    private String path;
    private int productId;

    public int getPictureDtoId() {
        return pictureDtoId;
    }

    public void setPictureDtoId(int pictureDtoId) {
        this.pictureDtoId = pictureDtoId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PictureDto that = (PictureDto) o;

        if (pictureDtoId != that.pictureDtoId) return false;
        if (productId != that.productId) return false;
        return Objects.equals(path, that.path);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + pictureDtoId;
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + productId;
        return result;
    }

    @Override
    public String toString() {
        return "PictureDto{" +
                "pictureDtoId=" + pictureDtoId +
                ", path='" + path + '\'' +
                ", productId=" + productId +
                '}';
    }
}
