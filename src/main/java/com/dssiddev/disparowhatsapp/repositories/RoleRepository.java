package com.dssiddev.disparowhatsapp.repositories;

import com.dssiddev.disparowhatsapp.models.RoleModel;
import com.dssiddev.disparowhatsapp.models.enuns.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;


@Repository
public interface RoleRepository extends JpaRepository<RoleModel, UUID> {

    Optional<RoleModel> findByRoleName(RoleType role);

}
