package com.orcamentos.kaspper.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.orcamentos.kaspper.dto.OrcamentoRequestDTO;
import com.orcamentos.kaspper.exception.ResourceNotFoundException;
import com.orcamentos.kaspper.model.Demanda;
import com.orcamentos.kaspper.model.Orcamento;
import com.orcamentos.kaspper.service.DemandaService;
import com.orcamentos.kaspper.service.OrcamentoService;

@RestController
@RequestMapping("/api/orcamentos")
@CrossOrigin(origins = "http://localhost:4200")
public class OrcamentoController {

	@Autowired
	private OrcamentoService orcamentoService;
	@Autowired
	private DemandaService demandaService;

	// Endpoint para listar todos os orçamentos
	@GetMapping
	public ResponseEntity<List<Orcamento>> listarTodos() {
		return ResponseEntity.ok(orcamentoService.listarTodos());
	}

	// Endpoint para buscar um orçamento por ID
	@GetMapping("/{id}")
	public ResponseEntity<Orcamento> buscarPorId(@PathVariable Long id) {
		Orcamento orcamento = orcamentoService.buscarPorId(id);
		return ResponseEntity.ok(orcamento);
	}

	// Endpoint para criar um novo orçamento
	@PostMapping
	public ResponseEntity<Orcamento> salvar(@RequestBody OrcamentoRequestDTO requestDTO) {
		Long idDemanda = requestDTO.getIdDemanda();

		// Buscar a demanda pelo ID
		Demanda demanda = demandaService.buscarPorId(idDemanda)
				.orElseThrow(() -> new ResourceNotFoundException("Demanda com ID " + idDemanda + " não encontrada."));

		// Gerar o orçamento com base na demanda e suas tarefas
		Orcamento novoOrcamento = orcamentoService.gerarOrcamento(demanda, requestDTO);

		return ResponseEntity.ok(novoOrcamento);
	}

	// Endpoint para excluir um orçamento
	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		orcamentoService.excluir(id);
		return ResponseEntity.noContent().build();
	}

	// Endpoint para atualizar informações de um orçamento
	@PutMapping("/{id}")
	public ResponseEntity<Orcamento> atualizar(@PathVariable Long id, @RequestBody Orcamento orcamento) {
		Orcamento orcamentoAtualizado = orcamentoService.atualizar(id, orcamento);
		return ResponseEntity.ok(orcamentoAtualizado);
	}

	// Endpoint com filtro
	@GetMapping("/filtrar")
	public ResponseEntity<List<Orcamento>> listarComFiltros(@RequestParam(required = false) String status,
			@RequestParam(required = false) Double valorMinimo, @RequestParam(required = false) Double valorMaximo) {
		List<Orcamento> orcamentosFiltrados = orcamentoService.listarComFiltros(status, valorMinimo, valorMaximo);
		return ResponseEntity.ok(orcamentosFiltrados);
	}

}
