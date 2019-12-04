package onlineTest;
import java.io.Serializable;
import java.util.*;


public class LetterGrade implements Serializable{

	// Private string[] stores letters.
	private String [] alphabet;

	// Private double[] stores range of numbers.
	private double[] range;

	// Constructor initializes instance variables to parameters. 
	public LetterGrade(String[] letter, double[] scale) {
		this.alphabet=letter;
		this.range=scale;
	}

	// Get letter grade for number
	public String getLetterGrade(double number) {

		// Check range array.
		for(int i=0; i<this.range.length; i++) {

			// If number is greater or equal to range[i] value
			if(number>=this.range[i]) {

				// reutrn correspoing letter from alphabet[i].
				return this.alphabet[i];
			}
		}
		return "";
	}


}