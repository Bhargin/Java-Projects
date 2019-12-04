package onlineTest;
import java.io.Serializable;
import java.util.*;

public class Exam implements Serializable{

	// String stores exam title.
	private String title; 
	
	// Int stores exam Id.
	private int Id; 
	
	// Map stores int question number and string text.
	private Map<Integer, String> question;
	
	// Map stores int question number and double points.
	private Map<Integer, Double> points;
	
	// Map stores true/false and multiple choice question and string correct answer.
	private Map<Integer, String> examAnswer;
	
	// Map stores fill- in-the-blanks question and string[] correct answer.
	private Map<Integer, String[]> CorrectFillInBlanksAnswer;

	// Create a exam with examId and title.
	public Exam(int examId, String title) {
		
		//Initialize each variables.
		this.title=title;
		this.Id=examId;
		this.examAnswer= new HashMap<>();
		this.question=new HashMap<>();
		this.points=new HashMap<>();
		this.CorrectFillInBlanksAnswer=new HashMap<>();
	}
	
	// Add true false question.
	public void addTrueFalse(int questionNumber, String text, double points, boolean answer) {
		
		// Capitalize answer.
		String booleanVal=String.valueOf(answer).substring(0,1)
				.toUpperCase() + String.valueOf(answer).substring(1);
		
		// Store each value to corresponding question number on map.
		this.question.put(questionNumber, text);
		this.points.put(questionNumber, points);
		this.examAnswer.put(questionNumber, booleanVal);
	}

	// Add multiple choice question.
	public void addMultipleChoice(int questionNumber, String text, double points, String[] answer) {
		
		// Sort answers array.
		Arrays.sort(answer);
		
		// Store each value to corresponding question number on map.
		this.question.put(questionNumber, text);
		this.points.put(questionNumber, points);
		this.examAnswer.put(questionNumber, toString(answer));
		
	}
	
	// Add fill-in-the-blanks question.
	public void addFillInTheBlanks(int questionNumber, String text, double points, String[] answer) {
		
		// Sort ansswers array.
		Arrays.sort(answer);
		
		// Store each value to corresponding question number on map.
		this.question.put(questionNumber, text);
		this.points.put(questionNumber, points);
		this.examAnswer.put(questionNumber, toString(answer));
		this.CorrectFillInBlanksAnswer.put(questionNumber, answer);
	}
	
	// Get exam key.
	public String getExamKey() {
		
		String answer="";
		Set<Integer>allQues=question.keySet();
		
		// For every question in exam.
		for(Integer number: allQues) {
			
			// Retrieve value related to question in each map.
			answer+="Question Text: " + this.question.get(number) + '\n';
			answer+="Points: " + this.points.get(number) + '\n';
			answer+="Correct Answer: " + this.examAnswer.get(number) + '\n';
			
		}
		
		return answer;
	}
	
	// Get examAnswer map.
	public Map<Integer, String> getExamAnswerMap(){
		return this.examAnswer;
	}
	
	// Get question map.
	public Map<Integer, String> getQuestionsMap(){
		return this.question;
	}
	
	// Get points map.
	public Map<Integer, Double> getPointsTable(){
		return this.points;
	}
	
	// Get CorrectFillInBlanksAnswer map.
	public Map<Integer, String[]> getFillInTheBlanksAnswer(){
		return this.CorrectFillInBlanksAnswer;
	}
	
	
	// Private toString method.
	private String toString(String[] array) {
		
		String s2 = "[";
		
		for(int i=0; i<array.length; i++) {
			
			if(!array[i].equals(array[array.length-1])) {
				s2+=array[i]+", ";
			} else {
				
				s2+=array[i] +"]";
			}
		}
		
		return s2;
	}
	
}
