package io.github.alberes.register.manager.controllers.dto;

public record AddressViaCEPDto(String cep,
                               String logradouro,
                               String complemento,
                               String unidade,
                               String bairro,
                               String localidade,
                               String uf,
                               String estado,
                               String regiao,
                               String ibge,
                               String gia,
                               String ddd,
                               String siafi) {
}
