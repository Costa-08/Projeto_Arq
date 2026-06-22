package simulador.mic1.uc;

import java.io.InputStream;
import java.util.Scanner;

import simulador.mic1.vd.RegistradorMicroinstrucao;


public class ControlStore {
    private final long[] lista = new long[256];
    private final RegistradorMicroinstrucao mir;
    private int valor;

    public ControlStore(RegistradorMicroinstrucao mir){
        
        this.mir=mir;

        InputStream arq = ControlStore.class.getResourceAsStream("/mic1/microinstructions.txt");
        if (arq!=null){
            int i = 0;
            try (Scanner scan = new Scanner(arq)){
                while (scan.hasNextLine()){
                    lista[i]=scan.nextLong(2);
                    i++; 
                }
            }
            for (int j = i; j<256; j++){
            lista[j]=0;
        }
        }else{
            System.out.println("Erro ao abrir o arquivo");
        }
    }

    public void recebeMPC(int valor){
        this.valor=valor;
        enviaSinalMIR(lista[valor]);
    }

    public int getValor(){
        return this.valor;
    }

    private void enviaSinalMIR(long microinst){
        mir.recebeMicroinstrucao(microinst);
    }

}
