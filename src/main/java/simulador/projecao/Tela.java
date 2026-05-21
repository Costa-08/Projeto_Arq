package simulador.projecao;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Tela extends Application {

    @Override
    public void start(Stage palcoPrincipal) {
        StackPane raiz = new StackPane();

        raiz.getChildren().add(new Label("tela!!!!!!!"));

        Scene cena = new Scene(raiz, 800, 600);

        palcoPrincipal.setTitle("Arquitetura Mic-1");
        palcoPrincipal.setScene(cena);
        palcoPrincipal.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}