package com.academia.controller;

import com.academia.Repository.InstrutorRepository;
import com.academia.Repository.MembroRepository;
import com.academia.model.pessoa.Instrutor;
import com.academia.model.pessoa.Membro;
import com.academia.model.treino.ExercicioMusculacao;
import com.academia.model.treino.FichaDeTreino;

import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.util.List;

public class InstrutoresController {

    private InstrutorRepository instrutorRepository = new InstrutorRepository();
    private MembroRepository membroRepository = new MembroRepository();

    @FXML private TableView<Instrutor> tabelaInstrutores;
    @FXML private TextField campoNome, campoCpf, campoCref, campoEspecialidade;

    @FXML private ListView<Membro> listaAlunosSupervisionados;
    @FXML private ComboBox<Membro> comboTodosMembros;
    @FXML private Button btnVincularAluno;
    @FXML private Button btnRemoverAluno;
    @FXML private Button btnRemoverInstrutor;

    @FXML private TextField txtExercicioNome, txtSeries, txtReps;
    @FXML private Button btnAddExercicio, btnSalvarFicha;
    @FXML private TextArea areaFichaPreview;

    private Instrutor instrutorSelecionado;
    private Membro alunoSelecionadoParaTreino;
    private FichaDeTreino fichaEmEdicao; // Ficha temporária que estamos montando

    @FXML
    public void initialize() {
        configurarTabelaInstrutores();
        carregarDadosIniciais();
        configurarListeners();
    }

    private void configurarTabelaInstrutores() {
        TableColumn<Instrutor, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colNome.setPrefWidth(150);

        TableColumn<Instrutor, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));
        colCpf.setPrefWidth(120);

        TableColumn<Instrutor, String> colCref = new TableColumn<>("CREF");
        colCref.setCellValueFactory(new PropertyValueFactory<>("cref"));
        colCref.setPrefWidth(100);

        TableColumn<Instrutor, String> colEspec = new TableColumn<>("Especialidade");
        colEspec.setCellValueFactory(new PropertyValueFactory<>("especialidade"));
        colEspec.setPrefWidth(150);

        tabelaInstrutores.getColumns().clear();
        tabelaInstrutores.getColumns().addAll(colNome, colCpf, colCref, colEspec);

        tabelaInstrutores.setItems(FXCollections.observableArrayList(instrutorRepository.listarInstrutores()));
    }

    private void carregarDadosIniciais() {
        List<Membro> todosMembros = membroRepository.listarMembros();
        comboTodosMembros.setItems(FXCollections.observableArrayList(todosMembros));

        StringConverter<Membro> converter = new StringConverter<>() {
            @Override public String toString(Membro m) { return (m != null) ? m.getNome() : ""; }
            @Override public Membro fromString(String s) { return null; }
        };
        comboTodosMembros.setConverter(converter);

        listaAlunosSupervisionados.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Membro item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) setText(null);
                else setText(item.getNome() + " (" + item.getCpf() + ")");
            }
        });
    }

    private void configurarListeners() {
        tabelaInstrutores.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            instrutorSelecionado = newVal;
            atualizarListaAlunosSupervisionados();

            btnVincularAluno.setDisable(newVal == null);
            btnRemoverAluno.setDisable(true);
            btnRemoverInstrutor.setDisable(newVal == null);

            limparEditorTreino();
        });

        listaAlunosSupervisionados.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            alunoSelecionadoParaTreino = newVal;

            boolean temAlunoSelecionado = (newVal != null);
            btnRemoverAluno.setDisable(!temAlunoSelecionado);

            if (temAlunoSelecionado) {
                fichaEmEdicao = new FichaDeTreino(newVal.getNome());
                areaFichaPreview.setText("Nova Ficha para: " + newVal.getNome() + "\nAdicione exercícios abaixo...");

                btnAddExercicio.setDisable(false);
                btnSalvarFicha.setDisable(false);
            } else {
                limparEditorTreino();
            }
        });
    }

    private void atualizarListaAlunosSupervisionados() {
        if (instrutorSelecionado != null) {
            List<Membro> alunos = instrutorSelecionado.getMembrosSupervisionados();
            listaAlunosSupervisionados.setItems(FXCollections.observableArrayList(alunos));
        } else {
            listaAlunosSupervisionados.getItems().clear();
        }
    }

    private void limparEditorTreino() {
        alunoSelecionadoParaTreino = null;
        fichaEmEdicao = null;
        areaFichaPreview.setText("");
        btnAddExercicio.setDisable(true);
        btnSalvarFicha.setDisable(true);
        txtExercicioNome.clear();
        txtSeries.clear();
        txtReps.clear();
    }

    @FXML
    private void handleSalvarInstrutor() {
        String nome = campoNome.getText();
        String cpf = campoCpf.getText();
        String cref = campoCref.getText();
        String espec = campoEspecialidade.getText();

        if (!nome.isEmpty() && !cpf.isEmpty()) {
            Instrutor novo = new Instrutor(nome, cpf, cref, espec);
            instrutorRepository.salvar(novo);

            campoNome.clear(); campoCpf.clear(); campoCref.clear(); campoEspecialidade.clear();
            tabelaInstrutores.setItems(FXCollections.observableArrayList(instrutorRepository.listarInstrutores()));
        }
    }

    @FXML
    private void handleVincularAluno() {
        Membro membroParaVincular = comboTodosMembros.getValue();
        if (instrutorSelecionado != null && membroParaVincular != null) {

            instrutorSelecionado.adicionarMembro(membroParaVincular);

            instrutorRepository.removerInstrutor(instrutorSelecionado.getCpf());
            instrutorRepository.salvar(instrutorSelecionado);

            atualizarListaAlunosSupervisionados();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Aluno vinculado com sucesso!");
            alert.show();
        }
    }

    @FXML
    private void handleRemoverAluno() {
        Membro membroParaRemover = listaAlunosSupervisionados.getSelectionModel().getSelectedItem();

        if (instrutorSelecionado != null && membroParaRemover != null) {

            instrutorSelecionado.removerMembro(membroParaRemover);

            instrutorRepository.removerInstrutor(instrutorSelecionado.getCpf()); // Tira o antigo
            instrutorRepository.salvar(instrutorSelecionado); // Salva o novo (sem o aluno)

            atualizarListaAlunosSupervisionados();

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Aluno desvinculado com sucesso!");
            alert.show();
        }
    }

    @FXML
    private void handleRemoverInstrutor() {
        Instrutor selecionado = tabelaInstrutores.getSelectionModel().getSelectedItem();

        if (selecionado != null) {
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                    "Tem certeza que deseja demitir o instrutor " + selecionado.getNome() + "?",
                    ButtonType.YES, ButtonType.NO);

            confirmacao.showAndWait();

            if (confirmacao.getResult() == ButtonType.YES) {
                instrutorRepository.removerInstrutor(selecionado.getCpf());

                tabelaInstrutores.setItems(FXCollections.observableArrayList(instrutorRepository.listarInstrutores()));

                tabelaInstrutores.getSelectionModel().clearSelection();
                instrutorSelecionado = null;
                atualizarListaAlunosSupervisionados();
            }
        }
    }

    @FXML
    private void handleAddExercicio() {
        try {
            String nome = txtExercicioNome.getText();
            int series = Integer.parseInt(txtSeries.getText());
            int reps = Integer.parseInt(txtReps.getText());

            ExercicioMusculacao exercicio = new ExercicioMusculacao(nome, series, reps);
            fichaEmEdicao.adicionarExercicio(exercicio);

            areaFichaPreview.appendText("\n- " + nome + ": " + series + "x" + reps);

            txtExercicioNome.clear();
            txtSeries.clear();
            txtReps.clear();
            txtExercicioNome.requestFocus();

        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR, "Séries e Repetições devem ser números!");
            alert.show();
        }
    }

    @FXML
    private void handleSalvarFichaPersonalizada() {
        if (instrutorSelecionado != null && alunoSelecionadoParaTreino != null && fichaEmEdicao != null) {

            membroRepository.atualizarFicha(alunoSelecionadoParaTreino.getCpf(), fichaEmEdicao);

            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Ficha personalizada salva com sucesso para " + alunoSelecionadoParaTreino.getNome());
            alert.show();

            limparEditorTreino();
        }
    }
}