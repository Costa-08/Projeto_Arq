public class Registrador {

    private int valor;

    private static final int MASK = 0xFFFF;

    /*
    
    No mic-1, registradores só suportam 16 bits, o que se encaixa no tipo primitivo short, porém é muito mais natural usar o tipo int do java.
    Por isso, resolvi tratar o valor interno do registrador como um inteiro e usar uma MASK para zerar qualquer bit além dos 16 permitidos e, assim,
    trabalhar como se fosse um valor de 16 bits, sem obrigar os outros desenvolvedores a trabalhar com o short.

    */

    public Registrador(){
        this.valor=0;
    }

    public void recebeValor(int novoValor){
        this.valor=novoValor&MASK;
    }

    public void enviaValor(Barramento bus){
        bus.recebeValor(valor);
    }



}
