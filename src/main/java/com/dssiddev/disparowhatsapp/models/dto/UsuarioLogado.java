package com.dssiddev.disparowhatsapp.models.dto;

import com.dssiddev.disparowhatsapp.models.RoleModel;
import com.dssiddev.disparowhatsapp.models.User;
import com.dssiddev.disparowhatsapp.models.enuns.RoleType;
import lombok.*;
import org.modelmapper.ModelMapper;

import java.util.Set;

@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioLogado {
    private Long id;
    private String viewUsername;
    private String username;
    private String permissao;

    public void formatarViewUsername() {
        if (this.username.contains("@e.post")) {
            String[] split = getUsername().replace("@e.post", "")
                    .split("_");
            this.viewUsername = split[0].substring(0, 1).toUpperCase() +split[0].substring(1)
            + " " +split[1].substring(0, 1).toUpperCase() + split[1].substring(1);
        } else {
            this.viewUsername = username.split("@")[0];
        }
    }

    public void inserirRole(Set<RoleModel> roles) {
        permissao = roles.stream().map(roleModel -> {
            return roleModel.getRoleName().equals(RoleType.ROLE_DEV) ?
                    RoleType.ROLE_ADMIN : roleModel.getRoleName()
                    .equals(RoleType.ROLE_ADMIN) ? RoleType.ROLE_ADMIN :
                    RoleType.ROLE_USER;
        }).toString();
    }
}
