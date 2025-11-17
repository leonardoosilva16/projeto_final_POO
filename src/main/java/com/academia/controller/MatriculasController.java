package com.academia.frontend.controller;

import com.academia.frontend.service.ServiceFacade;
import com.academia.model.Matricula;
import com.academia.model.pessoa.Membro;
import com.academia.model.plano.Plano;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Optional;

public class MatriculasController {

    @FXML private ComboBox<Membro> cbMembros;
    @FXML private ComboBox<Plano> cbPlanos;
    @FXML private Button btnVincular;
    @FXML private Button btnRenovar;
    @FXML private TextArea outputArea;

    @FXML
    public void initialize() {
        refresh();
    }

    private void refresh() {
        List<Membro> membros = ServiceFacade.membroRepo.listarMembros();
        cbMembros.setItems(FXCollections.observableArrayList(membros));

        List<Plano> planos = ServiceFacade.planoRepo.listarPlanos();
        cbPlanos.setItems(FXCollections.observableArrayList(planos));
    }

    @FXML
    private void onVincular() {
        Membro m = cbMembros.getSelectionModel().getSelectedItem();
        Plano p = cbPlanos.getSelectionModel().getSelectedItem();
        if (m == null || p == null) {
            outputArea.setText("Selecione membro e plano.");
            return;
        }
        // use atualizar to set new Matricula
        ServiceFacade.membroRepo.atualizar(m.getCpf(), null, p);
        outputArea.setText("Matrícula vinculada: " + m.getNome() + " -> " + p.getNome());
    }

    @FXML
    private void onRenovar() {
        Membro m = cbMembros.getSelectionModel().getSelectedItem();
        if (m == null) {
            outputArea.setText("Selecione um membro para renovar.");
            return;
        }
        ServiceFacade.membroRepo.renovarMatricula(m.getCpf());
        outputArea.setText("Matrícula renovada (se existia) para: " + m.getNome());
    }
}
