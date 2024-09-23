package com.dssiddev.disparowhatsapp.models.dto;


import com.dssiddev.disparowhatsapp.models.Conta;
import com.dssiddev.disparowhatsapp.models.RoleModel;
import com.dssiddev.disparowhatsapp.models.enuns.RoleType;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Data
public class ResumeUsuario {
    private Long usuarioId;
    private String nome;
    private String username;
    private String telefone;
    private String permissao;
    private String contas ="";

    public void carregarContas(Set<Conta> contas) {
        if (contas.isEmpty()) {
            return;
        }
        contas.forEach(conta -> this.contas += conta.getDescricao() + " ");
        this.contas = this.contas.replace(" ", "; ");
        this.contas = this.contas.substring(0, this.contas.length() - 2);
    }

    public void inserirMaiorPrivilegio(Set<RoleModel> roles) {;
        Set<RoleType> roleTypes = new HashSet<>();
        for (RoleModel role: roles){
            roleTypes.add(role.getRoleName());
        }
        this.permissao = roleTypes.contains(RoleType.ROLE_DEV) ? "DESENVOLVEDOR" :
                roleTypes.contains(RoleType.ROLE_ADMIN) ? "ADMINISTRADOR" : "USUARIO";
    }
}
