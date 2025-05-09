package org.study.hydro.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "storage_racks")
public class StorageRack implements Serializable {

    private static final long serialVersionUID = -5249233830527011318L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_racks")
    private Integer storageRackId;

    @Column(name = "name")
    private String name;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "shelf_id")
    private Shelf shelf;

    @ManyToMany(mappedBy = "storageRackList")
    private List<Product> productList;

    public StorageRack() {
    }

    public StorageRack(Integer storageRackId, String name, Shelf shelf) {
        this.storageRackId = storageRackId;
        this.name = name;
        this.shelf = shelf;
    }

    public StorageRack(String name, Shelf shelf) {
        this.name = name;
        this.shelf = shelf;
    }

    public StorageRack(String name) {
        this.name = name;
    }

    public StorageRack(Integer storageRackId, String name) {
        this.storageRackId = storageRackId;
        this.name = name;
    }

    public Integer getStorageRackId() {
        return storageRackId;
    }

    public void setStorageRackId(Integer storageRackId) {
        this.storageRackId = storageRackId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Shelf getShelf() {
        return shelf;
    }

    public void setShelf(Shelf shelf) {
        this.shelf = shelf;
    }

    public List<Product> getProductList() {
        return productList;
    }

    public void setProductList(List<Product> productList) {
        this.productList = productList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        StorageRack that = (StorageRack) o;

        if (!Objects.equals(storageRackId, that.storageRackId))
            return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(shelf, that.shelf)) return false;
        return Objects.equals(productList, that.productList);
    }

    @Override
    public int hashCode() {
        int result = storageRackId != null ? storageRackId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (shelf != null ? shelf.hashCode() : 0);
        result = 31 * result + (productList != null ? productList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StorageRack{" +
                "storageRackId=" + storageRackId +
                ", name='" + name + '\'' +
                ", shelf=" + shelf +
                ", productList=" + productList +
                '}';
    }
}
