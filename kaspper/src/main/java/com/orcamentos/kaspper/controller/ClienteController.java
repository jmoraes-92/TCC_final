package com.orcamentos.kaspper.controller;

import com.orcamentos.kaspper.model.Cliente;
import com.orcamentos.kaspper.service.ClienteService;
import com.orcamentos.kaspper.service.NotificacaoService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/clientes")
@CrossOrigin(origins = "http://localhost:4200")
public class ClienteController {

	@Autowired
	private ClienteService clienteService;

	@Autowired
	private NotificacaoService notificacaoService;

	@GetMapping
	public ResponseEntity<List<Cliente>> listarTodos() {
		return ResponseEntity.ok(clienteService.listarTodos());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Cliente> buscarPorId(@PathVariable Long id) {
		return ResponseEntity.ok(clienteService.buscarPorId(id));
	}

	@PostMapping
	public ResponseEntity<Cliente> salvar(@RequestBody Cliente cliente) {
		Cliente novoCliente = clienteService.salvar(cliente);

		// Cria a notificação para o administrador
		notificacaoService.enviarNotificacaoParaAdmin("Você tem uma nova solicitação de orçamento!");

		return ResponseEntity.ok(novoCliente);
	}

	@PutMapping("/{id}")
	public ResponseEntity<Cliente> atualizar(@PathVariable Long id, @RequestBody Cliente cliente) {
		Cliente clienteExistente = clienteService.buscarPorId(id);
		clienteExistente.setNome(cliente.getNome());
		clienteExistente.setEmail(cliente.getEmail());
		clienteExistente.setTelefone(cliente.getTelefone());
		clienteExistente.setEmpresa(cliente.getEmpresa());
		return ResponseEntity.ok(clienteService.salvar(clienteExistente));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> excluir(@PathVariable Long id) {
		clienteService.excluir(id);
		return ResponseEntity.noContent().build();
	}
}
