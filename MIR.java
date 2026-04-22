/*
O método recebeMicroInstrucao deve ser chamado por ciclo de clock, buscando a instrução (de 32 bits) na memória ROM 
e decodificando-a para um funcionamento mais limpo do resto do sistema.
Depois disso, os métodos "envia" devem ser chamados para entregar seus valores para os outros componentes do processador, 
*/

public class MIR {

    private int addr;
    private int busA;
    private int busB;
    private int busC;
    private boolean enc;
    private boolean wr;
    private boolean rd;
    private boolean MAR;
    private boolean MBR;
    private int sh;
    private int alu;
    private int cond;
    private boolean amux;

    public void recebeMicroicroInstrucao(int microInstrucao) {
        
        this.addr = microInstrucao & 0xFF; 

        this.busA = (microInstrucao >> 8) & 0xF;
        this.busB = (microInstrucao >> 12) & 0xF;
        this.busC = (microInstrucao >> 16) & 0xF;

        this.enc = ((microInstrucao >> 20) & 1) == 1;
        this.wr  = ((microInstrucao >> 21) & 1) == 1;
        this.rd  = ((microInstrucao >> 22) & 1) == 1;
        this.MAR = ((microInstrucao >> 23) & 1) == 1;
        this.MBR = ((microInstrucao >> 24) & 1) == 1;

        this.sh   = (microInstrucao >> 25) & 0x3;
        this.alu  = (microInstrucao >> 27) & 0x3;
        this.cond = (microInstrucao >> 29) & 0x3;

        this.amux = ((microInstrucao >> 31) & 1) == 1;

    }

    public int enviaADDR() { 
        return this.addr;
    }

    public int enviaBusA() { 
        return this.busA;
    }

    public int enviaBusB() { 
        return this.busB;
    }

    public int enviaBusC() { 
        return this.busC;
    }

    public boolean enviaENC() { 
        return this.enc;
    }

    public boolean enviaWR() { 
        return this.wr;
    }

    public boolean enviaRD() { 
        return this.rd;
    }

    public boolean enviaMAR() { 
        return this.MAR;
    }

    public boolean enviaMBR() { 
        return this.MBR;
    }

    public int enviaSH() { 
        return this.sh;
    }

    public int enviaALU() { 
        return this.alu;
    }

    public int enviaCOND() { 
        return this.cond;
    }

    public boolean enviaAMUX() { 
        return this.amux;
    }
}
