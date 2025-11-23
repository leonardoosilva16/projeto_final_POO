package com.academia.controller;

import com.academia.Repository.InstrutorRepository;
import com.academia.model.pessoa.Instrutor;
import com.academia.services.singleton.CatracaVirtual;
import com.academia.Repository.MembroRepository;
import com.academia.model.pessoa.Membro;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

public class CatracaController {

    private CatracaVirtual catraca = CatracaVirtual.getInstancia();
    private MembroRepository membroRepository = new MembroRepository();
    private InstrutorRepository instrutorRepository = new InstrutorRepository();

    @FXML
    private TextField campoCpf;
    @FXML
    private Label labelStatus;

    @FXML
    public void initialize() {
        labelStatus.setText("Aguardando verificação...");
        labelStatus.setTextFill(Color.DARKSLATEGRAY);
    }

    @FXML
    private void handleVerificarAcesso() {
        String cpf = campoCpf.getText();
        if (cpf == null || cpf.trim().isEmpty()) {
            labelStatus.setText("Por favor, digite um CPF.");
            labelStatus.setTextFill(Color.RED);
            return;
        }

        Membro membro = membroRepository.procurarMembro(cpf);

        if (membro != null) {
            boolean liberado = catraca.liberarAcesso(membro);
            atualizarTela(liberado, "Membro: " + membro.getNome(), "Matrícula Vencida!");
            return;
        }

        Instrutor instrutor = instrutorRepository.procurarInstrutor(cpf);

        if (instrutor != null) {
            boolean liberado = catraca.liberarAcesso(instrutor);
            atualizarTela(liberado, "Instrutor: " + instrutor.getNome(), "Acesso Negado");
            return;
        }

        labelStatus.setText("CPF não encontrado no sistema.");
        labelStatus.setTextFill(Color.RED);
    }

    private void atualizarTela(boolean liberado, String msgSucesso, String msgErro) {
        if (liberado) {
            labelStatus.setText("BEM-VINDO! " + msgSucesso);
            labelStatus.setTextFill(Color.GREEN);
        } else {
            labelStatus.setText("BLOQUEADO: " + msgErro);
            labelStatus.setTextFill(Color.RED);
        }
    }
}
