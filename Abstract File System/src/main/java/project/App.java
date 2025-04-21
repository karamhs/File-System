package project;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application{
    private static Scene scene;
    private static User user;
    private static Stage stage;

    public static void main(String[] args){
        Application.launch(args);
    }

    @Override
    public void start(Stage stage){
        App.stage = stage;
        
        // new User("User 1", "User2004!!", "user1@gmail.com", false);
        // new User("userAlpha", "Alpha2004!!", "alpha@gmail.com", false);
        // new User("userBeta", "Beta2004!!", "beta@gmail.com", false);
        // new Group("Group 1");
        // new Group("Group 2");

        Fil.load("SaveFile.txt");
        
        initScene("Login.fxml");
        stage.setScene(scene);
        stage.show();

    }

    private static void initScene(String fileName){
        try {
           FXMLLoader loader = new FXMLLoader(App.class.getResource(fileName));
           scene = new Scene(loader.load());
        }

        catch (IOException e){
            System.out.println("Scene Loading Failure: " + e.getMessage());
        }
    }

    public static void switchScene(String fileName){
        initScene(fileName);
        stage.setScene(scene);
        stage.show();
    }

    public static User getUser(){
        return user;
    }

    public static void setUser(User user){
        App.user = user;
    }
    
}
