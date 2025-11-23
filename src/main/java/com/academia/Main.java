package com.academia;

//classe de para testes

import com.academia.Repository.InstrutorRepository;
import com.academia.Repository.MembroRepository;
import com.academia.Repository.PlanoRepository;
import com.academia.model.Matricula;
import com.academia.model.pessoa.Instrutor;
import com.academia.model.pessoa.Membro;
import com.academia.model.plano.PlanoMusculacao;
import com.academia.model.plano.PlanoPremium;
import com.academia.model.treino.ExercicioMusculacao;
import com.academia.model.treino.FichaDeTreino;
import com.academia.services.singleton.CatracaVirtual;
import com.academia.services.templatemethod.MontadorDeFichaTreino;
import com.academia.services.templatemethod.MontadorFichaEmagrecimento;
import com.academia.services.templatemethod.MontadorFichaHipertrofia;

public class Main {
    public static void main(String[] args) {
        ExercicioMusculacao exercicio = new ExercicioMusculacao("supino", 4, 12);
        System.out.println(exercicio.getNomeExercicio());
        System.out.println(exercicio.getIntrucao());

        FichaDeTreino fichaDeTreino = new FichaDeTreino("Allan");
        fichaDeTreino.adicionarExercicio(new ExercicioMusculacao("supino inclinado", 4, 12));
        fichaDeTreino.adicionarExercicio(new ExercicioMusculacao("supino reto", 4, 12));
        System.out.println(fichaDeTreino.getExercicio());

        Membro allan = new Membro("Allan", "123.456.789-10");
        MembroRepository membroRepository = new MembroRepository();
        Membro joao = new Membro("Joao", "123.456.789-20");
        membroRepository.salvar(allan);
        membroRepository.salvar(joao);
        allan.setMatricula(new Matricula(new PlanoMusculacao()));
        System.out.println(membroRepository.listarMembros());
        System.out.println(membroRepository.procurarMembro("123.456.789-10"));
        membroRepository.atualizar("123.456.789-10", "Allan Pedro", new PlanoPremium());
        joao.setMatricula(new Matricula(new PlanoMusculacao()));
        System.out.println(membroRepository.listarMembros());

        MontadorDeFichaTreino montarFicha1 = new MontadorFichaHipertrofia();
        MontadorDeFichaTreino montarFicha2 = new MontadorFichaEmagrecimento();
        Instrutor bruno  = new Instrutor("Bruno", "987.654.321-00", "1234-G/SP", "Musculação");
        bruno.montarFicha(allan, montarFicha1);
        bruno.montarFicha(joao, montarFicha2);
        System.out.println(allan.getFichaDeTreino());
        System.out.println(joao.getFichaDeTreino());
        InstrutorRepository instrutorRepository = new InstrutorRepository();
        System.out.println(instrutorRepository.listarInstrutores());

        CatracaVirtual catraca = CatracaVirtual.getInstancia();
        catraca.liberarAcesso(allan);
    }
}
