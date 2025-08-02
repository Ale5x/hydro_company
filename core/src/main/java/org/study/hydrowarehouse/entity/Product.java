package org.study.hydrowarehouse.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Represents a product available in the warehouse system.
 * <p>
 * Mapped to the {@code products} table in the database, this entity contains technical specifications, additional
 * information, and relationships to other entities such as {@link ProductType}, {@link ProductCompany}, and
 * {@link ProductConnection}. It also maintains associations with {@link Picture} for product images and
 * {@link ProductSku} for stock keeping units, which track inventory details like storage location and country.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
@Entity
@Table(name = "products")
public class Product implements Serializable {

    private static final long serialVersionUID = 1987270399400466886L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer productId;

//    @Formula("(SELECT COUNT(*) FROM product_sku ps WHERE ps.product_id = product_id)")
    @Column(name = "count", nullable = false)
    private Integer count;

    @Column(name = "flow_rate", nullable = false)
    private Integer flowRate;

    @Column(name = "pressure", nullable = false)
    private Integer pressure;

    @Column(name = "pressure_max", nullable = false)
    private Integer pressureMax;

    @Column(name = "weight", nullable = false)
    private Double weight;

    @Column(name = "path_hydraulic_scheme", nullable = false, length = 255)
    private String pathHydraulicScheme;

    @Column(name = "additional_inf", columnDefinition = "TEXT")
    private String additionalInformation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_type_id", nullable = false)
    private ProductType productType;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Picture> picturePath;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_company_id", nullable = false)
    private ProductCompany productCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_connection_id", nullable = false)
    private ProductConnection productConnection;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductSku> productSkus;

    public Product() {
    }

    public Product(Integer productId) {
        this.productId = productId;
    }

    public Product(Integer productId, Integer count, Integer pressure, Integer pressureMax, Double weight,
                   Integer flowRate, String pathHydraulicScheme, String additionalInformation, List<Picture> picturePath) {
        this.productId = productId;
        this.flowRate = flowRate;
        this.count = count;
        this.pressure = pressure;
        this.pressureMax = pressureMax;
        this.weight = weight;
        this.pathHydraulicScheme = pathHydraulicScheme;
        this.picturePath = picturePath;
        this.additionalInformation = additionalInformation;
    }

    public Product(Integer count, Integer pressure, Integer pressureMax, Double weight, String pathHydraulicScheme,
                   Integer flowRate, String additionalInformation, List<Picture> picturePath) {
        this.flowRate = flowRate;
        this.count = count;
        this.pressure = pressure;
        this.pressureMax = pressureMax;
        this.weight = weight;
        this.pathHydraulicScheme = pathHydraulicScheme;
        this.additionalInformation = additionalInformation;
        this.picturePath = picturePath;
    }

    public Integer getProductId() {
        return productId;
    }

    public void setProductId(Integer productId) {
        this.productId = productId;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getFlowRate() {
        return flowRate;
    }

    public void setFlowRate(Integer flowRate) {
        this.flowRate = flowRate;
    }

    public Integer getPressure() {
        return pressure;
    }

    public void setPressure(Integer pressure) {
        this.pressure = pressure;
    }

    public Integer getPressureMax() {
        return pressureMax;
    }

    public void setPressureMax(Integer pressureMax) {
        this.pressureMax = pressureMax;
    }

    public Double getWeight() {
        return weight;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
    }

    public String getPathHydraulicScheme() {
        return pathHydraulicScheme;
    }

    public void setPathHydraulicScheme(String pathHydraulicScheme) {
        this.pathHydraulicScheme = pathHydraulicScheme;
    }

    public String getAdditionalInformation() {
        return additionalInformation;
    }

    public void setAdditionalInformation(String additionalInformation) {
        this.additionalInformation = additionalInformation;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }

    public List<Picture> getPicturePath() {
        return picturePath;
    }

    public void setPicturePath(List<Picture> picturePath) {
        this.picturePath = picturePath;
    }

    public ProductCompany getProductCompany() {
        return productCompany;
    }

    public void setProductCompany(ProductCompany productCompany) {
        this.productCompany = productCompany;
    }

    public ProductConnection getProductConnection() {
        return productConnection;
    }

    public void setProductConnection(ProductConnection productConnection) {
        this.productConnection = productConnection;
    }

    public List<ProductSku> getProductSkus() {
        return productSkus;
    }

    public void setProductSkus(List<ProductSku> productSkus) {
        this.productSkus = productSkus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (!Objects.equals(productId, product.productId)) return false;
        if (!Objects.equals(count, product.count)) return false;
        if (!Objects.equals(flowRate, product.flowRate)) return false;
        if (!Objects.equals(pressure, product.pressure)) return false;
        if (!Objects.equals(pressureMax, product.pressureMax)) return false;
        if (!Objects.equals(weight, product.weight)) return false;
        if (!Objects.equals(pathHydraulicScheme, product.pathHydraulicScheme))
            return false;
        if (!Objects.equals(additionalInformation, product.additionalInformation))
            return false;
        if (!Objects.equals(productType, product.productType)) return false;
        if (!Objects.equals(picturePath, product.picturePath)) return false;
        if (!Objects.equals(productCompany, product.productCompany))
            return false;
        if (!Objects.equals(productConnection, product.productConnection))
            return false;
        return Objects.equals(productSkus, product.productSkus);
    }

    @Override
    public int hashCode() {
        int result = productId != null ? productId.hashCode() : 0;
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (flowRate != null ? flowRate.hashCode() : 0);
        result = 31 * result + (pressure != null ? pressure.hashCode() : 0);
        result = 31 * result + (pressureMax != null ? pressureMax.hashCode() : 0);
        result = 31 * result + (weight != null ? weight.hashCode() : 0);
        result = 31 * result + (pathHydraulicScheme != null ? pathHydraulicScheme.hashCode() : 0);
        result = 31 * result + (additionalInformation != null ? additionalInformation.hashCode() : 0);
        result = 31 * result + (productType != null ? productType.hashCode() : 0);
        result = 31 * result + (picturePath != null ? picturePath.hashCode() : 0);
        result = 31 * result + (productCompany != null ? productCompany.hashCode() : 0);
        result = 31 * result + (productConnection != null ? productConnection.hashCode() : 0);
        result = 31 * result + (productSkus != null ? productSkus.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", count=" + count +
                ", flowRate=" + flowRate +
                ", pressure=" + pressure +
                ", pressureMax=" + pressureMax +
                ", weight=" + weight +
                ", pathHydraulicScheme='" + pathHydraulicScheme + '\'' +
                ", additionalInformation='" + additionalInformation + '\'' +
                ", productType=" + productType +
                ", picturePath=" + picturePath +
                ", productCompany=" + productCompany +
                ", productConnection=" + productConnection +
                ", productSkus=" + productSkus +
                '}';
    }
}