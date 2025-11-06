package com.academia.services.templatemethod;

import com.academia.model.treino.Exercicio;
import com.academia.model.treino.ExercicioMusculacao;

import java.util.ArrayList;
import java.util.List;

public class MontadorFichaHipertrofia extends MontadorDeFichaTreino{

    @Override
    protected List<Exercicio> adiconarExerciciosPrincipais() {
        List<Exercicio> exercicios = new ArrayList<>();
        exercicios.add(new ExercicioMusculacao("Supino Reto", 4, 12));
        exercicios.add(new ExercicioMusculacao("Triceps com Corda", 4, 12));
        exercicios.add(new ExercicioMusculacao("Supino Inclinado", 4, 10));
            exercicios.add(new ExercicioMusculacao("Extensão de Tríceps", 4, 12));
        return exercicios;
    }
}
