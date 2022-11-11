import java.util.ArrayList;
public class Basket implements Transaction {
    private String clientID;
    private String basketID;
    private ArrayList<BasketItem> basketItems;

    public Basket(String basketID, String clientID) {
        this.clientID = clientID;
        this.basketID = basketID;
        this.basketItems = new ArrayList<>();
    }
    public String getClientID() {
        return clientID;
    }
    public String getBasketID() {
        return basketID;
    }
    @Override
    public ArrayList<Item> getItems() {
        return new ArrayList<>(basketItems);
    }
    @Override
    public boolean containsItem(Item item) {
        return getItems().contains(item);
    }
    @Override
    public boolean containsItemSet(Item[] itemSet) {
        if(itemSet == null || itemSet.length == 0)
            return false;
        for(Item i : itemSet)
            if(!containsItem(i))
                return false;
        return true;
    }
    public static Basket parseBasketLine(String line){
        String[] basket = line.split(";");
        Basket b = new Basket(basket[0],basket[1]);
        for(int i=2;i<basket.length;i+=2)
            b.basketItems.add(new BasketItem(basket[i],Integer.parseInt(basket[i+1])));
        return b;
    }

}
