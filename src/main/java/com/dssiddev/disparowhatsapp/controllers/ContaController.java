package com.dssiddev.disparowhatsapp.controllers;


import com.dssiddev.disparowhatsapp.config.security.UserDetailsImpl;
import com.dssiddev.disparowhatsapp.excepttions.NotFoundException;
import com.dssiddev.disparowhatsapp.models.Conta;
import com.dssiddev.disparowhatsapp.models.User;
import com.dssiddev.disparowhatsapp.models.dto.ContaEntradaDTO;
import com.dssiddev.disparowhatsapp.models.dto.ContaSaidaDTO;
import com.dssiddev.disparowhatsapp.repositories.UserRepositorio;
import com.dssiddev.disparowhatsapp.services.ContaService;
import com.dssiddev.disparowhatsapp.services.ContatoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;
import java.util.Optional;
import java.util.List;

@RestController
@RequestMapping("/contas")
public class ContaController {

    @Autowired
    private ContaService service;

    @Autowired
    private UserRepositorio repositorio;

    @PostMapping("/novo")
    public ResponseEntity<Object> criarNovaConta(@RequestBody ContaEntradaDTO contaDTO) {
        try {
            service.salvar(contaDTO);
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{contaId}/ativar")
    public ResponseEntity<ContaSaidaDTO> ativarConta(@PathVariable("contaId") Long contaId) {
        if(!service.exists(contaId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(service.ativar(contaId));
    }

    @GetMapping
    public ResponseEntity<List<ContaSaidaDTO>> findAll() {
        List<ContaSaidaDTO> contas = service.findAll();
        if (contas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(contas);
    }

    @GetMapping("/{contaId}")
    public ResponseEntity<ContaSaidaDTO> findById(@PathVariable("contaId") Long contaId) {
        var conta = service.findById(contaId);
        if (!conta.isPresent()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(convertToDTO(conta.get()));
    }

    private ContaSaidaDTO convertToDTO(Conta conta) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(conta, ContaSaidaDTO.class);
    }

    @GetMapping("/do/logado")
    public ResponseEntity<List<ContaSaidaDTO>> listarContasDoUsuario() {
        var user = obterUsuarioLogado();
        if (!user.isPresent()) return ResponseEntity.notFound().build();
        List<ContaSaidaDTO> contas = service.listarContasDoUsuario(user.get().getId());
        if (contas.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok().body(contas);
    }

    private Optional<User> obterUsuarioLogado() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return repositorio.findById(userDetails.getId()); 
    }

}
