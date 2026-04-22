public class Amux {

    private boolean sinaisControle;
    private int valueLatchA;
    private int valueMBR;
    private int saida;

    public void recebeSinalControle(boolean sinal){
        this.sinaisControle=sinal;
    }

    public void recebeLatchA(int valor){
        this.valueLatchA=valor;
    }

    public void recebeMBR(int valor){
        this.valueMBR=valor;
    }

    private void selecionaSaida(){
        if (sinaisControle){
            saida=valueMBR;
        }
        else{
            saida=valueLatchA;
        }
    }

    public void enviaValor(ULA ula){
        selecionaSaida();
        ula.recebeAmux(saida);
    }

}
