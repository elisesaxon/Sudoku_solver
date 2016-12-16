//
// Elise Saxon
//
// CSCI 145
// Assignment 1
// April 25, 2015
//
// Sudoku Solver
// This program reads in a user-specified file (containing a sudoku puzzle), transfers the info
// into a 9x9 array, and uses recursion to solve the puzzle and print it out once it's complete
// or print a message that the puzzle cannot be solved, if there is no solution.
//

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class SolveSudoku {
	// MAIN FUNCTION
	// The main function calls a function to check the commandline arguments, then initiates
	// and calls a function to fill an array with the initial puzzle. Then it calls a solve
	// function to start recursion and attempt to solve the puzzle. It then prints the result. 
	public static void main (String[] args) {
		//turn commandline argument into variable
		String fileName = handleArgs(args);
		
		//initiate empty 9x9 array to fill with sudoku numbers
		int[][] sudokuPuzzle = new int[9][9];
		
		//open user-specified file, fill empty array with numbers
		fillPuzzle(fileName, sudokuPuzzle);
		
		//iterate through rows and columns to call checkPuzzle to see if puzzle input format
		//is valid to begin with (not more than one of a specific number per row, col, 3x3 box)
		for (int r = 0; r < 9; r++) {
			for (int c = 0; c < 9; c++) {
				//there will be multiple 0s per row, col, and box to begin
				if (sudokuPuzzle[r][c] != 0) {
					if ((checkPuzzle(sudokuPuzzle, r, c)) == false) {
						System.out.println("Puzzle has no solution.");
						System.exit(0);
					}
				}
			}
		}
		
		//start recursion at 0 for rows and columns
		int row = 0;
		int col = 0;
		
		//puzzle solved
		if (solve(sudokuPuzzle, row, col)) {
			//print array
			System.out.println("Puzzle solved!");
			//iterate through rows to print
			for (int i = 0; i < 9; i++) {
				//printSolved iterates through columns
				printSolved(sudokuPuzzle, i);
			}
		} else {
			//no solution
			System.out.println("Puzzle has no solution.");
		}
	}
	
	
	//FUNCTION: handleArgs
	//Input: commandline args | Output: variable storing file name
	// Intakes commandline arguments and, if user has entered only one argument, saves 
	// file name as "file" and returns "file" to main
	public static String handleArgs (String[] args) {
		String file = "";
		if (args.length != 1){
			//invalid number of arguments on commandline
			System.out.println("Enter one filename on commandline");
			System.out.println("Usage: java SolveSudoku filename");
			System.exit(0);
		} else {
			//set "file" variable to file name provided by user
			file = args[0];
		}
		return file;
	}
	
	
	//FUNCTION: fillPuzzle
	//Input: string of file name, empty puzzle array | Output: nothing (array changed in main)
	// This function opens the puzzle file, reads its contents, and fills the empty array with 
	// the numbers. 
	public static void fillPuzzle (String fileName, int[][] sudokuPuzzle) {
		String nums = "";
		try {
			File numFile = new File(fileName);
			Scanner input = new Scanner(numFile);
			//if readable and file contains info, extract info
			if (numFile.canRead()) {
				while(input.hasNext()) {
					//fill 9x9 array with file's info (as integers)
					for (int i = 0; i < 9; i++) {
						for (int j = 0; j < 9; j++) {
							sudokuPuzzle[i][j] = Integer.parseInt(input.next());
						}
					}
				}
			} else {
				//can't read file
				System.out.println("File '" + fileName + "' cannot be read.");
				System.out.println("Usage: java SolveSudoku filename");
				System.exit(0);
			}
		} catch (FileNotFoundException ex) {
			//file does not exist
			System.out.println("File '" + fileName + "' was not found.");
			System.exit(0);
		}
	}
	
	
	//FUNCTION: 
	//Input: full array of puzzle, current row and column | Output: true (valid puzzle) or false (invalid puzzle)
	// This function checks to make sure that the initial empty puzzle is workable. It checks for more than one
	// of the same number in a row, column, or box before it tries to solve it. If the puzzle has no repeated 
	// numbers in rows, columns, or boxes, it returns true and the main function will be able to call the solve function.
	public static boolean checkPuzzle(int[][] sudokuPuzzle, int row, int col) {
		//check with the column
		for (int c = 0; c < 9; c++) {
			//can't have the same number, must account for checking a number against itself
			if ((sudokuPuzzle[row][col] == sudokuPuzzle[row][c]) && (c != col)) {
				//more than one of that number in the column
				return false;
			}
		}
		//check with the row
		for (int r = 0; r < 9; r++) {
			//must account for checking a number against itself
			if ((sudokuPuzzle[row][col] == sudokuPuzzle[r][col]) && (r != row)) {
				//more than one of that number in the row
				return false;
			}
		}
		//check with each 3x3 box
		int boxRow = (row/3)*3;
		int boxCol = (col/3)*3;
		for (int r = boxRow; r < (boxRow + 3); r++) {
			for (int c = boxCol; c < (boxCol + 3); c++) {
				//account for checking a number against itself
				if ((sudokuPuzzle[row][col] == sudokuPuzzle[r][c]) && (r != row) && (c != col)) {
					//more than one of that number in the sub-box
					return false;
				}
			}
		} 
		//initial format of puzzle is valid
		return true;
	}
	
	
	//FUNCTION: solve
	//Input: puzzle, current row, current column | Output: true (solved) or false (not finished)
	// Overall, the solve function checks to see if the puzzle has been solved or not. If the puzzle has 
	// been solved, the function returns true to main. It checks if a space has a permanent number in it
	// and if not, it iterates through numbers 1 - 9 and calls the function checkNum to see if the number
	// works in that space. If so, it sets that number to that space and calls solve recursively again for the
	// next space to the right in the puzzle. If no numbers work in a space, the function sets that space to 0
	// so it can keep being manipulated and starts solve again. 
	public static boolean solve (int[][] sudokuPuzzle, int row, int col) {
		//you've passed the end of the row
		if (col == 9) {
			col = 0;
			//go to next row
			row ++;
		}
		//you've passed the last row
		if (row == 9) {
			//puzzle solved (return true to main)
			return true;
		}
		//permanent number there. Move one place to the right, start solve again
		if (sudokuPuzzle[row][col] != 0) {
			return solve(sudokuPuzzle, row, (col + 1));
		//can try a number
		} else {
			//i is the number you're trying for that box (iterate through nums 1-9)
			for (int num = 1; num <= 9; num++) {
				//call checkNum function to see if i works
				if (checkNum(sudokuPuzzle, num, row, col)) {
					//if i works, set it to that position
					sudokuPuzzle[row][col] = num;
					//check if puzzle is solved (start loop again)
					if (solve(sudokuPuzzle, row, (col + 1))) {
						return true;
					}
				}
			}
			//if no number i 1-9 works, step back, make this space manipulatable
			sudokuPuzzle[row][col] = 0;
			//puzzle not solvable if, after all above, no i works
			return false;
		}
	}
	
	
	//FUNCTION: checkNum
	//Input: puzzle, current num being tried, current row, current column | Output: true (num works) 
	//or false (num doesn't work)
	// This function is called by the solve function in order to check if the number the program is testing
	// does not conflict with any other numbers already in the row, column, and 3x3 box. If there are no 
	// conflicts, checkNum returns true. If there is a conflict, it returns false so solve will send another
	// number to check.
	public static boolean checkNum (int[][] sudokuPuzzle, int num, int row, int col) {
		//check in row
		for (int i = 0; i < 9; i++) {
			if (sudokuPuzzle[i][col] == num) {
				return false;
			}
		}
		//check in col
		for (int i = 0; i < 9; i++) {
			if (sudokuPuzzle[row][i] == num) {
				return false;
			}
		}
		//check in 3x3 boxes
		int boxRow = (row/3)*3;
		int boxCol = (col/3)*3;
		for (int i = boxRow; i < (boxRow + 3); i++) {
			for (int j = boxCol; j < (boxCol + 3); j++) {
				if (sudokuPuzzle[i][j] == num) {
					return false;
				}
			}
		}
		//if none of these catch anything, num works.
		return true;
	}
	
	
	//FUNCTION: printSolved
	//Input: final puzzle, current row (line) to print | Output: nothing (prints to terminal)
	// This function intakes the final puzzle and the current row to print, and iterates through 
	// the columns, printing each character in the row and then starting a new line.
	public static void printSolved(int[][] sudokuPuzzle, int line) {
		//iterate through columns, print each number and a space
		for (int i = 0; i < 9; i++) {
			System.out.print(sudokuPuzzle[line][i] + " ");
		}
		//new line after full row printed
		System.out.println();
	}

}