public class Barramento {
    
    private static void animacao(){

    // adicionar animações de movimento de dados aqui

    }
    
    
    public void enviar(int valor, Registrador destino){


        animacao();


        destino.setValor(valor);
    }

    public void enviar(int valor, Latch destino){


        animacao();


        destino.setValor(valor);
    }    



}
