package com.dssiddev.disparowhatsapp.models.enuns;

public enum RoleType {
        ROLE_DEV,
        ROLE_ADMIN,
        ROLE_USER;

    public static RoleType findRole(String permissao) {
        for (var roleT: RoleType.values()) {
            if (roleT.toString().equalsIgnoreCase(permissao)) return roleT;
        }
        return ROLE_DEV;
    }

}
