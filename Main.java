import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    private static class Str2bin{

        public static String ch16(String c){
            // Garantir 16 caracteres
            if(c.length()>16)
                c = c.substring(0,15);
            else
                while (c.length()<16)
                    c = c.concat(" ");
            return c;
        }

        public static String bin(String c){
            byte[] bytes = c.getBytes();
            StringBuilder binary = new StringBuilder();
            
            for (byte b : bytes) {
                int val = b;
                for (int i = 0; i < 8; i++) {
                    binary.append((val & 128) == 0 ? 0 : 1);
                    val <<= 1;
                }
            
            }
            return binary.toString();
        }

        public static String str(String c,int nBitChave) {
            String k = new String("");

            for(int i=0; i < nBitChave; i++){
                String str = new String("");
                for(int j=0; j<8; j++){
                    str = str.concat(c.substring(j,j+1));
                }
                byte b = Byte.parseByte(str); 
                int val = b;
                char ch = (char)val;
                k = k.concat(Character.toString(ch));
            }

            return k;
        }
    }

    public static void main (String[] args) {
        ArvorePatricia dicionario = new ArvorePatricia(128);

        if(args[0].length() == 0) {
            System.out.println("Error: nenhum arquivo passado por parametro.");
        }

        // Abre o arquivo
        ExtraiPalavra input;
        try{
            input = new ExtraiPalavra("delim.txt", args[0]);
        }catch (Exception e){
            System.out.println("Erro: arquivo não aberto");
            return;
        }

        // Insere cada chave na arvore
        for (int i = 0;; i++) {
            String c;
            try{
                c = input.nextWord().toLowerCase();
                c = Str2bin.ch16(c); // garantir 16 caracteres
            }catch (Exception e){
                break;
            }

            int teste = 0;
            int testeAlgo = c.charAt(teste);
            String resposta = Integer.toBinaryString(testeAlgo);
            if(resposta.equals("100000")) {
                dicionario.insere(new Item(c,input.i,input.j));
                System.out.println("carai");
            }
                
            System.out.println ("Inseriu chave "+ i + ": " + c + resposta +" - Linha " + input.i + " / Coluna " + input.j);
        }
        System.out.println("");

        // Imprime arvore
        dicionario.imprime ();

        System.out.println("");

        // Pesquisa cada chave na arvore
        File file = new File("pesquisa.txt");
        Scanner search_input;
        try{
            search_input = new Scanner(file);
        } catch (Exception e){
            System.out.println("Erro: arquivo de pesquisa não aberto");
            return;
        }

        int i=0;
        while(search_input.hasNext()) {
            String c = search_input.nextLine();
            System.out.println ("Pesquisando chave" + i + ": " + c);
            c = Str2bin.ch16(c);
            dicionario.pesquisa(c);
            i++;
        }

        try{
            input.fecharArquivos();
        } catch (Exception e){
            System.out.println("Erro: não foi possível fechar arquivo");
        }

//        search_input.close();
    }
}
