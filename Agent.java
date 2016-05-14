package sample;

import javafx.application.Platform;

import java.io.*;
import java.util.*;

/**
 * Created by Be The Change on 5/6/2016.
 */
public class Agent {
   /* class Think implements  Runnable{
        @Override
        public void run() {
            think(false);
            played=false;


        }
    }*/
    static class Case {
        static final int NOTADJ = 1, BOTHBLOCKED = 1, ONENODEWITHONESIDE =4, ONENODEBOTHSIDE = 10, TWONODESONESIDE = 28, TWONODESBOTHSIDE = 35, WINSIT = 5000;

    }

    static final int[][] POSVALS =
            {
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 1, 1, 1, 1, 0, 0},
                    {0, 0, 1, 3, 3, 1, 0, 0},
                    {0, 0, 1, 3, 3, 1, 0, 0},
                    {0, 0, 1, 1, 1, 1, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},
                    {0, 0, 0, 0, 0, 0, 0, 0},


            };

    enum DIR {ROW, COL, DIAGONAL1, DIAGONAL2}


    Controller controller;
    static short dump=0;
    static short xWinLines=135;
    static short oWinLines=135;
    boolean dumMove = false;
    short agentMoves = 0;
    short blocked = 0;
    final short MAXDEPTH =3;
    boolean played=false;
    State lastState;
    File file = new File("D:/outputXO.txt");
    PrintWriter pr=new PrintWriter(file);
   // Thread thread;
    int row, col;
    public Agent(Controller controller) throws FileNotFoundException {
        this.controller = controller;
        FileOutputStream fis = new FileOutputStream(file);
        PrintStream out = new PrintStream(fis);
       // thread=new Thread(new Think());
        //System.setOut(out);
    }

    public void think() {
        new Thread() {
            public void run() {
                startThinkink();
                Platform.runLater(new Runnable() {
                    public void run() {
                        controller.changeTurn(controller.matrixpane[row][col]);
                    }
                });
            }

        }.start();
    }

    public void startThinkink() {
        if (controller.gameEnds)
            return;
                if (dumMove) {

                    Random random = new Random(System.nanoTime());
                    do {
                        row = Math.abs(random.nextInt() % 8);
                        col = Math.abs(random.nextInt() % 8);

                    } while (controller.board[row][col] != '\u0000');
                    controller.changeTurn(controller.matrixpane[row][col]);
                    return;
                }
                State currentState;
                if (lastState == null) {
                    currentState = new State(controller.board, 'p', agentMoves, xWinLines, oWinLines);
                    currentState.chosenCol = (short) controller.col;
                    currentState.chosenRow = (short) controller.row;
                } else {
                    currentState = new State(lastState);
                    currentState.setPos((controller.row * 10) + (controller.col));
                }
                //  ////System.out.println("before | " +"player:: "+currentState.playerVals+" agent::: "+currentState.agentVals);
                heuristicEval(currentState);
                //  ////System.out.println("after | " +"player:: "+currentState.playerVals+" agent::: "+currentState.agentVals);
                //heuristicEval(currentState,false);
      /*  for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++)
                System.out.print(currentState.board[i][j] + "||");
            ////System.out.println();
        }*/
                ArrayList<TransitionFunction> availTransitions = new ArrayList<>();
                ArrayList<State> states = new ArrayList<>();
                for (int i = 0; i < currentState.availablepos.size(); i++) {
                    State state = new State(currentState);
                    state.setPos(currentState.availablepos.get(i));
                    heuristicEval(state);
                    // heuristicEval(state,false);
                    states.add(state);
                }
                Collections.sort(states);
                for (int i = 0; i < states.size(); i++) {
                    // availTransitions.add(new TransitionFunction(state, minimax(state,0)));
                    availTransitions.add(new TransitionFunction(states.get(i), alphaBeta(states.get(i), Integer.MIN_VALUE, Integer.MAX_VALUE, 0)));

                }
                Collections.sort(availTransitions);
                row = availTransitions.get(0).state.chosenRow;
                col = availTransitions.get(0).state.chosenCol;
                lastState = availTransitions.get(0).state;
                //   for(int i=0;i<availTransitions.size();i++)
                //     ////System.out.println(availTransitions.get(i).minimax+" ");
                //  ////System.out.println("------");
                ////System.out.println("chosen Heuristic :: " + availTransitions.get(0).minimax + " oMoves " + lastState.oMoves);
                agentMoves++;
                //controller.changeTurn(controller.matrixpane[row][col]);



    }

    public int alphaBeta(State state, int alpha, int beta, int depth) {
        if (controller.evaluate((int) state.chosenRow, (int) state.chosenCol, state.board, state.who) || depth == MAXDEPTH) {
           //
            //heuristicEval(state);
           // ////System.out.println("Returned Heuristic ::: "+(state.playerVals-state.agentVals)+(state.oMoves));
          //  ////System.out.println("Returned Player Vals :: "+state.playerVals+" Returned  AgentVals :: "+state.agentVals);
            if(controller.evaluate((int) state.chosenRow, (int) state.chosenCol, state.board, state.who)) {
           // if(state.who=='p'){
              //  for (int i = 0; i < 8; i++) {
                //    for (int j = 0; j < 8; j++)
                     //   System.out.print(state.board[i][j] + "||");
                    ////System.out.println();
                }
                ////System.out.println("**************");
              //  if(state.chosenRow==1&&state.chosenCol==3){
             //       int ss=2;
                    ////System.out.println("**************");
            //    }


                //    int uu;


           // }
            int player=state.playerVals;
            int agent=state.agentVals;
            ////System.out.println("Returned Player Vals :: "+player+" Returned  AgentVals :: "+agent);
            int heuristic=player-agent;
            heuristic+=state.oMoves;
            ////System.out.println("Returned Heuristic ::: "+heuristic);
             return (heuristic);
        }

        ArrayList<State> availStates = new ArrayList<>();

        if (state.who == 'c') {
            int val = Integer.MIN_VALUE;
            for (int i = 0; i < state.availablepos.size(); i++) {
                State xstate = (new State(state));
                xstate.setPos(state.availablepos.get(i));
                heuristicEval(xstate);
              //  heuristicEval(xstate,false);
                availStates.add(xstate);
                int newVal = alphaBeta(xstate, alpha, beta, depth + 1);
                val = Integer.max(val, newVal);
                alpha = Integer.max(val, alpha);
                if (beta <= alpha)
                    break;
            }

            ////System.out.println("Player Heuristic value= "+(val));
            ////System.out.println(" Player Vals :: "+state.playerVals+"  AgentVals :: "+state.agentVals);
            return val;
        } else {
            int val = Integer.MAX_VALUE;
            for (int i = 0; i < state.availablepos.size(); i++) {
                State xstate = (new State(state));
                xstate.setPos(state.availablepos.get(i));
                heuristicEval(xstate);
            //    heuristicEval(xstate,false);

                availStates.add(xstate);
                int newVal = alphaBeta(xstate, alpha, beta, depth + 1);
                val = Integer.min(val, newVal);
                beta = Integer.min(val, alpha);
                if (beta <= alpha)
                    break;
            }
            ////System.out.println("Computer Heuristic value= "+(val));
            ////System.out.println(" Player Vals :: "+state.playerVals+"  AgentVals :: "+state.agentVals);
            return val;
        }

    }



    public int minimax(State state, int depth) {
        if (controller.evaluate((int) state.chosenRow, (int) state.chosenCol, state.board, state.who) || depth == MAXDEPTH) {
            if (state.who == 'p') {

                return heuristicEval(state);

            } else {
                return -heuristicEval(state);
            }
        }
        int val;
        ArrayList<State> availStates = new ArrayList<>();

        if (state.who == 'p')
            val = -100000;
        else
            val = 100000;
        for (int i = 0; i < state.availablepos.size(); i++) {
            availStates.add((new State(state)));
            availStates.get(i).setPos(state.availablepos.get(i));

        }
        // ////System.out.println("working " + availStates.size());
        for (State xstate : availStates) {
            int newVal = minimax(xstate, depth + 1);
            if (state.who == 'p' && newVal > val)
                val = newVal;
            else if (state.who == 'c' && newVal < val)
                val = newVal;
        }
        return val;


    }

    int heuristicEval(State state) {

        int row = state.chosenRow;
        int col = state.chosenCol;
        int numOFfreeSides = getFreeSides(state.board, state.who, row, col);
        int heuristicVal = 0;
        Integer heuristicOnDir[] = new Integer[4];

     /*   for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++)
                System.out.print(state.board[i][j] + "||");
            ////System.out.println();
        }*/


        heuristicOnDir[0] = getCase(getNodesInLine(state.board, state.who, row, col, DIR.ROW), blocked);
        heuristicOnDir[1] = getCase(getNodesInLine(state.board, state.who, row, col, DIR.COL), blocked);
        heuristicOnDir[2] = getCase(getNodesInLine(state.board, state.who, row, col, DIR.DIAGONAL1), blocked);
        heuristicOnDir[3] = getCase(getNodesInLine(state.board, state.who, row, col, DIR.DIAGONAL2), blocked);
        heuristicVal += Collections.max(Arrays.asList(heuristicOnDir));
       /* heuristicVal+=getCase(getNodesInLine(state.board,state.who,row,col,DIR.ROW),blocked);
        heuristicVal+=getCase(getNodesInLine(state.board,state.who,row,col,DIR.COL),blocked);
        heuristicVal+=getCase(getNodesInLine(state.board,state.who,row,col,DIR.DIAGONAL1),blocked);
        heuristicVal+=getCase(getNodesInLine(state.board,state.who,row,col,DIR.DIAGONAL2),blocked);*/

        if(state.who=='p')
            state.playerVals+=heuristicVal +numOFfreeSides;//+POSVALS[state.chosenRow][state.chosenCol];
                    //state.oMoves +heuristicEval(state,false);
        else
           state.agentVals+=heuristicVal +numOFfreeSides+POSVALS[state.chosenRow][state.chosenCol];//+POSVALS[state.chosenRow][state.chosenCol];
                   //state.oMoves +heuristicEval(state,false);
        changeingInOppenentVal(state);
        return state.playerVals-state.agentVals;
                //+/*POSVALS[state.chosenRow][state.chosenCol] */

    }


    private int getFreeSides(char[][] board, char toCheck, int row, int col) {
        int ret = 0;
        if (row + 1 < board.length && board[row + 1][col] == '\u0000') ret++;
        if (col + 1 < board.length && board[row][col + 1] == '\u0000') ret++;
        if (row + 1 < board.length && col + 1 < board.length && board[row + 1][col + 1] == '\u0000') ret++;//
        if (row - 1 > -1 && col - 1 > -1 && board[row - 1][col - 1] == '\u0000') ret++;//
        if (row - 1 > -1 && board[row - 1][col] == '\u0000') ret++;
        if (col - 1 > -1 && board[row][col - 1] == '\u0000') ret++;
        if (col - 1 > -1 && row + 1 < board.length && board[row + 1][col - 1] == '\u0000') ret++;
        if (row - 1 > -1 && col + 1 < board.length && board[row - 1][col + 1] == '\u0000') ret++;
        return ret;
    }

    private int getNodesInLine(char[][] board, char toCheck, int row, int col, DIR dir) {
        int numofNodes = 0;
        blocked = 0;
        //  char op=toCheck=='p' ? 'c':'p';
        switch (dir) {
            case ROW:
                for (int i = 1; i <= 3; i++) {
                    if (col + i < board.length && board[row][col + i] == toCheck) {
                        numofNodes++;
                        continue;
                    } else if (col + i < board.length && board[row][col + i] == '\u0000') break;
                    blocked++;
                    break;

                }
                for (int i = 1; i <= 3; i++) {
                    if (col - i > -1 && board[row][col - i] == toCheck) {
                        numofNodes++;
                        continue;
                    } else if (col - i > -1 && board[row][col - i] == '\u0000') break;
                    blocked++;
                    break;
                }
                break;
            case COL:
                for (int i = 1; i <= 3; i++) {
                    if (row + i < board.length && board[row + i][col] == toCheck) {
                        numofNodes++;
                        continue;
                    } else if (row + i < board.length && board[row + i][col] == '\u0000') break;
                    blocked++;
                    break;
                }

                for (int i = 1; i <= 3; i++) {
                    if (row - i > -1 && board[row - i][col] == toCheck) {
                        numofNodes++;
                        continue;
                    } else if (row - i > -1 && board[row - i][col] == '\u0000') break;
                    blocked++;
                    break;
                }
                break;
            case DIAGONAL1:
                for (int i = 1; i <= 3; i++) {
                    if (row + i < board.length && col + i < board.length && board[row + i][col + i] == toCheck) {
                        numofNodes++;
                        continue;
                    } else if (row + i < board.length && col + i < board.length && board[row + i][col + i] == '\u0000')
                        break;
                    blocked++;
                    break;
                }
                for (int i = 1; i <= 3; i++) {
                    if (row - i > -1 && col - i > -1 && board[row - i][col - i] == toCheck) {
                        numofNodes++;
                        continue;
                    } else if (row - i > -1 && col - i > -1 && board[row - i][col - i] == '\u0000') break;
                    blocked++;
                    break;
                }
                break;
            case DIAGONAL2:
                for (int i = 1; i <= 3; i++) {
                    if (col - i > -1 && row + i < board.length && board[row + i][col - i] == toCheck) {
                        numofNodes++;
                        continue;
                    } else if (col - i > -1 && row + i < board.length && board[row + i][col - i] == '\u0000') break;
                    blocked++;
                    break;
                }
                for (int i = 1; i <= 3; i++) {
                    if (row - i > -1 && col + i < board.length && board[row - i][col + i] == toCheck) {
                        numofNodes++;
                        continue;
                    } else if (row - i > -1 && col + i < board.length && board[row - i][col + i] == '\u0000') break;
                    blocked++;
                    break;
                }
                break;

        }
        return numofNodes;


    }

    private int getCase(int nodes, int blocked) {
        if (nodes == 0)
            return Case.NOTADJ;
        else if ((nodes == 1 && blocked == 2) || (nodes == 2 && blocked == 2))
            return Case.BOTHBLOCKED;
        else if (nodes == 1 && blocked == 1)
            return Case.ONENODEWITHONESIDE;
        else if (nodes == 1 && blocked == 0)
            return Case.ONENODEBOTHSIDE;
        else if (nodes == 2 && blocked == 1)
            return Case.TWONODESONESIDE;
        else if (nodes == 2 && blocked == 0)
            return Case.TWONODESBOTHSIDE;
        else
            return Case.WINSIT;
    }
 /*   private int heuristicEval(State state,boolean dum){
        int erasedLines=0;
        for(int i=3;i>=0;i--){
            if(diagonal1Free(i,state))erasedLines--;
            if(diagonal2Free(i,state))erasedLines--;
            if(rowFree(i,state))erasedLines--;
            if(colFree(i,state))erasedLines--;


        }
         if(state.who=='p')
             state.oLines+=erasedLines;
        else
             state.xLines+=erasedLines;

        return state.oLines-state.xLines;
    }

    private boolean diagonal1Free(int i,State state){
        int row=state.chosenRow+i;int col=state.chosenCol-i;
        if(row>=state.board.length||col<0)return false;
        for(int j=0;j<=3;j++){
            int x=row-j;int y=col+j;
            if(x==state.chosenRow||y==state.chosenCol)continue;
            if(x<0||y>=state.board.length)
                return false;
            if(state.board[x][y]==state.who)
                return false;

        }
        return true;

    }
    private boolean diagonal2Free(int i,State state){
        int row=state.chosenRow+i;int col=state.chosenCol+i;
        if(row>=state.board.length||col>=state.board.length)return false;
        for(int j=0;j<=3;j++){
            int x=row-j;int y=col-j;
            if(x==state.chosenRow||y==state.chosenCol)continue;
            if(x<0||y<0)
                return false;
            if(state.board[x][y]==state.who)
                return false;

        }
        return true;

    }
    private boolean rowFree(int i,State state){
        int row=state.chosenRow;
        int col=state.chosenCol-i;
        if(col<0)return false;
        for(int j=0;j<=3;j++){
            int x=row;int y=col+j;
            if(x==state.chosenRow||y==state.chosenCol)continue;
            if(y>=state.board.length)
                return false;
            if(state.board[x][y]==state.who)
                return false;

        }
        return true;

    }
    private boolean colFree(int i,State state){
        int row=state.chosenRow+i;
        int col=state.chosenCol;
        if(row>=state.board.length)return false;
        for(int j=0;j<=3;j++){
            int x=row-j;int y=col;
            if(x==state.chosenRow||y==state.chosenCol)continue;
            if(y<0)
                return false;
            if(state.board[x][y]==state.who)
                return false;

        }
        return true;

    }*/
     void changeingInOppenentVal(State state){
         char op=(state.who=='p')?'c':'p';
         short row=state.chosenRow,col=state.chosenCol;
   //      for (int i = 0; i < 8; i++) {
   //          for (int j = 0; j < 8; j++)
     //            System.out.print(state.board[i][j] + "||");
      //       ////System.out.println();
      //   }
         int cases[]={1,4,7,10,100,5000};
         char oldBoard[][]=new char[8][8];
         for(int i=0;i<8;i++){
             for(int j=0;j<8;j++)
                 oldBoard[i][j]=state.board[i][j];

         }
       //  ////System.out.println("--------------------------");
         oldBoard[row][col]='\u0000';
        // ArrayList<Integer>arr=new ArrayList<>();
         int change=0;
         int newval=0,oldval=0;
         if(row-1>=0&&state.board[row-1][col]==op){
              newval=getCase(getNodesInLine(state.board, op, row-1, col, DIR.COL), blocked);
              oldval=getCase(getNodesInLine(oldBoard, op, row-1, col, DIR.COL), blocked);
             change--;

         }
         if(row-1>=0&&col+1<state.board.length&&state.board[row-1][col+1]==op){
             newval=getCase(getNodesInLine(state.board, op, row-1, col+1, DIR.DIAGONAL2), blocked);
             oldval=getCase(getNodesInLine(oldBoard, op, row-1, col+1, DIR.DIAGONAL2), blocked);
             change--;
         }
         if(col+1<state.board.length&&state.board[row][col+1]==op){
             newval=getCase(getNodesInLine(state.board, op, row, col+1, DIR.ROW), blocked);
             oldval=getCase(getNodesInLine(oldBoard, op, row, col+1, DIR.ROW), blocked);
             change--;

         }
         if(row+1<state.board.length&&col+1<state.board.length&&state.board[row+1][col+1]==op){
             newval=getCase(getNodesInLine(state.board, op, row+1, col+1, DIR.DIAGONAL1), blocked);
             oldval=getCase(getNodesInLine(oldBoard, op, row+1, col+1, DIR.DIAGONAL1), blocked);
             change--;
         }
         if(row+1< state.board.length&&state.board[row+1][col]==op){
             newval=getCase(getNodesInLine(state.board, op, row+1, col, DIR.COL), blocked);
             oldval=getCase(getNodesInLine(oldBoard, op, row+1, col, DIR.COL), blocked);
             change--;
         }
         if(row+1<state.board.length&&col-1>=0&&state.board[row+1][col-1]==op){
             newval=getCase(getNodesInLine(state.board, op, row+1, col-1, DIR.DIAGONAL2), blocked);
             oldval=getCase(getNodesInLine(oldBoard, op, row+1, col-1, DIR.DIAGONAL2), blocked);
             change--;

         }
         if(col-1>=0&&state.board[row][col-1]==op){
             newval=getCase(getNodesInLine(state.board, op, row, col-1, DIR.ROW), blocked);
             oldval=getCase(getNodesInLine(oldBoard, op, row, col-1, DIR.ROW), blocked);
             change--;
         }
         if(col-1>=0&&row-1>=0&&state.board[row-1][col-1]==op){
             newval=getCase(getNodesInLine(state.board, op, row-1, col-1, DIR.DIAGONAL1), blocked);
             oldval=getCase(getNodesInLine(oldBoard, op, row-1, col-1, DIR.DIAGONAL1), blocked);
             change--;
         }
         if(change!=0) {
             change += newval - oldval ;
             change-=1;
         }
         if(op=='p'){
             state.playerVals+=change;
          //   ////System.out.println("player Vals:: "+state.playerVals);
         }
         else
             state.agentVals+=change;

     }
    class State implements Comparable{
        ArrayList<Integer> availablepos;
        short xLines;
        short oLines;
        short oMoves;
        char[][] board;
        short chosenRow;
        short chosenCol;
        char who;
        int playerVals;
        int agentVals;

        public State(State preState) {
            this.xLines=preState.xLines;
            this.oLines=preState.oLines;
            availablepos = new ArrayList<>();
            this.board = new char[8][8];
            this.playerVals=preState.playerVals;
            this.agentVals=preState.agentVals;
            oMoves = preState.oMoves;
            if (preState.who == 'p')
                this.who = 'c';
            else
                this.who = 'p';
           // this.who = preState.who == 'p' ? 'c' : 'p';
            for (short i = 0; i < preState.board.length; i++) {
                for (short j = 0; j < preState.board.length; j++) {
                    board[i][j] = preState.board[i][j];
                    if (board[i][j] == '\u0000')
                        availablepos.add((10 * i) + j);
                }
            }
            if(who=='c'&&controller.playerFirst)
                oMoves++;
            else if(who=='p'&&!controller.playerFirst){
                oMoves++;
            }


        }

        public State(char[][] boardCopy, char who, short oMoves,short xLines,short oLines) {
            this.xLines=xLines;
            this.oLines=oLines;
            this.oMoves = oMoves;
            this.who = who;
            if(who=='c'&&controller.playerFirst)
                oMoves++;
            else if(who=='p'&&!controller.playerFirst){
                oMoves++;
            }
            availablepos = new ArrayList<>();
            this.board = new char[8][8];
            for (short i = 0; i < boardCopy.length; i++) {
                for (short j = 0; j < boardCopy.length; j++) {
                    board[i][j] = boardCopy[i][j];
                    if (board[i][j] == '\u0000')
                        availablepos.add((10 * i) + j);
                }
            }
        }

        public void setPos(int x) {

            availablepos.remove(availablepos.indexOf(x));
            chosenRow = (short) (x / 10);
            chosenCol = (short) (x % 10);
            board[chosenRow][chosenCol] = who;
        }


        @Override
        public int compareTo(Object o) {
            int x= playerVals-agentVals;
            int y= ((State)(o)).playerVals-((State)(o)).agentVals;
            if(x>y)
                return 1;
            if(x<y)
                return -1;
            else
                return 0;
           // return playerVals-agentVals;
        }
    }

    /**
     * Created by Be The Change on 5/7/2016.
     */
    class TransitionFunction implements Comparable<TransitionFunction> {
        State state;
        int minimax;

        TransitionFunction(State state, int val) {
            this.state = state;
            this.minimax = val;
        }

        @Override
        public int compareTo(TransitionFunction o) {
            if (this.minimax > o.minimax)
                return 1;
            else if (this.minimax < o.minimax)
                return -1;
            else
                return 0;
        }

    }
}
