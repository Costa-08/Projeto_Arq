package simulador.mic1;

import java.util.ArrayList;
import java.util.HashMap;

public class Assembler {
    
    private ArrayList<String> codigoUsuario;      // Guarda o código original (ex: linha por linha)
    private ArrayList<Integer> macroInstrucoes;
    private final HashMap<String, Integer> tabelaTraducao;
    private HashMap<String, Integer> tabelaEnderecos;

    public int getPosHalt(){
        int i=0;
        for (Integer x : macroInstrucoes){
            if (x==0xFFFF) break;
            i++; 
        }
        return i;
    }

    public Assembler(){
        this.codigoUsuario = new ArrayList<>();
        this.macroInstrucoes = new ArrayList<>();
        this.tabelaTraducao = new HashMap<>();
        this.tabelaEnderecos = new HashMap<>();
        preencherTabelaTraducao();
    }

    public void setCodigoUsuario(String codigoCompleto) {
        
        if (codigoCompleto == null || codigoCompleto.trim().toUpperCase().isEmpty()) {
            return;
        }
        
        this.codigoUsuario.clear(); 

        String[] linhas = codigoCompleto.split("\\r?\\n");

        for (String linha : linhas) {
            this.codigoUsuario.add(linha.toUpperCase());
        }

    }
    
    public ArrayList<Integer> getMacroInstrucoes() {
        return this.macroInstrucoes;
    }

    private void preencherTabelaTraducao(){
        this.tabelaTraducao.put("LODD", 0);
        this.tabelaTraducao.put("STOD", 1<<12);
        this.tabelaTraducao.put("ADDD", 2<<12);
        this.tabelaTraducao.put("SUBD", 3<<12);
        this.tabelaTraducao.put("JPOS", 4<<12);
        this.tabelaTraducao.put("JZER", 5<<12);
        this.tabelaTraducao.put("JUMP", 6<<12);
        this.tabelaTraducao.put("LOCO", 7<<12);
        this.tabelaTraducao.put("LODL", 8<<12);
        this.tabelaTraducao.put("STOL", 9<<12);
        this.tabelaTraducao.put("ADDL", 10<<12);
        this.tabelaTraducao.put("SUBL", 11<<12);
        this.tabelaTraducao.put("JNEG", 12<<12);
        this.tabelaTraducao.put("JNZE", 13<<12);
        this.tabelaTraducao.put("CALL", 14<<12);
        this.tabelaTraducao.put("PSHI", 61440);
        this.tabelaTraducao.put("POPI", 61952);
        this.tabelaTraducao.put("PUSH", 62464);
        this.tabelaTraducao.put("POP", 62976);
        this.tabelaTraducao.put("RETN", 63488);
        this.tabelaTraducao.put("SWAP", 64000);
        this.tabelaTraducao.put("INSP", 252<<8);
        this.tabelaTraducao.put("DESP", 254<<8);
        this.tabelaTraducao.put("HALT", 0xFFFF);
    }


    public String compilar(){

        int endMemoria = 0;
        tabelaEnderecos.clear();
        for (String linhaOriginal : this.codigoUsuario) { //tratar inicialmente das labels passo 1
            
            String linhaSemComentario = linhaOriginal.split("//")[0].trim();
            if (linhaSemComentario.isEmpty()) continue;
            String[] linhaLimpa = linhaSemComentario.replaceAll("\\s+", " ").split(" ");
                        
            int tamanhoLinha = linhaLimpa.length;
            
            if (linhaLimpa[0].endsWith(":")){ // tem label

                if (tabelaEnderecos.containsKey(linhaLimpa[0])) return ("label "+linhaLimpa[0]+" repetida"); // label repetida
                else{
                    tabelaEnderecos.put(linhaLimpa[0], endMemoria);
                }

                if (tamanhoLinha>1){
                    endMemoria++;
                }

            } else{
                endMemoria++;
            }
            
        }

        ArrayList<Integer> codigo = new ArrayList<>();

        for (String linhaOriginal : this.codigoUsuario){ // passo 2

            String linhaSemComentario = linhaOriginal.split("//")[0].trim();
            if (linhaSemComentario.isEmpty()) continue;
            String[] linhaLimpa = linhaSemComentario.replaceAll("\\s+", " ").split(" ");

            int tamanhoLinha = linhaLimpa.length;

            if (tamanhoLinha>3){
                return "Linha "+linhaOriginal+" inválida";
            } else {
                switch (tamanhoLinha){

                    case 1 -> {
                        if (!tabelaEnderecos.containsKey(linhaLimpa[0])){ // se não for uma label
                            if (tabelaTraducao.containsKey(linhaLimpa[0])){
                                int opcode = tabelaTraducao.get(linhaLimpa[0]);

                                if ((opcode < 61440 || opcode > 64000) && opcode != 65535) { 
                                    return "Linha " + linhaOriginal + " inválida: Falta operando";
                                }
                                codigo.add(opcode);
                            }
                            else{
                                try {
                                    int num = Integer.decode(linhaLimpa[0]);

                                    if (num < -32768 || num > 65535){
                                        return ("Linha "+linhaOriginal+" inválida");
                                    }

                                    codigo.add(num & 0xFFFF);

                                } catch (Exception e) {
                                    return ("Linha "+linhaOriginal+" inválida");
                                }
                            }
                        }
                    }

                    case 2 -> {
                        if (tabelaEnderecos.containsKey(linhaLimpa[0])){ // inicio é label
                            
                            if (tabelaTraducao.containsKey(linhaLimpa[1])){
                                int opcode = tabelaTraducao.get(linhaLimpa[1]);
                                if ((opcode < 61440 || opcode > 64000) && opcode != 65535) { 
                                    return "Linha " + linhaOriginal + " inválida: Falta operando";
                                }
                                codigo.add(opcode);
                            } else{
                                try {
                                    int num = Integer.decode(linhaLimpa[1]);
                                    if (num < -32768 || num > 65535){
                                        return ("Linha "+linhaOriginal+" inválida");
                                    }
                                    codigo.add(num & 0xFFFF);
                                } catch (Exception e) {
                                    return ("Linha "+linhaOriginal+" inválida");
                                }
                            }
                        }
                        else{ // instrucao com mnemonico e qualquer outra coisa

                            if (tabelaTraducao.containsKey(linhaLimpa[0])){

                                int resultado = tabelaTraducao.get(linhaLimpa[0]);

                                if ((resultado >= 61440 && resultado <= 64000) || resultado == 65535) {
                                    return "Linha " + linhaOriginal + " inválida: Instrução não aceita operando";
                                }

                                if (tabelaEnderecos.containsKey(linhaLimpa[1]+":")){ // FIM -> FIM:
                                    resultado += tabelaEnderecos.get(linhaLimpa[1]+":");
                                }

                                else if (tabelaEnderecos.containsKey(linhaLimpa[1].substring(1)+":")){ // &i -> i:
                                    resultado += tabelaEnderecos.get(linhaLimpa[1].substring(1)+":");
                                }

                                else {
                                    try {
                                        int num = Integer.decode(linhaLimpa[1]);

                                        if (num < -2048 || num > 4095){
                                            return ("Linha "+linhaOriginal+" inválida");
                                        }

                                        resultado = resultado | (num & 0xFFF);

                                    } catch (Exception e) {
                                        return ("Linha "+linhaOriginal+" inválida");
                                    }
                                }

                                codigo.add(resultado);


                            } else return ("Linha "+linhaOriginal+" inválida");

                        }
                    }

                    case 3 -> {
                        if (!linhaLimpa[0].endsWith(":")) {
                           return "Linha " + linhaOriginal + " inválida";
                        }
                        else{
                            if (tabelaTraducao.containsKey(linhaLimpa[1])){
                                /* 
                                if (tabelaEnderecos.containsKey(linhaLimpa[2]+":")){
                                    codigo.add(tabelaTraducao.get(linhaLimpa[1]) + tabelaEnderecos.get(linhaLimpa[2]+":"));
                                } else return ("Linha "+linhaOriginal+" inválida");
                                */
                                int resultado = tabelaTraducao.get(linhaLimpa[1]);

                                if ((resultado >= 61440 && resultado <= 64000) || resultado == 65535) {
                                    return "Linha " + linhaOriginal + " inválida: Instrução não aceita operando";
                                }

                                if (tabelaEnderecos.containsKey(linhaLimpa[2]+":")){ // FIM -> FIM:
                                    resultado += tabelaEnderecos.get(linhaLimpa[2]+":");
                                }

                                else if (tabelaEnderecos.containsKey(linhaLimpa[2].substring(1)+":")){ // &i -> i:
                                    resultado += tabelaEnderecos.get(linhaLimpa[2].substring(1)+":");
                                }

                                else {
                                    try {
                                        int num = Integer.decode(linhaLimpa[2]);

                                        if (num < -2048 || num > 4095){
                                            return ("Linha "+linhaOriginal+" inválida");
                                        }

                                        resultado = resultado | (num & 0xFFF);

                                    } catch (Exception e) {
                                        return ("Linha "+linhaOriginal+" inválida");
                                    }
                                }

                                codigo.add(resultado);

                            } else return ("Linha "+linhaOriginal+" inválida");
                            }

                    }


                }
            }

        }

        this.macroInstrucoes=codigo;
        return "CERTO";
    }

}