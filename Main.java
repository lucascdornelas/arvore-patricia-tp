import java.io.*;
import java.util.Scanner;

public class Main {
    public static final int nCharPalavra = 16;
    public static final int nBitsChar = 8;
    private static class Str2bin{
        public static String char16(String c){
            // Garantir 16 caracteres
            if(c.length()>nCharPalavra)
                c = c.substring(0,nCharPalavra-1);
            else
                while (c.length()<nCharPalavra)
                    c = c.concat(" ");
            return c;
        }

        public static String bin(String c){
            byte[] bytes = c.getBytes();
            StringBuilder binary = new StringBuilder();
            
            for (byte b : bytes) {
                int val = b;
                for (int i = 0; i < nBitsChar; i++) {
                    binary.append((val & nCharPalavra*nBitsChar) == 0 ? 0 : 1);
                    val <<= 1;
                }
            }
            return binary.toString();
        }

        public static String str(String c,int nBitChave) {
            String k = new String("");

            for(int i = 0; i < nBitChave; i++){
                String str = new String("");
                for(int j = 0; j < nBitsChar; j++){
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
        ArvorePatricia dicionario = new ArvorePatricia(nCharPalavra*nBitsChar);

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

                //verificando se a string e vazia
                if(c.compareTo(" ") == (-1)) 
                    c = input.nextWord().toLowerCase();
                    
                //garantir 16 caracteres no caractere     
                c = Str2bin.char16(c); 
            }catch (Exception e){
                break;
            }

            dicionario.insere(new Item(c,input.linhaMatriz,input.colunaMatriz));
                
            System.out.println ("Inseriu chave "+ i + ": " + c + c.charAt(0) + " - Linha " + input.linhaMatriz + " / Coluna " + input.colunaMatriz);
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
            c = Str2bin.char16(c);
            dicionario.pesquisa(c);
            i++;
        }

        try{
            input.fecharArquivos();
        } catch (Exception e){
            System.out.println("Erro: não foi possível fechar arquivo");
        }

        search_input.close();
    }
}