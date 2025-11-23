package com.academia.controller;

import com.academia.Repository.MembroRepository;
import com.academia.Repository.PlanoRepository;
import com.academia.model.Matricula;
import com.academia.model.pessoa.Membro;
import com.academia.model.plano.Plano;
import com.academia.model.treino.FichaDeTreino;
import com.academia.services.templatemethod.MontadorDeFichaTreino;
import com.academia.services.templatemethod.MontadorFichaEmagrecimento;
import com.academia.services.templatemethod.MontadorFichaHipertrofia;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

import java.text.SimpleDateFormat;
import java.util.List;

public class MembrosControler {

    private MembroRepository membroRepository = new MembroRepository();
    private PlanoRepository planoRepository = new PlanoRepository();

    @FXML private TableView<Membro> tabelaMembros;
    @FXML private TextField campoNome;
    @FXML private TextField campoCpf;
    @FXML private ComboBox<Plano> comboPlanos;

    @FXML private Label lblPlanoAtual;
    @FXML private Label lblVencimento;
    @FXML private Button btnRenovar;
    @FXML private Button btnRemoverMembro;

    @FXML private ComboBox<String> comboTipoTreino;
    @FXML private Button btnGerarFicha;
    @FXML private TextArea areaTreino;

    private ObservableList<Membro> membrosVisiveis;
    private Membro membroSelecionado; // Guarda quem está selecionado na tabela

    @FXML
    public void initialize() {
        configurarTabela();
        carregarDadosIniciais();
        configurarListenerDeSelecao();
    }

    private void configurarTabela() {
        TableColumn<Membro, String> colNome = new TableColumn<>("Nome");
        colNome.setCellValueFactory(new PropertyValueFactory<>("nome"));

        TableColumn<Membro, String> colCpf = new TableColumn<>("CPF");
        colCpf.setCellValueFactory(new PropertyValueFactory<>("cpf"));

        tabelaMembros.getColumns().addAll(colNome, colCpf);
    }

    private void carregarDadosIniciais() {
        List<Membro> listaMembros = membroRepository.listarMembros();
        membrosVisiveis = FXCollections.observableArrayList(listaMembros);
        tabelaMembros.setItems(membrosVisiveis);

        List<Plano> listaPlanos = planoRepository.listarPlanos();
        comboPlanos.setItems(FXCollections.observableArrayList(listaPlanos));

        comboPlanos.setConverter(new StringConverter<Plano>() {
            @Override
            public String toString(Plano p) { return (p != null) ? p.getNome() : ""; }
            @Override
            public Plano fromString(String s) { return null; }
        });

        comboTipoTreino.setItems(FXCollections.observableArrayList("Hipertrofia", "Emagrecimento"));
    }

    private void configurarListenerDeSelecao() {
        tabelaMembros.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    mostrarDetalhesMembro(newValue);

                    btnRemoverMembro.setDisable(newValue == null);
                }
        );
    }

    private void mostrarDetalhesMembro(Membro m) {
        this.membroSelecionado = m;

        if (m != null) {
            btnRenovar.setDisable(false);
            btnGerarFicha.setDisable(false);

            if (m.getMatricula() != null) {
                lblPlanoAtual.setText("Plano: " + m.getMatricula().getPlano().getNome());
                SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                lblVencimento.setText("Vence em: " + sdf.format(m.getMatricula().getDataVencimento()));
            } else {
                lblPlanoAtual.setText("Plano: Sem matrícula");
                lblVencimento.setText("Vence em: --/--/--");
            }

            if (m.getFichaDeTreino() != null) {
                areaTreino.setText("Ficha de: " + m.getFichaDeTreino().getNomeAluno() + "\n\n" +
                        m.getFichaDeTreino().getExercicio().toString());
            } else {
                areaTreino.setText("Nenhuma ficha de treino criada.");
            }

        } else {
            // Se nada selecionado, limpa tudo
            lblPlanoAtual.setText("Plano: -");
            lblVencimento.setText("Vence em: -");
            areaTreino.setText("");
            btnRenovar.setDisable(true);
            btnGerarFicha.setDisable(true);
        }
    }

    @FXML
    private void handleSalvarMembro() {
        String nome = campoNome.getText();
        String cpf = campoCpf.getText();
        Plano planoSelecionado = comboPlanos.getValue();

        if (!nome.isEmpty() && !cpf.isEmpty() && planoSelecionado != null) {
            Membro novoMembro = new Membro(nome, cpf);

            novoMembro.setMatricula(new Matricula(planoSelecionado));

            membroRepository.salvar(novoMembro);

            campoNome.clear();
            campoCpf.clear();
            comboPlanos.getSelectionModel().clearSelection();
            carregarDadosIniciais();
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Dados Incompletos");
            alert.setContentText("Por favor, preencha Nome, CPF e escolha um Plano.");
            alert.showAndWait();
        }
    }

    @FXML
    private void handleRemoverMembro() {
        Membro membroSelecionado = tabelaMembros.getSelectionModel().getSelectedItem();

        if (membroSelecionado != null) {
            Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION,
                    "Tem a certeza que deseja remover o membro " + membroSelecionado.getNome() + "?",
                    ButtonType.YES, ButtonType.NO);

            confirmacao.showAndWait();

            if (confirmacao.getResult() == ButtonType.YES) {
                boolean removido = membroRepository.removerMembro(membroSelecionado.getCpf());

                if (removido) {
                    carregarDadosIniciais();

                    tabelaMembros.getSelectionModel().clearSelection();
                    mostrarDetalhesMembro(null);

                    Alert alert = new Alert(Alert.AlertType.INFORMATION, "Membro removido com sucesso!");
                    alert.show();
                }
            }
        }
    }

    @FXML
    private void handleRenovarMatricula() {
        if (membroSelecionado != null) {
            membroRepository.renovarMatricula(membroSelecionado.getCpf());

            mostrarDetalhesMembro(membroSelecionado);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Matrícula renovada com sucesso!");
            alert.show();
        }
    }

    @FXML
    private void handleGerarFicha() {
        String tipo = comboTipoTreino.getValue();

        if (membroSelecionado != null && tipo != null) {
            MontadorDeFichaTreino montador = null;

            if (tipo.equals("Hipertrofia")) {
                montador = new MontadorFichaHipertrofia();
            } else if (tipo.equals("Emagrecimento")) {
                montador = new MontadorFichaEmagrecimento();
            }

            if (montador != null) {
                FichaDeTreino novaFicha = montador.montar(membroSelecionado);
                membroSelecionado.setFichaDeTreino(novaFicha);

                membroRepository.atualizar(membroSelecionado.getCpf(), membroSelecionado.getNome(), null);

                mostrarDetalhesMembro(membroSelecionado);
            }
        }
    }
}