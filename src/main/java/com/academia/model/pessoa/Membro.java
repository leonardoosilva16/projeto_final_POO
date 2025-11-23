package com.academia.model.pessoa;

import com.academia.model.Matricula;
import com.academia.model.pessoa.Pessoa;
import com.academia.model.treino.FichaDeTreino;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class  Membro extends Pessoa {
    private Matricula matricula;
    private FichaDeTreino fichaDeTreino;

    public Membro () {
        super();
    }

    public Membro(String nome, String cpf) {
        super(nome, cpf);
    }

    public Matricula getMatricula() {
        return matricula;
    }

    public void setMatricula(Matricula matricula) {
        this.matricula = matricula;
    }

    public FichaDeTreino getFichaDeTreino() {
        return fichaDeTreino;
    }

    public void setFichaDeTreino(FichaDeTreino fichaDeTreino) {
        this.fichaDeTreino = fichaDeTreino;
    }

    @Override
    public String toString() {
        SimpleDateFormat formato = new SimpleDateFormat("EEEE dd MMM", new Locale("pt", "BR"));

        String nome = getNome();
        String plano = (matricula != null && matricula.getPlano() != null)
                ? matricula.getPlano().getNome()
                : "Sem plano";
        String vencimento = (matricula != null && matricula.getDataVencimento() != null)
                ? formato.format(matricula.getDataVencimento())
                : "Sem vencimento";

        return "{Membro: " + nome +
                " | Plano: " + plano +
                " | Vencimento: " + vencimento +
                "}";
    }
}