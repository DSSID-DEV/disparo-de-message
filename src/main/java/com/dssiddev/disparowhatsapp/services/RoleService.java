package com.dssiddev.disparowhatsapp.services;


import com.dssiddev.disparowhatsapp.models.RoleModel;
import com.dssiddev.disparowhatsapp.models.enuns.RoleType;
import com.dssiddev.disparowhatsapp.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository repository;

    public Optional<RoleModel> findByRoleName(RoleType role) {
        return repository.findByRoleName(role);
    }
}
