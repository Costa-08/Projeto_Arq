package simulador.mic1.vd;

import simulador.mic1.memoriaprincipal.MemoriaPrincipal;

public class RegistradorBufferMemoria {
    
    private int valor;

    private final MemoriaPrincipal mp;
    private final Barramento busC;
    private final Amultiplexador amux;


    private int somaSinais;


    public RegistradorBufferMemoria(MemoriaPrincipal memory, Barramento bus, Amultiplexador mx){
        this.mp=memory;
        this.busC=bus;
        this.amux=mx;
    }

    public void recebeValor(int novoValor){
        this.valor = novoValor;
    }

    public void recebeSinaisMIR(int mbr, int rd, int wr){
        // Preferi deixar em int ao invés de boolean para fazer conta e poder usar um switch na recebe clock
        this.somaSinais = mbr*4+rd*2+wr;
        /*
        000 - 0 - idle
        001 - 1 - write
        010 - 2 - read
        100 - 4 - copy cbus
        else: error
        */
    }

    public void escrita(){
        mp.store(this.valor);
    }

    public void leitura(){
        this.valor=mp.load();
    }   

    public void receberCBus(){
        this.valor=busC.getConteudo();
    }

    public void recebeClock(){ // final do subciclo4
        switch (this.somaSinais){
            case 0 -> {}
            case 1 -> escrita();
            case 2 -> leitura();
            case 4 -> receberCBus();
            case 5 -> receberCBus(); // ignora o wr dessa microinstrução e pega o da próxima
            case 6 -> {
                escrita();
                receberCBus();
            }
            default -> System.out.println("Erro no recebeClock do MBR");
        }
        amux.recebeMBR(this.valor);

    }

    public void printValorMBR(){
        System.out.println("Valor MBR:"+this.valor);
    }
}
