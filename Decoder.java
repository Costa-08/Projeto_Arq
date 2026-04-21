public class Decoder {
    /*
    
    A classe não é muito útil haja vista a implementação dos registradores como um array, tornando a identificação deles
    o próprio int retirado do MIR, mas talvez seja importante para a parte gráfica.
    
    */
    
    public static int decodificar(int valor){
        return valor & 0x000F;
    }
    
}
