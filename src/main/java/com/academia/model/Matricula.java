package com.academia.model;

import com.academia.model.plano.Plano;

import java.util.Calendar;
import java.util.Date;

public class Matricula {
    private Plano plano;
    private Date dataMatricula;
    private Date dataVencimento;

    public Matricula() {}

    public Matricula(Plano plano) {
        this.plano = plano;
        this.dataMatricula = new Date(); //pegua a data de hoje

        //define a data que a data de vencimento é após 30 dias
        Calendar c = Calendar.getInstance();
        c.setTime(this.dataMatricula);
        c.add(Calendar.DAY_OF_MONTH, 30);
        this.dataVencimento = c.getTime();
    }

    public Plano getPlano() {
        return plano;
    }
    public Date getDataVencimento() {
        return dataVencimento;
    }
    public void setPlano(Plano plano) {
        this.plano = plano;
    }

    //pensei em colocar uma classe pra a Renovação da Matrícula
    public void renovar() {
        Calendar c = Calendar.getInstance();
        c.setTime(new Date());
        c.add(Calendar.DAY_OF_MONTH, 30);
        this.dataVencimento = c.getTime();
        System.out.println("💪 Matrícula renovada! Novo vencimento: " + dataVencimento);
    }

    //vai ser usado na classe da Catraca para determianr quais matriculas estão vencidas
    public boolean isAtiva() {
        return new Date().before(this.dataVencimento);
    }
}
