package com.orcamentos.kaspper.service;

import com.orcamentos.kaspper.model.Notificacao;
import com.orcamentos.kaspper.model.Usuario;
import com.orcamentos.kaspper.repository.NotificacaoRepository;
import com.orcamentos.kaspper.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class NotificacaoService {

	@Autowired
	private NotificacaoRepository notificacaoRepository;

	@Autowired
	private UsuarioRepository usuarioRepository;

	// Método para enviar notificação para o administrador
	public Notificacao enviarNotificacaoParaAdmin(String mensagem) {
		Long idAdmin = 1L; // ID fixo do administrador
		Usuario admin = usuarioRepository.findById(idAdmin).orElseThrow(() -> {
			System.err.println("Administrador com ID " + idAdmin + " não encontrado.");
			return new IllegalArgumentException("Administrador não encontrado.");
		});

		Notificacao notificacao = new Notificacao();
		notificacao.setUsuario(admin);
		notificacao.setMensagem(mensagem);
		notificacao.setVisualizada(false);
		notificacao.setDataEnvio(LocalDateTime.now());

		return notificacaoRepository.save(notificacao);
	}

	// Novo método: Enviar notificação para qualquer usuário
	public Notificacao enviarNotificacao(Long idUsuario, String mensagem) {
		Usuario usuario = usuarioRepository.findById(idUsuario)
				.orElseThrow(() -> new IllegalArgumentException("Usuário com ID " + idUsuario + " não encontrado."));

		return criarNotificacao(usuario, mensagem);
	}

	// Método reutilizável para criar e salvar notificações
	private Notificacao criarNotificacao(Usuario usuario, String mensagem) {
		Notificacao notificacao = new Notificacao();
		notificacao.setUsuario(usuario);
		notificacao.setMensagem(mensagem);
		notificacao.setDataEnvio(LocalDateTime.now());
		notificacao.setVisualizada(false);

		return notificacaoRepository.save(notificacao);
	}

	// Método para marcar notificação como visualizada
	public Notificacao marcarComoVisualizada(Long idNotificacao) {
		Notificacao notificacao = notificacaoRepository.findById(idNotificacao)
				.orElseThrow(() -> new IllegalArgumentException("Notificação não encontrada."));
		notificacao.setVisualizada(true);
		return notificacaoRepository.save(notificacao);
	}

	// Listar notificações por usuário
	public List<Notificacao> listarNotificacoesPorUsuario(Long idUsuario) {
		return notificacaoRepository.findByUsuarioId(idUsuario);
	}

	// Listar notificações não visualizadas
	public List<Notificacao> listarNaoVisualizadas() {
		return notificacaoRepository.findByVisualizadaFalse();
	}

}
