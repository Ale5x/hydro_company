package org.study.hydro.entity;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "products")
public class Product implements Serializable {

    private static final long serialVersionUID = -7789179148102341314L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_products")
    private Integer productId;

    @Column(name = "count")
    private Integer count;

    @Column(name = "sku")
    private String stockKeepingUnit;

    @Column(name = "flow_rate")
    private Integer flowRate;

    @Column(name = "pressure")
    private Integer pressure;

    @Column(name = "pressure_max")
    private Integer pressureMax;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "path_hydraulic_scheme")
    private String pathHydraulicScheme;

    @Column(name = "additional_inf")
    private String additionalInformation;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_products_type")
    @Fetch(FetchMode.JOIN)
    private ProductType productType;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "product", fetch = FetchType.LAZY, orphanRemoval = true)
    @Fetch(FetchMode.SUBSELECT)
    private List<Picture> picturePath;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_product_companies")
    @Fetch(FetchMode.JOIN)
    private ProductCompany productCompany;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_products_connection")
    @Fetch(FetchMode.JOIN)
    private ProductConnection productConnection;

    @ManyToMany(cascade = {CascadeType.ALL})
    @JoinTable(
            name = "storage_racks_products",
            joinColumns = {@JoinColumn(name = "id_products")},
            inverseJoinColumns = {@JoinColumn(name = "id_storage_racks")}
    )
    @Fetch(FetchMode.SUBSELECT)
    private List<StorageRack> storageRackList;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_countries")
    @Fetch(FetchMode.JOIN)
    private Country countryProduct;

    public Product() {
    }

    public Product(Integer productId) {
        this.productId = productId;
    }

    public Product(Integer productId, Integer count, Integer pressure, Integer pressureMax, Double weight,
                   Integer flowRate, String stockKeepingUnit, String pathHydraulicScheme, String additionalInformation,
                   List<Picture> picturePath) {
        this.productId = productId;
        this.flowRate = flowRate;
        this.count = count;
        this.pressure = pressure;
        this.pressureMax = pressureMax;
        this.weight = weight;
        this.stockKeepingUnit = stockKeepingUnit;
        this.pathHydraulicScheme = pathHydraulicScheme;
        this.picturePath = picturePath;
        this.additionalInformation = additionalInformation;
    }

    public Product(Integer count, Integer pressure, Integer pressureMax, Double weight, String pathHydraulicScheme,
                   Integer flowRate, String stockKeepingUnit, String additionalInformation, List<Picture> picturePath) {
        this.flowRate = flowRate;
        this.count = count;
        this.pressure = pressure;
        this.pressureMax = pressureMax;
        this.weight = weight;
        this.stockKeepingUnit = stockKeepingUnit;
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

    public String getStockKeepingUnit() {
        return stockKeepingUnit;
    }

    public void setStockKeepingUnit(String stockKeepingUnit) {
        this.stockKeepingUnit = stockKeepingUnit;
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

    public List<StorageRack> getStorageRackList() {
        return storageRackList;
    }

    public void setStorageRackList(List<StorageRack> storageRackList) {
        this.storageRackList = storageRackList;
    }

    public Country getCountryProduct() {
        return countryProduct;
    }

    public void setCountryProduct(Country countryProduct) {
        this.countryProduct = countryProduct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Product product = (Product) o;

        if (!Objects.equals(productId, product.productId)) return false;
        if (!Objects.equals(count, product.count)) return false;
        if (!Objects.equals(stockKeepingUnit, product.stockKeepingUnit))
            return false;
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
        if (!Objects.equals(storageRackList, product.storageRackList))
            return false;
        return Objects.equals(countryProduct, product.countryProduct);
    }

    @Override
    public int hashCode() {
        int result = productId != null ? productId.hashCode() : 0;
        result = 31 * result + (count != null ? count.hashCode() : 0);
        result = 31 * result + (stockKeepingUnit != null ? stockKeepingUnit.hashCode() : 0);
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
        result = 31 * result + (storageRackList != null ? storageRackList.hashCode() : 0);
        result = 31 * result + (countryProduct != null ? countryProduct.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", count=" + count +
                ", stockKeepingUnit='" + stockKeepingUnit + '\'' +
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
                ", storageRackList=" + storageRackList +
                ", countryProduct=" + countryProduct +
                '}';
    }
}