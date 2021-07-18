import java.io.*;
import java.util.Scanner;

public class Main {
    public static final int nCharPalavra = 16;
    public static final int nBitsChar = 8;
    private static class Str2bin{
        public static String char16(String str){
            // Garantir 16 caracteres
            if(str.length()>nCharPalavra)
                str = str.substring(0,nCharPalavra-1);
            else
                while (str.length()<nCharPalavra)
                    str = str.concat(" ");
            return str;
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
            String str;
            try{
                str = input.nextWord().toLowerCase();

                //verificando se a string e vazia
                if(str.compareTo(" ") == (-1)) 
                str = input.nextWord().toLowerCase();
                    
                //garantir 16 caracteres no caractere     
                str = Str2bin.char16(str); 
            }catch (Exception e){
                break;
            }

            // inserindo o item
            dicionario.insere(new Item(str,input.linhaMatriz,input.colunaMatriz));
                
            System.out.println ("Inseriu chave "+ i + ": " + str + str.charAt(0) + " - Linha " + input.linhaMatriz + " / Coluna " + input.colunaMatriz);
        }
        System.out.println();

        // Imprime arvore
        dicionario.imprime();

        System.out.println("");

        // Pesquisa cada chave(contida no arquivo pesquisa.txt) na arvore
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