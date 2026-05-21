package simulador.mic1.uc;

import simulador.mic1.vd.RegistradorMicroinstrucao;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class ControlStore {
    private long[] lista = new long[256];
    private RegistradorMicroinstrucao mir;

    public ControlStore(RegistradorMicroinstrucao mir){
        
        this.mir=mir;

        File arquivo = new File("microinstructions.txt");
        int i = 0;
        try (Scanner scan = new Scanner(arquivo)){
            while (scan.hasNextLine()){
                lista[i]=scan.nextLong(2);
                i++; 
            }
        } catch(FileNotFoundException e){
            System.out.println("Arquivo não encontrado");
        }

        for (int j = i; j<256; j++){
            lista[j]=0;
        }

    }

    public void recebeMPC(int valor){
        enviaSinalMIR(lista[valor]);
    }

    private void enviaSinalMIR(long microinst){
        mir.recebeMicroinstrucao(microinst);
    }

}
