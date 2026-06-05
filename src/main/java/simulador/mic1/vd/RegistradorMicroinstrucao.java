package simulador.mic1.vd;
/*
O método recebeMicroInstrucao deve ser chamado por ciclo de clock, buscando a instrução (de 32 bits) na memória ROM 
e decodificando-a para um funcionamento mais limpo do resto do sistema.
Depois disso, os métodos "envia" devem ser chamados para entregar seus valores para os outros componentes do processador, 
*/

import simulador.mic1.uc.LogicaMicroSequencia;
import simulador.mic1.uc.MicroMultiplexador;

public class RegistradorMicroinstrucao {
    private long microsInstrucao;
    private int addr;
    private int busA;
    private int busB;
    private int busC;
    private boolean enc;
    private int wr;
    private int rd;
    private boolean MAR;
    private int MBR;
    private int sh;
    private int alu;
    private int cond;
    private boolean amux;

    private final Decodificador decA;
    private final Decodificador decB;
    private final Decodificador decC;
    private final RegistradorEnderecoMemoria mar;
    private final RegistradorBufferMemoria mbr;
    private final Shifter shift;
    private final UnidadeLogicoAritmetica ula;
    private final Amultiplexador amultix;
    private MicroMultiplexador mmux;
    private LogicaMicroSequencia logms;

    public RegistradorMicroinstrucao(Decodificador decA, Decodificador decB, Decodificador decC, RegistradorEnderecoMemoria mar, 
                                    RegistradorBufferMemoria mbr, Shifter shift, UnidadeLogicoAritmetica ula, Amultiplexador amultix){
        this.decA=decA;
        this.decB=decB;
        this.decC=decC;
        this.mar=mar;
        this.mbr=mbr;
        this.shift=shift;
        this.ula=ula;
        this.amultix=amultix;
    } 

    public void setMMux(MicroMultiplexador mux){
        this.mmux=mux;
    }

    public void setLogica(LogicaMicroSequencia log){
        this.logms=log;
    }

    public void printValorMicroinst(){
        System.out.println("Valor da microinstrucao: "+Long.toBinaryString(microsInstrucao));
    }

    public void recebeMicroinstrucao(long microInstrucao) {
       
        this.microsInstrucao=microInstrucao;
        
        this.addr = (int) microInstrucao & 0xFF; 

        this.busA = (int)(microInstrucao >> 8) & 0xF;
        this.busB = (int)(microInstrucao >> 12) & 0xF;
        this.busC = (int)(microInstrucao >> 16) & 0xF;

        this.enc = ((microInstrucao >> 20) & 1) == 1;
        this.wr  = (int)((microInstrucao >> 21) & 1);
        this.rd  = (int)((microInstrucao >> 22) & 1);
        this.MAR = (int)((microInstrucao >> 23) & 1) == 1;
        this.MBR = (int)((microInstrucao >> 24) & 1);

        this.sh   = (int)(microInstrucao >> 25) & 0x3;
        this.alu  = (int)(microInstrucao >> 27) & 0x3;
        this.cond = (int)(microInstrucao >> 29) & 0x3;

        this.amux = ((microInstrucao >> 31) & 1) == 1;
    
    }

    private void enviaBusA() { 
        decA.recebeSinalAbus(busA);
    }

    private void enviaBusB() { 
        decB.recebeSinalBbus(busB);
    }

    private void enviaBusCandENC() { 
        decC.recebeSinaisCbus(busC, enc);
    }

    private void enviaMAR() { 
        mar.recebeSinalControle(this.MAR);
    }

    private void enviatudoMBR() { 
        mbr.recebeSinaisMIR(MBR, rd, wr);
    }

    private void enviaSH() { 
        shift.recebeSinaisControle(this.sh);
    }

    private void enviaULA() { 
        ula.recebeSinalControle(this.alu);
    }

    private void enviaAMUX() { 
        amultix.recebeSinalControle(this.amux);
    }

    /*

    Parte que ainda precisa fazer: 

    */

    private void enviaCOND() { 
        logms.recebeSinalControle(cond);
    }

    private void enviaADDR() { 
        mmux.recebeSinalControle(addr);
    }

    public void enviaSinaisControle(){
        enviaBusA();
        enviaBusB();
        enviaBusCandENC();
        enviaMAR();
        enviatudoMBR();
        enviaSH();
        enviaULA();
        enviaAMUX();
        enviaCOND();
        enviaADDR();
    }
}
