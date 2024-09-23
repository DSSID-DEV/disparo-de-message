package com.dssiddev.disparowhatsapp.repositories;

import com.dssiddev.disparowhatsapp.models.Conta;
import com.dssiddev.disparowhatsapp.models.Contato;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Repository
public interface ContaRepository extends JpaRepository<Conta, Long> {

    boolean existsByDescricao(String descricao);

    @Query(value = "select conta from Conta conta where conta.user.id = :userId")
    List<Conta> findByUserId(Long userId);

    Optional<Conta> findByDescricao(String descricao);
}
