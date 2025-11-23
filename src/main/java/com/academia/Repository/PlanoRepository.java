package com.academia.Repository;

import com.academia.model.plano.Plano;
import com.academia.model.plano.PlanoMusculacao;
import com.academia.model.plano.PlanoPremium;

import java.util.ArrayList;
import java.util.List;

public class PlanoRepository {

    private List<Plano> planos;

    public PlanoRepository() {
        this.planos = new ArrayList<>();

        this.planos.add(new PlanoMusculacao());
        this.planos.add(new PlanoPremium());
    }

    public List<Plano> listarPlanos() {
        return this.planos;
    }

    public Plano buscarPlano(String nomePlano) {
        for (Plano p : this.planos) {
            if (p.getNome().equals(nomePlano)) {
                return p;
            }
        }
        return null;
    }

}