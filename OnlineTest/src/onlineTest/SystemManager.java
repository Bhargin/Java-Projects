package onlineTest;
import java.io.*;
import java.util.*;


public class SystemManager implements Manager, Serializable {

	// Map stores all created exams.
	private Map<Integer, Exam> examMap= new HashMap<>();

	// Map stores all created students.
	private Map<String, Student> studentMap= new HashMap<>();

	// Arrays stores letter and numeric grade pertaining to a score.
	private String[] letter;
	private double[] scale;

	// Add exam with exam Id and title.
	public boolean addExam(int examId, String title) {

		// Return true if the exam does not exist.
		if(!examMap.containsKey(examId)) {
			Exam temp= new Exam(examId, title);

			// Create and add exam to examMap
			examMap.put(examId, temp);
			return true;

		}
		// Return false if exam already exists.
		return false;
	}

	// Add to false question to specified exam.
	public void addTrueFalseQuestion(int examId, int questionNumber, String text, double points, boolean answer) {

		// Get the with examId.
		Exam sample=examMap.get(examId);

		// Add question to the exam.
		sample.addTrueFalse(questionNumber, text, points, answer);	

		// Re-add the exam in examMap.
		examMap.put(examId, sample);
	}

	// Add multiple choice question to exam.
	public void addMultipleChoiceQuestion(int examId, int questionNumber, String text, double points, String[] answer) {

		Exam sample=examMap.get(examId);
		sample.addMultipleChoice(questionNumber, text, points, answer);	
		examMap.put(examId, sample);
	}

	// Add fill-in-the-blanks question to exam.
	public void addFillInTheBlanksQuestion(int examId, int questionNumber, String text, double points, String[] answer) {

		Exam sample=examMap.get(examId);
		sample.addFillInTheBlanks(questionNumber, text, points, answer);	
		examMap.put(examId, sample);
	}

	// Return exam key.
	public String getKey(int examId) {

		// Get the exam key for specified exam in examMap.
		String key=examMap.get(examId).getExamKey();
		return key;
	}

	// Add student.
	public boolean addStudent(String name) {

		// Return true if student does not exist. 
		if(!studentMap.containsKey(name)) {
			Student s1= new Student(name);

			// Create and add student to studentMap
			studentMap.put(name, s1);
			return true;
		}

		// Return false if student already exists
		return false;
	}

	// Answer true false question.
	public void answerTrueFalseQuestion(String studentName, int examId, int questionNumber, boolean answer) {

		// Get student from studentMap.
		Student s1= studentMap.get(studentName);

		// Add answer to student's answer list.
		s1.answerTrueFalse(questionNumber, answer, examId);

	}

	// Answer multiple choice question.
	public void answerMultipleChoiceQuestion(String studentName, int examId, int questionNumber, String [] answer) {

		// Get student from studentMap.
		Student s1= studentMap.get(studentName);

		// Add answer to student's answer list.
		s1.answerMultipleChoice(questionNumber, answer, examId);
	}

	// Answer fill-in-the-blanks question.
	public void answerFillInTheBlanksQuestion(String studentName, int examId, int questionNumber, String [] answer) {

		// Get student from studentMap.
		Student s1= studentMap.get(studentName);

		// Add answer to student's answer list.
		s1.answerFillInTheBlanks(questionNumber, answer, examId);
	}

	// Get student's exam score for specified exam.
	public double getExamScore(String studentName, int examId) {

		// Create a report for specified student and exam.
		Report r1= new Report(studentMap.get(studentName), examMap.get(examId), examId);

		// Grade student's exam attempt.
		r1.createReport(studentMap.get(studentName));

		// Return student's final score.
		return r1.getFinalScore();
	}

	// Get grading report for student and specified exam.
	public String getGradingReport(String studentName, int examId) {

		// Create a report for specified student and exam.
		Report r1= new Report(studentMap.get(studentName), examMap.get(examId), examId);

		// Return report.
		return r1.createReport(studentMap.get(studentName));
	}

	public void setLetterGradesCutoffs(String[] letterGrades, double[] cutoffs) {

		this.letter=letterGrades;
		this.scale=cutoffs;
	}

	// Get course numeric grade for student.
	public double getCourseNumericGrade(String studentName) {

		// If student exams have not been graded. 
		if(studentMap.get(studentName).getexamAverages()==0.0) {

			// Grade every exam student attempted.
			for(Integer exam: studentMap.get(studentName).getAttemptedExamSet()) {
				Report r1= new Report(studentMap.get(studentName), examMap.get(exam), exam);	

				// Create a report for each exam.
				r1.createReport(studentMap.get(studentName));
			}

			// Calculate and set student's final course grade.
			studentMap.get(studentName).setGrade(studentMap.get(studentName).calculateCourseGrade());

			// If student exams have been graded.
		} else {

			// Calculate and set student's final course grade.
			studentMap.get(studentName).setGrade(studentMap.get(studentName).calculateCourseGrade());
		}
		// Return student's final grade.
		return studentMap.get(studentName).getCourseGrade();
	}

	// Get student's course letter grade.
	public String getCourseLetterGrade(String studentName) {

		// Create a new letter grade object with specified scale.
		LetterGrade grade= new LetterGrade(this.letter, this.scale);

		// Return letter for student's course grade.
		return grade.getLetterGrade(studentMap.get(studentName).getCourseGrade());
	}

	// Get both numeric and letter grades for all students.
	public String getCourseGrades() {

		// TreeSet stores student names in sorted order
		TreeSet<String> students= new TreeSet<>();	
		Set<String>list= this.studentMap.keySet();	

		String result="";

		// Add all students in sorted order to treeSet.
		for(String name: list) {
			students.add(name);
		}

		// For every student in treeSet
		for(String name: students) {

			Student s1=studentMap.get(name);

			// Grade all attempted exams if not previously graded 
			if(s1.getexamAverages()==0.0) {
				for(Integer exam: s1.getAttemptedExamSet()) {
					Report r1= new Report(s1, examMap.get(exam), exam);	
					r1.createReport(s1);
				}

				// Calculate course grade;
				s1.setGrade(s1.calculateCourseGrade());

				// Else just get stduent's course grade.
			}else {
				s1.setGrade(s1.calculateCourseGrade());

			}

			// Calculate letter grade for student's final score.
			LetterGrade grade= new LetterGrade(this.letter, this.scale);
			result+= s1.name +" " 
					+ s1.getCourseGrade() 
					+" " + grade.getLetterGrade(s1.getCourseGrade()) + "\n";
		}

		return result;
	}

	// Get max score for exam.
	public double getMaxScore(int examId) {

		Set<String> student= studentMap.keySet();

		// Set stores all score for specified exam for all students.
		Set<Double> max= new HashSet<>();

		// For every student.
		for(String name: student) {
			Student s1= studentMap.get(name);

			// Create a reprot for student and specified exam.
			Report r1= new Report(s1, examMap.get(examId), examId);	
			r1.createReportStat(s1);

			// Add student's score to max.
			max.add(r1.getFinalScore());

		}

		// Return maximum value from the set.
		return  Math.round(Collections.max(max));
	}

	// Get min score for exam.
	public double getMinScore(int examId) {

		Set<String> student= studentMap.keySet();

		// Set stores all score for specified exam for all students.
		Set<Double> min= new HashSet<>();

		// For every student.
		for(String name: student) {
			Student s1= studentMap.get(name);

			// Create a report for student and specified exam.
			Report r1= new Report(s1, examMap.get(examId), examId);	
			r1.createReportStat(s1);

			// Add student's score to min.
			min.add(r1.getFinalScore());
		}

		// Return minimum value from the set.
		return Math.round(Collections.min(min));
	}

	// Get average for specified exam
	public double getAverageScore(int examId) {

		Set<String> student= studentMap.keySet();

		// Number of attempts for specified exam.
		int count=0;

		// Sum of students score for specified exam.
		double average=0;

		// For every student.
		for(String name: student) {
			Student s1= studentMap.get(name);

			// Create a report for student and specified exam.
			Report r1= new Report(s1, examMap.get(examId), examId);	
			r1.createReportStat(s1);
			count++;
			// Add student's score to average.
			average+=(r1.getFinalScore());
		}

		//Calculate average for specified exam.
		return Math.floor((average/count));
	}

	// Save Manager object.
	public void saveManager(Manager manager, String fileName) {

		try {

			FileOutputStream outFile = new FileOutputStream(fileName); 
			ObjectOutputStream output = new ObjectOutputStream(outFile); 

			output.writeObject(manager);
			output.close();
			outFile.close();

		} 

		catch(IOException exception){ 
			System.out.print("Error");
		}
	}

	// Restore manager object.
	public Manager restoreManager(String fileName) {

		SystemManager manager=null;

		try {

			FileInputStream inFile = new FileInputStream(fileName); 
			ObjectInputStream input = new ObjectInputStream(inFile); 

			manager = (SystemManager)input.readObject();
			input.close();
			inFile.close();

		} 

		catch(IOException exception){ 
			System.out.print("Error");

		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}

		return manager;
	}

}
