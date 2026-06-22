package simulador.mic1;/*
Registradores:

0: PC - Program Counter
1: AC - Acumulator
2: SP - Stack Pointer
3: IR - Instruction Register
4: TIR - Temporary Instruction Register
5: 0 
6: +1
7: -1
8: AMASK - Adress Mask (Ox0FFF)
9: SMASK - Stack Mask (0x00FF)
10: A
11: B
12: C
13: D
14: E
15: F

*/

import java.util.ArrayList;

import simulador.mic1.memoria.CacheL1;
import simulador.mic1.memoria.MemoriaPrincipal;
import simulador.mic1.uc.ContadorMicroPrograma;
import simulador.mic1.uc.ControlStore;
import simulador.mic1.uc.Incrementador;
import simulador.mic1.uc.LogicaMicroSequencia;
import simulador.mic1.uc.MicroMultiplexador;
import simulador.mic1.vd.Amultiplexador;
import simulador.mic1.vd.Barramento;
import simulador.mic1.vd.Decodificador;
import simulador.mic1.vd.LatchA;
import simulador.mic1.vd.LatchB;
import simulador.mic1.vd.Registrador;
import simulador.mic1.vd.RegistradorBufferMemoria;
import simulador.mic1.vd.RegistradorEnderecoMemoria;
import simulador.mic1.vd.RegistradorMicroinstrucao;
import simulador.mic1.vd.Shifter;
import simulador.mic1.vd.UnidadeLogicoAritmetica;

public class Mic1 {

    @SuppressWarnings("ConvertToTryWithResources")

    int clock = 1;
    int numMacroinstrucoes;
    
    private MemoriaPrincipal memP;
    private CacheL1 cache;

    private Barramento busA;
    private Barramento busB;
    private Barramento busC;
    private Registrador[] registradores;

    private Decodificador decA;
    private Decodificador decB;
    private Decodificador decC;

    private LatchA latchA;
    private LatchB latchB;

    private Shifter shifter;

    private UnidadeLogicoAritmetica ULA;

    private Amultiplexador Amux;

    private RegistradorEnderecoMemoria mar;
    private RegistradorBufferMemoria mbr;

    private RegistradorMicroinstrucao mir;
    private ControlStore microMemoria;
    private ContadorMicroPrograma mpc;
    private MicroMultiplexador mmux;

    private LogicaMicroSequencia logMicroSequencia;

    private Incrementador inc;
    private Assembler assembler;

    public Mic1 () {

        this.memP = new MemoriaPrincipal();
        this.cache = new CacheL1(this.memP);
        this.busA = new Barramento();
        this.busB = new Barramento();
        this.busC = new Barramento();

        this.registradores = new Registrador[16];
        for (int i = 0; i < 16; i++) {
            this.registradores[i] = new Registrador(this.busA, this.busB, this.busC);
        }
        this.registradores[2].recebeValor(2048);
        this.registradores[5].recebeValor(0);
        this.registradores[6].recebeValor(1);
        this.registradores[7].recebeValor(-1);
        this.registradores[8].recebeValor(0x0FFF);
        this.registradores[9].recebeValor(0x00FF);

        this.decA = new Decodificador(this.registradores);
        this.decB = new Decodificador(this.registradores);
        this.decC = new Decodificador(this.registradores);

        this.latchA = new LatchA(this.busA);
        this.latchB = new LatchB(this.busB);

        this.shifter = new Shifter(this.busC);

        this.ULA = new UnidadeLogicoAritmetica(this.shifter);

        this.Amux = new Amultiplexador(this.ULA);

        this.mar = new RegistradorEnderecoMemoria(this.cache, this.latchB);
        this.mbr = new RegistradorBufferMemoria(this.cache, this.busC, this.Amux);

        this.mir = new RegistradorMicroinstrucao(this.decA, this.decB, this.decC, this.mar, this.mbr, this.shifter, this.ULA, this.Amux);
        this.microMemoria = new ControlStore(this.mir);

        this.mpc = new ContadorMicroPrograma(this.microMemoria);
        this.mmux = new MicroMultiplexador(this.mpc);

        this.logMicroSequencia = new LogicaMicroSequencia(this.mmux);

        this.inc = new Incrementador(this.mmux);


        this.latchB.setUnidadeLogicoAritmetica(ULA);

        this.latchA.setAmultiplexador(Amux);

        this.mir.setMMux(this.mmux);

        this.ULA.setLogica(this.logMicroSequencia);
        this.mir.setLogica(this.logMicroSequencia);

        this.mpc.setIncrementador(this.inc);

        this.assembler = new Assembler();

    }

    public boolean preparaMemoria(String[] arrayInstrucoes) {
        
        this.assembler.setCodigoUsuario(arrayInstrucoes);

        String statusCompilacao = this.assembler.compilar();
        
        if (!statusCompilacao.equals("CERTO")) {
            System.err.println("Erro na compilação: " + statusCompilacao);
            return false; // 2. Avisa a Tela que falhou
        }
        
        ArrayList<Integer> macroInstrucoes = assembler.getMacroInstrucoes();
        this.numMacroinstrucoes = assembler.getPosHalt();

        this.printRegs(registradores, mar, mbr);

        if (numMacroinstrucoes <= 2000){

            for (int i = 0; i < numMacroinstrucoes; i++){
                this.memP.preencheMP(i, macroInstrucoes.get(i));
            }
            
        }else{
            System.out.println("Macroinstruções demais para a memória principal");
            return false; // Avisa a Tela que falhou por falta de espaço
        }

        return true; // 3. Avisa a Tela que tudo deu certo!
    }

    public boolean acabou() {
        return this.registradores[0].getValor() == this.numMacroinstrucoes + 1;
    }

    public int getValorReg(int indice) {
        return this.registradores[indice].getValor();
    }

    public int getValorMAR() { 
        return this.mar.getValorMAR();
    }

    public int getValorMBR() {
        return this.mbr.getValorMBR();
    }

    public void ciclo(){
        /*
        Subciclo 1, mir recebe seus bits e os sinais de controle são enviados para todos os 
        componentes devidos, e os dados dos registradores selecionados vão para os barramentos A e B
        */

        // System.out.println("\n\n");
        // printRegs(registradores, mar, mbr);
        // mar.printValorMAR();
        // mbr.printValorMBR();
        // System.out.println("\n\n"); //debug

        //System.out.println("Valor do PC: " + registradores[0].getValor() + "\n");

        this.mpc.recebeClock();
        // mir.printValorMicroinst();//debug            
        this.mir.enviaSinaisControle();        

        /*
        Subciclo 2, os latches capturam os dados dos barramentos A e B, enviando-os para o amux, a ula e o mar
        o mar também recebe clock e dependendo do sinal de controle recebido, guarda ou não o valor do latch B
        */

        this.latchB.recebeClock();
        this.latchA.recebeClock();
        this.mar.recebeClock();


        /*
        Subciclo 3, os dados descem a via de dados com as operações sendo realizadas
        a Micro-Sequence logic coletaria as flags da ula, mas ainda não a implementamos
        */

        // subciclo 4
        this.decC.recebeClock();
        this.mbr.recebeClock();
    }


    private void printRegs(Registrador[] registradores, RegistradorEnderecoMemoria mar, RegistradorBufferMemoria mbr){
        
        int []valores = new int[16];
        for (int i=0; i<16; i++){
            System.out.printf("Valor no registrador[%d]: %d \n", i, registradores[i].getValor());
        }
        //this.mar.getValorMAR();
        //this.mbr.printValorMBR();
    }

}
