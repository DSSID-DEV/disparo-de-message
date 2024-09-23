package com.dssiddev.disparowhatsapp.repositories;

import com.dssiddev.disparowhatsapp.models.Conta;
import com.dssiddev.disparowhatsapp.models.Questionario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionarioRepositorio extends JpaRepository<Questionario, Long> {
    List<Questionario> findByContaId(Long contaId);

    @Query("select q from Questionario q where q.conta.id in (:contaIds)")
    List<Questionario> findByInContaId(@Param("contaIds") List<Long> contaIds);

    @Query("select q from Questionario q where q.conta.id in (:contas)")
    List<Questionario> listarTodosDosIds(List<Long> contas);
}
