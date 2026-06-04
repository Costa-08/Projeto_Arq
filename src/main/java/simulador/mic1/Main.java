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

public class Main {
    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) {
        
        
        // Inicialização dos objetos do processador

        MemoriaPrincipal memP = new MemoriaPrincipal();

        Barramento busA = new Barramento();
        Barramento busB = new Barramento();
        Barramento busC = new Barramento();

        Registrador[] registradores = new Registrador[16];

        for (int i=0; i<16; i++){
            registradores[i]=new Registrador(busA, busB, busC);
        }
        
        registradores[2].recebeValor(2048);
        registradores[5].recebeValor(0);
        registradores[6].recebeValor(1);
        registradores[7].recebeValor(-1);
        registradores[8].recebeValor(0x0FFF);
        registradores[9].recebeValor(0x00FF);

        Decodificador decA = new Decodificador(registradores); 
        Decodificador decB = new Decodificador(registradores); 
        Decodificador decC = new Decodificador(registradores);

        LatchA latchA = new LatchA(busA);
        LatchB latchB = new LatchB(busB);

        Shifter shifter = new Shifter(busC);

        UnidadeLogicoAritmetica ULA = new UnidadeLogicoAritmetica(shifter);
        latchB.setUnidadeLogicoAritmetica(ULA);

        Amultiplexador Amux = new Amultiplexador(ULA);
        latchA.setAmultiplexador(Amux);

        RegistradorEnderecoMemoria mar = new RegistradorEnderecoMemoria(memP, latchB);

        RegistradorBufferMemoria mbr = new RegistradorBufferMemoria(memP, busC, Amux);

        RegistradorMicroinstrucao mir = new RegistradorMicroinstrucao(decA, decB, decC, mar, mbr, shifter, ULA, Amux);
        
        ControlStore microMemoria = new ControlStore(mir);

        ContadorMicroPrograma mpc = new ContadorMicroPrograma(microMemoria);

        MicroMultiplexador mmux = new MicroMultiplexador(mpc);

        mir.setMMux(mmux);

        LogicaMicroSequencia logMicroSequencia = new LogicaMicroSequencia(mmux);

        ULA.setLogica(logMicroSequencia);
        mir.setLogica(logMicroSequencia);

        Incrementador inc = new Incrementador(mmux);

        mpc.setIncrementador(inc);


        printRegs(registradores, mar, mbr);
        
        // Iniciando as macroinstrucoes do terminal na mp

        Scanner scan = new Scanner(System.in);
        System.out.print("Insira a quantidade de macroinstrucoes: ");
        int numMacroinstrucoes = scan.nextInt();
        scan.nextLine();
        if (numMacroinstrucoes<=2000){
            for (int i=0; i<numMacroinstrucoes; i++){
                System.out.print("Insira a macroinstrucao: ");
                String microinstrucao=scan.nextLine();
                int microinstrucaoreal = Integer.parseInt(microinstrucao, 2);
                memP.preencheMP(i, microinstrucaoreal);
            }
        }else{
            System.out.println("Macroinstruções demais para a memória principal");
        }

        while ((registradores[0].getValor()!=numMacroinstrucoes+1)){
            /*
            Subciclo 1, mir recebe seus bits e os sinais de controle são enviados para todos os 
            componentes devidos, e os dados dos registradores selecionados vão para os barramentos A e B
            */

            //System.out.println("\n\n");
            //printRegs(registradores);
            //mar.printValorMAR();
            //mbr.printValorMBR();
            //System.out.println("\n\n"); //debug

            mpc.recebeClock();
            //mir.printValorMicroinst();//debug            
            mir.enviaSinaisControle();        

            /*
            Subciclo 2, os latches capturam os dados dos barramentos A e B, enviando-os para o amux, a ula e o mar
            o mar também recebe clock e dependendo do sinal de controle recebido, guarda ou não o valor do latch B
            */
            latchB.recebeClock();
            latchA.recebeClock();
            mar.recebeClock();


            /*
            Subciclo 3, os dados descem a via de dados com as operações sendo realizadas
            a Micro-Sequence logic coletaria as flags da ula, mas ainda não a implementamos
            */

            // subciclo 4

            decC.recebeClock();
            mbr.recebeClock();
            
        }

        
        // printar resultados e estados para verificação

        printRegs(registradores, mar, mbr);
        scan.close();
    }

    private static void printRegs(Registrador[] registradores, RegistradorEnderecoMemoria mar, RegistradorBufferMemoria mbr){
        // Para analisar a situação do código
        for (int i=0; i<16; i++){
            System.out.printf("Valor no registrador[%d]: %d \n", i, registradores[i].getValor());
        }
        mar.printValorMAR();
        mbr.printValorMBR();
    }

}
