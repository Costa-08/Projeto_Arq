package simulador.mic1.vd;

public class Decodificador {
    
    private int sinalControle;
    private boolean sinalENC;
    private final Registrador[] registradores;

    public Decodificador(Registrador[] listaRegistradores){
        this.registradores=listaRegistradores;
    }

    public void recebeSinalAbus(int sinal){
        this.sinalControle=sinal&0x000F;
        for (int i = 0; i<16; i++){
            if (i==sinalControle){
                registradores[i].recebeSinalControleA(true);
            }
            else{
                registradores[i].recebeSinalControleA(false);
            }
        }
    }
    public void recebeSinalBbus(int sinal){
        this.sinalControle=sinal&0x000F;
        for (int i = 0; i<16; i++){
            if (i==sinalControle){
                registradores[i].recebeSinalControleB(true);
            }
            else{
                registradores[i].recebeSinalControleB(false);
            }
        }
    }
    public void recebeSinaisCbus(int sinal, boolean sinalE){
        this.sinalControle=sinal&0x000F;
        this.sinalENC=sinalE;
    }

    public void recebeClock(){
        if (this.sinalENC){
            for (int i = 0; i<16; i++){
                if (i==this.sinalControle){
                    registradores[i].recebeSinalControleC(true);
                }
                else{
                    registradores[i].recebeSinalControleC(false);
                }
            }
        }
    }


}
