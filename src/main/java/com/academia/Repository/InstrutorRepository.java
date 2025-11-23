package com.academia.Repository;

import com.academia.model.pessoa.Instrutor;
import com.academia.model.plano.Plano;
import com.academia.model.treino.Exercicio;
import com.academia.services.adapter.ExercicioTypeAdapter;
import com.academia.services.adapter.PlanoTypeAdapter;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class InstrutorRepository {

    private List<Instrutor> instrutores;
    private static final String FILE_PATH = "instrutores.json";
    private Gson gson;

    public InstrutorRepository() {
        this.gson = new GsonBuilder()
                .registerTypeAdapter(Plano.class, new PlanoTypeAdapter())
                .registerTypeAdapter(Exercicio.class, new ExercicioTypeAdapter())
                .setPrettyPrinting()
                .create();

        this.instrutores = carregarDoArquivo();
    }

    private List<Instrutor> carregarDoArquivo() {
        try (Reader reader = new FileReader(FILE_PATH)) {
            Type listType = new TypeToken<ArrayList<Instrutor>>() {}.getType();
            List<Instrutor> lista = gson.fromJson(reader, listType);
            if (lista == null) return new ArrayList<>();
            return lista;
        } catch (FileNotFoundException e) {
            return new ArrayList<>();
        } catch (IOException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }

    private void salvarNoArquivo() {
        try (Writer writer = new FileWriter(FILE_PATH)) {
            gson.toJson(this.instrutores, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void salvar(Instrutor instrutor) {
        this.instrutores.add(instrutor);
        salvarNoArquivo();
    }

    public Instrutor procurarInstrutor(String cpf) {
        for (Instrutor instrutor : this.instrutores) {
            if (instrutor.getCpf().equals(cpf)) {
                return instrutor;
            }
        }
        return null;
    }

    public List<Instrutor> listarInstrutores() {
        return this.instrutores;
    }

    public boolean removerInstrutor(String cpf) {
        boolean removeu = this.instrutores.removeIf(instrutor -> instrutor.getCpf().equals(cpf));
        if (removeu) {
            salvarNoArquivo();
            return true;
        }
        return false;
    }
}