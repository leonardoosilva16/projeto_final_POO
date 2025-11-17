package com.academia.frontend.controller;

import com.academia.frontend.service.ServiceFacade;
import com.academia.model.pessoa.Instrutor;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Optional;

public class InstrutoresController {

    @FXML private TableView<Instrutor> tableInstrutores;
    @FXML private TableColumn<Instrutor, String> colNome;
    @FXML private TableColumn<Instrutor, String> colCpf;
    @FXML private TableColumn<Instrutor, String> colCref;

    private final ObservableList<Instrutor> data = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getNome()));
        colCpf.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCpf()));
        colCref.setCellValueFactory(cell -> new javafx.beans.property.SimpleStringProperty(cell.getValue().getCref()));
        refresh();
    }

    private void refresh() {
        List<Instrutor> list = ServiceFacade.instrutorRepo.listarInstrutores();
        data.setAll(list);
        tableInstrutores.setItems(data);
    }

    @FXML
    private void onNovo() {
        Dialog<Instrutor> dialog = new Dialog<>();
        dialog.setTitle("Novo Instrutor");

        TextField nomeF = new TextField();
        TextField cpfF = new TextField();
        TextField crefF = new TextField();
        TextField especialidadeF = new TextField();

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.add(new Label("Nome:"), 0, 0);
        grid.add(nomeF, 1, 0);
        grid.add(new Label("CPF:"), 0, 1);
        grid.add(cpfF, 1, 1);
        grid.add(new Label("CREF:"), 0, 2);
        grid.add(crefF, 1, 2);
        grid.add(new Label("Especialidade:"), 0, 3);
        grid.add(especialidadeF, 1, 3);

        dialog.getDialogPane().setContent(grid);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
        dialog.setResultConverter(bt -> bt == ButtonType.OK ? new Instrutor(nomeF.getText(), cpfF.getText(), crefF.getText(), especialidadeF.getText()) : null);

        Optional<Instrutor> r = dialog.showAndWait();
        r.ifPresent(i -> {
            ServiceFacade.instrutorRepo.salvarInstrutor(i);
            refresh();
        });
    }

    @FXML
    private void onRemover() {
        Instrutor sel = tableInstrutores.getSelectionModel().getSelectedItem();
        if (sel == null) {
            showAlert("Selecione um instrutor");
            return;
        }
        ServiceFacade.instrutorRepo.removerInstrutor(sel.getCpf());
        refresh();
    }

    private void showAlert(String txt) {
        new Alert(Alert.AlertType.INFORMATION, txt, ButtonType.OK).showAndWait();
    }
}
