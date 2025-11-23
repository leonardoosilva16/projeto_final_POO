package com.academia.services.templatemethod;

import com.academia.model.treino.Exercicio;
import com.academia.model.treino.ExercicioAerobico;
import com.academia.model.treino.ExercicioMusculacao;

import java.util.ArrayList;
import java.util.List;

public class MontadorFichaEmagrecimento extends MontadorDeFichaTreino {

    @Override
    protected List<Exercicio> adiconarExerciciosPrincipais() {
        List<Exercicio> exercicios = new ArrayList<>();
        exercicios.add(new ExercicioMusculacao("Agachamento (circuito)", 4, 12));
        exercicios.add(new ExercicioAerobico("Esterira", 30));
        exercicios.add(new ExercicioMusculacao("Jump Squats", 5, 30));
        exercicios.add(new ExercicioMusculacao("High Knees", 5, 30));
        exercicios.add(new ExercicioAerobico("Descanso", 5));
        return exercicios;
    }
}
