package model;

import java.util.Random;

/* This class must extend Game */
public class ClearCellGame extends Game {

	private Random randomNum; 
	private int strategy;
	private int score;


	public ClearCellGame(int maxRows, int maxCols, Random random,
			int strategy) {
		super(maxRows, maxCols);
		randomNum = random;
		score = 0;
		this.strategy = strategy;
	}

	@Override
	public boolean isGameOver() {
		for (int i = 0; i < board[0].length; i++) {
			if (board[board.length - 1][i] != BoardCell.EMPTY) {
				return true;
			}
		}
		
		return false;

		
	}

	@Override
	public int getScore() {
		return score;
	}



	@Override
	public void nextAnimationStep() {
	    // Check if the game is not over
	    if (!isGameOver()) {
	        // Create a new board with the same dimensions
	        BoardCell[][] newBoard = new BoardCell[board.length][board[0].length];
	        
	        // Shift each row down by one position
	        for (int i = 0; i < newBoard.length; i++) {
	            for (int j = 0; j < newBoard[0].length; j++) {
	                // For the first row, generate a non-empty random board cell
	                if (i == 0) {
	                    newBoard[i][j] = BoardCell.getNonEmptyRandomBoardCell(randomNum);
	                } else {
	                    // For other rows, copy the cell from the row above
	                    newBoard[i][j] = board[i - 1][j];
	                }
	            }
	        }
	        
	        // Update the board with the new configuration
	        board = newBoard;
	    }
	}

	@Override
	public void processCell(int rowIndex, int colIndex) {
	    // Check if the game is not over or the selected cell is empty
	    if (!isGameOver() || board[rowIndex][colIndex] == BoardCell.EMPTY) {
	        // Get the selected cell
	        BoardCell selectedCell = board[rowIndex][colIndex];
	        
	        // Clear adjacent cells and get the score increment
	        int scoreIncrement = clearAdjacentCells(rowIndex, colIndex, selectedCell);
	        
	        // Set the selected cell as empty
	        board[rowIndex][colIndex] = BoardCell.EMPTY;
	        
	        // Update the score
	        score += scoreIncrement + 1;
	        
	        // Collapse empty rows
	        collapseEmptyRows();
	    }
	}

	private int clearAdjacentCells(int rowIndex, int colIndex, BoardCell selectedCell) {
	    // Initialize the score increment
	    int scoreIncrement = 0;
	    
	    // Define the directions to check for adjacent cells
	    int[][] directions = {
	            {-1, 0},  // Up
	            {1, 0},   // Down
	            {0, -1},  // Left
	            {0, 1},   // Right
	            {-1, -1}, // Diagonal Up-left
	            {-1, 1},  // Diagonal Up-right
	            {1, -1},  // Diagonal Down-left
	            {1, 1}    // Diagonal Down-right
	    };
	    
	    // Iterate over each direction
	    for (int[] direction : directions) {
	        int dRow = direction[0];
	        int dCol = direction[1];
	        
	        // Start from the adjacent cell
	        int newRow = rowIndex + dRow;
	        int newCol = colIndex + dCol;
	        
	        // Clear cells while they are valid and have the selected cell type
	        while (isValidCell(newRow, newCol) && board[newRow][newCol] == selectedCell) {
	            board[newRow][newCol] = BoardCell.EMPTY;
	            scoreIncrement++;
	            newRow += dRow;
	            newCol += dCol;
	        }
	    }
	    
	    // Return the score increment
	    return scoreIncrement;
	}

	private boolean isValidCell(int row, int col) {
	    // Get the maximum number of rows and columns
	    int numRows = getMaxRows();
	    int numCols = getMaxCols();
	    
	    // Check if the given row and column are within the valid range
	    return row >= 0 && row < numRows && col >= 0 && col < numCols;
	}

	private void collapseEmptyRows() {
	    // Get the maximum number of rows and columns
	    int numRows = getMaxRows();
	    int numCols = getMaxCols();
	    
	    // Initialize the empty row index
	    int emptyRow = 0;
	    
	    // Iterate over each row
	    for (int row = 0; row < numRows; row++) {
	        // Check if the row is empty
	        boolean rowIsEmpty = true;
	        for (int col = 0; col < numCols; col++) {
	            if (board[row][col] != BoardCell.EMPTY) {
	                rowIsEmpty = false;
	                break;
	            }
	        }
	        
	        // If the row is not empty, collapse it by shifting it to the empty row index
	        if (!rowIsEmpty) {
	            // Check if the current row needs to be collapsed
	            if (emptyRow != row) {
	                // Copy each cell to the empty row
	                for (int col = 0; col < numCols; col++) {
	                    board[emptyRow][col] = board[row][col];
	                }
	            }
	            // Increment the empty row index
	            emptyRow++;
	        }
	    }
	    
	    // Clear the remaining rows after the empty row index
	    for (int row = emptyRow; row < numRows; row++) {
	        for (int col = 0; col < numCols; col++) {
	            board[row][col] = BoardCell.EMPTY;
	        }
	    }
	}

}

