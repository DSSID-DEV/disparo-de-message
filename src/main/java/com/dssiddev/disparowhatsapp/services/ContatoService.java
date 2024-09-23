package com.dssiddev.disparowhatsapp.services;

import com.dssiddev.disparowhatsapp.models.Contato;
import com.dssiddev.disparowhatsapp.models.dto.ContatoDTO;
import com.dssiddev.disparowhatsapp.models.dto.ReturnContato;
import com.dssiddev.disparowhatsapp.models.enuns.TipoDeArquivo;
import com.dssiddev.disparowhatsapp.repositories.ContatoRepository;
import com.dssiddev.disparowhatsapp.repositories.specification.ContatoSpecification;
import com.dssiddev.disparowhatsapp.services.constantes.Constantes;
import com.dssiddev.disparowhatsapp.services.utils.Utils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class ContatoService {

	private ReturnContato returnContato;
	private Set<Contato> contatos;
	@Autowired
	private ContatoRepository repository;


	@Transactional
    public ReturnContato extrairContato(MultipartFile contatoFile) {
		if (contatoFile == null){
			System.out.println("ESTÁ NULO");
			return null;
		}
		if (!contatoFile.isEmpty()) {
			TipoDeArquivo tipo = getTipo(contatoFile.getOriginalFilename());
			switch (tipo) {
				case CSV:
					extrairDoArquivoCSV(contatoFile);
					break;
				case VCF:
					extrairDoArquivoVCF(contatoFile);
					break;
				case TXT:
					extrairDoArquivoTXT(contatoFile);
					break;
			}
			saveAll();
		}
		return returnContato;
    }

	private static TipoDeArquivo getTipo(String fileName) {
		String tipo = fileName.split("\\.")[1];
		return TipoDeArquivo.from(tipo);
	}

	private void saveAll() {
		for (Contato contato : contatos) {
			var registro = repository.findByCellPhone(contato.getCellPhone());
			if (registro.isPresent()) {
				returnContato.descartar();
			} else {
				repository.save(contato);
				returnContato.contabilizarSalvo();
			}
		}
		returnContato.setQtdEmBaseDeDados(Integer.parseInt(String.valueOf(repository.count())));
		returnContato.finalizarProcessos();

	}


	private void extrairDoArquivoTXT(MultipartFile contatoFile) {
		Set<Contato> contatos = new HashSet<>();
	}

	private void extrairDoArquivoVCF(MultipartFile contatoFile) {
		contatos = new HashSet<>();
		returnContato = new ReturnContato();
		try {
			Scanner scanner = new Scanner(contatoFile.getInputStream());
			Contato contato = null;
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				if(line.startsWith("BEGIN")) {
					contato = new Contato();
				}

				if(line.startsWith("FN") || line.startsWith("FN;CHARSET=UTF-8")) returnContato.contarProcessados();

				if (Utils.linhaValidaDoArquivoVCF(line)) {
					if (line.startsWith("FN")) contato.setNome(obterNomeVCF(line));

					if ((line.startsWith("TEL;CELL:") ||
							line.startsWith("TEL;X-Celular:")) &&
					Utils.temCriteriosMinimosParaCelular(line)) contato.setCellPhone(obterNomeVCF(line));

				}

				if(line.startsWith("END:")) {
					if (validarContato(contato)) {
						contato.setAtivo(true);
						contatos.add(contato);
					} else {
						returnContato.descartar();
					}
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}


	private String obterNomeVCF(String line) {
		String dado = line.split(":")[1].isBlank() ? "" : line.split(":")[1];
		log.info(String.format("linha: %s com o dado: %s", line, dado));
		if(line.startsWith("FN")) return dado.isBlank() ||
				line.startsWith("FN;CHARSET=UTF-8") ? Constantes.SEM_NOME : dado;

		if(line.startsWith("TEL" +
				";CELL:") ||
				line.startsWith("TEL;X-Celular:"))
			return dado.isBlank() ? "" : Utils.validarNumber(dado);

		return dado;
	}

	private void extrairDoArquivoCSV(MultipartFile contatoFile) {
		contatos = new HashSet<>();
		returnContato = new ReturnContato();
		try {
			Scanner scanner = new Scanner(contatoFile.getInputStream());
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				returnContato.contarProcessados();
				if (Utils.linhaValida(line)) {
					Contato contato = new Contato();
					contato.setNome(obterNome(line));
					contato.setCellPhone(obterCellFone(line));
					if (validarContato(contato)) {
						contato.setAtivo(true);
						contatos.add(contato);
					}
				} else {
					returnContato.descartar();
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private boolean validarContato(Contato contato) {
//		return !contato.getNome().isBlank() && !contato.getCellPhone().isBlank();
		if(contato.getNome() == null) {
			contato.setNome(Constantes.SEM_NOME);
		}
		for(String prefix : Constantes.PREFIXOS){
			log.info(String.format("O contato %s contem o préfixo? R: %s", contato.getNome(),
					(contato.getNome() == null || contato.getNome().substring(prefix.length()).length() < 2) ? "SIM" : "NÃO"));
			if(contato.getNome() == null || contato.getNome().substring(prefix.length()).length() < 2) return false;
			if(contato.getNome().startsWith(prefix + " ")) return false;
		}
		var exists = contato.getCellPhone() != null ? repository.existsBycellPhone(contato.getCellPhone()) : true;
		return !exists && !contato.getCellPhone().isBlank();
	}

	private String obterCellFone(String line) {
		String numero = Utils.obterDado(line, Constantes.POSICAO_CELLPHONE);
		return Utils.validarNumber(numero);
	}
	private String obterNome(String line) {
		String nome = Utils.obterDado(line, Constantes.POSICAO_NOME);
		return line.split(Constantes.VIRGULA)[0].replace("~", Constantes.VAZIO);
	}

	public List<Contato> findAll() {
		return repository.findAll();
	}

	private ContatoDTO convertToDTO(Contato contato) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(contato, ContatoDTO.class);
	}
	public Page<Contato> listarTodos(String contato, Pageable pageable) {
		Specification<Contato> finalSpec = ContatoSpecification.containsNomeOrCellPhone(contato);
		return repository.findAll(finalSpec, pageable);
	}

}
