package simulador.mic1.vd;

public class Amultiplexador {

    private boolean sinaisControle;
    private int valueLatchA;
    private int valueMBR;
    private int saida;
    private final UnidadeLogicoAritmetica ulaDestino;

    public Amultiplexador(UnidadeLogicoAritmetica ula){
        this.ulaDestino=ula;
    }

    public void recebeSinalControle(boolean sinal){
        this.sinaisControle=sinal;
    }

    public void recebeMBR(int valor){
        this.valueMBR=valor;
    }

    public void recebeLatchA(int valor){
        this.valueLatchA=valor;
        selecionaSaida();
        enviaValor();
        
    }

    private void selecionaSaida(){
        if (sinaisControle){
            saida=valueMBR;
        }
        else{

            saida=valueLatchA;
        }
    }

    public void enviaValor(){
        ulaDestino.recebeAmux(saida);
    }

}
