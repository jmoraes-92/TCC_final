package com.orcamentos.kaspper.controller;

import com.orcamentos.kaspper.model.Demanda;
import com.orcamentos.kaspper.model.Orcamento;
import com.orcamentos.kaspper.repository.DemandaRepository;
import com.orcamentos.kaspper.repository.OrcamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/orcamentos")
public class OrcamentoViewController {

	@Autowired
	private OrcamentoRepository orcamentoRepository;

	@Autowired
	private DemandaRepository demandaRepository;

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
	public String salvar(@RequestParam Long idDemanda, @ModelAttribute Orcamento orcamento, Model model) {
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

		model.addAttribute("orcamento", orcamentoOptional.get());
		return "orcamento-detalhes";
	}

}
