package com.orcamentos.kaspper.service;

import com.orcamentos.kaspper.exception.ResourceNotFoundException;
import com.orcamentos.kaspper.model.Demanda;
import com.orcamentos.kaspper.model.Orcamento;
import com.orcamentos.kaspper.model.Tarefa;
import com.orcamentos.kaspper.model.enums.Prioridade;
import com.orcamentos.kaspper.repository.DemandaRepository;
import com.orcamentos.kaspper.repository.OrcamentoRepository;
import com.orcamentos.kaspper.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrcamentoService {

	@Autowired
	private OrcamentoRepository orcamentoRepository;

	@Autowired
	private TarefaRepository tarefaRepository;

	@Autowired
	private DemandaRepository demandaRepository;

	// Gerar orçamento baseado em uma única demanda
	public Orcamento gerarOrcamento(Demanda demanda) {
		List<Tarefa> tarefas = tarefaRepository.findByDemanda(demanda);

		BigDecimal custoEstimado = calcularCustoEstimado(demanda.getPrioridade(), tarefas.size());
		int prazoEstimado = calcularPrazoEstimado(tarefas.size());

		Orcamento orcamento = new Orcamento();
		orcamento.setDemanda(demanda);
		orcamento.setValor(custoEstimado);
		orcamento.setPrazoEstimado(prazoEstimado);
		orcamento.setDataGeracao(LocalDateTime.now());
		orcamento.setObservacoes("Orçamento gerado automaticamente.");
		orcamento.setStatus("PENDENTE");

		return orcamentoRepository.save(orcamento);
	}

	private BigDecimal calcularCustoEstimado(Prioridade prioridade, int quantidadeTarefas) {
		// Implementação do cálculo de custo estimado
		return BigDecimal.ZERO;
	}

	private int calcularPrazoEstimado(int quantidadeTarefas) {
		// Implementação do cálculo de prazo estimado
		return 0;
	}

	// salvar
	public Orcamento salvar(Orcamento orcamento) {
		return orcamentoRepository.save(orcamento);
	}

	// buscar
	public Orcamento buscarPorId(Long id) {
		return orcamentoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Orçamento não encontrado"));
	}

	// listar
	public List<Orcamento> listarTodos() {
		return orcamentoRepository.findAll();
	}

	// excluir
	public void excluir(Long id) {
		Orcamento orcamento = orcamentoRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Orçamento com ID " + id + " não encontrado."));

		boolean temDemandasAssociadas = demandaRepository.existsByOrcamentoId(id);
		if (temDemandasAssociadas) {
			throw new DataIntegrityViolationException(
					"Não é possível excluir o orçamento porque há demandas associadas.");
		}

		orcamentoRepository.delete(orcamento);
	}

	// atualizar
	public Orcamento atualizar(Long id, Orcamento orcamentoAtualizado) {
		// O método buscarPorId já lança a exceção, então não é necessário usar
		// orElseThrow
		Orcamento orcamentoExistente = buscarPorId(id);

		orcamentoExistente.setValor(orcamentoAtualizado.getValor());
		orcamentoExistente.setPrazoEstimado(orcamentoAtualizado.getPrazoEstimado());
		orcamentoExistente.setObservacoes(orcamentoAtualizado.getObservacoes());
		orcamentoExistente.setStatus(orcamentoAtualizado.getStatus());

		return orcamentoRepository.save(orcamentoExistente);
	}

	public List<Orcamento> listarComFiltros(String status, Double valorMinimo, Double valorMaximo) {
		if (status != null && valorMinimo != null && valorMaximo != null) {
			return orcamentoRepository.findByStatusAndValorBetween(status, valorMinimo, valorMaximo);
		} else if (status != null) {
			return orcamentoRepository.findByStatus(status);
		} else if (valorMinimo != null && valorMaximo != null) {
			return orcamentoRepository.findByValorBetween(valorMinimo, valorMaximo);
		} else {
			return orcamentoRepository.findAll();
		}
	}

	// Gerar orçamento com múltiplas demandas
	public List<Orcamento> gerarOrcamentoComMultiplasDemandas(List<Long> demandaIds) {
		List<Demanda> demandas = demandaRepository.findAllById(demandaIds);
		List<Orcamento> orcamentos = new ArrayList<>();

		for (Demanda demanda : demandas) {
			List<Tarefa> tarefas = tarefaRepository.findByDemanda(demanda);

			BigDecimal custoEstimado = calcularCustoEstimado(demanda.getPrioridade(), tarefas.size());
			int prazoEstimado = calcularPrazoEstimado(tarefas.size());

			Orcamento orcamento = new Orcamento();
			orcamento.setDemanda(demanda);
			orcamento.setValor(custoEstimado);
			orcamento.setPrazoEstimado(prazoEstimado);
			orcamento.setDataGeracao(LocalDateTime.now());
			orcamento.setObservacoes("Orçamento gerado automaticamente.");
			orcamento.setStatus("PENDENTE");

			orcamentos.add(orcamentoRepository.save(orcamento));
		}

		return orcamentos;
	}

}
