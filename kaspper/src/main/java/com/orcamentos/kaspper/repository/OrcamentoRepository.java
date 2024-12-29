package com.orcamentos.kaspper.repository;

import com.orcamentos.kaspper.model.Orcamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrcamentoRepository extends JpaRepository<Orcamento, Long> {

    // Consulta baseada no ID da demanda
    List<Orcamento> findByDemandaId(Integer idDemanda);

    // Consultas para suporte a filtros
    List<Orcamento> findByStatusAndValorBetween(String status, Double valorMinimo, Double valorMaximo);

    List<Orcamento> findByStatus(String status);

    List<Orcamento> findByValorBetween(Double valorMinimo, Double valorMaximo);
}
