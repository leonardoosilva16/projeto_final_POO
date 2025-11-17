package com.academia.frontend.controller;

import com.academia.frontend.service.ServiceFacade;
import com.academia.model.pessoa.Membro;
import com.academia.model.treino.FichaDeTreino;
import com.academia.services.templatemethod.MontadorDeFichaTreino;
import com.academia.services.templatemethod.MontadorFichaEmagrecimento;
import com.academia.services.templatemethod.MontadorFichaHipertrofia;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;

public class FichaTreinoController {

    @FXML private ComboBox<Membro> cbMembros;
    @FXML private ChoiceBox<String> choiceTipo;
    @FXML private Button btnGerar;
    @FXML private TextArea outputArea;

    @FXML
    public void initialize() {
        List<Membro> membros = ServiceFacade.membroRepo.listarMembros();
        cbMembros.setItems(FXCollections.observableArrayList(membros));

        choiceTipo.getItems().addAll("Hipertrofia", "Emagrecimento");
        choiceTipo.setValue("Hipertrofia");
    }

    @FXML
    private void onGerar() {
        Membro m = cbMembros.getSelectionModel().getSelectedItem();
        String tipo = choiceTipo.getValue();
        if (m == null) {
            outputArea.setText("Selecione um membro.");
            return;
        }
        MontadorDeFichaTreino montador;
        if ("Hipertrofia".equals(tipo)) {
            montador = new MontadorFichaHipertrofia();
        } else {
            montador = new MontadorFichaEmagrecimento();
        }
        FichaDeTreino ficha = montador.montar(m);
        m.setFichaDeTreino(ficha);
        outputArea.setText("Ficha gerada para " + m.getNome() + "\n\n" + ficha.toString());
    }
}
