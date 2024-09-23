package com.dssiddev.disparowhatsapp.controllers;


import com.dssiddev.disparowhatsapp.excepttions.ConstraintUniqueException;
import com.dssiddev.disparowhatsapp.models.RoleModel;
import com.dssiddev.disparowhatsapp.models.dto.ResumeUsuario;
import com.dssiddev.disparowhatsapp.models.dto.UsuarioDTO;
import com.dssiddev.disparowhatsapp.models.enuns.RoleType;
import com.dssiddev.disparowhatsapp.services.UsuarioService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/usuarios")
@RequiredArgsConstructor
public class UsuarioController {

    private final UsuarioService service;
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> save(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            service.save(usuarioDTO);
        } catch (ConstraintUniqueException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getCause());
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PutMapping(value = "/{usuarioId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Object> update(@PathVariable(value="usuarioId") Long usuariId, @RequestBody UsuarioDTO usuarioDTO) {
        var usuario = service.findById(usuariId);
        if (!usuario.isPresent()) {
            return ResponseEntity.notFound().build();
        }
        service.atualizar(usuarioDTO, usuariId);
        return ResponseEntity.ok().build();
    }


    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<ResumeUsuario>> findAll() {
        List<ResumeUsuario> usuarios = service.findAll();
        if (usuarios.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(usuarios);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping(value = "/{usuarioId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<UsuarioDTO> findById(@PathVariable("usuarioId") Long usuarioId) {
        var usuarioExistente = service.buscarUsuario(usuarioId);
        if(usuarioExistente == null)
            return ResponseEntity.notFound().build();
        return ResponseEntity.status(HttpStatus.OK).body(usuarioExistente);
    }
    @PreAuthorize("hasAnyRole('ADMIN')")
    @DeleteMapping("/{usuarioId}")
    public ResponseEntity<Object> deleteUsuario(@PathVariable("usuarioId") Long usuarioId) {
        var usuarioExistente = service.findById(usuarioId);
        if(!usuarioExistente.isPresent())
            return ResponseEntity.notFound().build();
        return !service.deleteUsuario(usuarioExistente.get()) ? ResponseEntity.badRequest().build() : ResponseEntity.ok().build();
    }

}
