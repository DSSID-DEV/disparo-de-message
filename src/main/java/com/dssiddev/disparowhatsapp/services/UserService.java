package com.dssiddev.disparowhatsapp.services;


import com.dssiddev.disparowhatsapp.controllers.commom.ChecarConflito;
import com.dssiddev.disparowhatsapp.models.User;
import com.dssiddev.disparowhatsapp.models.dto.UserDTO;
import com.dssiddev.disparowhatsapp.models.dto.UsuarioLogado;
import com.dssiddev.disparowhatsapp.repositories.UserRepositorio;
import com.dssiddev.disparowhatsapp.services.constantes.Constantes;
import com.dssiddev.disparowhatsapp.utils.EncryptPassword;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
public class UserService {

    @Autowired
    private UserRepositorio repositorio;

    public User save(User user) {
        return repositorio.save(user);
    }

    public UserDetails logar(String username, String password) {
        return repositorio.logar(username, password);
    }

    private UsuarioLogado convertToDTO(User logado) {
        if (logado != null) {
            ModelMapper modelMapper = new ModelMapper();
            var userLogado = modelMapper.map(logado, UsuarioLogado.class);
            userLogado.formatarViewUsername();
            userLogado.inserirRole(logado.getRoles());
            return userLogado;
        }
        return null;
    }

    public boolean contains() {
        return repositorio.count() > 0;
    }

    public User findByPassword(String provisoria) {
        return repositorio.findByPassword(provisoria);
    }

    public String alterarSenha(User user, String newPassword) {
        String newPasswordEncrypted = encryptPassword(newPassword);
        user.setPassword(newPasswordEncrypted);
        repositorio.save(user);
        return new String("Senha alterada com sucesso!");
    }

    public User findByTelefoneOrEmail(String contato) {
        return repositorio.findbyTelefoneOrEmail(contato);
    }

    public User gerarSenhaProvisoria(User user) {
        String senhaGerada = gerarSenha();
        String sennhaCriptografada = encryptPassword(senhaGerada);
        user.setPassword(sennhaCriptografada);
        repositorio.save(user);
        user.setPassword(senhaGerada);
        return user;
    }

    private String encryptPassword(String senha) {
        EncryptPassword encryptPassword = new EncryptPassword();
        return encryptPassword.encryptPassword(senha);
    }

    private String gerarSenha() {
        Random random = new SecureRandom();
        return String.join("", IntStream.range(0, 6).mapToObj(i ->
        String.valueOf(Constantes.CHARACTERS.charAt(random.nextInt(Constantes.CHARACTERS.length()))))
                .collect(Collectors.toList()));
    }

    public ChecarConflito checarConflito(UserDTO userDTO) {

        ChecarConflito conflito = new ChecarConflito();

        conflito.setHas(false);

        if (repositorio.existsByUsername(userDTO.getUsername())) {
            conflito.setConflict("O nome de usuário já está em uso!");
            conflito.setMsgLog("O nome de usuário {} já está em uso".replace("{}", userDTO.getUsername()));
            conflito.setHas(true);
        }

        if(repositorio.existsByEmail(userDTO.getUsername())) {
            conflito.setConflict("O email do usuário já está em uso!");
            conflito.setMsgLog("O email {} já está em uso".replace("{}", userDTO.getUsername()));
            conflito.setHas(true);
        }
        return conflito;
    }
}
