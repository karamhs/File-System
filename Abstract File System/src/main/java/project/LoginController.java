package project;

import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;

public class LoginController {

    @FXML
    private TextField username;

    @FXML
    private PasswordField passwd;
    
    @FXML
    private Label error;
   
    @FXML
    private void login(){
    
        for(Entity ent : User.getUsers()){
            User user = (User) ent;
            if(user.getName().equals(username.getText()) && user.getPasswd() == user.hashPasswd(passwd.getText())){
                App.setUser(user);
                App.switchScene("Main.fxml");
                return;
            }
        }

        error.setText("Wrong User Name or Password");

    }
}

