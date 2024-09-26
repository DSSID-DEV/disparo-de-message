package com.dssiddev.disparowhatsapp.services;

import com.dssiddev.disparowhatsapp.config.security.UserDetailsImpl;
import com.dssiddev.disparowhatsapp.models.*;
import com.dssiddev.disparowhatsapp.models.dto.*;
import com.dssiddev.disparowhatsapp.models.enuns.ContentType;
import com.dssiddev.disparowhatsapp.repositories.*;
import com.dssiddev.disparowhatsapp.services.constantes.Constantes;
import com.dssiddev.disparowhatsapp.services.constantes.EndPoints;
import com.dssiddev.disparowhatsapp.utils.EncoderAndDecoderBase64;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.*;
import java.util.*;

@Slf4j
@Service
public class WhatsAppService {

	private static final int _200 = 200;

	private static final String FILE = "file=";
	private static final String COUNTRY = "55";
	private static final String NUMBER = "number= ";
	private static final String MESSAGE = "message=";
	private static final String METHOD_POST = "POST";
	private static final String SEND_MEDIA = "send-media";
	private static final String CONTRIBUICAO = "Contribuição: ";
	private static final String SEND_MESSAGE = "send-message";
	private static final String API = "http://95.216.210.141:8005/";
	private static final String NUMERO_DA_SORTE = "Número para sorteio: ";
	private static final String IMG = "http://lh3.ggpht.com/_QFL-9aHGKOE/TJZN24NZwlI/AAAAAAAAHFc/cZk60h1Z9sg/obg.gif";

	private static final HttpClient client = HttpClient.newBuilder()
			.connectTimeout(Duration.ofSeconds(20))
			.build();

	@Autowired
	private ContatoRepository contatoRepository;
	@Autowired
	private QuestionarioRepositorio questionarioRepositorio;

	@Autowired
	private ContaRepository contaRepository;

	@Autowired
	private UserRepositorio userRepositorio;

	@Value("${dssid.dev.apiKey}")
	private String apiKey;

	@Value("${dssid.dev.host-api-whatsapp}")
	private String hostApiWhatsapp;

	@Value("${dssid.dev.sessionId}")
	private String sessionDefault;

	private Set<MensagemEnviadaDTO> mensagensEnviadas;

	private int loop = 1;

	public Set<MensagemEnviadaDTO> sendMessage(PayloadDTO payload, String sessionId) {
		try {
			log.info(String.format("Iniciar processo de envio de whatsapp!"));
			this.mensagensEnviadas = new HashSet<>();
			sendWhatsapp(payload, useSessionId(sessionId));
			log.info(String.format("Termino do processo de envio de whatsapp!"));
		} catch (IOException e) {
			log.error(String.format("Erro ao tentar enviar mensagem via whatsapp [caused by: {%s}", e.getCause()));
			throw new RuntimeException(e);
		}
		return mensagensEnviadas;
    }

	private String useSessionId(String sessionId) {
		return sessionId.isBlank() ? sessionDefault : sessionId;
	}

	private void sendWhatsapp(PayloadDTO payload, String sessionId) throws IOException {
		List<Contato> contatos = contatoRepository.findAll();
		log.info(String.format("Lista de contatos carregados com %s contatos!", contatos.size()));
		String naoQuerReceberMensagem = criarDePermissao();
		for (Contato contato :  contatos) {
			if(contato.isAtivo()) {
	//			String jsonTemplate = templateFormatJson();
					var mensagemEnviada = new MensagemEnviadaDTO();
					String saudacao = criarSaudacao(contato);
	//			String message = jsonTemplate.replace("{saudacao}", criarSaudacao());
				log.info(String.format("Envio de saudação via whatsapp com a saudacao: %s", saudacao));
				sendMessageText(contato, saudacao, useSessionId(sessionId));
				mensagemEnviada.setSaudacao(saudacao);
				if (!payload.getTitulo().isBlank()) {
					log.info(String.format("Envio do título da mensagem via whatsapp com o título: %s", payload.getTitulo()));
					sendMessageText(contato, payload.getTitulo(), useSessionId(sessionId));
//				message = message.replace("{titulo}", payload.getTitulo());
					mensagemEnviada.setTitulo(payload.getTitulo());
				}

				if (payload.getFile() != null) {
					log.info(String.format("Envio de arquivo via whatsapp com o arquivo: %s", payload.getFile().getName()));
					sendArquivo(contato, payload.getFile(), useSessionId(sessionId));
//				message = message.replace("{arquivo}", payload.getFile().getName());
					mensagemEnviada.setArquivo(payload.getFile().getName());
				}

				if (!payload.getMensagem().isBlank()) {
					log.info(String.format("Envio da mensagem via whatsapp com a mensagem: %s", payload.getMensagem()));
					sendMessageText(contato, payload.getMensagem(), useSessionId(sessionId));
//				message = message.replace("{message}", payload.getMensagem());
					mensagemEnviada.setMensagem(payload.getMensagem());
				}

				if (!payload.getLink().isBlank()) {
					log.info(String.format("Envio link via whatsapp com o link: %s", payload.getLink()));
					sendMessageText(contato, payload.getLink(), useSessionId(sessionId));
//				message = message.replace("{link}", payload.getLink());
					mensagemEnviada.setLink(payload.getLink());
				}

				if (!payload.getConclusao().isBlank()) {
					log.info(String.format("Envio da conclusão via whatsapp com o conclusão: %s", payload.getConclusao()));
					sendMessageText(contato, payload.getConclusao(), useSessionId(sessionId));
//				message = message.replace("{conclusao}", payload.getConclusao());
					mensagemEnviada.setConclusao(payload.getConclusao());
				}

				log.info(String.format("Envio de permissão para envio: %s", naoQuerReceberMensagem));
				sendMessageText(contato, naoQuerReceberMensagem, useSessionId(sessionId));
				mensagemEnviada.setConclusao(naoQuerReceberMensagem);

				mensagemEnviada.addContato(contato);
				this.mensagensEnviadas.add(mensagemEnviada);

				try {
					Thread.sleep(200);
				} catch (InterruptedException Ie) {
					Ie.printStackTrace();
				}
			}
		}

	}

	private String configurarChatId(String cellPhone) {
		System.out.println(cellPhone.substring(1) + Constantes.CUS);
		return cellPhone.substring(1) + Constantes.CUS;
	}

	private String criarSaudacao(Contato contato) {
		StringBuilder strBuilder = new StringBuilder();
//		strBuilder.append("*").append(obterNomeFormatado(contato.getNome())).append(", ");
		strBuilder.append("*Olá").append(", ");
		if (compararHora(Constantes.MEIO_DIA)){
			strBuilder.append(Constantes.BOM_DIA);
		} else if (compararHora(Constantes.DEZOITO_HORAS)) {
			strBuilder.append(Constantes.BOA_TARDE);
		} else {
			strBuilder.append(Constantes.BOA_NOITE);
		}
		return strBuilder.append("!*").toString();
	}

	private String criarSaudacaoPesquisa() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("*Olá ");
		if (compararHora(Constantes.MEIO_DIA)){
			strBuilder.append(Constantes.BOM_DIA);
		} else if (compararHora(Constantes.DEZOITO_HORAS)) {
			strBuilder.append(Constantes.BOA_TARDE);
		} else {
			strBuilder.append(Constantes.BOA_NOITE);
		}
		strBuilder.append("!*");
		return strBuilder
				.append(Constantes.QUEBRA_LINHA)
				.append("*Nós somos o _IPBT - Instituto de Pesquisa Brasil Transparente_.*").toString();
	}

	private String obterNomeFormatado(String nome) {
		String[] nomes = nome.split("\\s+");
		int length = nomes.length -1;
		return iniciaisMaiusculas(nomes[0].length() > 3 ?  limparNome(nomes[0]) :
				limparNome(nomes[1]));// + " " + iniciaisMaiusculas(limparNome(nomes[length]));
	}

	private String iniciaisMaiusculas(String nome) {
		return Character.toUpperCase(nome.charAt(0))+nome.substring(1);
	}

	private String limparNome(String nome) {
		return nome.replaceAll("[^a-zA-Z]", "");
	}

//	private MessageWhatsapp prepararMessage(PayloadDTO payload) throws IOException {
//		MessageWhatsapp whatsapp = new MessageWhatsapp();
//		if (payload.getContentType().equals(ContentType.MESSAGE_MEDIA)) {
//			String base64Data = Base64.getEncoder().encodeToString(payload.getFile().getBytes());
//			ContentMedia contentMedia = new ContentMedia();
//			contentMedia.setMimetype(payload.getFile().getContentType());
//			contentMedia.setData(base64Data);
//			contentMedia.setFilename(payload.getFile().getOriginalFilename());
//			whatsapp.setContentType(payload.getContentType().getType());
//			whatsapp.setContent(contentMedia);
//		}
//		if (payload.getContentType().equals(ContentType.STRING)) {
//			whatsapp.setContentType(payload.getContentType().getType());
//			whatsapp.setContent(payload.getMensagem());
//		}
//		if (payload.getContentType().equals(ContentType.MESSAGE_MEDIA_FROM_URL)) {
//			whatsapp.setContentType(payload.getContentType().getType());
//			whatsapp.setContent(payload.getLink());
//		}
//
//		if (payload.getContentType().equals(ContentType.POLL)) {
//			whatsapp.setContentType(payload.getContentType().getType());
//			//TODO: CRIAR CÓDIGO AQUI!!!
//		}
//		return whatsapp;
//	}

	private String convertObjectToJson(MessageWhatsapp whatsapp) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.writeValueAsString(whatsapp);
		} catch (JsonProcessingException e) {
			e.printStackTrace();
			return null;
		}
	}

	private boolean existsFile(MultipartFile file) {
		return file.isEmpty();
	}


	private boolean compararHora(int hora) {
		ZoneId zoneIdBrasilia = ZoneId.of("America/Sao_Paulo");
		ZonedDateTime zonedDateTimeBrasilia = ZonedDateTime.now(zoneIdBrasilia);
		LocalDateTime turno = zonedDateTimeBrasilia.toLocalDateTime();
		return turno.getHour() < hora;
	}

	public boolean enviarSenhaProvisoria(User user) {
		String conteudo = "Sua senha provisória " + user.getPassword();
		MessageWhatsapp message = new MessageWhatsapp();
		message.setContentType("string");
		message.setContent("*ePost*/n" + conteudo);
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = buildNewHttpRequestPost(useSessionId(""), convertObjectToJson(message));
			HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
			return response.statusCode() == 200;
		} catch (InterruptedException | IOException | URISyntaxException e) {
			log.error("Erro ao tentar enviar mensagem de whatsapp: ", e.getMessage());
		}
		return false;
    }
    public void iniciarPesquisa(Long pesquisaId) throws IOException {
		var questionario = questionarioRepositorio.findById(pesquisaId);
		criarMessagemDeIntroducaoEEnviar(questionario.get());
    }

	private Object convertJsonToMessageSendDTO(HttpResponse<String> response) {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
			return objectMapper.readValue(response.body(), MessageSendDTO.class);
		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
	}
	private MessageSendAndReceived convertToEntity(MessageSendDTO messageSend) {
		ModelMapper modelMapper = new ModelMapper();
		return modelMapper.map(messageSend, MessageSendAndReceived.class);
	}

	private void criarMessagemDeIntroducaoEEnviar(Questionario questionario) throws IOException {
		var contatos = contatoRepository.findAll();
		PayloadDTO payload = new PayloadDTO();
		payload.setTitulo("*Auditoria dos resultados das últimas pesquisas em Pernambuco*");
		payload.setContentType(ContentType.STRING);
		for (var contato: contatos) {
			String mensagem = criarSaudacao(contato)+ Constantes.QUEBRA_LINHA + Constantes.QUEBRA_LINHA;
			mensagem += payload.getTitulo() + Constantes.QUEBRA_LINHA + Constantes.QUEBRA_LINHA + mensagem;
			var messageWhatsapp = new MessageWhatsapp();
			messageWhatsapp.setContentType(ContentType.STRING.getType());
			messageWhatsapp.setContent(mensagem);
			messageWhatsapp.setChatId(configurarChatId(contato.getCellPhone()));
			sendPesquisa(convertObjectToJson(messageWhatsapp));
		}
	}

	private HttpResponse<String> sendPesquisa(String json) throws IOException {
		HttpResponse<String> response = null;
		try {
			HttpClient client = HttpClient.newHttpClient();
			HttpRequest request = buildNewHttpRequestPost(useSessionId(""), json);
			response = client.send(request, HttpResponse.BodyHandlers.ofString());

		} catch (URISyntaxException | InterruptedException  e) {
			log.error("Erro ao tentar enviar mensagem de whatsapp: ", e.getMessage());
		}
		return response;
	}

	private String criarDePermissao() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("_Se este conteúdo não faz sentido para você ou se você não quer receber mensagem com este conteúdo")
				.append("envie uma mensagem com a palavra_ SAIR*");
		return strBuilder.append("!*").toString();
	}

	private ContatoDTO remover(String telefone) {
		ContatoDTO contato = new ContatoDTO();
		contato.setCellPhone(telefone.substring(0,4)+telefone.substring(5));
		return contato;
	}

	private HttpRequest buildNewHttpRequestPost(String sessionId, String json) throws URISyntaxException {
		String uri = this.hostApiWhatsapp + EndPoints.CLIENT_SEND_MESSAGE.replace("{sessionId}", sessionId);
		return HttpRequest.newBuilder()
				.uri(new URI(uri))
				.header(EndPoints.ACCEPT, "*/*")
				.header(EndPoints.CONTENT_TYPE, EndPoints.APPLICATION_JSON)
				.header(EndPoints.X_API_KEY, apiKey)
				.POST(HttpRequest.BodyPublishers.ofString(json))
				.build();
	}
	private HttpRequest buildNewHttpRequestGet(String url, String session) {
		String uri = url + session;
		return HttpRequest.newBuilder()
				.uri(URI.create(uri))
				.timeout(Duration.ofSeconds(20))
				.header(EndPoints.ACCEPT, EndPoints.APPLICATION_JSON)
				.header(EndPoints.X_API_KEY, apiKey)
				.GET()
				.build();
	}

	public void sendPesquisa(MultipartFile file, String titulo, String link, String conclusao, String sessionId, Long pesquisaId) {

		List<Contato> contatos = contatoRepository.findAll();
		if(contatos.isEmpty()) return;

		sessionId = obterSession();

		for (var contato : contatos) {
			if (!titulo.isBlank()) sendMessageText(contato, String.format("%s %s %s %s", criarSaudacaoPesquisa(),
					Constantes.QUEBRA_LINHA, titulo, Constantes.QUEBRA_LINHA), sessionId);
			if (file != null) sendArquivo(contato, file, sessionId);
			if (!link.isBlank()) sendMessageLink(contato, link, sessionId, pesquisaId);
			if (!conclusao.isBlank()) sendMessageText(contato, conclusao, sessionId);
		}
	}

	private String obterSession() {
		var user = obterUsuarioLogado().orElse(null);
		if(user == null) return null;

		List<Conta> contas = contaRepository.findByUserId(user.getId());
		return contas.get(0).getDescricao();
	}

	private Optional<User> obterUsuarioLogado() {
		var authentication = SecurityContextHolder.getContext().getAuthentication();
		var userDetails = (UserDetailsImpl) authentication.getPrincipal();
		return userRepositorio.findById(userDetails.getId());
	}

	private void sendMessageLink(Contato contato, String link, String sessionId) {
		var messageWhatsapp = new MessageWhatsapp();
		messageWhatsapp.setChatId(configurarChatId(contato.getCellPhone()));
		messageWhatsapp.setContentType(ContentType.STRING.getType());
		messageWhatsapp.setContent(link);
		String json = convertObjectToJson(messageWhatsapp);
		sendMessageNow(useSessionId(sessionId), json);
	}

	private void sendMessageLink(Contato contato, String link, String sessionId, Long pesquisaId) {
		var messageWhatsapp = new MessageWhatsapp();
		messageWhatsapp.setChatId(configurarChatId(contato.getCellPhone()));
		messageWhatsapp.setContentType(ContentType.STRING.getType());
		String chatId = EncoderAndDecoderBase64.encoderBase64(contato.getCellPhone());
		String urlCompleto = link+"/"+chatId+"/"+pesquisaId;
		messageWhatsapp.setContent(urlCompleto);
		String json = convertObjectToJson(messageWhatsapp);
		sendMessageNow(useSessionId(sessionId), json);
	}

	private void sendArquivo(Contato contato, MultipartFile file, String sessionId) {
		var messageWhatsapp = new MessageWhatsapp();
		messageWhatsapp.setChatId(configurarChatId(contato.getCellPhone()));
		messageWhatsapp.setContentType(ContentType.MESSAGE_MEDIA.getType());
		String base64Data = "";
		var contentMedia = new ContentMedia();
		try {
			base64Data = Base64.getEncoder().encodeToString(file.getBytes());
			contentMedia.setData(base64Data);
			contentMedia.setFilename(file.getOriginalFilename());
			contentMedia.setMimetype(file.getContentType());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		if(base64Data.isBlank()) return;

		messageWhatsapp.setContent(contentMedia);
		String json = convertObjectToJson(messageWhatsapp);

		sendMessageNow(useSessionId(sessionId), json);
	}

	private void sendMessageText(Contato contato, String text, String sessionId) {

		MessageWhatsapp messageWhatsapp = new MessageWhatsapp();
		messageWhatsapp.setChatId(configurarChatId(contato.getCellPhone()));
		messageWhatsapp.setContentType(ContentType.STRING.getType());
		messageWhatsapp.setContent(text);
		String json = "";
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			json = objectMapper.writeValueAsString(messageWhatsapp);

		} catch (JsonProcessingException e) {
			throw new RuntimeException(e);
		}
		sendMessageNow(useSessionId(sessionId), json);
	}

	private void sendMessageNow(String sessionId, String json) {
		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = null;
		try {
			request = buildNewHttpRequestPost(sessionId, json);
			client.send(request, HttpResponse.BodyHandlers.ofString());
		} catch (URISyntaxException | IOException | InterruptedException e) {
			log.error("Erro ao tentar enviar mensagem de whatsapp: ", e.getMessage());
		}

		try {
			Thread.sleep(200);
		}catch (InterruptedException Ie) {
			Ie.printStackTrace();
		}
	}

}
