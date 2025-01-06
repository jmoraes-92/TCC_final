package com.orcamentos.kaspper.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

public class OrcamentoRequestDTO {

    @JsonProperty("id_demanda")
    private Long id_demanda;

    @JsonProperty("valor")
    private BigDecimal valor;

    @JsonProperty("prazo_estimado")
    private Integer prazoEstimado;

    public Long getIdDemanda() {
        return id_demanda;
    }

    public void setIdDemanda(Long id_demanda) {
        this.id_demanda = id_demanda;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public Integer getPrazoEstimado() {
        return prazoEstimado;
    }

    public void setPrazoEstimado(Integer prazoEstimado) {
        this.prazoEstimado = prazoEstimado;
    }

    @Override
    public String toString() {
        return "OrcamentoRequestDTO{" +
                "id_demanda=" + id_demanda +
                ", valor=" + valor +
                ", prazoEstimado=" + prazoEstimado +
                '}';
    }
}
