package com.dssiddev.disparowhatsapp.models;

import com.dssiddev.disparowhatsapp.models.enuns.RoleType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "TB_ROLES")
public class RoleModel implements Serializable {

    private static final long serialVersionUID = 74891148550346692L;

    @Id
    @Column(name = "ROLE_ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID roleId;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE_NAME", nullable = false, unique = true, length = 30)
    private RoleType roleName;

    @JsonIgnore
    public String getAuthority() {
        return this.roleName.toString();
    }
}