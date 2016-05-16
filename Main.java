package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.FileNotFoundException;
import java.io.IOException;

public class Main extends Application {
    Stage primaryStage;
    boolean playerIsX=false;
    int level=0;
    boolean forFun=false;
    @Override
    public void start(Stage primaryStage) throws Exception{
        this.primaryStage=primaryStage;
        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        showMainMenu();
    }
    public void showMainMenu() throws IOException {
        FXMLLoader loader =  new FXMLLoader(getClass().getResource("MainMenu.fxml"));
        loader.setController(new MainMenu(this));
        Parent root=loader.load();

        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setScene(new Scene(root, 450, 450));
        primaryStage.setResizable(false);
        // Controller controller=new Controller();
        primaryStage.show();
    }

    public void showBoard() throws IOException {
        primaryStage.close();
        FXMLLoader loader =  new FXMLLoader(getClass().getResource("sample.fxml"));
        loader.setController(new Controller(playerIsX,level,forFun,this));
        Parent root=loader.load();

        primaryStage.setTitle("Tic Tac Toe");
        primaryStage.setScene(new Scene(root, 450, 450));
        primaryStage.setResizable(false);
        // Controller controller=new Controller();
        primaryStage.show();

    }
    public static void main(String[] args) {
        launch(args);
    }
}
