package simulador.mic1.memoria;

public class CacheL1 {
    private final MemoriaPrincipal mp;
    private int valorMAR;
    private class LinhaCache {
        boolean valid = false;
        int tag = -1;
        int[] data = new int[4];
    }
    private final LinhaCache[] linhas = new LinhaCache[16];
    public CacheL1(MemoriaPrincipal memoriaPrincipal) {
        this.mp = memoriaPrincipal;
        for (int i = 0; i < 16; i++) {
            linhas[i] = new LinhaCache();
        }
    }
    public void recebeValorMAR(int valor) {
        this.valorMAR = valor;
    }
    public int load() {
        int address = valorMAR & 0x7FF;
        int offset = address & 3;
        int index = (address >> 2) & 15;
        int tag = (address >> 6) & 31;
        LinhaCache linha = linhas[index];
        if (linha.valid && linha.tag == tag) {
            System.out.println("[CACHE] HIT no endereço " + address);
            return linha.data[offset];
        } else {
            System.out.println("[CACHE] MISS no endereço " + address + ". Carregando bloco...");
            int enderecoBaseBloco = address & ~3;
            for (int i = 0; i < 4; i++) {
                mp.recebeValorMAR(enderecoBaseBloco + i);
                linha.data[i] = mp.load();
            }
            linha.tag = tag;
            linha.valid = true;
            return linha.data[offset];
        }
    }
    public void store(int valor) {
        int address = valorMAR & 0x7FF;
        int offset = address & 3;
        int index = (address >> 2) & 15;
        int tag = (address >> 6) & 31;
        LinhaCache linha = linhas[index];
        if (linha.valid && linha.tag == tag) {
            linha.data[offset] = valor;
        }
        mp.recebeValorMAR(address);
        mp.store(valor);
    }
}
