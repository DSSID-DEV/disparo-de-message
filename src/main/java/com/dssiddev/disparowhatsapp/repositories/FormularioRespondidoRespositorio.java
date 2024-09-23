package com.dssiddev.disparowhatsapp.repositories;

import com.dssiddev.disparowhatsapp.models.FormularioRespondido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FormularioRespondidoRespositorio extends JpaRepository<FormularioRespondido,Long> {

    @Query("select formulario from FormularioRespondido formulario where formulario.chatId = :chatId and formulario.formularioId = :formularioId ")
    FormularioRespondido buscarFormularioRespondido(@Param("chatId") String chatId, @Param("formularioId") Long formularioId);

    @Query("select formulario from FormularioRespondido formulario where formulario.formularioId = :formularioId ")
    FormularioRespondido findFormulariosRespondido(@Param("formularioId") Long formularioId);
}
