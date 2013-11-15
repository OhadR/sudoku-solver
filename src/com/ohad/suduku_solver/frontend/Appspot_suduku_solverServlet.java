/*
this is the first version - gets ALL indexes: 00, 01, 02 till 77 and fills the table,
then try to solve it.
after solving, it put it on the response, so the user sees a new generated HTML with the result.

next version will be using AJAX so the current page will be updated.
ohad, 22-Dec-2010.
*/

package com.ohad.frontend;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

import com.ohad.suduku_solver.SolutionItem;
import com.ohad.suduku_solver.SudukuPuzzle;
import com.ohad.suduku_solver.SudukuSolver;
import static com.ohad.suduku_solver.SudukuPuzzle.TABLE_SIZE;


@SuppressWarnings("serial")
public class Appspot_suduku_solverServlet extends HttpServlet 
{
	private int table[][] = new int[TABLE_SIZE][TABLE_SIZE];

	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException 
	{
		resp.setContentType("text/plain");
		
		PrintWriter writer = resp.getWriter();

		try
		{
			for(int x = 0; x < TABLE_SIZE; ++x)
			{
				for(int y = 0; y < TABLE_SIZE; ++y)
				{
					String indexes = String.valueOf(x) + String.valueOf(y);
					String value = req.getParameter(indexes);
					if(value == "")
					{
						table[x][y] = 0;
					}
					else
					{
						int valueOf = Integer.valueOf(value);
						if(valueOf < 0 || valueOf > 9)
						{
							throw new NumberFormatException();
						}
						table[x][y] = valueOf;
					}
				}
			}
		}
		catch(NumberFormatException nfe)
		{
			writer.println("Please use only numbers, 0-9, and try again.");
			return;
		}



//        for(int x = 0; x < TABLE_SIZE; ++x)
//        {
//            for(int y = 0; y < TABLE_SIZE; ++y)
//            {
//        		resp.getWriter().print( table[x][y] + " " );
//            }
//            resp.getWriter().println();
//        }

		SudukuPuzzle input = new SudukuPuzzle(table);
		SudukuSolver solver = new SudukuSolver();

		//we need to put TABLE_SIZE numers, each one TABLE_SIZE times:
		SolutionItem puzzleToSolve = new SolutionItem(input, 1, 0, -1);
		SolutionItem solved = solver.solve(puzzleToSolve);
		if(solved == null)
		{
			writer.println("could not solve this fucking puzzle");
		}
		else
		{
			printPuzzle(writer, solved.puzzle);
			writer.println("solved puzzle in " + solver.getNumSteps() + " steps" );
		}
	}

	private void printPuzzle(PrintWriter printWriter, SudukuPuzzle puzzle) 
	{
		printWriter.println("the solved puzzle:");
		puzzle.printPuzzle(printWriter);
	}
}
