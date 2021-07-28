public class Item {
    public String chave;
    public static int size;
    public int linha;
    public int coluna;

    public Item (String chave,int i,int j) {
        this.chave = chave;
        this.linha = i;
        this.coluna = j;
        Item.size = this.chave.length();
    }
}
