package org.study.hydrowarehouse.entity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * Represents a user role within the system.
 * <p>
 * Mapped to the {@code roles} table in the database, this entity defines different roles (e.g., ADMIN, USER) that can
 * be assigned to {@link User} entities to manage permissions and access control.
 * </p>
 *
 * @author Aliaksandr Pishchala
 */
@Entity
@Table(name = "roles", uniqueConstraints = @UniqueConstraint(columnNames = "name"))
public class Role implements Serializable {

    private static final long serialVersionUID = 8586938457852993437L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "role_id")
    private Integer roleId;

    @Enumerated(EnumType.STRING)
    private ERole name;

    @OneToMany(mappedBy = "role", fetch = FetchType.LAZY)
    private List<User> users;

    public Role() {
    }

    public Role(ERole name) {
        this.name = name;
    }

    public Role(Integer roleId, ERole name) {
        this.roleId = roleId;
        this.name = name;
    }

    public Integer getRoleId() {
        return roleId;
    }

    public void setRoleId(Integer roleId) {
        this.roleId = roleId;
    }

    public ERole getName() {
        return name;
    }

    public void setName(ERole name) {
        this.name = name;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Role role = (Role) o;

        if (!Objects.equals(roleId, role.roleId)) return false;
        if (name != role.name) return false;
        return Objects.equals(users, role.users);
    }

    @Override
    public int hashCode() {
        int result = roleId != null ? roleId.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (users != null ? users.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Role{" +
                "roleId=" + roleId +
                ", role=" + name +
                ", users=" + users +
                '}';
    }
}