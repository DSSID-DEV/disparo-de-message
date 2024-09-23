package com.dssiddev.disparowhatsapp.services;


import com.dssiddev.disparowhatsapp.excepttions.ConstraintUniqueException;
import com.dssiddev.disparowhatsapp.models.Conta;
import com.dssiddev.disparowhatsapp.models.RoleModel;
import com.dssiddev.disparowhatsapp.models.User;
import com.dssiddev.disparowhatsapp.models.Usuario;
import com.dssiddev.disparowhatsapp.models.dto.ResumeUsuario;
import com.dssiddev.disparowhatsapp.models.dto.UsuarioDTO;
import com.dssiddev.disparowhatsapp.models.enuns.RoleType;
import com.dssiddev.disparowhatsapp.repositories.ContaRepository;
import com.dssiddev.disparowhatsapp.repositories.RoleRepository;
import com.dssiddev.disparowhatsapp.repositories.UserRepositorio;
import com.dssiddev.disparowhatsapp.repositories.UsuarioRepository;
import com.dssiddev.disparowhatsapp.services.constantes.Constantes;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.text.Normalizer;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class UsuarioService {

    private final UsuarioRepository repository;
    private final UserRepositorio userRepositorio;

    private final ContaRepository contaRepository;

    private final WhatsAppService whatsAppService;

    private final RoleRepository roleRepository;

    @Transactional
    public void save(UsuarioDTO usuarioDTO) throws ConstraintUniqueException {
        var usuario = convertToEntitu(usuarioDTO);
        repository.save(usuario);
        whatsAppService.enviarSenhaProvisoria(usuario.getUser());
    }

    private void verificarConstraintUnique(String contas) throws ConstraintUniqueException {
        if (contas.isBlank()) {
            return;
        }

        String[] split = contas.split("/s+/");

        for (String conta : split) {
            if(contaRepository.existsByDescricao(conta)) {
                throw new ConstraintUniqueException(String.format("Já consta uma conta com o nome: %s",conta));
            }
        }
    }

    private Usuario convertToEntitu(UsuarioDTO usuarioDTO) throws ConstraintUniqueException{
        var usuario = new Usuario();
        usuario.setNome(usuarioDTO.getNome());
        usuario.setEmail(usuarioDTO.getEmail());
        usuario.setTelefone(usuarioDTO.getTelefone());
        var user = prepararUser(usuarioDTO);
        usuario.setUser(user);
        return usuario;
    }

    private User prepararUser(UsuarioDTO usuarioDTO) throws ConstraintUniqueException{
        var user = new User();
        user.setUsername(prepararUserName(usuarioDTO.getNome(),  usuarioDTO.getEmail()));
        user.setTelefone(usuarioDTO.getTelefone());
        user.setEmail(usuarioDTO.getEmail());
        String passWordGerado = gerarPassWord();
        user.setPassword(passWordGerado);

        RoleModel roleModel = salvarRoles(usuarioDTO.getPermissao());
        user.inserirRoles(roleModel);
        user.setContas(prepararContas(user, usuarioDTO.getContas()));
        userRepositorio.save(user);
        return user;
    }

    private RoleModel salvarRoles(String permissao) {

        RoleModel roleModel = CriaRole(permissao);
        return roleRepository.save(roleModel);
    }

    private RoleModel CriaRole(String permissao) {
        permissao = permissao.equalsIgnoreCase("DESENVOLVEDOR") ? "DEV" :
                permissao.equalsIgnoreCase("ADMINISTRADOR") ? "ADMIN" : "USER";
        String role = "ROLE_"+ permissao;
        RoleModel roleModel = new RoleModel();
        RoleType roleType = RoleType.findRole(role);
        roleModel.setRoleName(roleType);
        return roleModel;
    }

    private String gerarPassWord() {
        Random random = new SecureRandom();
        return String.join("", IntStream.range(0, 6).mapToObj(i ->
                String.valueOf(Constantes.CHARACTERS.charAt(random.nextInt(Constantes.CHARACTERS.length()))))
                .collect(Collectors.toList()));
    }

    private Set<Conta> prepararContas(User user, String contasString) throws ConstraintUniqueException{
        Set<Conta> contas = new HashSet<>();
        if (contasString.isBlank()) {
            return contas;
        }
        String[] descricoes = contasString.split(";");
        for (String descricao : descricoes) {
            verificarConstraintUnique(descricao);
            var conta = new Conta();
            conta.setDescricao(descricao);
            conta.setUser(user);
            conta.setAtiva(false);
            contaRepository.save(conta);
            contas.add(conta);
        }
        return contas;
    }

    private String prepararUserName(String nome, String email) {
        nome = Normalizer.normalize(nome, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "").toLowerCase();
        String[] names = nome.split(" ");
        int length = names.length -1 ;
        String username = names.length < 1 ? email : (names[0]+"_"+names[length]);
        if (!userRepositorio.existsByUsername(username)) {
            username = !username.contains("@") ? username + "@" + "e.post" : username;
        }
       return username;
    }

    public List<ResumeUsuario> findAll() {
        List<Usuario> usuarios = repository.buscarTodos();

        if (usuarios.isEmpty()) {
            return new ArrayList<>();
        }
        return usuarios.stream().map(usuario -> convertToResumeUsuario(usuario)).collect(Collectors.toList());
    }

    private ResumeUsuario convertToResumeUsuario(Usuario usuario) {
        var resumoUsuario = new ResumeUsuario();
        resumoUsuario.setUsuarioId(usuario.getUsuarioId());
        resumoUsuario.setNome(usuario.getNome());
        resumoUsuario.setUsername(usuario.getUser().getUsername());
        resumoUsuario.setTelefone(usuario.getTelefone());
        resumoUsuario.inserirMaiorPrivilegio(usuario.getUser().getRoles());
        resumoUsuario.carregarContas(usuario.getUser().getContas());

        return resumoUsuario;
    }

    public boolean exists(Long usuarioId) {
        return repository.existsById(usuarioId);
    }

    public Optional<Usuario> findById(Long usuarioId) {
        return repository.findById(usuarioId);
    }

    @Transactional
    public boolean deleteUsuario(Usuario usuario) {
        removerAccess(usuario.getUser());
        repository.delete(usuario);
        return true;
    }

    private void removerAccess(User user) {
        List<Conta> contas = contaRepository.findByUserId(user.getId());
        if(!contas.isEmpty()){
            for(Conta conta : contas) {
                contaRepository.delete(conta);
            }
        }
        userRepositorio.delete(user);
    }

    public UsuarioDTO buscarUsuario(Long usuarioId) {
        var usuarioExisente = findById(usuarioId);
        if (!usuarioExisente.isPresent())
            return null;

        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(usuarioExisente.get(),UsuarioDTO.class);
    }

    @Transactional
    public void atualizar(UsuarioDTO dto, Long usuarioId) {
        var convertido = convertToEntity(dto);
        convertido.setUsuarioId(usuarioId);
        var user = convertido.getUser();
        var permissao = roleRepository.findByRoleName(CriaRole(dto.getPermissao()).getRoleName()).get();
        if (permissao != null) {
            permissao = salvarRoles(dto.getPermissao());
        }

        user.inserirRoles(permissao);
        convertido.setUser(user);
        repository.save(convertido);
    }

    public Usuario convertToEntity(UsuarioDTO usuario) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(usuario, Usuario.class);
    }
}
