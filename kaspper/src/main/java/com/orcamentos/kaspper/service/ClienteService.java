package com.orcamentos.kaspper.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.orcamentos.kaspper.exception.ResourceNotFoundException;
import com.orcamentos.kaspper.model.Cliente;
import com.orcamentos.kaspper.repository.ClienteRepository;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository clienteRepository;

	public Cliente salvar(Cliente cliente) {
	    Optional<Cliente> clienteExistente = clienteRepository.findByEmail(cliente.getEmail());
	    
	    if (clienteExistente.isPresent()) {
	        cliente.setId(clienteExistente.get().getId()); // Define o ID do cliente existente para atualizar
	    }
	    
	    return clienteRepository.save(cliente);
	}


	public List<Cliente> listarTodos() {
		return clienteRepository.findAll();
	}

	public Cliente buscarPorId(Long id) {
		return clienteRepository.findById(id)
				.orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado com ID: " + id));
	}

	public void excluir(Long id) {
		if (!clienteRepository.existsById(id)) {
			throw new ResourceNotFoundException("Cliente não encontrado com ID: " + id);
		}
		clienteRepository.deleteById(id);
	}
}
