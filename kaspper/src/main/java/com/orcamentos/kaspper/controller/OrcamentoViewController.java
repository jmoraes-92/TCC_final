package com.orcamentos.kaspper.controller;

import com.orcamentos.kaspper.exception.ResourceNotFoundException;
import com.orcamentos.kaspper.model.Cliente;
import com.orcamentos.kaspper.model.Demanda;
import com.orcamentos.kaspper.model.Orcamento;
import com.orcamentos.kaspper.model.Tarefa;
import com.orcamentos.kaspper.repository.ClienteRepository;
import com.orcamentos.kaspper.repository.DemandaRepository;
import com.orcamentos.kaspper.repository.OrcamentoRepository;
import com.orcamentos.kaspper.repository.TarefaRepository;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.thymeleaf.spring6.SpringTemplateEngine;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.OutputStream;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
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

	@Autowired
	private ClienteRepository clienteRepository;

	@Autowired
	private SpringTemplateEngine templateEngine;

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
		System.out.println("Exibindo detalhes do orçamento com ID: " + orcamento.getId());

		List<Tarefa> tarefas = tarefaRepository.findByDemandaId(orcamento.getDemanda().getId().intValue());

		LocalDate prazoFinal = tarefas.stream().filter(tarefa -> tarefa.getPrazo() != null).map(Tarefa::getPrazo)
				.max(LocalDate::compareTo).orElse(null);

		int prazoEstimado = prazoFinal != null ? (int) ChronoUnit.DAYS.between(LocalDate.now(), prazoFinal) : 0;

		model.addAttribute("orcamento", orcamento);
		model.addAttribute("tarefas", tarefas);
		model.addAttribute("prazoFinal", prazoFinal);
		model.addAttribute("prazoEstimado", prazoEstimado);
		model.addAttribute("valorTotal", orcamento.getValor());
		model.addAttribute("isPdf", false); // Para diferenciar a visualização no navegador e no PDF

		return "orcamento-detalhes";
	}

	@GetMapping("/gerar-pdf/{id}")
	public void gerarPdf(@PathVariable Long id, HttpServletResponse response) throws Exception {
		Optional<Orcamento> orcamentoOptional = orcamentoRepository.findById(id);
		if (orcamentoOptional.isEmpty()) {
			throw new RuntimeException("Orçamento não encontrado.");
		}

		Orcamento orcamento = orcamentoOptional.get();
		List<Tarefa> tarefas = tarefaRepository.findByDemandaId(orcamento.getDemanda().getId().intValue());

		// Log para verificar se os dados estão corretos
		System.out.println("Orçamento ID: " + orcamento.getId());
		System.out.println("Total de tarefas: " + tarefas.size());

		// Configurar o modelo de dados para o template
		Map<String, Object> model = new HashMap<>();
		model.put("orcamento", orcamento);
		model.put("tarefas", tarefas);
		model.put("locale", Locale.getDefault());
		model.put("isPdf", true); // Indica que é para geração de PDF

		// Renderizar o template em uma string usando StringWriter
		StringWriter writer = new StringWriter();
		templateEngine.process("orcamento-detalhes", new org.thymeleaf.context.Context(Locale.getDefault(), model),
				writer);

		String htmlContent = writer.toString();
		System.out.println("HTML Gerado para o PDF:");
		System.out.println(htmlContent);

		// Configurar a resposta HTTP para PDF
		response.setContentType("application/pdf");
		response.setHeader("Content-Disposition", "attachment; filename=orcamento-" + id + ".pdf");

		// Gerar PDF usando Flying Saucer
		try (OutputStream out = response.getOutputStream()) {
			ITextRenderer renderer = new ITextRenderer();
			renderer.setDocumentFromString(htmlContent);
			renderer.layout();
			renderer.createPDF(out, false);
			renderer.finishPDF();
		} catch (Exception e) {
			throw new RuntimeException("Erro ao gerar o PDF: " + e.getMessage(), e);
		}
	}

	@GetMapping("/tarefas")
	public String listarTarefas(Model model) {
		List<Tarefa> tarefas = tarefaRepository.findAll();
		if (tarefas.isEmpty()) {
			System.out.println("Nenhuma tarefa encontrada.");
		} else {
			System.out.println("Tarefas encontradas: " + tarefas.size());
		}
		model.addAttribute("tarefas", tarefas);
		return "tarefas";
	}

	@GetMapping("/tarefas/editar/{id}")
	public String editarTarefa(@PathVariable Long id, Model model) {
		Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);

		if (tarefaOptional.isEmpty()) {
			model.addAttribute("erro", "Tarefa não encontrada.");
			return "redirect:/orcamentos/tarefas";
		}

		Tarefa tarefa = tarefaOptional.get();

		if (tarefa.getDemanda() != null) {
			model.addAttribute("idDemanda", tarefa.getDemanda().getId());
		}

		model.addAttribute("tarefa", tarefa);
		return "tarefa-form";
	}

	@PostMapping("/tarefas/editar/{id}")
	public String salvarEdicaoTarefa(@PathVariable Long id, @Valid @ModelAttribute Tarefa tarefa, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "tarefa-form"; // Retorna o formulário caso haja erros de validação
		}

		Optional<Tarefa> tarefaOptional = tarefaRepository.findById(id);
		if (tarefaOptional.isEmpty()) {
			redirectAttributes.addFlashAttribute("erro", "Tarefa não encontrada.");
			return "redirect:/orcamentos/tarefas";
		}

		Tarefa tarefaExistente = tarefaOptional.get();
		tarefaExistente.setDescricao(tarefa.getDescricao());
		tarefaExistente.setResponsavel(tarefa.getResponsavel());
		tarefaExistente.setPrazo(tarefa.getPrazo());
		tarefaExistente.setStatus(tarefa.getStatus());

		tarefaRepository.save(tarefaExistente);
		redirectAttributes.addFlashAttribute("sucesso", "Tarefa editada com sucesso.");
		return "redirect:/orcamentos/tarefas"; // Redireciona para a lista de tarefas
	}

	@GetMapping("/clientes")
	public String listarClientes(Model model) {
		List<Cliente> clientes = clienteRepository.findAll();
		model.addAttribute("clientes", clientes);
		return "cliente-list";
	}

	@GetMapping("/clientes/novo")
	public String novoCliente(Model model) {
		model.addAttribute("cliente", new Cliente());
		return "cliente-form";
	}

	@PostMapping("/clientes")
	public String salvarCliente(@ModelAttribute Cliente cliente) {
		clienteRepository.save(cliente);
		return "redirect:/orcamentos/clientes";
	}

	@GetMapping("/clientes/editar/{id}")
	public String editarCliente(@PathVariable Long id, Model model) {
		Optional<Cliente> clienteOptional = clienteRepository.findById(id);
		if (clienteOptional.isEmpty()) {
			model.addAttribute("erro", "Cliente não encontrado.");
			return "redirect:/orcamentos/clientes";
		}
		model.addAttribute("cliente", clienteOptional.get());
		return "cliente-form";
	}

	@GetMapping("/clientes/deletar/{id}")
	public String deletarCliente(@PathVariable Long id, Model model) {
		try {
			clienteRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			model.addAttribute("erro", "Não é possível excluir o cliente, pois ele possui demandas associadas.");
			return listarClientes(model);
		}
		return "redirect:/orcamentos/clientes";
	}

}
