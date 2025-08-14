package org.study.hydrowarehouse.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

import java.util.Objects;

/**
 * Data Transfer Object for {@link org.study.hydrowarehouse.entity.Picture} entity.
 * <p>
 * This DTO is used to transfer picture data between the service layer and the presentation layer.
 * It extends {@link RepresentationModel} to support HATEOAS links.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
public class PictureDto extends RepresentationModel<PictureDto> {

    /**
     * Unique identifier of the picture.
     */
    private Integer pictureDtoId;

    /**
     * Path to the picture file.
     */
    private String path;

    /**
     * Identifier of the associated product.
     */
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
