package sample;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * Created by Be The Change on 5/14/2016.
 */
public class GameOver {
    public TextArea textArea;
    public Button main,again,exit;
    Stage stage;
    Main mainn;
    public GameOver(Main mainn) throws IOException {
         //  this.stage=stage;

         this.mainn=mainn;

    }
    public void initialize(){
        textArea.setText("Game Over ..... Do You Want To Play Again ? ");
        main.setOnAction(event -> {
            try {
                mainn.showMainMenu();
                stage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        });
        again.setOnAction(event -> {
            try {
                mainn.showBoard();
                stage.close();
            } catch (IOException e) {
                e.printStackTrace();
            }


        });
        exit.setOnAction(event -> {

               System.exit(0);


        });

    }
}
