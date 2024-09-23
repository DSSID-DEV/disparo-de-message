package com.dssiddev.disparowhatsapp.repositories;

import com.dssiddev.disparowhatsapp.models.Opcao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OpcaoRepositorio extends JpaRepository<Opcao, Long> {

    @Query("select opcao from Opcao opcao where opcao.questao.id = :id")
    List<Opcao> findAllByQuestaoId(@Param("id") Long id);
}
