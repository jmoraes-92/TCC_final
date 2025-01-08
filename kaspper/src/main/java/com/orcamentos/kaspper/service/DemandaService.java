package com.orcamentos.kaspper.service;

import com.orcamentos.kaspper.dto.DemandaRequestDTO;
import com.orcamentos.kaspper.exception.ResourceNotFoundException;
import com.orcamentos.kaspper.model.Cliente;
import com.orcamentos.kaspper.model.Demanda;
import com.orcamentos.kaspper.model.Tarefa;
import com.orcamentos.kaspper.model.enums.Prioridade;
import com.orcamentos.kaspper.model.enums.StatusDemanda;
import com.orcamentos.kaspper.model.enums.StatusTarefa;
import com.orcamentos.kaspper.repository.ClienteRepository;
import com.orcamentos.kaspper.repository.DemandaRepository;
import com.orcamentos.kaspper.repository.TarefaRepository;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DemandaService {

    @Autowired
    private DemandaRepository demandaRepository;

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional
    public Demanda criarDemandaComTarefas(Demanda demanda, List<Tarefa> tarefasBase) {
        if (demanda.getCliente() == null || demanda.getCliente().getId() == null) {
            throw new IllegalArgumentException("Cliente is required to save a Demanda.");
        }

        Cliente cliente = clienteRepository.findById(demanda.getCliente().getId()).orElseThrow(
                () -> new ResourceNotFoundException("Cliente not found with id: " + demanda.getCliente().getId()));

        demanda.setCliente(cliente);
        Demanda savedDemanda = demandaRepository.save(demanda);

        for (Tarefa tarefaBase : tarefasBase) {
            Tarefa tarefa = new Tarefa();
            tarefa.setDemanda(savedDemanda);
            tarefa.setDescricao(tarefaBase.getDescricao());
            tarefa.setResponsavel(tarefaBase.getResponsavel());
            tarefa.setStatus(StatusTarefa.PENDENTE);
            tarefaRepository.save(tarefa);
        }

        return savedDemanda;
    }

    @Transactional
    public Demanda salvarDemanda(Demanda demanda) {
        if (demanda.getCliente() == null || demanda.getCliente().getId() == null) {
            throw new IllegalArgumentException("Cliente é obrigatório para salvar uma demanda.");
        }

        Cliente cliente = clienteRepository.findById(demanda.getCliente().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + demanda.getCliente().getId()));

        demanda.setCliente(cliente);
        return demandaRepository.save(demanda);
    }

    public Demanda salvar(DemandaRequestDTO demandaRequestDTO) {
        Demanda demanda = new Demanda();
        demanda.setDescricao(demandaRequestDTO.getDescricao());
        demanda.setPrioridade(Prioridade.valueOf(demandaRequestDTO.getPrioridade().toUpperCase()));
        demanda.setStatus(StatusDemanda.valueOf(demandaRequestDTO.getStatus().toUpperCase()));

        List<Tarefa> tarefasBase = demandaRequestDTO.getTarefas();
        return criarDemandaComTarefas(demanda, tarefasBase);
    }

    public Demanda adicionarTarefas(Long demandaId, List<Long> tarefaIds) {
        Demanda demanda = demandaRepository.findById(demandaId)
                .orElseThrow(() -> new ResourceNotFoundException("Demanda não encontrada"));
        List<Tarefa> tarefas = tarefaRepository.findAllById(tarefaIds);
        demanda.getTarefas().addAll(tarefas);
        return demandaRepository.save(demanda);
    }

    public List<Demanda> listarTodasDemandas() {
        return demandaRepository.findAll();
    }

    public Optional<Demanda> buscarPorId(Long id) {
        return demandaRepository.findById(id);
    }

    public Demanda atualizarDemanda(Long id, DemandaRequestDTO demandaRequestDTO) {
        Optional<Demanda> demandaOptional = buscarPorId(id);

        if (!demandaOptional.isPresent()) {
            throw new ResourceNotFoundException("Demanda com ID " + id + " não encontrada.");
        }

        Demanda demandaExistente = demandaOptional.get();
        demandaExistente.setDescricao(demandaRequestDTO.getDescricao());
        demandaExistente.setPrioridade(Prioridade.valueOf(demandaRequestDTO.getPrioridade().toUpperCase()));
        demandaExistente.setStatus(StatusDemanda.valueOf(demandaRequestDTO.getStatus().toUpperCase()));

        return demandaRepository.save(demandaExistente);
    }

    public void deletarDemanda(Long id) {
        Optional<Demanda> demandaOptional = buscarPorId(id);
        if (!demandaOptional.isPresent()) {
            throw new ResourceNotFoundException("Demanda com ID " + id + " não encontrada.");
        }

        Demanda demanda = demandaOptional.get();
        demandaRepository.delete(demanda);
    }
}
