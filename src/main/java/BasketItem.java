import java.util.Objects;

public class BasketItem implements Item{
    private String productID;
    private int quantity;

    public BasketItem(String productID,int quantity){
        if(quantity <=0) {
            System.out.println("Ongeldige hoeveelheid!");
            System.exit(0);
        }
        else {
            this.productID = productID;
            this.quantity = quantity;
        }
    }
    public int getQuantity() {
        return quantity;
    }
	
    @Override
    public String getItemName() {
        return productID;
    }
	
    @Override
    //Equals + hashcode via rechterklik & "Generate"
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        BasketItem that = (BasketItem) o;
        return Objects.equals(productID, that.productID);
    }
    @Override
    public int hashCode() {
        return Objects.hash(productID, quantity);
    }
}
