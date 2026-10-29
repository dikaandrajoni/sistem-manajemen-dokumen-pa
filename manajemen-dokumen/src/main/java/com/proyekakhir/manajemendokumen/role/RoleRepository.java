package com.proyekakhir.manajemendokumen.role;

import com.proyekakhir.manajemendokumen.core.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends CrudRepository<Role> {
    Role findByNameEquals(String roleName);
}
