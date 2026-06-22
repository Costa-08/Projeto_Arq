package simulador.projecao.microinstrucoes;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import simulador.mic1.Mic1;

public class SimulacaoMicrocodigo {

    @FXML private Label cicloClock;
    @FXML private TableView<LinhaTabela> tabelaDados;
    @FXML private TableColumn<LinhaTabela, String> colunaNome;
    @FXML private TableColumn<LinhaTabela, String> colunaValor;
    
    // Novos componentes injetados do FXML
    @FXML private Button botaoAvancar;
    @FXML private Button botaoVoltar;
    @FXML private TextArea logMicroinstrucoes;

    private ObservableList<LinhaTabela> listaElementos = FXCollections.observableArrayList();
    private int contadorCiclos = 0;
    private Mic1 motorSimulador;

    @FXML
    public void initialize() {
        String [] nomesRegs = "PC AC SP IR TIR 0 +1 -1 AMASK SMASK A B C D E F".split(" ");

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        
        listaElementos.clear(); 
        for (int i = 0; i < 16; i++) {
            listaElementos.add(new LinhaTabela("Registrador [" + nomesRegs[i] + "]", "0"));
        }
        listaElementos.add(new LinhaTabela("MAR", "0"));
        listaElementos.add(new LinhaTabela("MBR", "0"));
        tabelaDados.setItems(listaElementos);
    }

    public void exibir() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/nanoprocessos.fxml"));
            loader.setController(this);
            Parent raiz = loader.load();
            Scene cena = new Scene(raiz, 820, 600);
            Stage novoPalco = new Stage();
            novoPalco.setTitle("Simulação do Microcódigo");
            novoPalco.setScene(cena);
            novoPalco.show(); 
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Erro ao carregar a tela de Microcódigo.");
        }
    }

    // Configura o modo de controle da simulação
    public void configurarModo(boolean manual, Mic1 motor) {
        this.motorSimulador = motor;
        this.contadorCiclos = 0;
        if (logMicroinstrucoes != null) logMicroinstrucoes.clear();
        
        if (manual && botaoAvancar != null) {
            botaoAvancar.setVisible(true);
            botaoAvancar.setDisable(false);
        }
        if (botaoVoltar != null) {
            botaoVoltar.setVisible(false);
        }
    }

    // Faz aparecer o botão de saída quando o código acaba
    public void finalizarSimulacao() {
        if (botaoAvancar != null) botaoAvancar.setDisable(true);
        if (botaoVoltar != null) botaoVoltar.setVisible(true);
    }

    // Ação executada ao clicar em "Avançar 1 Ciclo"
    @FXML
    private void avancarCicloManual() {
        if (motorSimulador != null && !motorSimulador.acabou()) {
            motorSimulador.ciclo();
            atualizarTabela(motorSimulador);
            
            if (motorSimulador.acabou()) {
                finalizarSimulacao();
            }
        }
    }

    // Ação executada ao clicar em "Rodar outro código"
    @FXML
    private void voltarTelaInicial() {
        Stage stage = (Stage) botaoVoltar.getScene().getWindow();
        stage.close(); // Fecha a janela atual e volta nativamente para a inicial
    }

    public void atualizarTabela(Mic1 motor) {
        if (listaElementos.isEmpty() || listaElementos.size() < 18) return; 

        contadorCiclos++;
        if (cicloClock != null) {
            cicloClock.setText("Ciclo de Clock Atual: " + contadorCiclos);
        }

        // PRINTA A INFORMAÇÃO REAL DO PROCESSADOR NO LOG LATERAL
        if (logMicroinstrucoes != null) {
            int pcAtual = motor.getValorReg(0); 
            int mbrAtual = motor.getValorMBR(); 
            //String statusCache = motor.getStatusCache(); // Puxa o aviso da cache
            
            // Monta o texto base do ciclo
            StringBuilder logTexto = new StringBuilder();
            logTexto.append(String.format("Ciclo %d: %s \n PC: %d | MBR: %d\n", contadorCiclos, motorSimulador.stringMMI(), pcAtual, mbrAtual));
            
            // Se a cache trabalhou neste ciclo, adiciona no painel!
            // if (statusCache != null && !statusCache.isEmpty()) {
            //     logTexto.append(" | CACHE: ").append(statusCache);
            // }
            
            logTexto.append("\n"); // Pula de linha no final
            
            logMicroinstrucoes.appendText(logTexto.toString());
            logMicroinstrucoes.setScrollTop(Double.MAX_VALUE); // Rola para o fim automaticamente
        }

        for (int i = 0; i < 16; i++) {
            listaElementos.get(i).setValor(String.valueOf(motor.getValorReg(i)));
        }
        listaElementos.get(16).setValor(String.valueOf(motor.getValorMAR()));
        listaElementos.get(17).setValor(String.valueOf(motor.getValorMBR()));
        tabelaDados.refresh();
    }
}