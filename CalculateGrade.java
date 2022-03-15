//This assignment was created by 
//Dr. Aarathi Prasad on 9/9/21

//The goal of this assignment is to review the following 
//Java topics which you will be required to be familiar with 
//in order to complete the remaining programming assignments - 
//types, arithmetic expressions, variable scope, loops, 
//if/else, public/private, static/non-static methods, 
//arrays and ArrayLists operations, arrays pass-by-reference, file input/output

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class CalculateGrade {

	//STUDENT MUST ADD CODE TO THIS METHOD
	//TASK 1: STUDENT MUST ADD JAVA CODE TO DO THE FOLLOWING 
		//Loop through the file 
		//If you read midterm score, add it to exams[0]
		//If you read final exam score, add it to exams[1]
		//Every time you read a lab score, replace the first element in the labs array that contains a zero with this new score
		//Similarly, every time you read an asst score, replace the first element in the assts array that contains a zero with this new score

	// array is the array you'd like to use, score is score, and
	// index is the location you'd like to add it to (0 for midterms, 1 for finals here)
	static void addScoreToTotal(double[] array, double score, int index) {
			array[index] += score;
	}

	static void replaceZeroScore(double[] array, double score) {
		for (int i = 0; i < array.length; i++) {
			if (array[i] == 0) {
				array[i] = score;
				break;
			}
		}
	}

	private static void readFile(String filename, double[] labs, double[] assts, double[] exams) throws FileNotFoundException {
		//File is saved as a .csv and has the following format
		//Type of score, your score, total score
		//for e.g., lab, 83, 100
		//see test_scores.csv - DO NOT MODIFY test_scores.csv
		//to test with your own scores, modify the my_scores.csv file instead
		
		// How to access a file in java: https://www.javatpoint.com/how-to-open-a-file-in-java
		File scores = new File(filename);
		Scanner scanner = new Scanner(scores);
		// Can you use multiple delimiters in Java: https://stackoverflow.com/questions/4898263/multiple-delimiters-in-scanner-class-of-java
		scanner.useDelimiter(",|\\n");
		
		while (scanner.hasNext()) {
			String scoreType = scanner.next();
			// Java convert double stored as string to double: https://www.geeksforgeeks.org/convert-string-to-double-in-java/
			double score = Double.parseDouble(scanner.next());
			// totalScore is unused but the scanner must skip it to keep the order right
			double totalScore = Double.parseDouble(scanner.next());
			// process the token
			switch(scoreType) {
				case "lab":
					replaceZeroScore(labs, score);
					break;
				case "asst":
					replaceZeroScore(assts, score);
					break;
				case "midterm":
					addScoreToTotal(exams, score, 0);
					break;
				case "final":
					addScoreToTotal(exams, score, 1);
					break;
				default:
					System.out.println("Uncaught case in scanning.");
			}
			
		}
		scanner.close();
	}

	//STUDENT SHOULD NOT MODIFY THIS METHOD
	public static String myGrade(String filename, double[] labs, double[] assts, double[] exams) throws FileNotFoundException {
		//Reading grades into an ArrayList
		ArrayList<String> gradesList = new ArrayList<String>(Arrays.asList("A","A-","B+","B","B-","C+","C","C-","D+","D","F")); 

		//populate the different variables
		readFile(filename,labs,assts,exams);

		//obtain the total score for labs out of 100
		double lab_total = calculateLabGrade100(labs); 

		//obtain the total score for assignments out of 100
		double asst_total = calculateAsstGrade100(assts);

		//obtain the total score out of 100
		double total = calculateScaledTotal(exams, lab_total, asst_total); 

		//A print statement added just to see the scaled values for all the categories
		//Maybe commented out if unnecessary
		System.out.println("lab_total: " + lab_total+", asst_total: " + asst_total + ", total: " + total);
		return calculateGrade(total);
	}

	//STUDENT MUST ADD CODE TO THIS METHOD
	//TASK 2a: Compute the total score for labs and assignments
	//Use the arrays to compute your total score is out of 100
	//for e.g., if labs are 15% of the grade, you should
	//compute the total sum of labs using the weights 
	//for example if all labs are weighted equally then 
	//you just add up all the values and divide by the total
	//but since your lab scores are weighted, you need to apply the weight
	//to each value.

	//About lab grading from the syllabus: 
	//You can earn up to 100 points on each lab worksheet. Lab worksheets are 15% of your grade. There will be 8 labs. 
	//The four lowest scores will be equally weighed at 10% and 
	//the four higher scores will be equally weighted at 15%. 
	//So, if you scored 80, 90, 70, 75, 85, 90, 100, 100, and 
	//your lab worksheets were all weighed equally, you would earn 690/8 = 86.25 points. 
	//With the adjusted weights, you would instead earn 8+ 13.5 + 7 +7.5 + 8.5 + 13.5 + 15 + 15 = 88 points.

	public static double calculateLabGrade100(double[] labs) {
		// Ensure lowest scores are in the lowest indices for weighting
		Arrays.sort(labs);
		double result = 0;

		for (int i = 0; i < labs.length; i++) {
			if(i < 4) {
				result += labs[i] * .1;
			} else {
				result += labs[i] * .15;
			}
		}

		//replace return value with the value you compute
		return result;
	}

	//STUDENT MUST ADD CODE TO THIS METHOD
	//About assignment grading from the syllabus:
	//You can earn up to 100 points on each assignment. Programming assignments are 30% of your grade. 
	//There will be 6 programming assignments. Assignment 0 would be at 10%, among the remaining, the lower two grades would 
	//be weighted at 16% and the higher two grades would be weighted at 20% and the remaining one at 18%.
	//So, if you scored 70, 75, 80, 90, 100 and 100, with equal weights, you would earn 515/6 = 85.83 points. 
	//With the adjusted weights, you would earn 7 + 12 + 12.8 + 16.2+ 20 + 20 = 88 points
	public static double calculateAsstGrade100(double[] assts) {
		// Start result off with the weighted assignment 0 value
		double result = assts[0] * .1;

		// Make new array without assignment 0 score for sorting
		double[] remainingScores = new double[5];
		for (int i = 1; i < assts.length; i++) {
			remainingScores[i - 1] = assts[i];
		}

		// Sort remaining arrays to prepare for weighting
		Arrays.sort(remainingScores);

		// Apply appropriate weights and add to result
		for (int i = 0; i < remainingScores.length; i++) {
			if (i < 2) {
				result += remainingScores[i] * .16;
			} else if (i == 2) {
				result += remainingScores[i] * .18;
			} else {
				result += remainingScores[i] * .2;
			}
		}

		return result;
	}

	//STUDENT MUST ADD CODE TO THIS METHOD
	//About exam grading from the syllabus.
	//You can earn up to 100 points on each exam. 
	//Midterm is 25% of your grade and the final is 30% of your grade.
	//double exam_total = (.25*exams[0]) + (.3*exams[1]);

	//TASK2b: Compute the scaled score for labs, assignments, midterm and final exam
	//Once you have computed the score for each category out of 100, you need to now
	//scale it to what their percentage is. For example, labs are 15% of the grade,
	//so if you scored 88 points out of 100 for labs, your labs score will be .88*15 = 13.2
	public static double calculateScaledTotal(double[] exams, double lab_total, double asst_total) {
		// Add all categories with the appropriate weights
		double exam_total = (.25*exams[0]) + (.3*exams[1]);
		double result = exam_total;

		result += (lab_total * .15) + (asst_total * .3);
		
		//replace return value with the value you compute
		return result;
	}

	//STUDENT MUST ADD CODE TO THIS METHOD
	public static String calculateGrade (double total) {
		String grade;

		//TASK2c: Compute the grade
		//Once you have computed the scaled version of all the 
		//scores, i.e., you have a lab score out of 15, assignment score out of 30, 
		//the midterm out of 25 and the final exam out of 30,
		//add them up to get a score T out of 100
		//use the following ranges 
		//to assign a grade based on the value of T
		//95<=T<100	A
		//90<=T<95	A-
		//85<=T<90	B+ 
		//80<=T<85	B
		//75<=T<80	B-
		//70<=T<75	C+ 
		//65<=T<70	C
		//60<=T<65	C-
		//55<=T<60	D+
		//50<=T<55	D
		//<50 	 		F
		
		// It's ok that we only check the lower value b/c once a match is made,
		// the program leaves the logic block and doesn't evaluate further
		if (total >= 95) {
			grade = "A";
		} else if (total >= 90) {
			grade = "A-";
		} else if (total >= 85) {
			grade = "B+";
		} else if (total >= 80) {
			grade = "B";
		} else if (total >= 75) {
			grade = "B-";
		} else if (total >= 70) {
			grade = "C+";
		} else if (total >= 65) {
			grade = "C";
		} else if (total >= 60) {
			grade = "C-";
		} else if (total >= 55) {
			grade = "D+";
		} else if (total >= 50) {
			grade = "D";
		} else {
			grade = "F";
		}
		
		return grade;
	}

	//STUDENT MUST ADD CODE TO THIS METHOD
	public static void runTests(double[] labs, double[] assts, double[] exams) throws FileNotFoundException {
		boolean allTestsPassed = true;
		readFile("test_scores.csv",labs,assts,exams);
		//TEST 1a - Checking if midterm value is read correctly!
		if (exams[0] != 90.0) {
			System.out.println("ERROR in Test 1a: Incorrect value in midterm.");
			System.out.println("Expected to find a value of 90, but found " + exams[0] + " instead");
			allTestsPassed = false;
		}
		/////
		//STUDENT MUST ADD a test 1b to check if final exam value is read correctly
		//
		//ADD TEST 1b CODE HERE
		// Very similar to 1a except final is being checked
		if (exams[1] != 95) {
			System.out.println("ERROR in Test 1b: Incorrect value in final exam.");
			System.out.println("Expected to find a value of 95, but found " + exams[1] + " instead");
			allTestsPassed = false;
		}
		//END OF TEST 1b 
		/////
		//TEST 2a- Checking if labs are read correctly
		if (labs[1] != 90.0 || labs[2]!= 70.0) {
			System.out.println("ERROR in Test 2a: Incorrect values read into labs array.");
			System.out.println("Expected to find values of labs[1]=90.0 and labs[2]=70.0, but found labs[1]=" + labs[1] + " and labs[2]="+ labs[2] + " instead");
			allTestsPassed = false;
		}
		/////
		//STUDENT MUST ADD a test 2b to check if assignment values are read correctly
		//
		//ADD TEST 2b CODE HERE
		// Compare each read asst value to the expected value to make sure program is functioning correctly
		if (assts[0] != 70 || assts[1] != 75 || assts[2] != 80 || assts[3] != 90 || assts[4] != 100 || assts[5] != 100) {
			System.out.println("ERROR in Test 2b: Incorrect values read into assignment array.");
			System.out.println("Expected to find values of assts[0]=70.0, assts[1]=75.0, assts[2]=80.0, assts[3]=90.0, assts[4]=100.0, assts[5]=100.0, but found assts[0]=" + assts[0] + ", assts[1]="+ assts[1] + ", assts[2]=" + assts[2] + ", assts[3]=" + assts[3] + ", assts[4]=" + assts[4] + ", and assts[5]=" + assts[5] + " instead");
			allTestsPassed = false;
		}
		/////
		//Test 3a- Checking if weighted lab score out of 100 is correct
		double lab_total = calculateLabGrade100(labs); 
		if (lab_total != 88) {
			System.out.println("Test 3A failed: Calculation of weighted lab score out of 100 is incorrect.");
			System.out.println("Expected 88, but got " + lab_total + " instead.");
			allTestsPassed = false;
		}
		//Test 3b- Checking if weighted assignment score out of 100 is correct
		double asst_total = calculateAsstGrade100(assts); 
		if (asst_total != 88) {
			System.out.println("Test 3B failed: Calculation of weighted assignment score out of 100 is incorrect.");
			System.out.println("Expected 88, but got " + asst_total + " instead.");
			allTestsPassed = false;
		}
		//Test 4- Checking if scaled total score out of 100 is correct
		double scaled_total = calculateScaledTotal(exams, lab_total, asst_total); 
		if (scaled_total != 90.6) {
			System.out.println("Test 4 failed: Calculation of scaled total of 100 is incorrect.");
			System.out.println("Expected 94.6, but got " + scaled_total + " instead.");
			allTestsPassed = false;
		}

		//Test 5- Checking if grade is correct
		String grade = calculateGrade(scaled_total);
		if (grade.compareTo("A-") != 0)
		{
			System.out.println("Test 5 failed: Calculation of grade is incorrect.");
			System.out.println("Expected A-, but got " + grade + " instead");
			allTestsPassed = false;
		}
		
		//If allTestsPassed is still true, then all tests have passed
		//and print so!
		if (allTestsPassed)
			System.out.println("All tests passed!");

	}

	//STUDENT SHOULD NOT MODIFY THIS METHOD
	//This method enters zero in all the arrays
	public static void zeroTheArray(double[] arr) {
		for (int i=0; i<arr.length; ++i)
			arr[i] = 0.0;
	}

	//STUDENT SHOULD NOT MODIFY THIS METHOD
	public static void main (String[] args) throws FileNotFoundException {
		
		double labs[] = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		double assts[] = {0.0, 0.0, 0.0, 0.0, 0.0, 0.0};
		double exams[] = {0.0, 0.0};

		//Runs all tests to make sure your code works correctly with test_scores.csv
		//IMPORTANT: make sure you have not modified test_scores.csv
		//If you think you have, re-download the .csv file from theSpring
		runTests(labs,assts,exams);

		zeroTheArray(labs);
		zeroTheArray(assts);
		zeroTheArray(exams);

		Scanner scn = new Scanner(System.in);
		System.out.println("Enter a filename, including the .csv extension, for example my_scores.csv");
		String filename = scn.nextLine();
		System.out.println("Your grade, based on scores in "+ filename + " is : " + myGrade(filename,labs,assts,exams));
		scn.close();
	}
}