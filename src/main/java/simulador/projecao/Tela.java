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

            this.mic1 = new Mic1();
            this.microcodigo = new SimulacaoMicrocodigo();

            // AQUI ESTÁ O SEGREDO: Testamos o retorno da Mic1
            boolean compilouCerto = this.mic1.preparaMemoria(arrayInstrucoes);

            if (compilouCerto) {
                // SÓ ABRE A TELA SE ESTIVER TUDO CERTO
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
                // SE DEU ERRO DE COMPILAÇÃO (MACRO CÓDIGO INVÁLIDO):
                
                String codigoErrado = this.instrucoes.getText(); // Guarda o que o usuário digitou
                this.instrucoes.setText(""); // Esvazia a caixa temporariamente
                
                // Formata o fundo com a mensagem vermelha
                this.instrucoes.setStyle("-fx-prompt-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold; -fx-transition: 0.5s;");
                this.instrucoes.setPromptText("Seu macrocódigo é inválido!");

                javafx.animation.PauseTransition temporizadorErro = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3));
                temporizadorErro.setOnFinished(eventoErro -> {
                    this.instrucoes.setStyle("");
                    this.instrucoes.setPromptText("Digite aqui as instruções");
                    this.instrucoes.setText(codigoErrado); // Devolve o código para o usuário consertar
                });
                temporizadorErro.play();
            }
        } else {
            // 1. Aumenta a fonte, pinta de vermelho e muda o texto
            this.instrucoes.setStyle("-fx-prompt-text-fill: white; -fx-font-size: 15px; -fx-transition: 0.5s;");

            // 2. Cria um cronômetro de exatos 5 segundos
            javafx.animation.PauseTransition temporizador = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));
            
            // 3. Define o que acontece quando os 5 segundos acabarem
            temporizador.setOnFinished(evento -> {
                this.instrucoes.setStyle(""); // Apaga o estilo vermelho/grande para o CSS original voltar
                this.instrucoes.setPromptText("Digite aqui as instruções");
            });
            
            // 4. Dá o "play" no cronômetro
            temporizador.play();
        }
    }

    @FXML
    private void enviarDadosManual() {
        String instrucoesText = this.instrucoes.getText().trim();

        if (!instrucoesText.isEmpty()) {
            String[] arrayInstrucoes = instrucoesText.split("\\R");

            this.mic1 = new Mic1();
            this.microcodigo = new SimulacaoMicrocodigo();

            boolean compilouCerto = this.mic1.preparaMemoria(arrayInstrucoes);

            if (compilouCerto) {
                // PARA O RELÓGIO CASO ELE ESTIVESSE RODANDO NO AUTOMÁTICO
                if (relogioCentral != null) {
                    relogioCentral.stop();
                }

                // ABRE A TELA E PASSA 'TRUE' PARA ATIVAR O MODO MANUAL
                this.microcodigo.exibir();
                this.microcodigo.configurarModo(true, this.mic1);

                // IMPORTANTE: Não recriamos o Timeline aqui. Quem vai avançar os ciclos 
                // agora é o botão "Avançar 1 Ciclo" lá da outra tela.

            } else {
                String codigoErrado = this.instrucoes.getText(); 
                this.instrucoes.setText(""); 
                
                this.instrucoes.setStyle("-fx-prompt-text-fill: white; -fx-font-size: 15px; -fx-font-weight: bold; -fx-transition: 0.5s;");
                this.instrucoes.setPromptText("Seu macrocódigo é inválido!");

                javafx.animation.PauseTransition temporizadorErro = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(3));
                temporizadorErro.setOnFinished(eventoErro -> {
                    this.instrucoes.setStyle("");
                    this.instrucoes.setPromptText("Digite aqui as instruções");
                    this.instrucoes.setText(codigoErrado); 
                });
                temporizadorErro.play();
            }
        } else {
            // AVISO DE CAIXA VAZIA
            this.instrucoes.setStyle("-fx-prompt-text-fill: white; -fx-font-size: 15px; -fx-transition: 0.5s;");
            this.instrucoes.setPromptText("digite alguma coisa"); // Isso tinha sumido no seu arquivo!

            javafx.animation.PauseTransition temporizador = new javafx.animation.PauseTransition(javafx.util.Duration.seconds(2));
            
            temporizador.setOnFinished(evento -> {
                this.instrucoes.setStyle(""); 
                this.instrucoes.setPromptText("Digite aqui as instruções");
            });
            
            temporizador.play();
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