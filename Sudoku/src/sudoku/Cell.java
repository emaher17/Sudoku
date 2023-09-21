package sudoku;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

public class Cell {
	static int cellSide = 72;
	private int state;
	private boolean[][] emphasis = new boolean[3][3];
	private int count;
	//private boolean changeable;

	public Cell() {
		state = 0;
		resetEmphasis();
	}

	public Cell makeCopy() {
		Cell copy = new Cell();
		copy.state = this.state;
		boolean[][] newemphasis = new boolean[3][3];

		for (int x = 0; x < 3; x++)
		{
			for (int y = 0; y < 3; y++)
			{
				newemphasis[x][y] = emphasis[x][y];
			}
		}

		copy.emphasis = newemphasis;
		copy.count = this.count;
		return copy;
	}



	public void resetEmphasis() {
		for(int r=0;r<3;r++) {
			for(int c=0;c<3;c++) {
				emphasis[r][c] = true;
			}
		}
		count = 9;
	}

	public Cell(int state) {
		this.state = state;
	}

	private void setState() {
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				if(emphasis[i][j]) {
					state = j + i * 3 + 1;
				}
			}
		}
	}
	
	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public boolean[][] getEmphasis() {
		return emphasis;
	}

	public void setEmphasis(boolean[][] emphasis) {
		this.emphasis = emphasis;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
  
	public void click(int r, int c, boolean solvingMode) {
		if(solvingMode) {
			if(state!=0) {
				state = 0;
			}
			else if(emphasis[r][c]&&count==1) {
				setState();
			}
			else if(emphasis[r][c]) {
				emphasis[r][c] = false;
				count--;
				if(count==1) {
					setState();
				}
			}
			else if(!emphasis[r][c]){
				emphasis[r][c] = true;
				count++;
				if(count==1) {
					setState();
				}
			}
		} else {
			if(state!=0) {
				state = 0;
			} else {
				state = c + r*3 + 1;
			}
		}
	}

	public void draw(Graphics g, boolean red, int row, int col) {
		Font f = new Font("Roboto", Font.PLAIN, 30);
		g.setFont(f);
		int width = g.getFontMetrics().stringWidth(""+state);
		int height = g.getFontMetrics().getHeight();
		if(state==0) {
			f = new Font("Roboto", Font.PLAIN, 12);
			g.setFont(f);
			width = g.getFontMetrics().stringWidth(""+0);
			height = g.getFontMetrics().getHeight();
			for(int r=0;r<3;r++) {
				for(int c=0;c<3;c++) {
					if(emphasis[r][c]) {
						g.setColor(Color.white);
					} else {

						g.setColor(Color.lightGray);
					}
					g.fillRect(col*cellSide+cellSide*c/3, row*cellSide+cellSide*r/3,cellSide/3, cellSide/3);
					g.setColor(Color.black);
					g.drawString("" + (c + r*3 + 1), col*cellSide+cellSide*c/3+width/2+4, row*cellSide+cellSide*r/3+height/2+8);
				}
			}
		} else {
			if(!red) {
				g.setColor(Color.red);
			}
			else {
				g.setColor(Color.white);
			}
			g.fillRect(col*cellSide, row*cellSide, cellSide, cellSide);
			g.setColor(Color.black);
			g.drawString(""+state, col*cellSide+width/2+16, row*cellSide+height/2+30);
		}
	}
}
