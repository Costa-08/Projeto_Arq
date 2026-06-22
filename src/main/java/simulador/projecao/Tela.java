package simulador.projecao;

import simulador.mic1.Mic1;

import java.util.function.UnaryOperator;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import simulador.projecao.microinstrucoes.SimulacaoMicrocodigo;

public class Tela extends Application {

    private Mic1 mic1 = new Mic1();
    private SimulacaoMicrocodigo microcodigo = new SimulacaoMicrocodigo();
    
    // O RELÓGIO FOI MOVIDO PARA AQUI PARA PODER SER PARADO!
    private javafx.animation.Timeline relogioCentral;

    @FXML
    private Pane caixaSecundaria;

    @FXML
    private TextArea instrucoes;

    @FXML
    private Button botaoEnviarDados;

    @FXML
    private Button botaoControlarCiclos; 

    @FXML
    private void enviarDados() {
        String instrucoesText = this.instrucoes.getText().trim();

        if (!instrucoesText.isEmpty()) {
            String[] arrayInstrucoes = instrucoesText.split("\\R");

            // O SEGREDO: Cria um processador e uma tela novos e limpos a cada clique!
            this.mic1 = new Mic1();
            this.microcodigo = new SimulacaoMicrocodigo();

            this.mic1.preparaMemoria(arrayInstrucoes);
            this.microcodigo.exibir();
            this.microcodigo.configurarModo(false, this.mic1);

            if (relogioCentral != null) {
                relogioCentral.stop();
            }

            relogioCentral = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(500), evento -> {
                
                if (!this.mic1.acabou()) {
                    this.mic1.ciclo(); 
                    this.microcodigo.atualizarTabela(this.mic1); 
                } else {
                    System.out.println("Simulação concluída!");
                    relogioCentral.stop(); 
                    this.microcodigo.finalizarSimulacao(); 
                }
            }));

            relogioCentral.setCycleCount(javafx.animation.Animation.INDEFINITE);
            relogioCentral.play(); 
        } else {
            // SE A CAIXA ESTIVER VAZIA: Muda o texto de fundo e a cor para vermelho
            this.instrucoes.setStyle("-fx-prompt-text-fill: red;");
            this.instrucoes.setPromptText("digite alguma coisa");
        }
    }

    // --- MODO MANUAL ---
    @FXML
    private void enviarDadosManual() {
        String instrucoesText = this.instrucoes.getText().trim();

        if (!instrucoesText.isEmpty()) {
            String[] arrayInstrucoes = instrucoesText.split("\\R");

            if (relogioCentral != null) {
                relogioCentral.stop();
            }

            // O SEGREDO TAMBÉM AQUI: Zera tudo antes de rodar passo a passo!
            this.mic1 = new Mic1();
            this.microcodigo = new SimulacaoMicrocodigo();

            this.mic1.preparaMemoria(arrayInstrucoes);
            this.microcodigo.exibir();
            this.microcodigo.configurarModo(true, this.mic1);
        } else {
            // SE A CAIXA ESTIVER VAZIA: Muda o texto de fundo e a cor para vermelho
            this.instrucoes.setStyle("-fx-prompt-text-fill: red;");
            this.instrucoes.setPromptText("digite alguma coisa");
        }
    }

    @FXML
    public void initialize() {

        UnaryOperator<TextFormatter.Change> filtroNumeroQuatidade = change -> {
            if (change.getText().matches("[0-9]*")) {
                return change; 
            }
            return null; 
        };
    }

    @Override
    public void start(Stage palcoPrincipal) throws Exception {
        Parent raiz = FXMLLoader.load(getClass().getResource("/fxml/tela-inicial.fxml"));

        Scene cena = new Scene(raiz, 800, 600);

        palcoPrincipal.setTitle("Arquitetura Mic-1");
        palcoPrincipal.setScene(cena);
        palcoPrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}