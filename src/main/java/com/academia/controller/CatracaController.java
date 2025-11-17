package com.academia.frontend.controller;

import com.academia.frontend.service.ServiceFacade;
import com.academia.model.pessoa.Membro;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class CatracaController {

    @FXML private TextField cpfField;
    @FXML private TextArea outputArea;

    @FXML
    private void onLiberar() {
        String cpf = cpfField.getText();
        if (cpf == null || cpf.trim().isEmpty()) {
            outputArea.setText("Informe um CPF.");
            return;
        }
        Membro m = ServiceFacade.membroRepo.procurarMembro(cpf);
        if (m == null) {
            outputArea.setText("Membro não encontrado.");
            return;
        }
        boolean ok = ServiceFacade.catraca.liberarAcesso(m);
        outputArea.setText(ok ? "Acesso liberado para " + m.getNome() : "Acesso negado (matrícula inválida).");
    }
}
