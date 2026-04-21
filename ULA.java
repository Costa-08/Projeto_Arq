/*

00: +
01: and
10: A
11: inv(A)

*/

public class ULA {
    
    private final Latch latch1;
    private final Latch latch2;

    public ULA(Latch latch1, Latch latch2){
        this.latch1=latch1;
        this.latch2=latch2;
    }



    private int sinalControle;

    public void setSinalControle(int novoValor){
        sinalControle=novoValor&3;
    }

    
    
    private static final int MASK = 0xFFFF;

    private int saida;
    private boolean Zflag;
    private boolean Nflag;
    
    public int getSaida(){
        return saida&MASK;
    }

    public boolean getZflag(){
        return Zflag;
    }

    public boolean getNflag(){
        return Nflag;
    }

    private void operacao(){
        
        int valorA=latch1.getValor();
        int valorB=latch2.getValor();
        int resultado;

        switch (sinalControle){
            case 0:
                resultado = (valorA+valorB)&MASK;
                break;
            case 1:
                resultado = (valorA&valorB);
                break;
            case 2:
                resultado = valorA;
                break;
            case 3:
                resultado = (~valorA)&MASK;
                break;
            default:
                System.out.println("Operação inválida na ULA");
                resultado=0;
                break;
        }

        this.saida=resultado;
        this.Zflag=(resultado==0);
        this.Nflag=(resultado<0);
    }

}
