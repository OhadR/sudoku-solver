package com.ohad.suduku_solver.frontend;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.*;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.ohad.suduku_solver.engine.SolutionItem;
import com.ohad.suduku_solver.engine.SudukuPuzzle;
import com.ohad.suduku_solver.engine.SudukuSolver;


@SuppressWarnings("serial")
public class Appspot_suduku_solverServlet extends HttpServlet 
{
	public void doGet(HttpServletRequest req, HttpServletResponse resp)
	throws IOException 
	{
	    resp.setContentType("text/plain");

	    PrintWriter writer = resp.getWriter();
        ResultStatus status = new ResultStatus();

	    Gson gson = new Gson();
        String command = req.getParameter("command");
	    String jsonedPuzzle = req.getParameter("puzzle");
	    //if input contains something that is not int[][], we will get exception. so if user enters
	    //things like letters, i want the server side to protect itself (doing it in the UI is nice
	    //but if i have several UIs like several HTMLs, i dont want wach one of them to contains protection...
	    try
	    {
	        String errMsg = "";
	        int[][] inputPuzzle = gson.fromJson(jsonedPuzzle, int[][].class);

	        SudukuPuzzle input = new SudukuPuzzle(inputPuzzle);
	        SudukuSolver solver = new SudukuSolver();

	        //we need to put TABLE_SIZE numbers, each one TABLE_SIZE times:
	        SolutionItem puzzleToSolve = new SolutionItem(input, 0, -1);
	        SolutionItem solved = null;
	        if( command.equals( "solve" ))
	        {
	            solved = solver.solve(puzzleToSolve);
	        }
	        else if( command.equals( "gethint" ))
	        {
	            solved = solver.getHint(puzzleToSolve);
	        }
	        else
	        {
                errMsg = "illegal command from the javascript";
	        }

	        //Gson gson = new GsonBuilder().serializeNulls().create();
	        gson = new Gson(); 
	        
	        if(solved == null) //could not solve or illegal command
	        {
	            errMsg = (errMsg == "") ? "ERROR: could not solve this fucking puzzle; took me " + solver.getNumSteps() + " steps" : errMsg;  
	            status.setSuccess(false);
	            status.setMessage( errMsg );
	        }
	        else
	        {
	            status.setSuccess(true);
	            status.setMessage("solved puzzle in " + solver.getNumSteps() + " steps");
	            //since i work with Json i get the table and transfer it as is:
	            status.setPuzzle(solved.puzzle.getTable());
	        }
	    }
	    catch(JsonParseException jpe)
	    {
            status.setSuccess(false);
            status.setMessage("ERROR: The input must contains ONLY numbers");
	    }



		
        String json = gson.toJson( status );
        System.out.println( json );
        writer.println( json );
	}
}
