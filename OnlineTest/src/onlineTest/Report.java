package onlineTest;
import java.io.Serializable;
import java.util.*;


public class Report implements Serializable{

	// Total possible points for exam.
	private Double total;

	// Student final score for exam.
	private double finalScore;

	// correct exam true/false and multiple choice answers.
	private Map<Integer, String> examAnswer;

	// Student true/false and multiple choice answers.
	private Map<Integer, String> studentAnswer;

	// Question points.
	private Map<Integer, Double> questionPoints;

	// Student fill-in-the-blanks answers.
	private Map<Integer, String[]> studentFillInTheBlanksAnswer;

	// Correct exam fill-in-the-blanks answers.
	private Map<Integer, String[]> examFillInTheBlanksAnswer;

	// Create a new report for student, exam, and examId.
	public Report(Student student, Exam exam, int examId) {

		// Initialize each instance variable.
		this.examAnswer=exam.getExamAnswerMap();
		this.examFillInTheBlanksAnswer=exam.getFillInTheBlanksAnswer();
		this.studentAnswer=student.getStudentAnswersMap(examId);
		this.studentFillInTheBlanksAnswer=student.getStudentFillInTheBlanksAnswerMap(examId);
		this.questionPoints=exam.getPointsTable();


	}

	// create report for course grade calculation.
	public String createReport(Student student) {
		String result="";

		// partial score for fill-in-the-blanks answers.
		double partialScore=0;
		this.total=0.0;
		this.finalScore=0.0;

		// Set contains every question number and related answer for exam.
		Set<Integer>questions=examAnswer.keySet();

		// For every exam question.
		for(Integer number: questions) {

			result+="Question #"+number +" ";

			// If student true/false and multiple choice answer map contains question.
			if(this.studentAnswer!=null && this.studentAnswer.containsKey(number)) {

				// If answer is correct.
				if(this.studentAnswer.get(number).equals(this.examAnswer.get(number))) {
					result+=this.questionPoints.get(number) 
							+ " points out of " + this.questionPoints.get(number) +"\n";

					// Increment the final Score for exam
					finalScore+=this.questionPoints.get(number);

					// If answer is not correct.
				} else if(!this.studentAnswer.get(number).equals(this.examAnswer.get(number))) {
					result+="0.0" + " points out of " + this.questionPoints.get(number) + "\n";
				}

				// If student fill-in-the-blanks answer map question.
			} else if(this.studentFillInTheBlanksAnswer!=null 
					&& this.studentFillInTheBlanksAnswer.containsKey(number)) {

				// Check how many of the filled-in-blanks are correct.
				for(String choice: this.studentFillInTheBlanksAnswer.get(number)) {
					for(String correct: this.examFillInTheBlanksAnswer.get(number)) {

						// If any filled-in-blanks is correct.
						if(choice.equals(correct)){

							// Increment final score by partialScore.
							partialScore+=this.questionPoints.get(number)
											/(this.examFillInTheBlanksAnswer.get(number).length);
						}
					}
				}

				result+=partialScore + " points out of " + this.questionPoints.get(number) + "\n";
				finalScore+=partialScore;
				partialScore=0;
			}

			// Increment total possible points for exam.
			total+=this.questionPoints.get(number);
		}

		result+="Final Score: " + finalScore + " out of " + total;

		// Increase student's exam averages.
		student.increasefinalScore(this.finalScore/this.total);
		return result;
	}

	// Create a report for min, max, and average calculation.
	public String createReportStat(Student student) {
		String result="";
		double partialScore=0;
		this.total=0.0;
		this.finalScore=0.0;

		Set<Integer>questions=examAnswer.keySet();

		for(Integer number: questions) {
			result+="Question #"+number +" ";

			if(this.studentAnswer!=null && this.studentAnswer.containsKey(number)) {
				if(this.studentAnswer.get(number).equals(this.examAnswer.get(number))) {

					result+=this.questionPoints.get(number) 
							+ " points out of " + this.questionPoints.get(number) +"\n";
					finalScore+=this.questionPoints.get(number);

				} else if(!this.studentAnswer.get(number).equals(this.examAnswer.get(number))) {

					result+="0.0" + " points out of " + this.questionPoints.get(number) + "\n";
				}

			} else if(this.studentFillInTheBlanksAnswer!=null 
					&& this.studentFillInTheBlanksAnswer.containsKey(number)) {

				for(String choice: this.studentFillInTheBlanksAnswer.get(number)) {
					for(String correct: this.examFillInTheBlanksAnswer.get(number)) {

						if(choice.equals(correct)){
							partialScore+=this.questionPoints.get(number)
										/(this.examFillInTheBlanksAnswer.get(number).length);
						}
					}
				}

				result+=partialScore + " points out of " + this.questionPoints.get(number) + "\n";
				finalScore+=partialScore;
				partialScore=0;
			}

			total+=this.questionPoints.get(number);
		}

		result+="Final Score: " + finalScore + " out of " + total;
		return result;
	}

	// Return student's final score for exam.
	public double getFinalScore() {
		return this.finalScore;
	}

}
