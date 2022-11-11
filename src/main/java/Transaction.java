import java.util.ArrayList;

interface Transaction {
    ArrayList<Item> getItems();

    boolean containsItem(Item item);

    boolean containsItemSet(Item[] itemSet);
}
