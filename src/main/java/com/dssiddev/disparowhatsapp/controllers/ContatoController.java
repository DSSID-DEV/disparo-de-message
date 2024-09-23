package com.dssiddev.disparowhatsapp.controllers;

import com.dssiddev.disparowhatsapp.models.Contato;
import com.dssiddev.disparowhatsapp.models.dto.ReturnContato;
import com.dssiddev.disparowhatsapp.services.ContatoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/contatos")
public class ContatoController {
	
	@Autowired
	private ContatoService service;
		
//	@GetMapping
//	public List<Contato> findAll(){
//		return service.findAll();
//	}

	@PostMapping("/upload")	
    @PreAuthorize("hasAnyRole('USER')")
	public ResponseEntity<ReturnContato> extrairContatos(@RequestParam("contatoFile") MultipartFile contatoFile){
		return ResponseEntity.status(HttpStatus.CREATED).body(service.extrairContato(contatoFile));
	}

	@PreAuthorize("hasAnyRole('USER')")
	@GetMapping
	public Page<Contato> listarTodos(
			@RequestParam(defaultValue = "0") int page,
			@RequestParam(defaultValue = "10") int size,
			@RequestParam(required = false) String search
	) {

		Pageable pegeable = PageRequest.of(page, size);
		return service.listarTodos(search,pegeable);
	}
}
