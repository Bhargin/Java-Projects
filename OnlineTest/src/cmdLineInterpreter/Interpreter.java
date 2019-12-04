package cmdLineInterpreter;

import java.util.*;

import onlineTest.SystemManager;

/**
 * 
 * By running the main method of this class we will be able to
 * execute commands associated with the SystemManager.  This command
 * interpreter is text based. 
 *
 */
public class Interpreter {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.print("Welcome to Online Test Manager\n\n");

		SystemManager manager = new SystemManager();
		Scanner in= new Scanner(System.in);
		int choice;
		boolean repeat=false;

		// Do-while loop.
		do {

			// Print out all of options
			System.out.print(" \n 1. Add Student"  + '\n'  + " 2. Add an Exam" + '\n'  + " 3. Add a true/false question" + '\n'  
					+ " 4. Answer a true/false question" + '\n'  + " 5. Get the exam score for a student"
					+ '\n'  + " 6. Exit" + "\n\n Enter Choice: ");

			// Set int choice to scanner input.
			choice=in.nextInt();

			// Switch depending on int choice
			switch(choice) {

			// Add student.
			case(1):
				System.out.print("\nEnter Student Name: ");
			String s1=in.next();
			manager.addStudent(s1);

			break;

			// Add Exam.
			case(2):
				System.out.print("\nEnter Exam Id: ");
			int Id= in.nextInt();

			System.out.print("\nEnter Exam Title: ");
			String title=in.next();

			manager.addExam(Id, title);

			break;

			// Add TrueFalseQuestion.
			case(3):
				System.out.print("\nEnter Exam Id: ");
			int examId= in.nextInt();
			in.nextLine();

			System.out.print("\nEnter Question Number: ");
			int question = in.nextInt();
			in.nextLine();

			System.out.print("\nEnter Question Text: ");
			String text= in.next();

			System.out.print("\nEnter Points: ");
			double points=in.nextDouble();

			System.out.print("\nEnter Correct Answer: ");
			boolean correct = in.nextBoolean();

			manager.addTrueFalseQuestion(examId, question, text, points, correct);
			break;

			// Answer TrueFalse Question.
			case(4):
				System.out.print("\nEnter Student Name: ");
			String name=in.next();

			System.out.print("\nEnter Exam Id: ");
			int examId2= in.nextInt();

			System.out.print("\nEnter Question Number: ");
			int questionNumber = in.nextInt();

			System.out.print("\nEnter Student Answer: ");
			boolean studentAnswer = in.nextBoolean();

			manager.answerTrueFalseQuestion(name, examId2, questionNumber, studentAnswer);
			break;

			// Get ExamScore.
			case(5):

				System.out.print("\nEnter Student Name: ");
			String studentName=in.next();

			System.out.print("\nEnter Exam Id: ");
			int examId3= in.nextInt();

			System.out.print("Exam Score: " + manager.getExamScore(studentName, examId3));
			break;

			// End all processing.
			case(6):
				repeat=true;
			System.out.print("\n\nManager Terminated");
			break;

			// Default option.
			default: 

				System.out.print("\nChose from exisitng choices.\n");
			}

		}while(!repeat);

		in.close();
	}

}
