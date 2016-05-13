package sample;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Optional;

public class Controller {
    //computer takes 'c' player takes 'p'
    boolean playerFirst = true;
    boolean playerToPlay = playerFirst;
    boolean gameEnds=false;
    public GridPane gridPane;
    public StackPane[][] matrixpane = new StackPane[8][8];
    int row,col;
    // boolean[][] player = new boolean[8][8];
  //  public boolean[][] computer = new boolean[8][8];
    public char[][]board=new char[8][8];
    PaneHandler paneHandler = new PaneHandler();
    Integer sum = 0;
    Agent agent;

    public Controller() throws FileNotFoundException {
        agent = new Agent(this);

    }

    @FXML
    public void initialize() throws FileNotFoundException {


        for (int i = 0; i < 8; i++)
            for (int j = 0; j < 8; j++) {
                matrixpane[i][j] = getNodeByRowColumnIndex(i, j);
                matrixpane[i][j].setOnMouseClicked(paneHandler);
            }
        if(!playerFirst)
            agent.think();

    }

     boolean evaluate(Integer row, Integer col,char[][]arr,char toCheck) {
         sum=0;
            checkAllDirections(row, col,arr,toCheck);
            return sum>=3;

    }
     boolean evaluate(Integer row, Integer col,char toCheck) {
         sum=0;
        checkAllDirections(row, col,board,toCheck);
        return sum>=3;

    }

    private void checkAllDirections(Integer row, Integer col,char[][]arr,char toCheck) {
        int count=0;
        for (int i = 0; i < 24; i++) {
            if (i % 3 == 0 && sum != 3) {
                sum = 0;
                count = 0;
            }
            count++;
            if (i < 3) checkThisarr(arr, row + count, col,toCheck);
            else if (i < 3 * 2) checkThisarr(arr, row - count, col,toCheck);
            else if (i < 3 * 3) checkThisarr(arr, row, col + count,toCheck);
            else if (i < 3 * 4) checkThisarr(arr, row, col - count,toCheck);
            else if (i < 3 * 5) checkThisarr(arr, row + count, col + count,toCheck);
            else if (i < 3 * 6) checkThisarr(arr, row - count, col - count,toCheck);
            else if (i < 3 * 7) checkThisarr(arr, row - count, col + count,toCheck);
            else if (i < 3 * 8) checkThisarr(arr, row + count, col - count,toCheck);


            if (sum >= 3) return;
        }
        return;
    }

    private void checkThisarr(char[][] arr, Integer row, Integer col,char toCheck) {
        if (sum >= 3)
            return;
        if (row > 7 || row < 0 || col > 7 || col < 0) {
            sum = 0;
            return;
        } else if (arr[row][col] != toCheck) {
            sum = 0;
            return;
        } else
            sum++;


    }

    private void drawAt(StackPane source) {
        if (source.getChildren().size() != 0)
            return;


        if ((playerFirst && playerToPlay) || (!playerFirst && !playerToPlay)) {

            Line line1 = new Line(0, 0, 30, 30);
            Line line2 = new Line(30, 0, 0, 30);
            line1.setStrokeWidth(3);
            line1.setStroke(Color.GREEN);
            line2.setStrokeWidth(3);
            line2.setStroke(Color.GREEN);
            source.getChildren().add(line1);
            source.getChildren().add(line2);
            source.setAlignment(line1, Pos.CENTER);
            source.setAlignment(line2, Pos.CENTER);

        } else {
            Shape shape = new Circle(0, 0, 20);
            source.setAlignment(shape, Pos.CENTER);
            shape.setFill(Paint.valueOf("BLACK"));
            shape.setStroke(Paint.valueOf("RED"));
            shape.setStrokeWidth(3);
            source.getChildren().add(shape);

        }


    }
    void changeTurn(StackPane source) {
        drawAt(source);
        if (playerToPlay) {
            row=gridPane.getRowIndex(source);col=gridPane.getColumnIndex(source);
            board[gridPane.getRowIndex(source)][gridPane.getColumnIndex(source)] = 'p';
            if(evaluate(gridPane.getRowIndex(source), gridPane.getColumnIndex(source),'p')){
                //add that game ends and player won
                gameEnds=true;
                //UI.gameOver("player");

            }
            playerToPlay = !playerToPlay;
            Platform.runLater(() -> agent.think());
           // agent.think();


        } else {
            board[gridPane.getRowIndex(source)][gridPane.getColumnIndex(source)] = 'c';
            if(evaluate(gridPane.getRowIndex(source), gridPane.getColumnIndex(source),'c')){
                //add that game ends and player won
                gameEnds=true;
                //UI.gameOver("computer");

            }
            playerToPlay=!playerToPlay;

        }


    }

    public StackPane getNodeByRowColumnIndex(final int row, final int column) {
        // Node result = null;

        for (Node node : gridPane.getChildren()) {
            Integer i = gridPane.getRowIndex(node);
            Integer j = gridPane.getColumnIndex(node);
            if (i.intValue() == row && j.intValue() == column)
                return (StackPane) node;

        }
        return null;
    }

    class PaneHandler implements EventHandler<MouseEvent> {

        @Override
        public void handle(MouseEvent event) {
            //   Shape shape;
            StackPane source = ((StackPane) event.getSource());
            if (source.getChildren().size()==0&&playerToPlay&&!gameEnds) {
                changeTurn(source);

            }


        }
    }




}
