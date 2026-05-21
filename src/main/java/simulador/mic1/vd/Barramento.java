package simulador.mic1.vd;

public class Barramento {
    
    private int conteudo;

    public void recebeValor(int valor){
        this.conteudo=valor;
    }

    public int getConteudo(){ // Fuga da nomenclatura usada no resto por ser uma fuga da lógica também, nesse caso os registradores
        return this.conteudo; // que podem vir e coletar o dado quando receberem o sinal de controle para tal
    }

}
