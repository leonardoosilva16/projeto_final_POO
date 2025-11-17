package com.academia.frontend.controller;

import com.academia.frontend.service.ServiceFacade;
import com.academia.model.pessoa.Membro;
import com.academia.model.plano.Plano;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Optional;

public class AlunosController {

    @FXML private TableView<Membro> tableAlunos;
    @FXML private TableColumn<Membro, String> colNome;
    @FXML private TableColumn<Membro, String> colCpf;
    @FXML private TableColumn<Membro, String> colPlano;

    private final ObservableList<Membro> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNome()));
        colCpf.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCpf()));
        colPlano.setCellValueFactory(cell -> {
            Plano p = cell.getValue().getMatricula() != null ? cell.getValue().getMatricula().getPlano() : null;
            String nome = (p != null) ? p.getNome() : "—";
            return new javafx.beans.property.SimpleStringProperty(nome);
        });
        refreshTable();
    }

    private void refreshTable() {
        List<Membro> membros = ServiceFacade.membroRepo.listarMembros();
        data.setAll(membros);
        tableAlunos.setItems(data);
    }

    @FXML
    private void onNovo() {
        Dialog<Membro> dialog = new Dialog<>();
        dialog.setTitle("Novo Membro");

        Label nomeL = new Label("Nome:");
        TextField nomeF = new TextField();
        Label cpfL = new Label("CPF:");
        TextField cpfF = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(nomeL, 0, 0);
        grid.add(nomeF, 1, 0);
        grid.add(cpfL, 0, 1);
        grid.add(cpfF, 1, 1);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(btn -> {
            if (btn == ButtonType.OK) {
                return new Membro(nomeF.getText(), cpfF.getText());
            }
            return null;
        });

        Optional<Membro> result = dialog.showAndWait();
        result.ifPresent(m -> {
            ServiceFacade.membroRepo.salvar(m);
            refreshTable();
        });
    }

    @FXML
    private void onEditar() {
        Membro selected = tableAlunos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Por favor, selecione um membro.");
            return;
        }

        Dialog<Membro> dialog = new Dialog<>();
        dialog.setTitle("Editar Membro");

        TextField nomeF = new TextField(selected.getNome());

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(nomeF, 1, 0);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(btn -> btn == ButtonType.OK ? selected : null);

        Optional<Membro> result = dialog.showAndWait();
        result.ifPresent(m -> {
            ServiceFacade.membroRepo.atualizar(m.getCpf(), nomeF.getText(), null);
            refreshTable();
        });
    }

    @FXML
    private void onRemover() {
        Membro selected = tableAlunos.getSelectionModel().getSelectedItem();
        if (selected == null) {
            showAlert("Por favor, selecione um membro.");
            return;
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Remover " + selected.getNome() + "?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> resposta = alert.showAndWait();
        if (resposta.isPresent() && resposta.get() == ButtonType.YES) {
            ServiceFacade.membroRepo.listarMembros().removeIf(m -> m.getCpf().equals(selected.getCpf()));
            refreshTable();
        }
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);
        alert.showAndWait();
    }
}
