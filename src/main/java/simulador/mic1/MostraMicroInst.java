package simulador.mic1;

import java.io.InputStream;
import java.util.Scanner;

import simulador.mic1.uc.ControlStore;

public class MostraMicroInst {
    
    private final ControlStore cs;
    private final String[] memoria;
    public MostraMicroInst(ControlStore cs){
        this.cs=cs;
        this.memoria = new String[256];

        InputStream arq = ControlStore.class.getResourceAsStream("/mic1/microinstructions2.txt");
        if (arq!=null){
            int i = 0;
            try (Scanner scan = new Scanner(arq)){
                while (scan.hasNextLine()){
                    memoria[i]=scan.nextLine();
                    i++; 
                }
            }
            for (int j = i; j<256; j++){
            memoria[j]=null;
        }
        }else{
            System.out.println("Erro ao abrir o arquivo");
        }
    }

    public String getMicroInst(){
        return memoria[this.cs.getValor()];
    }
}
