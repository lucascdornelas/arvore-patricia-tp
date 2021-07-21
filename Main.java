import java.io.*;
import java.util.Scanner;

public class Main {
    public static final int nCharPalavra = 16;
    public static final int nBitsChar = 8;
    private static class Str2bin{
        public static String str16(String str){

            // Garantir 16 bits
            if(str.length() > nCharPalavra)
                str = str.substring(0,nCharPalavra-1);
            else
                while (str.length() < nCharPalavra)
                    str = str.concat(" ");
            return str;
        }
    }

    public static ArvorePatricia insereArvore(ArvorePatricia dicionario, ExtraiPalavra input) {
        // Insere cada chave na arvore
        for (int i = 0;; i++) {
            String str;
            try{
                str = input.nextWord().toLowerCase();

                //verificando se a string e vazia
                if(str.compareTo(" ") == (-1)) 
                    str = input.nextWord().toLowerCase();
                        
                //garantir 16 bits na palavra     
                str = Str2bin.str16(str); 

            }catch (Exception e){
                break;
            }

            // inserindo o item na arvore
            dicionario.insere(new Item(str,input.linhaMatriz,input.colunaMatriz));
                    
            System.out.println ("Inseriu chave "+ i + ": " + str + str.charAt(0) + " - Linha " + input.linhaMatriz + " / Coluna " + input.colunaMatriz);
        }
        return dicionario;
    }

    public static Scanner pesquisaArvore(ArvorePatricia dicionario, Scanner search_input) {
         // Pesquisa cada chave(contida no arquivo pesquisa.txt) na arvore
         File file = new File("pesquisa.txt");
         try{
             search_input = new Scanner(file);
         } catch (Exception e){
             System.out.println("Erro: arquivo de pesquisa não aberto");
             return null;
         }
 
         int index = 0;
         while(search_input.hasNext()) {
             String c = search_input.nextLine();
             System.out.println ("Pesquisando chave" + index + ": " + c);
             c = Str2bin.str16(c);
             dicionario.pesquisa(c);
             index++;
         }
         return search_input;
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

        // inserindo elementos na arvore
        dicionario = insereArvore(dicionario, input);

        System.out.println();

        // Imprime arvore
        dicionario.imprime();

        System.out.println();

        Scanner search_input = null;

        // pesquisando elementos na arvore
        search_input = pesquisaArvore(dicionario, search_input);

        try{
            input.fecharArquivos();
        } catch (Exception e){
            System.out.println("Erro: não foi possível fechar arquivo");
        }

        search_input.close();
    }
}