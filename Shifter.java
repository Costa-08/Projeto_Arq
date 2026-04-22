public class Shifter {
    

    private int valorULA;

    private int sinaisControle;

    private int saida;

    private static final int MASK = 0xFFFF;

    public void recebeULA(int valor){
        this.valorULA=valor;
    }

    public void recebeSinaisControle(int valor){
        this.sinaisControle=valor&3;
    }

    private void operacao(){
        switch (sinaisControle){
            case 0 -> saida = valorULA & MASK;
            case 1 -> saida = (valorULA << 1) & MASK;
            case 2 -> saida = (valorULA >> 1) & MASK;
            default -> System.out.println("sinal controle inválido no shifter");
        }
    }

    public void enviaSaida(Barramento cbus){
        operacao();
        cbus.recebeValor(saida);
    }

}
