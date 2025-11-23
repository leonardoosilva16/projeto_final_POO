package com.academia.services.singleton;

import com.academia.model.pessoa.Instrutor;
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
        if (membro.getMatricula() != null && membro.getMatricula().isAtiva()) {
            System.out.println(" Acesso liberado para " + membro.getNome());
            return true;
        } else {
            System.out.println(" Acesso negado! Matrícula vencida.");
            return false;
        }
    }

    public boolean liberarAcesso(Instrutor instrutor) {
        if (instrutor != null) {
            // Aqui você poderia verificar se o instrutor está "ativo" ou de férias,
            // mas por enquanto vamos dar acesso total.
            System.out.println("🎓 Acesso liberado para instrutor: " + instrutor.getNome());
            return true;
        }
        return false;
    }
}

