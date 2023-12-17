package org.study.hydro.dao;

import org.study.hydro.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserDao {

    int save(User user);

    List<User> users(int limit, int offset);

    Optional<User> getUserById(int id);
}
