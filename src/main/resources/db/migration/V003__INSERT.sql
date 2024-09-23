INSERT INTO TB_USERS (email, password, telefone, username)
VALUES ('dssid.dev@gmail.com', '$2a$12$MqD8SrcDzZu0jkyVMCzlgOsBNzvLN4rP1kZe9A946EEpRsaBj3Nk2', 
'81992560214', 'demetrio.santana@ePost');

INSERT INTO TB_USUARIOS (email, nome, telefone, user_id) VALUES ('dssid.dev@gmail.com', 'Demétrio Antonio de Santana', 
'81992560214', (select id from TB_USERS WHERE email = 'dssid.dev@gmail.com'));

INSERT INTO TB_ROLES (role_name) VALUES ('ROLE_DEV');

INSERT INTO TB_USERS_ROLES (user_id, role_id) VALUES (
(select id from TB_USERS WHERE email = 'dssid.dev@gmail.com'),
(select role_id from TB_ROLES WHERE role_name = 'ROLE_DEV'));
