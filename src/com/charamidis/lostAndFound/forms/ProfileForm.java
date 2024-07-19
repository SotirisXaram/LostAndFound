package com.charamidis.lostAndFound.forms;

import com.charamidis.lostAndFound.utils.MessageBoxOk;
import com.charamidis.lostAndFound.models.User;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.control.*;
import javafx.geometry.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;


public class ProfileForm {
    Label lblFirstName,lblLastName,lblBirthday,lblRole,lblPassword,lblAm,lblLastSignedIn;
    TextField txtFirstName,txtLastName,txtBirthday,txtRole,txtPassword,txtAm,txtLastSignedIn;
    GridPane grid;
    Connection connection;
    Scene profileScene;
    Button btnChange ;
    public ProfileForm(Connection conn, User user){
        connection = conn;
        lblFirstName = new Label("ΟΝΟΜΑ:");
        lblLastName = new Label("ΕΠΩΝΥΜΟ:");
        lblAm = new Label("AM:");
        lblRole = new Label("ΡΟΛΟΣ:");
        lblBirthday = new Label("ΗΜΕΡΟΜΗΝΙΑ ΓΕΝΝΗΣΗΣ:");
        lblLastSignedIn = new Label("ΤΕΛΕΥΤΑΙΑ ΠΡΟΣΒΑΣΗ:");
        lblPassword = new Label("ΚΩΔΙΚΟΣ ΠΡΟΣΒΑΣΗΣ:");

        txtAm = new TextField((String.valueOf(user.getAm())));
        txtBirthday = new TextField(user.getBirthday());
        txtRole = new TextField(user.getRole());
        txtFirstName = new TextField(user.getFirstName());
        txtLastName = new TextField(user.getLastName());
        txtPassword = new TextField("************");
        txtLastSignedIn = new TextField(user.getDateLoggedIn()+"    "+user.getTimeLoggedIn());

        txtAm.setEditable(false);
        txtRole.setEditable(false);
        txtLastSignedIn.setEditable(false);
        txtFirstName.setEditable(false);
        txtBirthday.setEditable(false);
        txtLastName.setEditable(false);
        txtPassword.setEditable(false);

        btnChange = new Button("Password Change");

        grid = new GridPane();
        grid.setHgap(3);
        grid.setVgap(5);

        grid.setPrefWidth(500);
        grid.setAlignment(Pos.TOP_CENTER);


        grid.add(lblFirstName,0,0);
        grid.add(txtFirstName,1,0);


        grid.add(lblLastName,0,1);
        grid.add(txtLastName,1,1);


        grid.add(lblAm,0,2);
        grid.add(txtAm,1,2);



        grid.add(lblBirthday,0,3);
        grid.add(txtBirthday,1,3);



        grid.add(lblRole,0,4);
        grid.add(txtRole,1,4);



        grid.add(lblLastSignedIn,0,5);
        grid.add(txtLastSignedIn,1,5);



        grid.add(lblPassword,0,6);
        grid.add(txtPassword,1,6);

        grid.add(btnChange,1,7);
        GridPane.setHalignment(btnChange,HPos.CENTER);

        btnChange.setOnAction(e->{
            txtPassword.setEditable(true);
            txtPassword.setText("");
            btnChange.setVisible(false);

            Button savebtn = new Button("Αποθήκευση");
            Button cancelbtn = new Button("Άκυρο");

            HBox hboxbtns = new HBox();
            hboxbtns.getChildren().addAll(savebtn,cancelbtn);
            hboxbtns.setSpacing(5);
            hboxbtns.setAlignment(Pos.TOP_CENTER);
            grid.add(hboxbtns,1,7);

            savebtn.setOnAction(saveEvent->{
                if(txtPassword.getText().trim().isEmpty()){
                    new MessageBoxOk("ΠΑΡΑΚΑΛΩ ΔΩΣΤΕ ΚΩΔΙΚΟ");
                }else {
                     String hashedPassword = null;
                     hashedPassword = BCrypt.hashpw(txtPassword.getText().trim(),BCrypt.gensalt());
                    if(!hashedPassword.isEmpty()){
                        try{
                            String updQuery = "UPDATE users SET password = ? WHERE am = ?";
                            PreparedStatement stm = conn.prepareStatement(updQuery);
                            stm.setString(1,hashedPassword);
                            stm.setInt(2,user.getAm());
                            stm.executeUpdate();
                            stm.close();
                            hboxbtns.setVisible(false);
                            btnChange.setVisible(true);
                            txtPassword.setText("**********");
                            txtPassword.setEditable(false);
                        }catch (SQLException exception){
                            new MessageBoxOk(exception.getMessage());
                            exception.printStackTrace();
                        }
                    }else{
                        new MessageBoxOk("Error hashing the password\n");
                    }

                }
            });

            cancelbtn.setOnAction(cancelEvent->{
                txtPassword.setEditable(false);
                txtPassword.setText("************");
                hboxbtns.setVisible(false);
                btnChange.setVisible(true);
            });



        });


        ColumnConstraints col0 = new ColumnConstraints();
        col0.setPrefWidth(200);
        col0.setHalignment(HPos.RIGHT);


        ColumnConstraints col1 = new ColumnConstraints();
        col1.setHgrow(Priority.SOMETIMES);
        col1.setHalignment(HPos.LEFT);

        ColumnConstraints col2 = new ColumnConstraints();
        col2.setHgrow(Priority.SOMETIMES);
        col2.setHalignment(HPos.RIGHT);

        grid.getColumnConstraints().addAll(col0,col1,col2);





        profileScene = new Scene(grid);


    }

    public Scene getScene(){
        return this.profileScene;
    }

}
