package com.academia.services.singleton;

import com.academia.model.pessoa.Membro;

public class CatracaVirtual {
    private static CatracaVirtual instancia;

    private CatracaVirtual() {}

    public static CatracaVirtual getInstancia() {
        if (instancia == null)
            instancia = new CatracaVirtual();
        return instancia;
    }

    public boolean liberarAcesso(Membro membro) {
         if (membro == null) {
            return false;
        }
        if (membro.getMatricula() != null && membro.getMatricula().isAtiva()) {
            System.out.println(" Acesso liberado para " + membro.getNome());
            return true;
        } else {
            System.out.println(" Acesso negado! Matrícula vencida.");
            return false;
        }
    }
}

