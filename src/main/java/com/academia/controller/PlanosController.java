package com.academia.controller;

import com.academia.Repository.PlanoRepository;
import com.academia.model.plano.Plano;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class PlanosController {

    private PlanoRepository planoRepository = new PlanoRepository();

    @FXML
    private TableView<Plano> tabelaPlanos;
    @FXML
    private TableColumn<Plano, String> colunaNome;
    @FXML
    private TableColumn<Plano, Double> colunaPreco;

    @FXML
    public void initialize() {
        // Configura colunas
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaPreco.setCellValueFactory(new PropertyValueFactory<>("preco"));

        // Carrega a lista (que agora vem fixa do Repositório)
        carregarPlanosNaTabela();
    }

    private void carregarPlanosNaTabela() {
        ObservableList<Plano> planos = FXCollections.observableArrayList(
                planoRepository.listarPlanos()
        );
        tabelaPlanos.setItems(planos);
    }
}