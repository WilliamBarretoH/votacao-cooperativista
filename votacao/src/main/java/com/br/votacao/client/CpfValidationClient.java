package com.br.votacao.client;

import com.br.votacao.client.dto.CpfValidationResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


//No momento que estava desenvolvendo o serviço de CPF passado no teste não esta funcionando
//Mas quis demonstrar integração utilizando o Feign Client
//No meu service deixarei apenas um valor fixo para bypass em qualquer cpf
@FeignClient(name = "cpfValidationClient", url = "https://user-info.herokuapp.com")
public interface CpfValidationClient {

    @GetMapping("/users/{cpf}")
    ResponseEntity<CpfValidationResponse> validarCpf(@PathVariable("cpf") String cpf);
}
