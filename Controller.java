package sample;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Optional;

public class Controller {
    //computer takes 'c' player takes 'p'
    class WinSet{
        int row;
        int col;

        public WinSet(Integer row, Integer col) {
            this.row=row;
            this.col=col;
        }
    }
    boolean playForFun=false;
    boolean playerFirst = false;
    boolean playerToPlay = false;
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
    boolean dontContinue=false;
    boolean space=false;
    ArrayList<WinSet>winSets;
    Main main;
    public Controller(boolean playerIsX, int level,boolean playForFun,Main main) throws FileNotFoundException {
        agent = new Agent(this);
        agent.MAXDEPTH= (short) level;
        playerFirst=playerIsX;
        playerToPlay=playerIsX;
        this.playForFun=playForFun;
        this.main=main;
        winSets=new ArrayList<>();

    }

    @FXML
    public void initialize() throws FileNotFoundException {


        for (int i = 0; i < 8; i++){
            for (int j = 0; j < 8; j++) {
                matrixpane[i][j] = getNodeByRowColumnIndex(i, j);
                matrixpane[i][j].setOnMouseClicked(paneHandler);
            }
        }
        if(!playerFirst)
            agent.think();

    }

    boolean evaluate(Integer row, Integer col,char[][]arr,char toCheck) {
         sum=0;
            winSets.add(new WinSet(row,col));
            checkAllDirections(row, col,arr,toCheck);
            if(sum<3)
                winSets.clear();
            return sum>=3;

    }
     boolean evaluate(Integer row, Integer col,char toCheck) {
         sum=0;
         winSets.add(new WinSet(row,col));
        checkAllDirections(row, col,board,toCheck);
         if(sum<3)
             winSets.clear();
        return sum>=3;

    }


    private void checkAllDirections(Integer row, Integer col,char[][]arr,char toCheck) {
        int count=0;
        for (int i = 0; i < 24; i++) {
            if (i % 6 == 0 && sum != 3) {
                sum = 0;
                winSets.clear();

            }
            if(count%3==0){
                count=0;
                dontContinue=false;
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
        if (sum >= 3 ||dontContinue)
            return;
        if (row > 7 || row < 0 || col > 7 || col < 0) {
            sum = 0;
            return;
        } else if (arr[row][col] != toCheck) {
            dontContinue=true;
            return;
        } else{
            winSets.add(new WinSet(row,col));

            sum++;}


    }

/*
    private void checkAllDirections(Integer row, Integer col,char[][]arr,char toCheck) {
        int count=0;
        for (int i = 0; i < 24; i++) {
            if (i % 6 == 0 && sum != 3) {
                sum = 0;
                space=false;
                winSets.clear();
              //  count = 0;
            }
            if(count%3==0)
                count=0;
            count++;
            if (i < 3 &&!space) checkThisarr(arr, row + count, col,toCheck);
            else if (i < 3 * 2 &&!space) checkThisarr(arr, row - count, col,toCheck);
            else if (i < 3 * 3&&!space) checkThisarr(arr, row, col + count,toCheck);
            else if (i < 3 * 4&&!space) checkThisarr(arr, row, col - count,toCheck);
            else if (i < 3 * 5&&!space) checkThisarr(arr, row + count, col + count,toCheck);
            else if (i < 3 * 6&&!space) checkThisarr(arr, row - count, col - count,toCheck);
            else if (i < 3 * 7&&!space) checkThisarr(arr, row - count, col + count,toCheck);
            else if (i < 3 * 8&&!space) checkThisarr(arr, row + count, col - count,toCheck);


            if (sum >= 3) return;
        }
        return;
    }

    private void checkThisarr(char[][] arr, Integer row, Integer col,char toCheck) {
        if (sum >= 3)
            return;
        if (row > 7 || row < 0 || col > 7 || col < 0) {
           // sum = 0;
            return;
        }
        else if(arr[row][col]=='\u0000'){
            space=true;
            return;
        }
        else if (arr[row][col] != toCheck) {
            return;
        } else {
            winSets.add(new WinSet(row,col));
            sum++;
        }

    }*/


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
    void changeTurn(StackPane source) throws IOException {
        drawAt(source);
        if (playerToPlay) {
            row=gridPane.getRowIndex(source);col=gridPane.getColumnIndex(source);
            board[gridPane.getRowIndex(source)][gridPane.getColumnIndex(source)] = 'p';
            if(evaluate(gridPane.getRowIndex(source), gridPane.getColumnIndex(source),'p')){
                //add that game ends and player won
                gameEnds=true;
                winSets.add(new WinSet(gridPane.getRowIndex(source),gridPane.getColumnIndex(source)));
                gameEnds();
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
                winSets.add(new WinSet(gridPane.getRowIndex(source),gridPane.getColumnIndex(source)));
                gameEnds();
                //UI.gameOver("computer");

            }
            playerToPlay=!playerToPlay;

        }


    }
    public void gameEnds() throws IOException {
        ArrayList<StackPane>win=new ArrayList<>();
        for(int i=0;i<winSets.size();i++){
            win.add(getNodeByRowColumnIndex(winSets.get(i).row,winSets.get(i).col));
            win.get(i).setStyle("-fx-background-color: yellow;");
           // win.get(i).setBackground();

        }
            new Runnable() {
                public void run() {
                    try {
                        Thread.sleep(1500);
                        Platform.runLater(() ->

                                {
                                    try {

                                        buildDialog();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                        );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
            }.run();

//
    }

    private void buildDialog() throws IOException {
       /* FXMLLoader anotherLoader = new FXMLLoader(getClass().getResource("sample.fxml")); // FXML for second stage
        Stage anotherStage = new Stage();
        Parent anotherRoot = anotherLoader.load();
        Scene anotherScene = new Scene(anotherRoot);
        anotherLoader.setController(new GameOver(anotherStage,main));
        anotherStage.setScene(anotherScene);
        anotherStage.show();
*/
        Stage anotherStage = new Stage();
        FXMLLoader anotherLoader =  new FXMLLoader(getClass().getResource("GameOver.fxml"));
        GameOver gameOver=new GameOver(main);
        anotherLoader.setController(gameOver);
        Parent anotherRoot = anotherLoader.load();
        Scene anotherScene = new Scene(anotherRoot);
        anotherStage.setScene(anotherScene);
        gameOver.stage=anotherStage;
        anotherStage.show();
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
                try {
                    changeTurn(source);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        }
    }




}
