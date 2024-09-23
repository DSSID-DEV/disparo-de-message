package com.dssiddev.disparowhatsapp.controllers;

import com.dssiddev.disparowhatsapp.handles.MultipartFileHandler;
import com.dssiddev.disparowhatsapp.models.dto.MensagemEnviadaDTO;
import com.dssiddev.disparowhatsapp.models.dto.PayloadDTO;
import com.dssiddev.disparowhatsapp.models.enuns.ContentType;
import com.dssiddev.disparowhatsapp.repositories.ContaRepository;
import com.dssiddev.disparowhatsapp.services.ContaService;
import com.dssiddev.disparowhatsapp.services.ContatoService;
import com.dssiddev.disparowhatsapp.services.QuestionarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.dssiddev.disparowhatsapp.services.WhatsAppService;
import org.springframework.web.multipart.MultipartFile;

import java.util.Set;

@RestController
@RequestMapping("/whatsapp")
public class WhatsAppController {
	
	@Autowired
	private WhatsAppService service;

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private QuestionarioService questionarioService;

	@Autowired
	private ContaService contaService;

	@Autowired
	MultipartFileHandler multipartFileHandler;

	@PreAuthorize("hasAnyRole('USER')")
	@PostMapping(path = "/enviar", produces = MediaType.APPLICATION_JSON_VALUE,
			consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.OK)
	public ResponseEntity<Set<MensagemEnviadaDTO>> sendMessage(@RequestParam(value = "arquivo", required = false) MultipartFile arquivo,
															   @RequestParam(value = "titulo", required = false) String titulo,
															   @RequestParam(value = "mensagem", required = false) String mensagem,
															   @RequestParam(value = "link", required = false) String link,
															   @RequestParam(value = "conclusao", required = false) String conclusao,
															   @RequestParam(value = "session", required = false) String sessionId,
															   @RequestParam("contentType") String contentType) throws Exception	{
		MultipartFile file = arquivo != null ? multipartFileHandler.payloadHandle(arquivo) : null;
		ContentType type = ContentType.from(contentType);
		return ResponseEntity.status(HttpStatus.OK)
				.body(service.sendMessage(new PayloadDTO(type, titulo, file, mensagem, link, conclusao), sessionId));
	}


	@DeleteMapping("/{contaId}")
	@ResponseStatus(HttpStatus.NOT_FOUND)
	public void delete(@PathVariable("contaId") Long contaId) {
		contaService.delete(contaId);
	}


}
