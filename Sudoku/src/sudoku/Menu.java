package sudoku;

import java.awt.EventQueue;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowListener;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingUtilities;

public class Menu extends JPanel implements ActionListener
{
	private JButton hint, undo, redo, gen, solve, save, open;
	private JRadioButton a,b;
	private JLabel entry, solving;
	private PopUp p;


	public Menu()
	{
		this.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.insets = new Insets(10, 10, 10, 10);

		hint = new JButton("Hint");
		hint.setActionCommand("hint");
		hint.addActionListener(this);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 0;
		this.add(hint, c);

		undo = new JButton("Undo");
		undo.setActionCommand("undo");
		undo.addActionListener(this);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 0;
		this.add(undo, c);

		redo = new JButton("Redo");
		redo.setActionCommand("redo");
		redo.addActionListener(this);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 0;
		this.add(redo, c);

		c.insets = new Insets(0, 0, 0, 0);
		a = new JRadioButton();
		a.setActionCommand("entry");
		a.addActionListener(this);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 4;
		c.gridy = 0;
		this.add(a, c);

		entry = new JLabel("Entry");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		c.gridy = 0;
		this.add(entry, c);

		c.insets = new Insets(10, 10, 10, 10);
		gen = new JButton("Generate");
		gen.setActionCommand("gen");
		gen.addActionListener(this);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 0;
		c.gridy = 1;
		this.add(gen, c);

		solve = new JButton("Solve");
		solve.setActionCommand("solve");
		solve.addActionListener(this);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 1;
		c.gridy = 1;
		this.add(solve, c);

		save = new JButton("Save");
		save.setActionCommand("save");
		save.addActionListener(this);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 2;
		c.gridy = 1;
		this.add(save, c);

		open = new JButton("Open");
		open.setActionCommand("open");
		open.addActionListener(this);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 3;
		c.gridy = 0;
		this.add(open, c);

		c.insets = new Insets(0, 0, 0, 0);
		b = new JRadioButton();
		b.setActionCommand("solving");
		b.addActionListener(this);
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 4;
		c.gridy = 1;
		this.add(b, c);

		solving = new JLabel("Solving");
		c.fill = GridBagConstraints.HORIZONTAL;
		c.gridx = 5;
		c.gridy = 1;
		this.add(solving, c);

		a.setSelected(true);
	}


	protected void updateUndoRedo() {
		redo.setEnabled(Window.getGameState().redo_enabled());
		undo.setEnabled(Window.getGameState().undo_enabled());
	}

	public void updateSolve() {
		solve.setEnabled(Window.mode);
	}

	public void updateHint() {
		hint.setEnabled(Window.mode);
	}

	public void updateGenerate() {
		gen.setEnabled(!Window.mode);
	}

	public void actionPerformed(ActionEvent e)
	{
		String eventName = e.getActionCommand();

		if(eventName.equals("hint")) {
			p = new PopUp( "Are you sure you want a hint?", "Yes", "No", false);
			if(p.getYesOrNo()) {
				Window.getGameState().hint();
				Window.board.repaint();
				
			}
		}

		if(eventName.equals("solve")) {

			new PopUp( "Are you sure you want to solve the puzzle?", "Yes", "No", false);
		}

		// Does not Work Todo
		if(eventName.equals("gen")) {
			p = new PopUp( "Generate a new puzzle (This puzzle will be lost)");
		//	p.repaint();
			int hints = p.getHints();
			
			if(hints!=-1) {
				Window.board.setBoard(Puzzle.createPuzzle());
				p.doneLoading();
				for(int i=1;i<=hints;i++) {
					Window.getGameState().hint();
				}
			}
			Window.board.repaint();



		if(eventName.equals("gen")) {
			
			// Open the popup and tell it to repaint on this thread
			PopUp pop = new PopUp("Generate a new puzzle (This puzzle will be lost)");
			pop.repaint();
			
			Runnable generatePuzzle = new Runnable() {
				public void run() {
					
					/*
					 * This is where the puzzle is supposed to be loaded
					 * into the GameState. Needs a bit more glue to work
					 */
					GameState gs = Window.getGameState();
					int[][] solution = Puzzle.createPuzzle();
					gs = new GameState(solution);
					pop.doneLoading();
				}
			};
			
			/*
			 * Wait until the popup is done painting before using the thread;
			 * otherwise the popup won't paint until the puzzle is ready
			 */
			EventQueue.invokeLater(generatePuzzle);
		}

		if(eventName.equals("save")) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.showSaveDialog(null);
			File f = fileChooser.getSelectedFile();
			try {
				Window.getGameState().save(f);
			} catch (Exception E) {
				if (f != null)
					new PopUp("Could not save file.", "Okay", "", true);
			}
		}

		if(eventName.equals("redo")) {
			Window.getGameState().redo();
			Window.repaintBoard();
		}

		if(eventName.equals("undo")) {
			Window.getGameState().undo();
			Window.repaintBoard();
		}

		if(eventName.equals("open")) {
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.showOpenDialog(null);
			File f = fileChooser.getSelectedFile();
			try {
				GameState g = GameState.load(f);
				g.setHistoryArrays();
				Window.setGameState(g);
				Window.repaintBoard();
			} catch (Exception E) {
				if (f != null)
					new PopUp("Could not load file.", "Okay", "", true);
			}
		}

		if(eventName.equals("entry")) {
			//toggles the solving button (b) if it is selected already
			if(Window.mode) {
				b.setSelected(false);
				p = new PopUp("Switching to Entry Mode will erase the current puzzle", "Ok", "Cancel", true);
				if(p.getYesOrNo()) {
					
					Window.mode = false;
					updateHint();
					updateGenerate();
					updateSolve();
					b.setSelected(false);
					Window.resetEmphasis();
				}else {
					a.setSelected(false);
					b.setSelected(true);
				}
			}

		}

		if(eventName.equals("solving")) {
			//toggles the entry button (a) if it is selected already
			Window.mode = true;
			int[][] temp = Puzzle.copyArray(Window.getGameState().placements);
			Window.getGameState().solution = Puzzle.solvePuzzle(temp);
			
			Window.getGameState().setHistoryArrays();

			updateHint();
			updateGenerate();
			updateSolve();
			a.setSelected(false);
		}

		updateUndoRedo();
	}
}
