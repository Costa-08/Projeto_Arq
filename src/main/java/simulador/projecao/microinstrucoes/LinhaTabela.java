package simulador.projecao.microinstrucoes;

import javafx.beans.property.SimpleStringProperty;

public class LinhaTabela {
    private final SimpleStringProperty nome;
    private final SimpleStringProperty valor;

    public LinhaTabela(String nome, String valor) {
        this.nome = new SimpleStringProperty(nome);
        this.valor = new SimpleStringProperty(valor);
    }

    // O JavaFX obrigatoriamente usa estes getters para extrair os dados
    public String getNome() { 
        return nome.get(); 
    }

    public String getValor() { 
        return valor.get(); 
    }

    // Método setter para atualizar o valor dinamicamente
    public void setValor(String novoValor) { 
        this.valor.set(novoValor); 
    }
}
