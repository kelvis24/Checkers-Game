package checkers;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public abstract class Heuristic {
	
	private String name;
	private String description;
	
	public Heuristic() {
	}
	
	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getDescription() {
		return this.description;
	}
	
	public String getName() {
		return this.name;
	}
	
	public abstract int HeuristicValue(checkerGame game, JButton []cell, ImageIcon currentPlayer);
	
}
