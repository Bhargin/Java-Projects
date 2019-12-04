package processor;

import java.io.*;
import java.text.NumberFormat;
import java.util.*;
import java.util.Scanner;

public class Report implements Runnable{

	// String refers to order's filename.
	String filename;

	// Common lock object.
	Object lockObj;

	// Integer refers to the clientID of each order.
	private int clientId;

	// Map product name and quantity purchased.
	private Map<String, Integer> receipt;

	// Map Cost for each product in price list.
	private Map<String, Double> cost;

	// Map product name and total cost considering quantity.
	private Map<String, Double> sum;

	// Map Client's order summary to clientId.
	Map<Integer, String> clientSummary; 

	// Keeps track of the total quantity of items after processing every order. 
	Map<String, Integer>grandSummary;


	// Constructor initializes the following parameters.
	public Report(String filename, Object obj, Map<String, Double> cost,
			Map<Integer, String> clientSummary, Map<String, Integer> grandSummary) {

		this.filename=filename;	
		this.lockObj=obj;
		this.cost=cost;
		this.clientSummary=clientSummary;
		this.grandSummary=grandSummary;
	}

	// Run method for thread.
	public void run() {

		// Read data from text file.
		try {

			// Scanner reads order file for item bought.
			Scanner fileScanner = new Scanner(new File(this.filename));

			// BufferedReader reads order file to get clientId.
			BufferedReader buffReader = new BufferedReader(new FileReader(this.filename));
			this.receipt=new TreeMap<String, Integer>();
			this.sum=new TreeMap<String, Double>();	

			// Array stores the values read by BufferedReader.
			String[] temp= buffReader.readLine().split(" ");
			this.clientId=Integer.parseInt(temp[1]);

			// While there is a item in order file.
			while (fileScanner.hasNext()) {

				// Get the product name.
				String product = fileScanner.next();

				// Get the purchase date.
				String date =fileScanner.next();

				// If client has already purchased the item before.
				if(this.receipt.containsKey(product)){

					// Increase current item quantity and cost.
					int quantity=this.receipt.get(product);
					double total=this.sum.get(product);
					total=total+this.cost.get(product);
					quantity=quantity+1;	

					// Put the new values in the map again.
					this.receipt.put(product,quantity);
					this.sum.put(product, total);

					// If client has not purchased the item before.
				} else {
					this.receipt.put(product, 1);
					this.sum.put(product, this.cost.get(product));
				}
			}

			// Remove clientId information from receipt.
			this.receipt.remove("ClientId:");
			this.sum.remove("ClientId:");
			System.out.println("\nReading order for client with id: " + this.clientId);

			// Obtain lock object.
			synchronized(lockObj) {
				String result="";
				double total=0;
				result+="----- Order details for client with Id: " + this.clientId + " -----";

				// Create a summary for client's order.
				for(String item: this.receipt.keySet()) {
					result+="\nItem's name: " + item 
							+ ", Cost per item: " + NumberFormat.getCurrencyInstance().format(this.cost.get(item)) 
							+ ", Quantity: " + this.receipt.get(item) 
							+ ", Cost: " + NumberFormat.getCurrencyInstance().format(this.sum.get(item));

					// Increase total item quantity in map if applicable. 
					if(!grandSummary.containsKey(item)) {
						grandSummary.put(item, this.receipt.get(item));
					} else {
						int totalQuantity= grandSummary.get(item);
						totalQuantity=totalQuantity+this.receipt.get(item);
						grandSummary.put(item, totalQuantity);
					}
				}

				// Calculate the total for whole order.
				for(String item: this.sum.keySet()) {
					total=total + this.sum.get(item);
				}

				result+="\nOrder Total: " + NumberFormat.getCurrencyInstance().format(total);

				// Map client summary with clientId.
				clientSummary.put(this.clientId, result);
			}

			// Terminate scanner object.
			fileScanner.close();	
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}		
}
