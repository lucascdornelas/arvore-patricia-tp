public class Item {
    public String chave;
    public static int size;
    // outros componentes do registro
    public int i;
    public int j;

    public Item (String chave,int i,int j) {
        this.chave = chave;
        this.i = i;
        this.j = j;
        Item.size = this.chave.length();
    }
}
