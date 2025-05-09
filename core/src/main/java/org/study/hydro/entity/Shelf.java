package org.study.hydro.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "shelf")
public class Shelf implements Serializable {

    private static final long serialVersionUID = -7734520313839121890L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_shelf")
    private Integer shelfId;

    @Column(name = "name")
    private String name;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "shelf")
    private List<StorageRack> storageRackList;


    public Shelf() {
    }

    public Shelf(String name, List<StorageRack> storageRackList) {
        this.name = name;
        this.storageRackList = storageRackList;
    }

    public Shelf(Integer shelfId, String name) {
        this.shelfId = shelfId;
        this.name = name;
    }

    public Shelf(String name) {
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

    public List<StorageRack> getStorageRackList() {
        return storageRackList;
    }

    public void setStorageRackList(List<StorageRack> storageRackList) {
        this.storageRackList = storageRackList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Shelf shelf = (Shelf) o;

        if (!Objects.equals(shelfId, shelf.shelfId)) return false;
        if (!Objects.equals(name, shelf.name)) return false;
        return Objects.equals(storageRackList, shelf.storageRackList);
    }

    @Override
    public int hashCode() {
        int result = shelfId != null ? shelfId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (storageRackList != null ? storageRackList.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Shelf{" +
                "shelfId=" + shelfId +
                ", name='" + name + '\'' +
                ", storageRackList=" + storageRackList +
                '}';
    }
}
