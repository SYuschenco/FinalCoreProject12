package ua.goit.gauswithhgui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    static Scene mainScene;

    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("sample.fxml"));
        Parent fxmlMain = fxmlLoader.load();
        Controller mainController = fxmlLoader.getController();
        mainController.setMainStage(primaryStage);
        mainScene = new Scene(fxmlMain, 900, 900);
        primaryStage.setTitle("Application for solving a system of linear equations by Gauss");
        primaryStage.setMinHeight(900);
        primaryStage.setMinWidth(900);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }

    public static void main(String[] args){
        launch(args);
    }
}
