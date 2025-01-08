package com.orcamentos.kaspper.controller;

import com.orcamentos.kaspper.model.Demanda;
import com.orcamentos.kaspper.model.Orcamento;
import com.orcamentos.kaspper.model.Tarefa;
import com.orcamentos.kaspper.repository.DemandaRepository;
import com.orcamentos.kaspper.repository.OrcamentoRepository;
import com.orcamentos.kaspper.repository.TarefaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Controller
@RequestMapping("/orcamentos")
@CrossOrigin(origins = "http://localhost:4200")
public class OrcamentoViewController {

	@Autowired
	private OrcamentoRepository orcamentoRepository;

	@Autowired
	private DemandaRepository demandaRepository;

	@Autowired
	private TarefaRepository tarefaRepository;

	@GetMapping
	public String listarOrcamentos(Model model) {
		List<Orcamento> orcamentos = orcamentoRepository.findAll();
		model.addAttribute("orcamentos", orcamentos);
		return "orcamentos-list";
	}

	@GetMapping("/novo")
	public String novoOrcamento(Model model) {
		Orcamento orcamento = new Orcamento();
		List<Demanda> demandas = demandaRepository.findAll();
		model.addAttribute("orcamento", orcamento);
		model.addAttribute("demandas", demandas);
		return "orcamento-form";
	}

	@PostMapping
	public String salvar(@RequestParam Long idDemanda, @RequestParam BigDecimal valor,
			@RequestParam Integer prazoEstimado, @ModelAttribute Orcamento orcamento, Model model) {
		Optional<Demanda> demandaOptional = demandaRepository.findById(idDemanda);

		if (demandaOptional.isEmpty()) {
			model.addAttribute("erro", "Demanda selecionada não encontrada.");
			List<Demanda> demandas = demandaRepository.findAll();
			model.addAttribute("orcamento", orcamento);
			model.addAttribute("demandas", demandas);
			return "orcamento-form";
		}

		Demanda demanda = demandaOptional.get();
		orcamento.setDemanda(demanda);
		orcamento.setValor(valor);
		orcamento.setPrazoEstimado(prazoEstimado);

		Orcamento orcamentoSalvo = orcamentoRepository.save(orcamento);

		return "redirect:/orcamentos/" + orcamentoSalvo.getId();
	}

	@GetMapping("/editar/{id}")
	public String editarOrcamento(@PathVariable Long id, Model model) {
		Optional<Orcamento> orcamentoOptional = orcamentoRepository.findById(id);

		if (orcamentoOptional.isEmpty()) {
			model.addAttribute("erro", "Orçamento não encontrado.");
			return "redirect:/orcamentos";
		}

		List<Demanda> demandas = demandaRepository.findAll();
		model.addAttribute("orcamento", orcamentoOptional.get());
		model.addAttribute("demandas", demandas);
		return "orcamento-form";
	}

	@GetMapping("/deletar/{id}")
	public String deletarOrcamento(@PathVariable Long id) {
		orcamentoRepository.deleteById(id);
		return "redirect:/orcamentos";
	}

	@GetMapping("/{id}")
	public String exibirOrcamento(@PathVariable Long id, Model model) {
		Optional<Orcamento> orcamentoOptional = orcamentoRepository.findById(id);

		if (orcamentoOptional.isEmpty()) {
			model.addAttribute("erro", "Orçamento não encontrado.");
			return "redirect:/orcamentos";
		}

		Orcamento orcamento = orcamentoOptional.get();
		List<Tarefa> tarefas = tarefaRepository.findByDemandaId(orcamento.getDemanda().getId().intValue());

		LocalDate prazoFinal = tarefas.stream().filter(tarefa -> tarefa.getPrazo() != null).map(Tarefa::getPrazo)
				.max(LocalDate::compareTo).orElse(null);

		int prazoEstimado = prazoFinal != null ? (int) ChronoUnit.DAYS.between(LocalDate.now(), prazoFinal) : 0;

		BigDecimal valorTotal = orcamento.getValor();

		model.addAttribute("orcamento", orcamento);
		model.addAttribute("tarefas", tarefas);
		model.addAttribute("prazoFinal", prazoFinal);
		model.addAttribute("prazoEstimado", prazoEstimado);
		model.addAttribute("valorTotal", valorTotal);

		return "orcamento-detalhes";
	}

}
