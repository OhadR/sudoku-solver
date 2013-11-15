package com.ohad.suduku_solver.frontend;

public class ResultStatus
{
    boolean     success;
    String      message;
    int[][]     puzzle;

    public void setSuccess(boolean success)
    {
        this.success = success;
    }
    public void setMessage(String message)
    {
        this.message = message;
    }
    public void setPuzzle(int[][] puzzle)
    {
        this.puzzle = puzzle;
    }
    
}
