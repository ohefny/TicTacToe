package sample;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by Be The Change on 5/14/2016.
 */
public class MainMenu {
   public Button easyBut,medBut,hardBut;
    public RadioButton playx,playo;
    public CheckBox forFun;
    Main main;
    public MainMenu(Main main) throws FileNotFoundException {
       // agent = new Agent(this);
        this.main=main;

    }

    @FXML
    public void initialize() throws FileNotFoundException {
        forFun.setOnAction((e)-> main.forFun=forFun.isSelected());
        easyBut.setOnAction(event -> {
                    try {
                        main.level=0;
                        main.playerIsX=playx.isSelected();
                        main.showBoard();
                        //System.out.println("fuck you");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
        );
        hardBut.setDisable(true);
        hardBut.setOnAction(event -> {
                    try {
                        main.level=3;
                        main.playerIsX=playx.isSelected();
                        main.showBoard();
                        //System.out.println("fuck you");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
        );
        medBut.setOnAction(event -> {
                    try {
                        main.level=2;
                        main.playerIsX=playx.isSelected();
                        main.showBoard();
                        //System.out.println("fuck you");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
        );
    }

}
