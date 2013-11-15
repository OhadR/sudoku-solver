package com.ohad.suduku_solver.engine;

import java.util.LinkedList;

public class SudukuSolver 
{
    private /*static*/ LinkedList<SolutionItem> puzzlesList = new LinkedList<SolutionItem>();
    private int numSteps = 0;


    public SolutionItem solve(SolutionItem puzzleToSolve)
    {
        //determine what is the first number to insert the puzzle:
        puzzleToSolve.nextToInsert = getNextNumToInsert(puzzleToSolve.puzzle, 1);
        puzzlesList.addLast(puzzleToSolve);

        while( true )
        {
            ++numSteps;
            if(puzzlesList.isEmpty())
            {
                break;
            }
            SolutionItem current = puzzlesList.getLast();
            int X = current.X_ofNextItem;
            int Y = current.Y_ofNextItem;
            int numToInsert = current.nextToInsert;

//            SolutionItem new_si = insertNumberToPuzzle(current.puzzle.clone(), numToInsert, X, Y+1);
            SolutionItem new_si = smartInsertNumberToPuzzle(current.puzzle.clone(), numToInsert, X, Y+1);

            if(new_si == null)	//if insertion failed - this path invalid
            {
                //path is invalid, roll back:
                //NOTE: not only I do not put it on the list, i throw the last item which caused this stuck:
                puzzlesList.removeLast();
                continue;
            }

            //update the current where its next number was located (see SolutionItem doc)
            current.X_ofNextItem = new_si.X_ofNextItem;
            current.Y_ofNextItem = new_si.Y_ofNextItem;



            if(new_si.puzzle.isSolved())
            {
                System.out.println("solved the puzzle in " + numSteps + " steps.");
                return new_si;
            }

            if(new_si.puzzle.isNumberFinished(numToInsert))    //next iteration we insert the sequential number 
            {
                new_si.nextToInsert = getNextNumToInsert(new_si.puzzle, numToInsert + 1);
                //if we insert "new" number than start search from beginning: (0,0)
                new_si.X_ofNextItem = 0;
                //i put -1 bcoz after popping, i inc the Y index. this is the same reason i create in the servlet 
                //the SolutionItem with -1:
                new_si.Y_ofNextItem = -1;   

            }
            else
            {
                new_si.nextToInsert = numToInsert;
            }

            puzzlesList.addLast(new_si);

        }
        
        System.out.println("WOW! no solutions. num steps: " + numSteps);
        return null;
    }

    /**
     * this method is responsible to analyse what is the next number we should enter the puzzle, 
     * in ascending order. it starts looking from a given "startAt" number, considering all numbers till
     * that are already entered. e.g. startAt=4, we search if we have 9 4's in the puzzle. if we do, 
     * we search for 5, and so on. till we find a number that not all 9 instances are in the puzzle.
     * if we didn't find, we return 9, meaning puzzle is full.
     * @param puzzle
     * @param startAt
     * @return
     */
    private int getNextNumToInsert(SudukuPuzzle puzzle, int startAt)
    {
        while( startAt < SudukuPuzzle.TABLE_SIZE)
        {
            if(puzzle.isNumberFinished(startAt))
            {
                ++startAt;
            }
            else
            {
                break;
            }
        }
        return startAt;
    }

    /**
     * this method tries to insert number into the puzzle. in the beginning it is called with
     * x=y=0, but if it fails, different values are being tried, so this method gives back 
     * the x and y values that she places the number, so we will know where to start from 
     * in the next try
     * 
     * the "catch" here is the internal counter that has to be reset every iteration of the outer counter.
     * so I keep a flag that tells him if it is the first iteration do not reset, otherwise i will get the same
     * results everytime. but in other oterations, start over from 0.
     * 
     * @param currentPuzzle
     * @param toInsert
     * @param x
     * @param y
     * @return
     */
    private SolutionItem insertNumberToPuzzle(
                                              SudukuPuzzle currentPuzzle, 
                                              int toInsert,
                                              int x,
                                              int y) 
    {
        boolean firstIteration = true;

        for(; x < SudukuPuzzle.TABLE_SIZE; ++x)
        {
            y = firstIteration ? y : 0;
            firstIteration = false;
            for(; y < SudukuPuzzle.TABLE_SIZE; ++y)
            {
                if( currentPuzzle.isInsertionValid(toInsert, x, y) )
                {
                    currentPuzzle.insertNumber(toInsert, x, y);
                    SolutionItem si = new SolutionItem(currentPuzzle.clone(), x, y);
                    return si;
                }
            }
        }
        return null;
    }

    
    private SolutionItem smartInsertNumberToPuzzle(
                                              SudukuPuzzle currentPuzzle, 
                                              int toInsert,
                                              int x,
                                              int y) 
    {
        SolutionItem si = insertNumberToPuzzle(currentPuzzle, toInsert, x, y);
        
        if( si == null )
        {
            return null;
        }
        
        //now iterate to previous lines to make sure our number exists in all lines and there 
        //are no "holes" - o/w there is no point to keep going.
        if( si.puzzle.isNumberExistInAllLines(toInsert, si.X_ofNextItem) )
        {
            return si;
        }
        else
        {
            return null;
        }
    }
    
    public int getNumSteps()
    {
    	return numSteps;
    }
    
    public SolutionItem getHint(SolutionItem puzzleToSolve)
    {
        SolutionItem solved = solve(puzzleToSolve);
        if(solved == null)
        {
            return null;
        }
        
        int[][] givenPuzzle = puzzleToSolve.puzzle.getTable();
        
        boolean hintGiven = false;
        
        while( !hintGiven )
        {
            //check randomly the indices for x and y and add them to the input-puzzle:
            double dRandom_x = Math.random() * 9;
            double dRandom_y = Math.random() * 9;

            int random_x = (int)dRandom_x;
            int random_y = (int)dRandom_y;

            if( givenPuzzle[random_x][random_y] == 0 )
            {
                givenPuzzle[random_x][random_y] = solved.puzzle.getTable()[random_x][random_y];
                solved.puzzle = new SudukuPuzzle(givenPuzzle);
                hintGiven = true;
            }
        }
        
        return solved;
        
    }

    public static void main(String[] args) 
    {
        int[][] xxx = new int[SudukuPuzzle.TABLE_SIZE][SudukuPuzzle.TABLE_SIZE];
     	xxx[0] = new int[SudukuPuzzle.TABLE_SIZE];
        xxx[0][4] = 2;


        SudukuPuzzle input = new SudukuPuzzle(xxx);
        SudukuSolver solver = new SudukuSolver();

        //we need to put TABLE_SIZE numers, each one TABLE_SIZE times:
        SolutionItem si = new SolutionItem(input, 0, -1);
//        SolutionItem solved = solver.solve(si);
        SolutionItem solved = solver.getHint(si);
        solved.puzzle.printPuzzle();

    }

}

/*

0	7	0	0	0	4	0	9	0
2	0	0	0	9	0	0	0	7
0	0	3	0	8	7	5	0	0
3	1	6	4	0	9	0	0	0
0	0	0	0	1	0	0	0	0
0	0	0	7	0	2	4	0	1
0	0	5	6	0	0	8	0	0
4	0	0	0	0	0	0	0	3
8	6	0	9	0	0	0	1	0
 */

