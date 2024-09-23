package com.dssiddev.disparowhatsapp.controllers;


import com.dssiddev.disparowhatsapp.config.security.UserDetailsImpl;

import com.dssiddev.disparowhatsapp.models.User;
import com.dssiddev.disparowhatsapp.excepttions.NotFoundException;
import com.dssiddev.disparowhatsapp.handles.MultipartFileHandler;
import com.dssiddev.disparowhatsapp.models.Questionario;
import com.dssiddev.disparowhatsapp.models.dto.*;
import com.dssiddev.disparowhatsapp.models.Conta;
import com.dssiddev.disparowhatsapp.repositories.UserRepositorio;
import com.dssiddev.disparowhatsapp.repositories.ContaRepository;
import com.dssiddev.disparowhatsapp.services.QuestionarioService;
import com.dssiddev.disparowhatsapp.services.WhatsAppService;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import java.time.LocalDateTime;

import javax.annotation.security.PermitAll;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pesquisas")
public class PesquisaController {


    @Autowired
    private ContaRepository contaRepository;

    @Autowired
    MultipartFileHandler multipartFileHandler;

    @Autowired
    private WhatsAppService whatsAppService;
    @Autowired
    private QuestionarioService service;
    @Autowired
    private UserRepositorio repositorio;

    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping("/questionario/upload/file/{sessionId}")
    public ResponseEntity<Object> processarArquivoDoQuestionarios(@PathVariable("sessionId") Long sessionId, @RequestParam("file") MultipartFile file) {
        var conta = contaRepository.findById(sessionId);
        if (!conta.isPresent()) return ResponseEntity.notFound().build();

        var processado = service.processarArquivoDoQuestionarios(conta.get(), file);

        return processado ? ResponseEntity.status(HttpStatus.CREATED).build() : ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }


    @ResponseStatus(HttpStatus.OK)
    @PreAuthorize("hasAnyRole('ADMIN')")
    @PostMapping(path = "/enviar/{pesquisaId}", produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<MensagemEnviadaDTO> sendPesquisa(@RequestParam("introducao") String titulo,
                                                           @RequestParam("video") MultipartFile arquivo,
                                                           @RequestParam("link") String link,
                                                           @RequestParam("conclusao") String conclusao,
                                                           @RequestParam("session") String sessionId,
                                                           @PathVariable("pesquisaId") Long pesquisaId) throws Exception	{
        MultipartFile file = multipartFileHandler.payloadHandle(arquivo);
        whatsAppService.sendPesquisa(file, titulo, link, conclusao, sessionId, pesquisaId);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @PostMapping("/pesquisa")
    @PreAuthorize("permitAll()")
    @ResponseStatus(HttpStatus.OK)
    public void salvarOpcaoSelecionada(@RequestBody FormularioRespondidoDTO respondido){
        service.salvarOpcaoSelecionada(respondido);
    }

    @PreAuthorize("permitAll()")
    @PutMapping("/{chatId}/{pesquisaId}")
    public ResponseEntity<ChatIdStatus> concluirpesquisado(@PathVariable("chatId") String chatId, @PathVariable("pesquisaId") Long pesquisaId){
        var chatIdStatus = service.concluirpesquisado(chatId, pesquisaId);
        return ResponseEntity.status(HttpStatus.OK).body(chatIdStatus);
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{chatId}/{pesquisaId}")
    public ResponseEntity<ChatIdStatus> verficarSeRespondeuPesquisa(@PathVariable("chatId") String chatId, @PathVariable("pesquisaId") Long pesquisaId){
        var chatIdStatus = service.verficarSeRespondeuPesquisa(chatId, pesquisaId);
        return ResponseEntity.status(HttpStatus.OK).body(chatIdStatus);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN')")
    public ResponseEntity<List<NomeIdContaResponse>> buscarPesquisaDaConta() {
        var user = obterUsuarioLogado();
        if(user.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        List<Conta> contas = contaRepository.findByUserId(user.get().getId());
        return ResponseEntity.status(HttpStatus.OK).body(service.buscarPesquisasDasContas(contas));
    }

    @PreAuthorize("permitAll()")
    @GetMapping("/{pesquisaId}")
    public ResponseEntity<FormularioDTO> findPesquisa(@PathVariable("pesquisaId") Long pesquisaId) {
        FormularioDTO formulario = null;
        try {
            formulario = service.findPesquisa(pesquisaId);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(formulario);
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping(value = "/conta/{contaId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ClienteDataDeCriacaoDTO>> listarTodos(
            @PathVariable("contaId") Long contaId) {
        List<Questionario> questionarios = service.listarTodos(contaId);
       if (!questionarios.isEmpty()) {
          return ResponseEntity.status(HttpStatus.OK).body(questionarios
                  .stream()
                  .map(questionario -> convertToClienteDataCriacaoDTO(questionario))
                  .collect(Collectors.toList()));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasAnyRole('USER')")
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ClienteDataDeCriacaoDTO>> listarDoUsuarioTodos(
            @PathVariable("userId") Long userId) {
        List<Questionario> questionarios = new ArrayList<>();
        var contas = contaRepository.findByUserId(userId);

        if (!contas.isEmpty()) {
            questionarios = service.listarTodosDosIds(contas);
        }
        if (!questionarios.isEmpty()) {
            return ResponseEntity.status(HttpStatus.OK)
                    .body(questionarios
                            .stream()
                            .map(questionario -> convertToClienteDataCriacaoDTO(questionario))
                            .collect(Collectors.toList()));
        }
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @GetMapping("/resultado/{formularioId}/parcial")
    public ResponseEntity<Object> getResultadoParcial(@PathVariable("formularioId") Long formularioId) {

        Object object = service.resultado(formularioId);

        return null;
    }

    @PreAuthorize("hasAnyRole('USER')")
    @PostMapping("iniciar/{pesquisaId}")
    public ResponseEntity<Object> iniciarPesquisa(@PathVariable("pesquisaId") Long pesquisaId){
        var questionario = service.findById(pesquisaId);
        if(!questionario.isPresent()) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"status\": \"Não foi encontrado nenhuma pequisa com esse parâmetro!\"}");
        try {
            whatsAppService.iniciarPesquisa(pesquisaId);
        }catch (IOException e) {
            e.printStackTrace();
        }
        return ResponseEntity.status(HttpStatus.OK).body("{\"status\": \"Pesquisa em andadamento\"}");
    }

    private Optional<User> obterUsuarioLogado() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();
        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
        return repositorio.findById(userDetails.getId()); 
    }

    public ClienteDataDeCriacaoDTO convertToClienteDataCriacaoDTO(Questionario questionario) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(questionario, ClienteDataDeCriacaoDTO.class);
    }
}
/*

 private String obterObjeto() {
        return  "[\n" +
                "      {\n" +
                "        \"id\": 1,\n" +
                "        \"text\": \"Qual o seu sexo?\",\n" +
                "        \"options\": [\n" +
                "          { \"id\": 1, \"text\": \"Masculino\", \"image\": \"\" },\n" +
                "          { \"id\": 2, \"text\": \"Feminino\", \"image\": \"\" },\n" +
                "          { \"id\": 3, \"text\": \"Não declarar\", \"image\": \"\" }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 2,\n" +
                "        \"text\": \"Qual a sua faixa etária?\",\n" +
                "        \"options\": [\n" +
                "          { \"id\": 1, \"text\": \"Entre 16 e 25 anos\", \"image\": \"\" },\n" +
                "          { \"id\": 2, \"text\": \"Entre 26 e 35 anos\", \"image\": \"\" },\n" +
                "          { \"id\": 3, \"text\": \"Entre 36 e 45 anos\", \"image\": \"\" },\n" +
                "          { \"id\": 4, \"text\": \"Entre 46 e 55 anos\", \"image\": \"\" },\n" +
                "          { \"id\": 5, \"text\": \"Entre 56 e 65 anos\", \"image\": \"\" },\n" +
                "          { \"id\": 6, \"text\": \"Mais de 66 anos\", \"image\": \"\" } " +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 3,\n" +
                "        \"text\": \"Qual o seu nível escolar?\",\n" +
                "        \"options\": [\n" +
                "          { \"id\": 1, \"text\": \"Ensino fundamental incompleto\", \"image\": \"\" },\n" +
                "          { \"id\": 2, \"text\": \"Ensino fundamental completo\", \"image\": \"\" },\n" +
                "          { \"id\": 3, \"text\": \"Ensino médio incompleto\", \"image\": \"\" },\n" +
                "          { \"id\": 4, \"text\": \"Ensino médio completo\", \"image\": \"\" },\n" +
                "          { \"id\": 5, \"text\": \"Ensino superior incompleto\", \"image\": \"\" },\n" +
                "          { \"id\": 6, \"text\": \"Ensino superior completo\", \"image\": \"\" }" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 4,\n" +
                "        \"text\": \"Qual o bairro/sítio que você mora?\",\n" +
                "        \"options\": [\n" +
                "          { \"id\": 1, \"text\": \"Centro\", \"image\": \"\" },\n" +
                "          { \"id\": 2, \"text\": \"Da Glória\", \"image\": \"\" },\n" +
                "          { \"id\": 3, \"text\": \"Da Saudade\", \"image\": \"\" },\n" +
                "          { \"id\": 4, \"text\": \"São Gonçalo\", \"image\": \"\" },\n" +
                "          { \"id\": 5, \"text\": \"Matadouro\", \"image\": \"\" },\n" +
                "          { \"id\": 6, \"text\": \"Vila Boa Esperança\", \"image\": \"\" },\n" +
                "          { \"id\": 7, \"text\": \"Nova Itaenga\", \"image\": \"\" },\n" +
                "          { \"id\": 8, \"text\": \"Do Campo\", \"image\": \"\" },\n" +
                "          { \"id\": 9, \"text\": \"Da Salina\", \"image\": \"\" },\n" +
                "          { \"id\": 10, \"text\": \"Mãe Rainha\", \"image\": \"\" },\n" +
                "          { \"id\": 11, \"text\": \"Do Moinho\", \"image\": \"\" },\n" +
                "          { \"id\": 12, \"text\": \"Arrombados\", \"image\": \"\" },\n" +
                "          { \"id\": 13, \"text\": \"Camboa\", \"image\": \"\" },\n" +
                "          { \"id\": 14, \"text\": \"Marreco\", \"image\": \"\" },\n" +
                "          { \"id\": 15, \"text\": \"Imbé\", \"image\": \"\" },\n" +
                "          { \"id\": 16, \"text\": \"Cai-cai\", \"image\": \"\" },\n" +
                "          { \"id\": 17, \"text\": \"Açude de Pedras\", \"image\": \"\" },\n" +
                "          { \"id\": 18, \"text\": \"Lameiro\", \"image\": \"\" },\n" +
                "          { \"id\": 19, \"text\": \"Eixo Grande\", \"image\": \"\" },\n" +
                "          { \"id\": 20, \"text\": \"Quatis\", \"image\": \"\" }" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 5,\n" +
                "        \"text\": \"Em quem você votaria para Prefieto de Lagoa de Itaenga?\",\n" +
                "        \"options\": [\n" +
                "          { \"id\": 1, \"text\": \"Toinho da Câmara\", \"image\": \"https://raw.githubusercontent.com/ds-solutions/candidatosImage/main/toinho15.jpg\" },\n" +
                "          { \"id\": 2, \"text\": \"Carlinhos do Moinho\", \"image\": \"https://raw.githubusercontent.com/ds-solutions/candidatosImage/main/carlinhos40.jpg\" },\n" +
                "          { \"id\": 3, \"text\": \"Dimas Natanael\", \"image\": \"https://raw.githubusercontent.com/ds-solutions/candidatosImage/main/dimas40.jpg\" }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 6,\n" +
                "        \"text\": \"Qual dos candidatos você julga o mais preparado?\",\n" +
                "        \"options\": [\n" +
                "          { \"id\": 1, \"text\": \"Toinho da Câmara\", \"image\": \"https://raw.githubusercontent.com/ds-solutions/candidatosImage/main/toinho15.jpg\" },\n" +
                "          { \"id\": 2, \"text\": \"Carlinhos do Moinho\", \"image\": \"https://raw.githubusercontent.com/ds-solutions/candidatosImage/main/carlinhos40.jpg\" },\n" +
                "          { \"id\": 3, \"text\": \"Dimas Natanael\", \"image\": \"https://raw.githubusercontent.com/ds-solutions/candidatosImage/main/dimas40.jpg\" }\n" +
                "        ]\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 7,\n" +
                "        \"text\": \"Qual dos candidatos você NÃO votaria se as eleições fossem hoje?\",\n" +
                "        \"options\": [\n" +
                "          { \"id\": 1, \"text\": \"Toinho da Câmara\", \"image\": \"https://raw.githubusercontent.com/ds-solutions/candidatosImage/main/toinho15.jpg\" },\n" +
                "          { \"id\": 2, \"text\": \"Carlinhos do Moinho\", \"image\": \"https://raw.githubusercontent.com/ds-solutions/candidatosImage/main/carlinhos40.jpg\" },\n" +
                "          { \"id\": 3, \"text\": \"Dimas Natanael\", \"image\": \"https://raw.githubusercontent.com/ds-solutions/candidatosImage/main/dimas40.jpg\" }\n" +
                "        ]\n" +
                "      }\n" +
                "    ]\n" +
                "";

    }

 */