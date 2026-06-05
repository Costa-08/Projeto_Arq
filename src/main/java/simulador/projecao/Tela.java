package simulador.projecao;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Tela extends Application {

    @FXML
    private Pane caixaSecundaria;

    @FXML
    private TextField quantidadeInstrucoes;

    @FXML
    private TextArea instrucoes;

    @FXML
    public void initialize() {
        
        quantidadeInstrucoes.layoutXProperty().bind(
            caixaSecundaria.widthProperty()
            .subtract(quantidadeInstrucoes.widthProperty())
            .divide(2)
        );

        quantidadeInstrucoes.setLayoutY(30);

        instrucoes.layoutXProperty().bind(
            caixaSecundaria.widthProperty().subtract(instrucoes.widthProperty()).divide(2)
        );

        // AGORA A MÁGICA: O 'Y' do TextArea vai ficar amarrado à base do TextField!
        instrucoes.layoutYProperty().bind(
            quantidadeInstrucoes.layoutYProperty()       // Pega a posição Y do de cima
                                .add(quantidadeInstrucoes.heightProperty()) // Soma com a altura do de cima
                                .add(20)                 // Adiciona 20 pixels de "margem" entre eles
        );

        caixaSecundaria.prefHeightProperty().bind(
            instrucoes.layoutYProperty()       // Pega onde a caixa de baixo começa
                    .add(instrucoes.heightProperty()) // Soma o tamanho que ela tem
                    .add(30)                 // Adiciona os 30px de chão
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