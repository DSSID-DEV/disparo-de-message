package com.dssiddev.disparowhatsapp.services;

import com.dssiddev.disparowhatsapp.excepttions.NotFoundException;
import com.dssiddev.disparowhatsapp.models.*;
import com.dssiddev.disparowhatsapp.models.dto.*;
import com.dssiddev.disparowhatsapp.repositories.*;
import com.dssiddev.disparowhatsapp.services.constantes.Constantes;
import com.dssiddev.disparowhatsapp.models.dto.NomeIdContaResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class QuestionarioService {

    @Autowired
    private QuestionarioRepositorio repositorio;

    @Autowired
    private QuestaoRepositorio questaoRepositorio;

    @Autowired
    private FormularioRespondidoRespositorio formularioRespondidoRespositorio;

    @Autowired
    private QuestaoRespondidaRepositorio questaoRespondidaRepositorio;

    @Autowired
    private OpcaoRepositorio opcaoRepositorio;

    @Transactional
    public void salvarQuestinario(Questionario questionario) {
        repositorio.save(questionario);
    }

    @Transactional
    private void salvarQuestao(Questao questao) {
        questaoRepositorio.save(questao);
    }

    @Transactional
    private void salvarOpcao(Opcao opcao) {
       opcaoRepositorio.save(opcao);
    }

    public boolean processarArquivoDoQuestionarios(Conta conta, MultipartFile file) {
        Questionario questionario = new Questionario();
        questionario.setConta(conta);
        questionario.setCriadoEm(LocalDateTime.now());
        List<Questao> questoes = new ArrayList<>();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))){
            String linha;
            String messagem = "";
            while((linha = br.readLine()) != null) {
                if(linha.contains("Cliente:")) {
                    questionario.setCliente(linha.split(":")[1].trim());
                }
                if(linha.contains("Município")) {
                    questionario.setMunicipio(linha.split(":")[1].trim());
                } else if (containsIterrogacaoOrPipe(linha)){
                        salvarQuestinario(questionario);
                        String pergunta = linha.split("\\?")[0] + "?";
                        Questao questao = new Questao();
                        questao.setQuestionario(questionario);
                        questao.setDescricao(pergunta);

                        obterOpcoes(questao, linha.split("\\?")[1]);
                    }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public List<NomeIdContaResponse> buscarPesquisasDasContas(List<Conta> contas) {
        List<Questionario> questionarios = repositorio.listarTodosDosIds(extrairIds(contas));
        System.out.println(questionarios.size());
        return questionarios.stream().map(questionario -> convertToNomeIdContaResponse(questionario))
        .collect(Collectors.toList());
    }

    private List<Long> extrairIds(List<Conta> contas) {
        return contas.stream()
                .map(Conta::getId).collect(Collectors.toList());
    }

    private NomeIdContaResponse convertToNomeIdContaResponse(Questionario questionario) {
        return NomeIdContaResponse.builder()
            .id(questionario.getId())
            .criadoEm(questionario.getCriadoEm())
            .municipio(questionario.getMunicipio())
            .cliente(questionario.getCliente())
            .conta(questionario.getConta().getDescricao())
            .build();
    }


    private void obterOpcoes(Questao questao, String linha) {
        salvarQuestao(questao);
        for(String op: linha.split("\\|")) {
            Opcao opcao = new Opcao();
            opcao.setDescricao(op.split(";")[0]);
            opcao.setImage(op.split(";")[1]);
            opcao.setQuestao(questao);
            salvarOpcao(opcao);
        }
    }

    private boolean containsIterrogacaoOrPipe(String linha) {
        return linha.contains("?") || linha.contains("|");
    }

    public List<Questionario> listarTodos(Long contaId) {
       return repositorio.findByContaId(contaId);
    }

    public List<Questionario> listarTodosDosIds(List<Conta> contas) {
        List<Long> ids = contas.stream().map(conta -> conta.getId()).collect(Collectors.toList());
        return repositorio.findByInContaId(ids);
    }

    public Optional<Questionario> findById(Long pesquisaId) {
        return repositorio.findById(pesquisaId);
    }

    @Transactional
    public void salvarOpcaoSelecionada(FormularioRespondidoDTO respondido) {
        var formulario = formularioRespondidoRespositorio
                .buscarFormularioRespondido(respondido.getChatId(),
                        respondido.getFormularioId());

        if (formulario == null) {
            formulario = new FormularioRespondido();
            formulario.setChatId(respondido.getChatId());
            formulario.setFormularioId(respondido.getFormularioId());
        }
        formulario.verificarValorBooleano();
        formulario = formularioRespondidoRespositorio.save(formulario);

        var selecionado = questaoRespondidaRepositorio.buscarQuestao(respondido.getFormularioId(), respondido.getQuestaoId());
        if(selecionado == null) {
            selecionado = new QuestaoRespondida();
            selecionado.setFormularioId(formulario.getFormularioId());
            selecionado.setQuestaoId(respondido.getQuestaoId());
            selecionado.setOpcaoId(respondido.getOpcaoId());
        }
       questaoRespondidaRepositorio.save(selecionado);
    }

    public ChatIdStatus concluirpesquisado(String chatId, Long formularioId) {

        var formulario = formularioRespondidoRespositorio.buscarFormularioRespondido(chatId, formularioId);
        if(formulario == null) return null;

        formulario.setRespondido(true);
        formularioRespondidoRespositorio.save(formulario);

        var chatIdStatus = new ChatIdStatus();
        chatIdStatus.setChatId(chatId);
        chatIdStatus.setFormularioId(formularioId);
        chatIdStatus.setRespondido(Boolean.TRUE);

        return chatIdStatus;
    }

    public ChatIdStatus verficarSeRespondeuPesquisa(String chatId, Long formularioId) {
        var formulario = formularioRespondidoRespositorio.buscarFormularioRespondido(chatId, formularioId);
        var chatIdStatus = new ChatIdStatus();
        chatIdStatus.setChatId(chatId);
        chatIdStatus.setFormularioId(formularioId);

        if(formulario == null) chatIdStatus.setRespondido(Boolean.FALSE);
        else chatIdStatus.setRespondido(formulario.getRespondido());

        return chatIdStatus;
    }

    public FormularioDTO findPesquisa(Long pesquisaId) throws NotFoundException {
        var questionario = repositorio.findById(pesquisaId);
        if(!questionario.isPresent()) throw new NotFoundException("Formulário não encontrado!");

        var formulario = new FormularioDTO();
        formulario.setFomrularioId(questionario.get().getId());
        formulario.setMunicipio(questionario.get().getMunicipio());
        formulario.setQuestoes(obterListaDeQuestoes(formulario.getFomrularioId()));

        return formulario;
    }

    private Set<QuestaoDTO> obterListaDeQuestoes(Long fomrularioId) {
        List<Questao> questoes = questaoRepositorio.findAllByFormularioId(fomrularioId);
        Set<QuestaoDTO> setQuestoes = new HashSet<>();
        for(var questao : questoes) {
            var questaoDTO = new QuestaoDTO();
            questaoDTO.setQuestaoId(questao.getId());
            questaoDTO.setText(questao.getDescricao());
            questaoDTO.setOpcoes(obterListaDeOpcoes(questao.getId()));
            setQuestoes.add(questaoDTO);
        }
        return setQuestoes;
    }

    private Set<OpcaoDTO> obterListaDeOpcoes(Long id) {
        List<Opcao> opcoes = opcaoRepositorio.findAllByQuestaoId(id);
        Set<OpcaoDTO> setOpcoes = new HashSet<>();
        for(var opcao : opcoes) {
             var opcaoDTO = new OpcaoDTO();
             opcaoDTO.setOpcaoId(opcao.getId());
             opcaoDTO.setText(opcao.getDescricao());
             if(!opcao.getImage().isBlank()) {
                opcaoDTO.setImage(Constantes.BASE_IMAGE_GIT + opcao.getImage());
             }
             setOpcoes.add(opcaoDTO);
        }
        return setOpcoes;
    }

    public List<ResultadoDaQuestaoDTO> resultado(Long formularioId) {
        FormularioRespondido formulario = formularioRespondidoRespositorio
                .findFormulariosRespondido(formularioId);
        Map<Long, List<QuestaoRespondida>> questoes = obterQuestoesRespondidas(formulario);
        formulario.setQuestoes(questoes.get(formularioId).stream().collect(Collectors.toSet()));
        return processarResultado(formulario);
    }

    private List<ResultadoDaQuestaoDTO> processarResultado(FormularioRespondido formulario) {
        Map<String, List<Opcao>> mapQuestaoOpcao = new HashMap<>();
        List<Questao> questoes = questaoRepositorio.findAllByFormularioId(formulario.getId());
        for(var questao : questoes) {
            if(!mapQuestaoOpcao.containsKey(questao.getDescricao())) {
                mapQuestaoOpcao.put(questao.getDescricao(), opcaoRepositorio.findAllByQuestaoId(questao.getId()));
            }
        }
        List<ResultadoDaQuestaoDTO> resultados = new ArrayList<>();
        for (var map : mapQuestaoOpcao.entrySet()) {
            var resultado = new ResultadoDaQuestaoDTO();
            resultado.setQuestao(map.getKey());
            for(var opcao : map.getValue()) {
                var resultadoColetado = new ResultadoColetado();
                resultadoColetado.setOpcao(opcao.getDescricao());
                resultadoColetado.setEscolhas(obterQuantidadeEscolhido(opcao.getId()));
                resultadoColetado.calcularPercentagem(obterQuantidadeTotal(opcao.getQuestao().getId()));

                resultado.getResultadosColetados().add(resultadoColetado);
            }
            resultados.add(resultado);
        }
        return resultados;
    }

    private Integer obterQuantidadeTotal(Long id) {
        return questaoRespondidaRepositorio.totalRespondido(id);
    }

    private Integer obterQuantidadeEscolhido(Long id) {
        return  questaoRespondidaRepositorio.somarSelecionado(id);
    }

    private Map<Long, List<QuestaoRespondida>> obterQuestoesRespondidas(FormularioRespondido formulario) {
        Map<Long, List<QuestaoRespondida>> questoes = new HashMap<>();
        List<QuestaoRespondida> questaoRespondidas = questaoRespondidaRepositorio.buscarQuestoesDoFormulario(formulario.getFormularioId());
        for (var questao : questaoRespondidas) {
            if (!questoes.containsKey(formulario.getFormularioId())) {
                List<QuestaoRespondida> respondidas = new ArrayList<>();
                respondidas.add(questao);
                questoes.put(formulario.getFormularioId(), respondidas);
            } else {
                questoes.get(formulario.getFormularioId()).add(questao);
            }
        }
        return questoes;
    }

    private List<Long> extrairIdDosQuestionarios(Long contaId) {
        return listarTodos(contaId).stream().map(Questionario::getId)
                .collect(Collectors.toList());
    }
}
