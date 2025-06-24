package org.study.hydrowarehouse.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "storage_racks")
public class StorageRack implements Serializable {

    private static final long serialVersionUID = -5204860185261656181L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rack_id")
    private Integer rackId;

    @Column(name = "name", nullable = false, length = 45)
    private String name;

    public StorageRack() {
    }

    public StorageRack(String name) {
        this.name = name;
    }

    public StorageRack(Integer rackId, String name) {
        this.rackId = rackId;
        this.name = name;
    }

    public Integer getRackId() {
        return rackId;
    }

    public void setRackId(Integer rackId) {
        this.rackId = rackId;
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

        StorageRack that = (StorageRack) o;

        if (!Objects.equals(rackId, that.rackId)) return false;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        int result = rackId != null ? rackId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "StorageRack{" +
                "rackId=" + rackId +
                ", name='" + name + '\'' +
                '}';
    }
}
