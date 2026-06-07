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

import java.util.Scanner;

import simulador.mic1.memoriaprincipal.MemoriaPrincipal;
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
    
    private MemoriaPrincipal memP;

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

    public Mic1 () {

        this.memP = new MemoriaPrincipal();
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

        this.mar = new RegistradorEnderecoMemoria(this.memP, this.latchB);
        this.mbr = new RegistradorBufferMemoria(this.memP, this.busC, this.Amux);

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

    }

    public void simular(int numMacroinstrucoes, String[] microinstrucao) {

        this.printRegs(registradores, mar, mbr);

        if (numMacroinstrucoes <= 2000){

            for (int i = 0; i < numMacroinstrucoes; i++){
                int microinstrucaoreal = Integer.parseInt(microinstrucao[i], 2);
                this.memP.preencheMP(i, microinstrucaoreal);

            }
        }else{
            System.out.println("Macroinstruções demais para a memória principal");
        }

        while ((this.registradores[0].getValor() != numMacroinstrucoes + 1)){
            this.ciclo();
        }

        // printar resultados e estados para verificação
        this.printRegs(registradores, mar, mbr);

        System.out.println("Sai do loop");
    }

    private void ciclo (){
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
        // Para analisar a situação do código
        for (int i=0; i<16; i++){
            System.out.printf("Valor no registrador[%d]: %d \n", i, registradores[i].getValor());
        }
        this.mar.printValorMAR();
        this.mbr.printValorMBR();
    }

}
