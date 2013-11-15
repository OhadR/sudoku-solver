package com.ohad.suduku_solver.engine;

public class SolutionItem 
{
	public SudukuPuzzle puzzle;
	//these items represent the next thing to do in this puzzle: what to insert next and where to start.
	//each item in the list is updated by its follower:
	public int nextToInsert;	//the next number to insert to puzzle
	public int X_ofNextItem;
	public int Y_ofNextItem;
	
	
	public SolutionItem(SudukuPuzzle puzzle, int X, int Y)
	{
		this.puzzle = puzzle;
		this.nextToInsert = 0;    //the application decides waht is the next number to insert
		this.X_ofNextItem = X;
		this.Y_ofNextItem = Y;
	}
	
	public String toString()
	{
	    return "nextToInsert=" + nextToInsert + 
	        ", X_ofNextItem=" + X_ofNextItem + 
	        ", Y_ofNextItem=" + Y_ofNextItem + 
	        ", puzzle=" + puzzle;
	}

}
