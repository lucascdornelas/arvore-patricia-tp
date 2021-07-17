import java.util.StringTokenizer;
import java.io.*;
public class ExtraiPalavra {
    private BufferedReader arqDelim, arqTxt;
    private StringTokenizer work;
    private String delimitadores;
    public int i;
    public int j;

    public ExtraiPalavra (String nomeArqDelim, String nomeArqTxt) throws Exception {

        this.arqDelim = new BufferedReader (new FileReader (nomeArqDelim));
        this.arqTxt = new BufferedReader (new FileReader (nomeArqTxt));

        // os delimitadores estão juntos em uma única linha do arquivo delim.txt
        int crlf = 0x0D0A;
        this.delimitadores = arqDelim.readLine() + '\r' + '\n' + (char)crlf;

        this.work = null;
        this.i=0;
        this.j=0;
    }

    public String nextWord() throws Exception{
        if (work == null || !work.hasMoreTokens()) {
            String linha = arqTxt.readLine(); i++; j=0;

            if (linha == null) {
                return null;
            }
            this.work = new StringTokenizer (linha, this.delimitadores);

            if (!work.hasMoreTokens()) {
                return ""; // ignora delimitadores
            }
        }
        j++;

        return this.work.nextToken();
    }
    
    public void fecharArquivos () throws Exception {

        this.arqDelim.close();
        this.arqTxt.close();
    }
}