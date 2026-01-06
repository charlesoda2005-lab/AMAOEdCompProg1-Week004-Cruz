import java.util.Scanner;
public class Grading {
    public static void main(String[] args) {
        try (Scanner sc = new Scanner(System.in)) {
            System.out.print("Please enter your score: ");
            int score = sc.nextInt();

            double numericGrade = 0.0;
            if (score >= 96) {
                System.out.println("Your grade is A+ Excellent");
                numericGrade = 1.00;
            } else if (score >= 91) {
                System.out.println("Your grade is A Very Good");
                numericGrade = 1.25;
            } else if (score >= 86) {
                System.out.println("Your grade is A- Very Good");
                numericGrade = 1.50;
            } else if (score >= 81) {
                System.out.println("Your grade is B+ Good");
                numericGrade = 1.75;
            } else if (score >= 75) {
                System.out.println("Your grade is B Good");
                numericGrade = 2.00;
            } else if (score >= 69) {
                System.out.println("Your grade is B- Good");
                numericGrade = 2.25;
            } else if (score >= 63) {
                System.out.println("Your grade is C+ Fair");
                numericGrade = 2.50;
            } else if (score >= 57) {
                System.out.println("Your grade is C Fair ");
                numericGrade = 2.75;
            } else if (score >= 50) {
                System.out.println("Your grade is C- Fair");
                numericGrade = 3.00;
            } else {
                System.out.println("Your grade is F Failed");
                numericGrade = 5.00;
            }
            System.out.println("Your average is " + numericGrade);
        }
    }
}