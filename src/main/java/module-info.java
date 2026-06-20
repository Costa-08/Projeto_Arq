module com.example.trabalhoarqmic1javafx {
    requires javafx.controls;
    requires javafx.fxml;

    opens simulador.mic1 to javafx.fxml;
    exports simulador.mic1;
    
    opens simulador.projecao to javafx.graphics, javafx.fxml;
    exports simulador.projecao;

    opens simulador.projecao.microinstrucoes to javafx.fxml;
    exports simulador.projecao.microinstrucoes;
}