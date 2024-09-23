package com.dssiddev.disparowhatsapp.services;

import com.dssiddev.disparowhatsapp.config.security.UserDetailsImpl;
import com.dssiddev.disparowhatsapp.excepttions.NotFoundException;
import com.dssiddev.disparowhatsapp.models.Conta;
import com.dssiddev.disparowhatsapp.models.User;
import com.dssiddev.disparowhatsapp.models.dto.ContaSaidaDTO;
import com.dssiddev.disparowhatsapp.models.dto.ContaEntradaDTO;
import com.dssiddev.disparowhatsapp.repositories.ContaRepository;
import com.dssiddev.disparowhatsapp.repositories.UserRepositorio;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ContaService {

    @Autowired
    private ContaRepository repository;

    @Autowired
    private UserRepositorio userRepositorio;

    @Transactional
    public void salvar(ContaEntradaDTO contaDTO) throws NotFoundException {
        var conta = convertToEntity(contaDTO);
        repository.save(conta);
    }

    private Conta convertToEntity(ContaEntradaDTO contaDTO) throws NotFoundException {
        var conta = new Conta();
        conta.setDescricao(contaDTO.getDescricao());
        conta.setTelefone(contaDTO.getTelefone());
        var user = obterUsuarioLogado();
        if (!user.isPresent()) throw new NotFoundException("Usuario não encontrado!");
        user.get().getContas().add(conta);
        conta.setUser(user.get());
        conta.setAtiva(conta.isAtiva());
        return conta;
    }

    public List<ContaSaidaDTO> listarContasDoUsuario(Long userId) {
        List<Conta> contas = repository.findByUserId(userId);
        return contas.stream().map(conta -> convertToDTO(conta)).collect(Collectors.toList());
    }

    private ContaSaidaDTO convertToDTO(Conta conta) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(conta, ContaSaidaDTO.class);
    }

    public List<ContaSaidaDTO> findAll() {
      List<Conta>  contas = repository.findAll();
      return contas.stream().map(conta -> convertToDTO(conta)).collect(Collectors.toList());
    }

    public Optional<User> obterUsuarioLogado() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return userRepositorio.findById(userDetails.getId()); 
    }

    public Optional<Conta> findById(Long contaId) {
        return repository.findById(contaId);
    }

    public ContaSaidaDTO ativar(Long contaId) {
        var conta = repository.findById(contaId).get();
        conta.setAtiva(true);
        repository.save(conta);
        return convertToDTO(conta);
    }
    public boolean exists(Long contaId) {
        return repository.existsById(contaId);
    }

    public void delete(Long contaId) {
        var conta = repository.findById(contaId).orElse(null);
        if(conta != null){
            conta.setUser(null);
            repository.save(conta);
        }
        repository.deleteById(contaId);
    }
}
