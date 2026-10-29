package com.proyekakhir.manajemendokumen.user;

import com.proyekakhir.manajemendokumen.core.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends CrudRepository<User> {

    User findByUsername(String username);

    List<User> findByRole(String role);

    User findByEmail(String userId);
}
