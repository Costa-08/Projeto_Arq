/*
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


public class Main {
    public static void main(String[] args) {
        
        
        // Inicializando os registradores
        Registrador[] registradores = new Registrador[16];

        for (int i=0; i<16; i++){
            registradores[i]=new Registrador();
        }
        
        registradores[2].recebeValor(2048);
        registradores[5].recebeValor(0);
        registradores[6].recebeValor(1);
        registradores[7].recebeValor(-1);
        registradores[8].recebeValor(0x0FFF);
        registradores[9].recebeValor(0x00FF);
        
        //inicializando resto


        

    }
}
