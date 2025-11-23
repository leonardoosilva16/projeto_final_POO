package com.academia.model.pessoa;

import com.academia.model.treino.FichaDeTreino;
import com.academia.services.templatemethod.MontadorDeFichaTreino;

import java.util.ArrayList;
import java.util.List;

public class Instrutor extends Pessoa {

    private String cref;
    private String especialidade;

    private List<Membro> membrosSupervisionados;

    public Instrutor() {
        super();
        this.membrosSupervisionados = new ArrayList<>();
    }

    public Instrutor(String nome, String cpf, String cref, String especialidade) {
        super(nome, cpf);
        this.cref = cref;
        this.especialidade = especialidade;
        this.membrosSupervisionados = new ArrayList<>();
    }

    public void montarFicha(Membro membro, MontadorDeFichaTreino montador) {
        FichaDeTreino novaFicha = montador.montar(membro);
        membro.setFichaDeTreino(novaFicha);

        if (!this.membrosSupervisionados.contains(membro)) {
            this.adicionarMembro(membro);
        }
    }

    public void adicionarMembro(Membro membro) {
        if (!this.membrosSupervisionados.contains(membro)) {
            this.membrosSupervisionados.add(membro);
        };
    }

    public String getCref() { return cref; }
    public void setCref(String cref) { this.cref = cref; }

    public String getEspecialidade() { return especialidade; }
    public void setEspecialidade(String especialidade) { this.especialidade = especialidade; }

    public List<Membro> getMembrosSupervisionados() {
        return membrosSupervisionados;
    }

    public void setMembrosSupervisionados(List<Membro> membrosSupervisionados) {
        this.membrosSupervisionados = membrosSupervisionados;
    }

    public void removerMembro(Membro membro) {
        this.membrosSupervisionados.remove(membro);
    }

    @Override
    public String toString() {
        return getNome();
    }
}