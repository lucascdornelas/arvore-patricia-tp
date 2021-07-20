package src;
import java.util.ArrayList;

public class ArvorePatricia {
    public static final int nBitsChar = 8;
    private static abstract class PatNo { 
    }
    private static class PatNoInt extends PatNo {
        int index; PatNo esq, dir;
    }
    private static class PatNoExt extends PatNo {
        // O tipo da chave depende da aplicacao
        String chave; 
        ArrayList<int[]> instancias;

        public PatNoExt() {
            instancias = new ArrayList<int[]>();
        }

        public void addInstacia(int linha, int coluna)
        {
            int[] array = {linha,coluna};
            boolean b = false;
            for(int i = 0; i < this.instancias.size(); i++) {
                int[] aux = this.instancias.get(i);
                if(aux[0] == linha && aux[1] == coluna) {
                    b = true;
                    break;
                }
            }
            if(!b)
                this.instancias.add(array);
        }
    }

    private PatNo raiz;
    private int nbitsChave;

    // Retorna o i-esimo bit da chave k a partir da esquerda
    private int bit (int index, String str) {
        if (index == 0) 
            return 0;
        index--; 
        int chave = (int)(str.charAt((int)(index / nBitsChar)));
        for (int j = 1; j <= nBitsChar - index % nBitsChar; j++) 
            chave = chave/2;
        return chave % 2;
    }

    // Verifica se p e no externo
    private boolean eExterno (PatNo p) {
        Class classe = p.getClass ();
        return classe.getName().equals(PatNoExt.class.getName());
    }

    private PatNo criaNoInt (int index, PatNo esq, PatNo dir) {
        PatNoInt p = new PatNoInt ();
        p.index = index; 
        p.esq = esq; 
        p.dir = dir;
        return p;
    }

    private PatNo criaNoExt (Item it) {
        PatNoExt p = new PatNoExt ();
        p.chave = it.chave;
        p.addInstacia(it.linha,it.coluna);
        return p;
    }

    private void pesquisa (String str, PatNo t) {
        if (this.eExterno (t)) {
            PatNoExt aux = (PatNoExt)t;
            // se a string estiver na chave
            if (aux.chave.equals(str)) {
                System.out.println ("Elemento encontrado");
                // imprimindo as linhas de ocorrencias
                for(int i=0; i<aux.instancias.size(); i++) 
                    System.out.println("Linha "+aux.instancias.get(i)[0]+" / Coluna "+aux.instancias.get(i)[1]);
            }
            else
                System.out.println ("Elemento nao encontrado");
        }
        else {
            PatNoInt aux = (PatNoInt)t;
            if (this.bit (aux.index, str) == 0) 
                pesquisa (str, aux.esq);
            else 
                pesquisa (str, aux.dir);
        }
    }

    private PatNo insereEntre (Item it, PatNo t, int index) {
        PatNoInt aux = null;
        if (!this.eExterno (t)) 
            aux = (PatNoInt)t;

        // Cria um novo no externo
        if (this.eExterno (t) || (index < aux.index)) { 
            PatNo p = this.criaNoExt (it);
            if (this.bit (index, it.chave) == 1) 
                return this.criaNoInt (index, t, p);
            else 
                return this.criaNoInt (index, p, t);
        }
        else {
            if (this.bit (aux.index, it.chave) == 1)
                aux.dir = this.insereEntre (it, aux.dir, index);
            else 
                aux.esq = this.insereEntre (it, aux.esq, index);
            return aux;
        }
    }

    private PatNo insere (Item it, PatNo t) {
        if (t == null) return this.criaNoExt (it);
        else {
            PatNo p = t;
            while (!this.eExterno (p)) {
                PatNoInt aux = (PatNoInt)p;
                if (this.bit (aux.index, it.chave) == 1) 
                    p = aux.dir;
                else 
                    p = aux.esq;
            }
            PatNoExt aux = (PatNoExt)p;
            // acha o primeiro bit diferente
            int i = 1; 
            while ((i <= this.nbitsChave) && (this.bit (i, it.chave) == this.bit (i, aux.chave))) 
                i++;
            if (i > this.nbitsChave) {
                System.out.println ("Erro: chave ja esta na arvore");
                if(this.eExterno(aux))
                    aux.addInstacia(it.linha,it.coluna);
                return t;
            }
            else 
                return this.insereEntre (it, t, i);
        }
    }

    private void centro (PatNo pai, PatNo filho, String str) {
        if (filho != null) {
            if (!this.eExterno (filho)) {
                PatNoInt aux = (PatNoInt)filho;
                centro (filho, aux.esq, "Esq");
                if (pai != null)
                    System.out.println ("Pai: "+ ((PatNoInt)pai).index + " " + str+ " Int: " + aux.index);
                else 
                    System.out.println ("Pai: "+ pai + " " + str+ " Int: " + aux.index);
                centro (filho, aux.dir, "Dir");
            } else {
                PatNoExt aux = (PatNoExt)filho;
                if (pai != null)
                    System.out.println ("Pai: "+ ((PatNoInt)pai).index + " " + str+ " Ext: " + aux.chave);
                else 
                    System.out.println ("Pai: "+ pai + " " + str+ " Ext: " + aux.chave);
            }
        }
    }

    public void imprime () {
        this.centro (null, this.raiz, "Raiz");
    }

    public ArvorePatricia(int nbitsChave) {
        this.raiz = null; this.nbitsChave = nbitsChave;
    }

    public void pesquisa(String k) { 
        this.pesquisa (k, this.raiz); 
    }

    public void insere(Item k) { 
        this.raiz = this.insere (k, this.raiz); 
    }
}
