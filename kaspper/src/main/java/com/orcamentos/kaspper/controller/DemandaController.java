package com.orcamentos.kaspper.controller;

import com.orcamentos.kaspper.dto.DemandaRequestDTO;
import com.orcamentos.kaspper.exception.ResourceNotFoundException;
import com.orcamentos.kaspper.model.Cliente;
import com.orcamentos.kaspper.model.Demanda;
import com.orcamentos.kaspper.model.Tarefa;
import com.orcamentos.kaspper.model.enums.Prioridade;
import com.orcamentos.kaspper.model.enums.StatusDemanda;
import com.orcamentos.kaspper.service.DemandaService;
import com.orcamentos.kaspper.service.TarefaService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/demandas")
@CrossOrigin(origins = "http://localhost:4200")
public class DemandaController {

	@Autowired
	private DemandaService demandaService;

	@Autowired
	private TarefaService tarefaService; 

	private boolean isValidStatus(String status) {
		try {
			StatusDemanda.valueOf(status.toUpperCase());
			return true;
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	@PostMapping("/criar-com-tarefas")
	public ResponseEntity<Demanda> criarDemandaComTarefas(@RequestBody DemandaRequestDTO demandaRequestDTO) {
		Demanda demanda = new Demanda();
		demanda.setDescricao(demandaRequestDTO.getDescricao());

		if (!isValidStatus(demandaRequestDTO.getStatus())) {
			throw new IllegalArgumentException("Status inválido: " + demandaRequestDTO.getStatus());
		}

		demanda.setPrioridade(Prioridade.valueOf(demandaRequestDTO.getPrioridade().toUpperCase()));
		demanda.setStatus(StatusDemanda.valueOf(demandaRequestDTO.getStatus().toUpperCase()));
		demanda.setCliente(demandaRequestDTO.getCliente());

		// Verificar se IDs de tarefas foram fornecidos
		if (demandaRequestDTO.getIdsTarefasExistentes() != null
				&& !demandaRequestDTO.getIdsTarefasExistentes().isEmpty()) {
			// Buscar tarefas existentes pelos IDs
			List<Tarefa> tarefasExistentes = tarefaService
					.buscarTarefasPorIds(demandaRequestDTO.getIdsTarefasExistentes());

			if (tarefasExistentes.isEmpty()) {
				throw new ResourceNotFoundException("Nenhuma tarefa encontrada para os IDs fornecidos.");
			}

			// Associar tarefas existentes à demanda
			demanda.setTarefas(tarefasExistentes);
		} else {
			throw new IllegalArgumentException("É necessário fornecer ao menos um ID de tarefa existente.");
		}

		// Salvar a demanda com as tarefas associadas
		Demanda novaDemanda = demandaService.salvarDemanda(demanda);
		return ResponseEntity.ok(novaDemanda);
	}

	@PostMapping("/criar")
	public ResponseEntity<Demanda> criarDemanda(@RequestBody DemandaRequestDTO demandaRequestDTO) {
		Demanda demanda = new Demanda();
		demanda.setDescricao(demandaRequestDTO.getDescricao());

		if (!isValidStatus(demandaRequestDTO.getStatus())) {
			throw new IllegalArgumentException("Status inválido: " + demandaRequestDTO.getStatus());
		}

		demanda.setPrioridade(Prioridade.valueOf(demandaRequestDTO.getPrioridade().toUpperCase()));
		demanda.setStatus(StatusDemanda.valueOf(demandaRequestDTO.getStatus().toUpperCase()));
		demanda.setCliente(demandaRequestDTO.getCliente());

		Demanda novaDemanda = demandaService.salvarDemanda(demanda);
		return ResponseEntity.ok(novaDemanda);
	}

	@GetMapping
	public ResponseEntity<List<Demanda>> listarDemandas() {
		List<Demanda> demandas = demandaService.listarTodasDemandas();
		return ResponseEntity.ok(demandas);
	}

	@GetMapping("/{id}")
	public ResponseEntity<Demanda> buscarPorId(@PathVariable Long id) {
		Optional<Demanda> demandaOptional = demandaService.buscarPorId(id);
		if (!demandaOptional.isPresent()) {
			throw new ResourceNotFoundException("Demanda com ID " + id + " não encontrada.");
		}

		Demanda demanda = demandaOptional.get();
		return ResponseEntity.ok(demanda);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Demanda> atualizarDemanda(@PathVariable Long id,
			@RequestBody DemandaRequestDTO demandaRequestDTO) {
		Demanda demanda = demandaService.atualizarDemanda(id, demandaRequestDTO);
		return ResponseEntity.ok(demanda);
	}

	@PutMapping("/{id}/adicionar-tarefas")
	public ResponseEntity<Demanda> adicionarTarefas(@PathVariable Long id, @RequestBody List<Long> tarefaIds) {
		Demanda demandaAtualizada = demandaService.adicionarTarefas(id, tarefaIds);
		return ResponseEntity.ok(demandaAtualizada);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deletarDemanda(@PathVariable Long id) {
		demandaService.deletarDemanda(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping("/clientes-solicitacoes")
	public ResponseEntity<List<Cliente>> listarClientesComSolicitacoes() {
	    List<Cliente> clientes = demandaService.listarClientesComSolicitacoes();
	    return ResponseEntity.ok(clientes);
	}

}
