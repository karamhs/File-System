package project;

import javafx.scene.control.CheckBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class MainController {

    @FXML
    private ListView<String> fileList;
    private ObservableList<String> files = FXCollections.observableArrayList();
    private String selectedName;
    private Fil selectedFile;

    @FXML
    private TextField fileName;

    @FXML
    private TextArea content;

    @FXML
    private CheckBox groupRead, groupWrite, otherRead, otherWrite;

    @FXML
    private Label error;

    @FXML
    private void initialize() {
        for (Entity file : Fil.getFiles()) {
            files.add(file.getName());
        }
        
        fileList.setItems(files);
    }

    @FXML
    private void createFile() {
        //A bit illogical here, but its ok :)
        try {
            
            Fil file = new Fil(fileName.getText(), App.getUser(), content.getText());
            files.add(file.getName());
            Group group = App.getUser().getGroup();
    
            if (otherRead.isSelected()) {
                for (Entity entity : User.getUsers()) {
                    file.addAccess(entity);
                }
            }

            if (otherWrite.isSelected()) {
                for (Entity entity : User.getUsers()) {
                    file.addWrite(entity);
                }
            }

            if (groupRead.isSelected()) {
                file.addAccess(group);
            }

            if (groupWrite.isSelected()) {
                file.addWrite(group);
            }

            fileName.setText("");
            content.setText("");

        }

        catch (Exception e) {
            error.setText(e.getMessage());
            return;
        }

    }

    private Fil findFile(){
        selectedName = fileList.getSelectionModel().getSelectedItem();

        if(selectedName == null){
            error.setText("No file is selected");
            return null;
        }

        error.setText("");

        for(Entity file : Fil.getFiles()){
            if(file.getName().equals(selectedName)){
                return (Fil) file;
            }
        }

        //Never reached, but for the error to disappear
        return null;
    }

    @FXML
    private void deleteFile() {
        selectedFile = findFile();

        if(selectedFile == null){
            return;
        }

        Fil.deleteFile(selectedFile);
        files.remove(files.indexOf(selectedFile.getName()));
    }


    //Maybe I had to make this an onAction functionaloty when changing the ViewList item..
    @FXML
    private void showContent() {
        selectedFile = findFile();

        if(selectedFile == null){
            return;
        }

        try{
            content.setText(selectedFile.getContent(App.getUser()));
        }

        catch(IllegalArgumentException e){
            error.setText(e.getMessage());
        }
    }

    @FXML
    private void writeContent() {
        selectedFile = findFile();

        if(selectedFile == null){
            return;
        }

        try{
            selectedFile.setContent(App.getUser(), content.getText());
        }

        catch (IllegalArgumentException e){
            error.setText(e.getMessage());
        }
    }

    //Now in a real scenario, you would need to choose a file to save to or load from, but lets stop here for now :)
    @FXML
    private void save() {
        Fil.save("SaveFile.txt");
    }

    private void updateFiles(){
        for(Entity fil : Fil.getFiles()){
            files.add(fil.getName());
        }
    }

    @FXML
    private void load(){
        Fil.load("SaveFile.txt");
        updateFiles();
    }

    @FXML
    private void switchToUsers() {
        App.switchScene("Users.fxml");
    }

}
