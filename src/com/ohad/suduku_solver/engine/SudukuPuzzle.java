package com.ohad.suduku_solver.engine;

import java.io.PrintWriter;

public class SudukuPuzzle 
{
	public static final int TABLE_SIZE = 9;
    private static final int SMALL_CUBE_SIZE = 3;   //small cubes are 3x3
	private int table[][] = new int[TABLE_SIZE][TABLE_SIZE];
	
	
	public SudukuPuzzle()
	{		
	}

	public SudukuPuzzle(int[][] table)
	{
		this.table = table;
	}
	
	
	/**
	 * gets a number to insert, and the location (indices) and checks if valid
	 * @param toIsert
	 */
	boolean isInsertionValid(int toInsert, int x, int y)
	{
		//toInsert MUST be  between 1 and TABLE_SIZE:
		if(toInsert < 1 || toInsert > TABLE_SIZE)
		{
			return false;
		}
		
		//location is not empty
		if(table[x][y] != 0)
		{
			return false;
		}
		
		//check column
		for(int i = 0; i < TABLE_SIZE; ++i)
		{
			if(i == x)
				continue;
			if(table[i][y] == toInsert)
				return false;
		}

		//check row
		for(int i = 0; i < TABLE_SIZE; ++i)
		{
			if(i == y)
				continue;
			if(table[x][i] == toInsert)
				return false;
		}
		
		//check small rectangles
		return checkSmallRect(toInsert, x, y);

		
//		return true;
		
	}
	
	private boolean checkSmallRect(int toInsert, int x, int y)
    {
        int x_cube = x / SMALL_CUBE_SIZE;
        int y_cube = y / SMALL_CUBE_SIZE;
        
        for(int i = x_cube*SMALL_CUBE_SIZE; i < (x_cube+1)*SMALL_CUBE_SIZE; ++i)
        {
            for(int j = y_cube*SMALL_CUBE_SIZE; j < (y_cube+1)*SMALL_CUBE_SIZE; ++j)
            {
                if(table[i][j] == toInsert)
                    return false;
                
            }

        }

        return true;
    }

    void insertNumber(int toInsert, int x, int y)
	{
		table[x][y] = toInsert;
	}
	
	public SudukuPuzzle clone()
	{
		SudukuPuzzle cloned = new SudukuPuzzle();

		for(int x = 0; x < SudukuPuzzle.TABLE_SIZE; ++x)
		{
			for(int y = 0; y < SudukuPuzzle.TABLE_SIZE; ++y)
			{
				cloned.table[x][y] = table[x][y]; 
			}
		}

		return cloned;
	}
	

	   /**
     * gets a number and checks if we have finished with it, meaning if it occurs TABLE_SIZE times.
     * @param number
     * @return
     */
    public boolean isNumberFinished(int number)
    {
        return getNumInstancesOfNumber(number) == TABLE_SIZE;
    }

    
    private int getNumInstancesOfNumber(int number)
	{
	    int counter = 0;
        //need to check that there is no "0"s
        for(int x = 0; x < SudukuPuzzle.TABLE_SIZE; ++x)
        {
            for(int y = 0; y < SudukuPuzzle.TABLE_SIZE; ++y)
            {
                if( table[x][y] == number )
                {
                    ++counter;
                }
            }
        }
        return counter;
	}
    
    
    public boolean isNumberExistInAllLines(int number, int tillLine)
    {
        for(int x = 0; x < tillLine; ++x)
        {
            if( !isNumberExistInLine(number, x) )
            {
                return false;
            }
        }
        return true;
        
    }

    public boolean isNumberExistInLine(int number, int line)
    {
            for(int y = 0; y < SudukuPuzzle.TABLE_SIZE; ++y)
            {
                if( table[line][y] == number )
                {
                    return true;
                }
            }
         return false;
        
    }

	public boolean isSolved()
	{
		//need to check that there is no "0"s
		for(int x = 0; x < SudukuPuzzle.TABLE_SIZE; ++x)
		{
			for(int y = 0; y < SudukuPuzzle.TABLE_SIZE; ++y)
			{
				if( table[x][y] == 0 )
				{
					return false;
				}
			}
		}
		return true;
	}

	public String toString()
	{
	    StringBuffer sb = new StringBuffer();
        for(int x = 0; x < SudukuPuzzle.TABLE_SIZE; ++x)
        {
            for(int y = 0; y < SudukuPuzzle.TABLE_SIZE; ++y)
            {
                sb.append( table[x][y]  + " "); 
            }
            sb.append("\n");
        }
	    
	    return sb.toString();
	}
	
	public int[][] getTable()
	{
	    return table;
	}
	
	public void printPuzzle() 
	{
		System.out.println( toString() );
	}
	
	public void printPuzzle(PrintWriter writer) 
	{
		writer.println( toString() );
	}

}
