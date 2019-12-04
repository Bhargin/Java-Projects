package onlineTest;
import java.io.Serializable;
import java.util.*;

public class Student implements Serializable{

	public String name;

	// Set stores examId of every exam student attempted.
	private Set<Integer> attemptedExam;

	// Student answers for true/false and multiple choice question.
	private Map<Integer, Map<Integer, String>> studentAnswerforSpecificexam;

	// Student answers for fill-in-the-blanks question.
	private Map<Integer, Map<Integer, String[]>> studentFillInAnswerforSpecificexam;

	// Student's exam averages.
	private double examAverages;

	// Student numeric grade;
	private double Grade; 

	// Create a new student with name.
	public Student(String name) {

		// Initialize all instance variables.
		this.name=name;
		this.studentAnswerforSpecificexam= new HashMap<>();
		this.studentFillInAnswerforSpecificexam= new HashMap<>();
		this.attemptedExam=new HashSet<Integer>();
		this.examAverages=0.0;
		this.Grade=0.0;
	}

	// Answer true/false question.
	public void answerTrueFalse(int questionNumber, boolean answer, int examId) {

		// Capitalize answer.
		String booleanVal=String.valueOf(answer).substring(0,1)
						.toUpperCase() + String.valueOf(answer).substring(1);

		// If student already answered the question.
		if(this.studentAnswerforSpecificexam.containsKey(examId)) {
			Map<Integer, String> temp=this.studentAnswerforSpecificexam.get(examId);
			temp.put(questionNumber, booleanVal);

			// Re-add response to map.
			this.studentAnswerforSpecificexam.put(examId, temp);
		}

		// Else create and add new response to map.
		else {
			Map<Integer, String> temp=new HashMap<>();
			this.attemptedExam.add(examId);
			temp.put(questionNumber, booleanVal);
			this.studentAnswerforSpecificexam.put(examId, temp);	
		}
	}

	// Answer multiple choice question.
	public void answerMultipleChoice(int questionNumber, String[] input, int examId) {

		//Sort input array.
		Arrays.sort(input);

		// If student already answered the question.
		if(this.studentAnswerforSpecificexam.containsKey(examId)) {
			Map<Integer, String> temp=this.studentAnswerforSpecificexam.get(examId);
			temp.put(questionNumber, toString(input));

			// Re-add response to map.
			this.studentAnswerforSpecificexam.put(examId, temp);
		}

		// Else create and add new response to map.
		else {
			Map<Integer, String> temp=new HashMap<>();
			temp.put(questionNumber, toString(input));
			this.attemptedExam.add(examId);
			this.studentAnswerforSpecificexam.put(examId, temp);	
		}
	}

	// Answer fill-in-the-blanks question.
	public void answerFillInTheBlanks(int questionNumber, String[] input, int examId) {

		// Sort input array.
		Arrays.sort(input);

		// If student already answered the question.
		if(this.studentFillInAnswerforSpecificexam.containsKey(examId)) {
			Map<Integer, String[]> temp2=this.studentFillInAnswerforSpecificexam.get(examId);
			temp2.put(questionNumber, input);

			// Re-add response to map.
			this.studentFillInAnswerforSpecificexam.put(examId, temp2);
		}

		// Else create and add new response to map.
		else {
			Map<Integer, String[]> temp2=new HashMap<>();
			this.attemptedExam.add(examId);
			temp2.put(questionNumber, input);
			this.studentFillInAnswerforSpecificexam.put(examId, temp2);	
		}
	}

	// Get studentAnswerforSpecificexam map.
	public Map<Integer, String> getStudentAnswersMap(int examId){
		return this.studentAnswerforSpecificexam.get(examId);
	}

	// Get studentFillInTheBlanksAnswer map.
	public Map<Integer, String[]> getStudentFillInTheBlanksAnswerMap(int examId){
		return this.studentFillInAnswerforSpecificexam.get(examId);
	}

	// Get attemptedExam set.
	public Set<Integer> getAttemptedExamSet(){	
		return this.attemptedExam;
	}

	// Increase total exam averages by number
	public void increasefinalScore(double number) {
		this.examAverages+=number;
	}

	// Get student's course grade.
	public double getCourseGrade() {

		// If grade is not calculated.
		if(this.Grade==0.0) {
			this.Grade=Math.round((this.examAverages/this.attemptedExam.size())*100);
		}
		return this.Grade;
	}

	// Calculate course grade.
	public double calculateCourseGrade() {
		return Math.round((this.examAverages/this.attemptedExam.size())*100);	
	}

	// Set student's grade to number
	public void setGrade(double number) {
		this.Grade=number;
	}

	// Get student's total exam averages.
	public double getexamAverages() {
		return this.examAverages;
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
