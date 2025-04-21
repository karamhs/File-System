package project;

import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.ListView;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;

public class UsersController {
    @FXML
    private ListView<String> usersList, groupsList;
    private ObservableList<String> users = FXCollections.observableArrayList();
    private ObservableList<String> groups = FXCollections.observableArrayList();

    private String selectedUserName, selectedGroupName;

    @FXML
    private TextField entityName, userName, groupName, email;

    @FXML
    private PasswordField passwd, passwd2;

    @FXML
    private RadioButton userRadio, groupRadio;

    @FXML
    private ToggleGroup entityType;

    @FXML
    private Label error;

    @FXML
    public void initialize() {
        entityType = new ToggleGroup();
        userRadio.setToggleGroup(entityType);
        groupRadio.setToggleGroup(entityType);

        for (Entity user : User.getUsers()) {
            users.add(user.getName());
        }

        for (Entity group : Group.getGroups()) {
            groups.add(group.getName());
        }

        usersList.setItems(users);
        groupsList.setItems(groups);

    }

    private void clearFields(){
        entityName.setText("");
        email.setText("");
        passwd.setText("");
        passwd2.setText("");
    }

    private boolean areValidFields() {
        if (entityName.getText().length() == 0 || email.getText().length() == 0 || passwd.getText().length() == 0
                || entityType.getSelectedToggle() == null) {
            error.setText("All required fields should be filled");
            return false;
        }

        else if (!passwd.getText().equals(passwd2.getText())) {
            error.setText("Passwords dont match");
            return false;
        }

        return true;
    }

    @FXML
    private void createEntity() {
        RadioButton selected = (RadioButton) entityType.getSelectedToggle();

        if (selected == null) {
            error.setText("No type is selected");
            return;
        }

        String type = selected.getText();

        if (type.equals("User")) {
            if (areValidFields()) {
                try {
                    User user = new User(entityName.getText(), passwd.getText(), email.getText(),
                            App.getUser().getAdmin()); // could have another radio button for type, but lets say users
                                                       // inherit it from the current user
                    users.add(user.getName());
                    clearFields();

                }

                catch (IllegalArgumentException e) {
                    error.setText(e.getMessage());
                }

            }
        }

        else {
            try {
                Group group = new Group(entityName.getText());
                groups.add(group.getName());
            }

            catch (IllegalArgumentException e) {
                error.setText(e.getMessage());
            }

        }

    }

    private User findSelectedUser() {
        selectedUserName = usersList.getSelectionModel().getSelectedItem();

        if (selectedUserName == null) {
            error.setText("No user is selected");
            return null;
        }

        error.setText("");

        for (Entity user : User.getUsers()) {
            if (user.getName().equals(selectedUserName)) {
                return (User) user;
            }
        }

        //Never reached, just for java compiler
        return null;
    }

    private Group findSelectedGroup() {
        selectedGroupName = groupsList.getSelectionModel().getSelectedItem();

        if (selectedGroupName == null) {
            error.setText("No group is selected");
            return null;
        }

        error.setText("");

        for (Entity group : Group.getGroups()) {
            if (group.getName().equals(selectedGroupName)) {
                return (Group) group;
            }
        }

        //never reached, jist for java compiler
        return null;
    }

    @FXML
    private void removeEntity() {
        User selectedUser = findSelectedUser();
        Group selectedGroup = findSelectedGroup();

        try{
            if(selectedUser != null){
                App.getUser().removeUser(selectedUser);
                users.remove(users.indexOf(selectedUserName));
            }
    
            if(selectedGroup != null){
                selectedGroup.removeGroup(App.getUser());
                groups.remove(groups.indexOf(selectedGroupName));
            }

            error.setText("");
        }

        catch (IllegalArgumentException e){
            error.setText(e.getMessage());
        }

    }

    private Group findGroup() {
        String name = groupName.getText();
        for (Entity entity : Group.getGroups()) {
            if (entity.getName().equals(name)) {
                return (Group) entity;
            }
        }

        error.setText("No group with that name");
        return null;

    }

    private User findUser() {
        String name = userName.getText();
        for (Entity entity : User.getUsers()) {
            if (entity.getName().equals(name)) {
                return (User) entity;
            }
        }

        error.setText("No User with that name");
        return null;
    }

    @FXML
    private void addUser() {
        User user = findUser();
        Group group = findGroup();

        if (user == null || group == null) {
            return;
        }

        try {
            App.getUser().addToGroup(user, group);
            userName.setText("");
            groupName.setText("");
        }

        catch (IllegalStateException e) {
            error.setText(e.getMessage());
        }

    }

    @FXML
    private void removeUser() {
        User user = findUser();
        Group group = findGroup();

        if (user == null || group == null) {
            return;
        }

        try {
            group.removeMember(App.getUser(), user);

            userName.setText("");
            groupName.setText("");
        }

        catch (IllegalStateException e) {
            error.setText(e.getMessage());
        }

    }

    @FXML
    private void switchBack() {
        App.switchScene("Main.fxml");
    }
}
