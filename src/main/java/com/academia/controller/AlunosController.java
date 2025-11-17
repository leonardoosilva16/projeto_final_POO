package com.academia.frontend.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;

public class DashboardController {

    @FXML private AnchorPane contentArea;

    @FXML
    public void initialize() {
        showMembros();
    }

    private void loadView(String path) {
        try {
            Parent pane = FXMLLoader.load(getClass().getResource(path));
            contentArea.getChildren().setAll(pane);
            AnchorPane.setTopAnchor(pane, 0.0);
            AnchorPane.setLeftAnchor(pane, 0.0);
            AnchorPane.setRightAnchor(pane, 0.0);
            AnchorPane.setBottomAnchor(pane, 0.0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML private void showMembros() { loadView("/fxml/MembrosView.fxml"); }
    @FXML private void showInstrutores() { loadView("/fxml/InstrutoresView.fxml"); }
    @FXML private void showPlanos() { loadView("/fxml/PlanosView.fxml"); }
    @FXML private void showMatriculas() { loadView("/fxml/MatriculasView.fxml"); }
    @FXML private void showFicha() { loadView("/fxml/FichaTreinoView.fxml"); }
    @FXML private void showCatraca() { loadView("/fxml/CatracaView.fxml"); }
}
