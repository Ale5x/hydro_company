package org.study.hydrowarehouse.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "shelves")
public class Shelf implements Serializable {

    private static final long serialVersionUID = 8409769357195142703L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shelf_id")
    private Integer shelfId;

    @Column(name = "name", nullable = false, length = 10)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "storage_racks_id", nullable = false)
    private StorageRack storageRack;

    public Shelf() {
    }

    public Shelf(String name) {
        this.name = name;
    }

    public Shelf(Integer shelfId, String name) {
        this.shelfId = shelfId;
        this.name = name;
    }

    public Integer getShelfId() {
        return shelfId;
    }

    public void setShelfId(Integer shelfId) {
        this.shelfId = shelfId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public StorageRack getStorageRack() {
        return storageRack;
    }

    public void setStorageRack(StorageRack storageRack) {
        this.storageRack = storageRack;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shelf shelf = (Shelf) o;

        if (!Objects.equals(shelfId, shelf.shelfId)) return false;
        if (!Objects.equals(name, shelf.name)) return false;
        return Objects.equals(storageRack, shelf.storageRack);
    }

    @Override
    public int hashCode() {
        int result = shelfId != null ? shelfId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (storageRack != null ? storageRack.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Shelf{" +
                "shelfId=" + shelfId +
                ", name='" + name + '\'' +
                ", storageRack=" + storageRack +
                '}';
    }
}
