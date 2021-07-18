import java.util.ArrayList;

public class ArvorePatricia {
    public static final int nBitsChar = 8;
    private static abstract class PatNo { }
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

        public void addInstacia(int i,int j)
        {
            int[] arr = {i,j};
            boolean existe = false;
            for(int a=0; a<this.instancias.size();a++) {
                int[] aux = this.instancias.get(a);
                if(aux[0] == i && aux[1] == j) {
                    existe = true;
                    break;
                }
            }
            if(!existe)
                this.instancias.add(arr);
        }
    }

    private PatNo raiz;
    private int nbitsChave;

    // Retorna o i-esimo bit da chave k a partir da esquerda
    private int bit (int i, String str) {
        if (i == 0) 
            return 0;
        i--; 
        int c = (int)(str.charAt((int)(i / nBitsChar)));
        for (int j = 1; j <= nBitsChar - i % nBitsChar; j++) 
            c = c/2;
        return c % 2;
    }

    // Verifica se p e no externo
    private boolean eExterno (PatNo p) {
        Class classe = p.getClass ();
        return classe.getName().equals(PatNoExt.class.getName());
    }

    private PatNo criaNoInt (int i, PatNo esq, PatNo dir) {
        PatNoInt p = new PatNoInt ();
        p.index = i; 
        p.esq = esq; 
        p.dir = dir;
        return p;
    }

    private PatNo criaNoExt (Item str) {
        PatNoExt p = new PatNoExt ();
        p.chave = str.chave;
        p.addInstacia(str.linha,str.coluna);
        return p;
    }

    private void pesquisa (String str, PatNo t) {
        if (this.eExterno (t)) {
            PatNoExt aux = (PatNoExt)t;
            if (aux.chave.equals(str)) {
                System.out.println ("Elemento encontrado");
                for(int i=0; i<aux.instancias.size();i++) {
                    System.out.println("Linha "+aux.instancias.get(i)[0]+" / Coluna "+aux.instancias.get(i)[1]);
                }
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

    private PatNo insereEntre (Item k, PatNo t, int i) {
        PatNoInt aux = null;
        if (!this.eExterno (t)) 
            aux = (PatNoInt)t;

        // Cria um novo no externo
        if (this.eExterno (t) || (i < aux.index)) { 
            PatNo p = this.criaNoExt (k);
            if (this.bit (i, k.chave) == 1) 
                return this.criaNoInt (i, t, p);
            else 
                return this.criaNoInt (i, p, t);
        }
        else {
            if (this.bit (aux.index, k.chave) == 1)
                aux.dir = this.insereEntre (k, aux.dir, i);
            else 
                aux.esq = this.insereEntre (k, aux.esq, i);
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
                else p = aux.esq;
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

    private void central (PatNo pai, PatNo filho, String str) {
        if (filho != null) {
            if (!this.eExterno (filho)) {
                PatNoInt aux = (PatNoInt)filho;
                central (filho, aux.esq, "Esq");
                if (pai != null)
                    System.out.println ("Pai: "+ ((PatNoInt)pai).index + " " + str+ " Int: " + aux.index);
                else 
                    System.out.println ("Pai: "+ pai + " " + str+ " Int: " + aux.index);
                central (filho, aux.dir, "Dir");
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
        this.central (null, this.raiz, "Raiz");
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
