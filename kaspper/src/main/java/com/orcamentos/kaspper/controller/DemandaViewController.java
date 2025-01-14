package com.orcamentos.kaspper.controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/orcamentos/demandas")
public class DemandaViewController {

    @GetMapping("/novo")
    public String exibirFormularioDemanda() {
        return "demanda-form";
    }
}