package processor;

import java.io.*;
import java.text.NumberFormat;
import java.util.*;

public class OrdersProcessor {

	public static void main(String[]args) throws InterruptedException {

		// Scanner reads user input.
		Scanner in= new Scanner(System.in);

		// Shared lock object.
		Object lockObj= new Object();

		// Map stores item name to unit price.
		Map<String, Double> cost= new TreeMap<String, Double>();

		// Map stores every client summary for all the orders process to clientId. 
		Map<Integer, String> clientSummary= new TreeMap<Integer, String>();

		// Map stores item's name and total quantity after processing every order.
		Map<String, Integer> grandSummary= new TreeMap<String, Integer>();


		// String refers to the price list.
		String priceList;

		// String refers to the order file base names.
		String orderBaseName;

		// String refer to the result file name.
		String resultFileName;

		// String refers to the final generated summary after completing processing.
		String fileText="";

		// Refers to the choice for multiple threading or single threading.
		char choice;

		// Total number of orders to process.
		int numberOfOrders;

		// Being displaying menu options and start reading input values.
		System.out.print( "Enter item's data file name: ");
		priceList=in.next();

		System.out.print("\nEnter 'y' for multiple threads, any other character otherwise: ");
		choice=in.next().charAt(0);

		System.out.print("\nEnter number of orders to process: ");
		numberOfOrders= in.nextInt();

		System.out.print("\nEnter order's base filename: ");
		orderBaseName=in.next();

		System.out.print("\nEnter result's filename: ");
		resultFileName=in.next();

		// Start timer.
		long startTime = System.currentTimeMillis();

		// Close scanner.
		in.close();

		// Initialize the cost map.
		try {
			Scanner priceScanner = new Scanner(new File(priceList));

			// Start reading information from price list text file.
			while(priceScanner.hasNextLine()) {
				String item=priceScanner.next();
				Double price= priceScanner.nextDouble();

				// Put values in the cost map.
				cost.put(item, price);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// Multiple Thread approach.
		if(choice =='y') {

			// Array stores multiple thread objects.
			Thread[] multipleThread= new Thread[numberOfOrders];

			// For every order create a new thread.
			for(int i=0; i<multipleThread.length; i++) {
				Report r1= new Report(orderBaseName + (i+1)+".txt", lockObj, cost
						, clientSummary, grandSummary);
				Thread t1= new Thread(r1);

				// Place every thread created in the array.
				multipleThread[i]=t1;
			}	

			// Start every thread in array.
			for(int i=0; i<multipleThread.length; i++) {
				multipleThread[i].start();
			}

			// Wait for every thread in the array to finish.
			for(int i=0; i<multipleThread.length; i++) {
				multipleThread[i].join();
			}

			// Single Thread approach.
		} else if (choice=='n'){

			// Declare a single thread.
			Thread singleThread;

			// For every order use the single thread reference.
			for(int i=0; i<numberOfOrders; i++) {

				//Use the same thread for each order.
				Report s1 =new Report(orderBaseName + (i+1)+".txt", lockObj, cost
						, clientSummary, grandSummary);
				singleThread = new Thread(s1);

				// Process one order entirely at a time.
				singleThread.start();
				singleThread.join();
			}
		}

		// Add every client summary to fileText string.
		for(Integer clientId: clientSummary.keySet()) {
			fileText+=clientSummary.get(clientId)+'\n';
		}

		String result="***** Summary of all orders *****";
		Double grandTotal=0.0;

		// For every product bought.
		for(String item: grandSummary.keySet()) {	

			// Add the total summary information about the product.
			result+="\nSummary - Item's name: " + item 
					+ ", Cost per item: " + NumberFormat.getCurrencyInstance().format(cost.get(item)) 
					+ ", Number sold: " + grandSummary.get(item) 
					+ ", Item's Total: " + NumberFormat.getCurrencyInstance()
					.format((grandSummary.get(item)*cost.get(item)));

			// Increase the grand total amount.
			grandTotal=grandTotal+grandSummary.get(item)*cost.get(item);
		}
		result+="\nSummary Grand Total: " + NumberFormat.getCurrencyInstance().format(grandTotal)+'\n';

		// Add results to String fileText.
		fileText+=result;

		// Write fileText to preferred resultFileName
		try {

			// Create a new fileWriter for resultFileName.
			FileWriter fileWriter = new FileWriter(resultFileName, false);

			// Write the final expected result to the file.
			fileWriter.write(fileText);
			fileWriter.close();

			// Stop the timer.
			long endTime = System.currentTimeMillis();

			// Print out the timing details.
			System.out.println("Processing time (msec): " + (endTime - startTime));
			System.out.println("Results can be found at: " + resultFileName);
		} catch (IOException e) {
			System.err.println(e.getMessage());
		}
	}
}

