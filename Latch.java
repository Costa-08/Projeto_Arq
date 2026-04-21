public class Latch {
    
    
    /*
    
    Classe muito parecida com a de Registrador, se não for ajudar com a parte gráfica, talvez delete depois
    
    */


    private int valor;

    private static final int MASK = 0xFFFF;

    public void setValor(int novoValor){
        this.valor=novoValor&MASK;
    }

    public int getValor(){
        return this.valor;
    }


}
