package org.study.hydro.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

public class PictureDto extends RepresentationModel<PictureDto> {

    private Integer pictureDtoId;
    private String path;
    private Integer productId;

    public Integer getPictureDtoId() {
        return pictureDtoId;
    }

    public void setPictureDtoId(Integer pictureDtoId) {
        this.pictureDtoId = pictureDtoId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        PictureDto that = (PictureDto) o;

        if (!Objects.equals(pictureDtoId, that.pictureDtoId)) return false;
        if (!Objects.equals(path, that.path)) return false;
        return Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (pictureDtoId != null ? pictureDtoId.hashCode() : 0);
        result = 31 * result + (path != null ? path.hashCode() : 0);
        result = 31 * result + (productId != null ? productId.hashCode() : 0);
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
