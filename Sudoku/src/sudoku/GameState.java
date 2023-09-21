package sudoku;

import java.io.*;
import java.util.Stack;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;

public class GameState
{
	public int[][] solution;
	public Cell[][] gameBoard;
	public int[][] placements = new int[9][9];
	public boolean[][] place = new boolean[9][9];  

	
	private class Change
	{
		Cell modifiedCell;
		int indexX;
		int indexY;

		Change(Cell mod, int x, int y)
		{
			modifiedCell = mod;
			indexX = x;
			indexY = y;
		}
	}

	private Stack<Change> history_undo;
	private Stack<Change> history_redo;

	// for XML conventions; do not use
	public GameState()
	{
		System.out.println("Warning: Please use the other GameState constructor");
	}

	public GameState(int[][] newSolution)
	{
		solution = newSolution;
		history_undo = new Stack<>();
		history_redo = new Stack<>();
		gameBoard = new Cell[9][9];

		for (int x = 0; x < 9; x++)
		{
			for (int y = 0; y < 9; y++)
			{
				gameBoard[x][y] = new Cell();
			}
		}
	}

	public void hint() {
		boolean blank = false;
		int r;
		int c;
		System.out.println("1");
		do {
			r = (int)(Math.random()*9);
			c = (int)(Math.random()*9);
			System.out.println(placements[r][c]);
			if(placements[r][c] == 0) {
				blank = true;
			}
		}
		while(!blank);
		System.out.println("2");
		gameBoard[r][c].setState(solution[r][c]);
	}
	public Cell[][] getBoard()
	{
		return gameBoard;
	}

	public void resetEmphasis()
	{
		for (int x = 0; x < 9; x++)
		{
			for (int y = 0; y < 9; y++)
			{
				gameBoard[x][y].resetEmphasis();
			}
		}
	}

	public void setCell(Cell newCell, int row, int col)
	{
		Change c = new Change(
			gameBoard[row][col], row, col
		);

		history_undo.push(c);
		history_redo.clear();
		gameBoard[row][col] = newCell;
  }

	public Cell getCell(int indexX, int indexY)
	{
		return gameBoard[indexX][indexY];
	}
	
	public int[][] getSolution() {
		return solution;
	}

	public void setSolution(int[][] solution) {
		this.solution = solution;
	}

	public Cell[][] getGameBoard() {
		return gameBoard;
	}

	public void setGameBoard(Cell[][] gameBoard) {
		this.gameBoard = gameBoard;
	}

	public void undo()
	{
		Change c = history_undo.pop();
		Change redo = new Change(
			gameBoard[c.indexX][c.indexY],
			c.indexX, c.indexY
		);

		history_redo.push(redo);
		gameBoard[c.indexX][c.indexY] = c.modifiedCell;
	}

	public void redo()
	{
		Change c = history_redo.pop();
		Change undo = new Change(
			gameBoard[c.indexX][c.indexY],
			c.indexX, c.indexY
		);

		history_undo.push(undo);
		gameBoard[c.indexX][c.indexY] = c.modifiedCell;
	}

	public boolean undo_enabled()
	{
		return !history_undo.isEmpty();
	}

	public boolean redo_enabled()
	{
		return !history_redo.isEmpty();
	}

	public void setHistoryArrays() {
		history_undo = new Stack<>();
		history_redo = new Stack<>();
	}
	
	public void save(File f) throws Exception
	{
		FileOutputStream FOS = new FileOutputStream(f);
		XMLEncoder E = new XMLEncoder(FOS);

		E.writeObject(this);
		E.close();
		FOS.close();
	}

	public static GameState load(File f) throws Exception
	{
		
		
		
		FileInputStream FIS = new FileInputStream(f);
		XMLDecoder D = new XMLDecoder(FIS);
		GameState g = (GameState) D.readObject();

		D.close();
		FIS.close();
		return g;
	}
}
