public class MemoriaPrincipal{

    private final int[] memoria = new int[2048];

    private static final int MASK = 0x0FFF;

    public void store(int endereco, int valor){
        memoria[endereco&MASK]=valor;
    }

    public int load(int endereco){
        return memoria[endereco&MASK];
    }

}