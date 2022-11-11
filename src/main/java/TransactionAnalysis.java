
import java.util.ArrayList;

public class TransactionAnalysis {

	private Transaction[] transactions;
	private Item[] items;

	public TransactionAnalysis(Transaction[] transactions, Item[] items) {
		this.transactions = transactions;
		this.items = items;
	}

	public String aPrioriAlgorithm(int threshold) {
		String result = "";
		ArrayList<Item[]> itemSets = createItemsetsOfSize1();        
		int itemsetNumber=1; //the current itemset being looked at

		while(itemSets.size()>0) {
			itemSets = calculateFrequencysItemSets(itemSets, threshold);
			if(!itemSets.isEmpty()) {
				result += "- Found " + itemSets.size()+ " frequent itemsets of size " + itemsetNumber + ":\n";
				result += printItemSets(itemSets);
				itemSets = createItemSetsFromPreviousOnes(itemSets);
			}
			itemsetNumber++;
		} 
		return result;
	}

	private ArrayList<Item[]> createItemsetsOfSize1() {
		ArrayList<Item[]> itemSets = new ArrayList<Item[]>();
		for(int i=0; i<items.length; i++) {
			Item[] itemSet = {items[i]};
			itemSets.add(itemSet);
		}
		return itemSets;
	}

	private Item searchForMissingItem(Item[] x, Item[] y) {
		for(int s1=0; s1<y.length; s1++) {
			boolean found = false;
			for(int s2=0; s2<x.length; s2++)
				if (x[s2].equals(y[s1])) { 
					found = true;
					break;
				}
			if (!found)// Y[s1] is not in X
				return y[s1];
		}
		return null;
	}

	private ArrayList<Item[]> createItemSetsFromPreviousOnes(ArrayList<Item[]> previousItemSets) {
		// by construction, all existing itemsets have the same size
		int previousSizeOfItemsets = previousItemSets.get(0).length;
		ArrayList<Item[]> newItemSets = new ArrayList<Item[]>(); //temporary candidates
		// compare each pair of itemsets of size n-1
		for(int i=0; i<previousItemSets.size(); i++)
			for(int j=i+1; j<previousItemSets.size(); j++) {
				Item[] X = previousItemSets.get(i);
				Item[] Y = previousItemSets.get(j);
				//make a string of the first n-2 tokens of the strings
				Item[] newCand = new Item[previousSizeOfItemsets +1];
				for(int s=0; s<newCand.length-1; s++)
					newCand[s] = X[s];
				newCand[newCand.length -1] = searchForMissingItem(X,Y);
				newItemSets.add(newCand);
			}
		return newItemSets;
	}

	private int frequencyItemSet(Item[] itemSet) {
		int frequency = 0;
		for(Transaction transaction: transactions)
			if(transaction.containsItemSet(itemSet))
				frequency++;
		return frequency;

	}

	public ArrayList<Item[]> calculateFrequencysItemSets(ArrayList<Item[]> itemSets, int tresHold) {
		ArrayList<Item[]> result = new ArrayList<Item[]>(); 
		int[] frequencies = new int[itemSets.size()];
		int i = 0;
		for(Item[] productSet: itemSets) {
			frequencies[i] = frequencyItemSet(productSet);
			i++;
		}
		for(int j=0; j < frequencies.length; j++)
			if(frequencies[j] >= tresHold)
				result.add(itemSets.get(j));
		return result;
	}

	public String printItemSets(ArrayList<Item[]> itemSets){
		String result = "{";
		for(Item[] items: itemSets) {
			result += "("; 
			for(Item item: items)
				result += item.getItemName() + ", ";
			result = result.substring(0, result.length()-2);
			result += ");";

		}
		result = result.substring(0, result.length()-1);
		return result + "}\n";
	}
}