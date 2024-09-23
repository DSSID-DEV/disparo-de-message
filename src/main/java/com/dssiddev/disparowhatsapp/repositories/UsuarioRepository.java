package com.dssiddev.disparowhatsapp.repositories;

import com.dssiddev.disparowhatsapp.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    @Query(value = "select usuario from Usuario usuario", nativeQuery = false)
    List<Usuario> buscarTodos();


}
