package org.study.hydrowarehouse.entity.Dto;

import org.springframework.hateoas.RepresentationModel;

public class ProductSkuDto extends RepresentationModel<ProductSkuDto> {

    private Integer productSkuDtoId;

    public Integer getProductSkuDtoId() {
        return productSkuDtoId;
    }

    public void setProductSkuDtoId(Integer productSkuDtoId) {
        this.productSkuDtoId = productSkuDtoId;
    }
}
