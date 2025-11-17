package com.academia.frontend.controller;

import com.academia.frontend.service.ServiceFacade;
import com.academia.model.plano.Plano;
import com.academia.model.plano.PlanoMusculacao;
import com.academia.model.plano.PlanoPremium;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.util.List;
import java.util.Optional;
import javafx.collections.FXCollections;

public class PlanosController {
    @FXML private ListView<Plano> listPlanos;

    @FXML
    public void initialize() {
        refresh();
    }

    private void refresh() {
        List<Plano> p = ServiceFacade.planoRepo.listarPlanos();
        listPlanos.setItems(FXCollections.observableArrayList(p));
    }

    @FXML
    private void onNovo() {
        ChoiceDialog<String> dialog = new ChoiceDialog<>("Plano Musculação", "Plano Musculação", "Plano Premium");
        dialog.setTitle("Adicionar Plano");
        dialog.setHeaderText("Escolha um tipo de plano");
        Optional<String> chosen = dialog.showAndWait();
        chosen.ifPresent(s -> {
            if (s.equals("Plano Musculação")) {
                ServiceFacade.planoRepo.salvar(new PlanoMusculacao());
            } else {
                ServiceFacade.planoRepo.salvar(new PlanoPremium());
            }
            refresh();
        });
    }
}
