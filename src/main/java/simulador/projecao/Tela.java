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
    private TextField quantidadeInstrucoes;

    @FXML
    private TextArea instrucoes;

    @FXML
    private Button botaoEnviarDados;

    // --- MODO AUTOMÁTICO ---
    @FXML
    private void enviarDados() {
        String quantidadeInstrucoesText = this.quantidadeInstrucoes.getText();
        String instrucoesText = this.instrucoes.getText().trim();

        if (quantidadeInstrucoesText != null && !quantidadeInstrucoesText.isEmpty() && !instrucoesText.isEmpty()) {
            
            String[] arrayInstrucoes = instrucoesText.split("\\R");

            this.mic1.preparaMemoria(arrayInstrucoes);
            this.microcodigo.exibir();

            // Avisa a outra janela que estamos no modo Automático (esconde o botão de passar passo a passo)
            this.microcodigo.configurarModo(false, this.mic1);

            // Pára qualquer simulação anterior que pudesse estar a correr
            if (relogioCentral != null) {
                relogioCentral.stop();
            }

            relogioCentral = new javafx.animation.Timeline(
                new javafx.animation.KeyFrame(javafx.util.Duration.millis(500), evento -> { // Ajustado para 500ms
                
                if (!this.mic1.acabou()) {
                    
                    this.mic1.ciclo(); // Avança 1 ciclo
                    this.microcodigo.atualizarTabela(this.mic1); // Atualiza os dados na tela
                    
                } else {
                    System.out.println("Simulação concluída!");
                    relogioCentral.stop(); // TRAVÃO QUE ACABA COM O LOOP INFINITO
                    this.microcodigo.finalizarSimulacao(); // Faz aparecer o botão "Rodar outro código"
                }
            }));

            relogioCentral.setCycleCount(javafx.animation.Animation.INDEFINITE);
            relogioCentral.play(); 
        }
    }

    // --- MODO MANUAL (NOVO!) ---
    @FXML
    private void enviarDadosManual() {
        String quantidadeInstrucoesText = this.quantidadeInstrucoes.getText();
        String instrucoesText = this.instrucoes.getText().trim();

        if (quantidadeInstrucoesText != null && !quantidadeInstrucoesText.isEmpty() && !instrucoesText.isEmpty()) {
            String[] arrayInstrucoes = instrucoesText.split("\\R");

            if (relogioCentral != null) {
                relogioCentral.stop();
            }

            this.mic1.preparaMemoria(arrayInstrucoes);
            this.microcodigo.exibir();
            
            // Ativa o modo manual: exibe o botão "Avançar 1 Ciclo" no ecrã de Microcódigo
            this.microcodigo.configurarModo(true, this.mic1);
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

        quantidadeInstrucoes.setTextFormatter(new TextFormatter<>(filtroNumeroQuatidade));

        quantidadeInstrucoes.layoutXProperty().bind(
            caixaSecundaria.widthProperty()
            .subtract(quantidadeInstrucoes.widthProperty())
            .divide(2)
        );

        quantidadeInstrucoes.setLayoutY(30);

        instrucoes.layoutXProperty().bind(
            caixaSecundaria.widthProperty().subtract(instrucoes.widthProperty()).divide(2)
        );

        instrucoes.layoutYProperty().bind(
            quantidadeInstrucoes.layoutYProperty()       
                                .add(quantidadeInstrucoes.heightProperty()) 
                                .add(20)                 
        );

        botaoEnviarDados.layoutXProperty().bind(
            caixaSecundaria.widthProperty().subtract(botaoEnviarDados.widthProperty()).divide(2)
        );

        botaoEnviarDados.layoutYProperty().bind(
            instrucoes.layoutYProperty()
                .add(instrucoes.heightProperty())
                .add(20)
        );

        caixaSecundaria.prefHeightProperty().bind(
            instrucoes.layoutYProperty()       
                    .add(instrucoes.heightProperty()) 
                    .add(botaoEnviarDados.heightProperty()) 
                    .add(30)                 
        );

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