package com.dssiddev.disparowhatsapp.controllers;

import com.dssiddev.disparowhatsapp.services.UserService;
import com.dssiddev.disparowhatsapp.services.WhatsAppService;
import com.dssiddev.disparowhatsapp.utils.EncryptPassword;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.websocket.server.PathParam;

@RestController
@CrossOrigin(origins = "http://localhost:3005", maxAge = 3600)
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService service;

    @Autowired
    private WhatsAppService whatsAppService;

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/contains")
    public ResponseEntity<Void> contains () {
       return !service.contains() ? ResponseEntity.noContent().build() :
               ResponseEntity.ok().build();
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/senha/alterar/{contato}")
    public ResponseEntity<String> enivarSenhaProvisoria(@PathVariable("contato") String contato) {
        var user = service.findByTelefoneOrEmail(contato);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seu email ou telefone não foi encontrato em nossos registros!");
        }
        user = service.gerarSenhaProvisoria(user);
        if(whatsAppService.enviarSenhaProvisoria(user)) {
            return ResponseEntity.status(HttpStatus.OK).body("Enviamos uma senha provisória para o seu whatsapp.");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Falha ao tentar enviar uma senha provisória!");

    }
    @PreAuthorize("hasAnyRole('USER')")
    @PutMapping("/senha/alterar/{provisoria}")
    public ResponseEntity<String> alterarSenha(@PathParam("provisoria") String provisoria, @RequestParam("newPassword") String newPassword) {
        EncryptPassword encryptPassword = new EncryptPassword();
        String senhaProvisoria = encryptPassword.encryptPassword(provisoria);
        var user = service.findByPassword(senhaProvisoria);
        if (user != null) {
            return ResponseEntity.ok().body(service.alterarSenha(user, newPassword));
        }
        return ResponseEntity.badRequest().body("Senha incorreta!");
    }
}
