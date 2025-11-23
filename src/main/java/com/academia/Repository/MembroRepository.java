package com.academia.Repository;

import com.academia.model.Matricula;
import com.academia.model.pessoa.Membro;
import com.academia.model.plano.Plano;

import com.academia.model.treino.Exercicio;
import com.academia.model.treino.FichaDeTreino;
import com.academia.services.adapter.ExercicioTypeAdapter;
import com.academia.services.adapter.PlanoTypeAdapter;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class MembroRepository {
    private List<Membro> membros;
    private static final String FILE_PATH = "membros.json";
    private Gson gson;

    public MembroRepository() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Plano.class, new PlanoTypeAdapter())
                .registerTypeAdapter(Exercicio.class, new ExercicioTypeAdapter())
                .setPrettyPrinting()
                .create();
        this.membros = carregarDOArquivo();
    }

    private List<Membro> carregarDOArquivo() {
        try (Reader reader = new FileReader(FILE_PATH)){
            Type listType = new TypeToken<List<Membro>>(){}.getType();
            List<Membro> lista = gson.fromJson(reader, listType);

            if (lista == null) {
                return new ArrayList<>();
            }
            System.out.println("Membros carregados do arquivo" + FILE_PATH);
            return lista;
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void salvarNoArquivo() {
        try (Writer write = new FileWriter(FILE_PATH)) {
            gson.toJson(this.membros, write);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void salvar(Membro membro) {
        this.membros.add(membro);
        salvarNoArquivo();
        System.out.println("Membro" + membro.getNome() + "salvo com sucesso!");
    }

    public Membro procurarMembro(String cpf) {
        for (Membro membro : this.membros) {
            if (membro.getCpf().equals(cpf)) {
                return membro;
            }
        }
        return null;
    }

    public boolean removerMembro(String cpf) {
        boolean removeu = this.membros.removeIf(membro -> membro.getCpf().equals(cpf));

        if (removeu) {
            salvarNoArquivo();
            return true;
        }
        return false;
    }

    public List<Membro> listarMembros() {
        return this.membros;
    }

    public void atualizar(String cpf, String novoNome, Plano novoPlano) {
        for (Membro m : membros) {
            if (m.getCpf().equals(cpf)) {
                if (novoNome != null && !novoNome.isEmpty()) {
                    m.setNome(novoNome);
                }
                if (novoPlano != null) {
                    m.setMatricula(new Matricula(novoPlano));
                }
                salvarNoArquivo();
            }
        }
        System.out.println("Membro com CPF " + cpf + " não encontrado.");
    }

    public void atualizarFicha(String cpf, FichaDeTreino novaFicha) {
        Membro membro = procurarMembro(cpf);

        if (membro != null) {
            membro.setFichaDeTreino(novaFicha);

            salvarNoArquivo();

            System.out.println("Ficha de treino atualizada para o membro: " + membro.getNome());
        }
    }

    public void renovarMatricula(String cpf) {
        Membro m = procurarMembro(cpf);
        if (m != null && m.getMatricula() != null) {
            m.getMatricula().renovar();
            salvarNoArquivo();
        } else {
            System.out.println("Membro não encontrado ou sem matrícula.");
        }
    }

}
