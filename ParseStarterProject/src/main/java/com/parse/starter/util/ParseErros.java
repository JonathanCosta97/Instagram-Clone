package com.parse.starter.util;

import java.util.HashMap;

public class ParseErros {

    HashMap<Integer, String> erros;

    public ParseErros() {

        this.erros = new HashMap<>();
        erros.put(201,"A senha não foi preenchida!");
        erros.put(202,"O usuario ja existe, escolha um outro nome de usuario!");
    }

    public String getErro(int codErro){

        return this.erros.get(codErro);
    }
}
