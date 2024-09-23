package com.dssiddev.disparowhatsapp.repositories;

import com.dssiddev.disparowhatsapp.models.QuestaoRespondida;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestaoRespondidaRepositorio extends JpaRepository<QuestaoRespondida, Long> {

    @Query(value = "select selecionado from QuestaoRespondida selecionado where selecionado.formularioId = :formularioId and selecionado.questaoId = :questaoId")
    QuestaoRespondida buscarQuestao(@Param("formularioId") Long formularioId, @Param("questaoId") Long questaoId);

    @Query(value = "select questao from QuestaoRespondida questao where questao.formularioId = :formularioId")
    List<QuestaoRespondida> buscarQuestoesDoFormulario(@Param("formularioId") Long formularioId);

    @Query(value = "select count(questao.id) from QuestaoRespondida questao where questao.opcaoId = :id")
    Integer somarSelecionado(@Param("id") Long id);

    @Query(value = "select count(questao.id) from QuestaoRespondida questao where questao.questaoId = :id")
    Integer totalRespondido(@Param("id") Long id);
}
