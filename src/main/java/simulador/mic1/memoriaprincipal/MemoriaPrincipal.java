package simulador.mic1.memoriaprincipal;

public class MemoriaPrincipal{

    private final int[] memoria = new int[2048];

    private static final int MASK = 0x7FF;

    private int valorMAR;

    public void preencheMP(int indice, int valor){
        memoria[indice]=valor;
    }

    public void printValoremX(int x ){
        System.out.println("Valor na memória em "+x+" : "+ memoria[x]);
    }

    public void recebeValorMAR(int valor){
        this.valorMAR=valor;
    }

    public void store(int valor){
        memoria[valorMAR&MASK]=valor;
    }

    public int load(){
        return memoria[valorMAR&MASK];
    }

}