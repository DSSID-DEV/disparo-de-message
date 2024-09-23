package com.dssiddev.disparowhatsapp.repositories;

import com.dssiddev.disparowhatsapp.models.Questao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestaoRepositorio extends JpaRepository<Questao, Long> {

    @Query("select questao from Questao questao where questao.questionario.id = :formularioId")
    List<Questao> findAllByFormularioId(@Param("formularioId") Long fomrularioId);
}
