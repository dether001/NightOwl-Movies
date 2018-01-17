/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package moviestest;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 *
 * @author dell
 */
public class FXMLDocumentController implements Initializable {
    
    @FXML
    private Label label;
    @FXML
    private Button btn_addMovie;
    @FXML
    private TextField txt_length;
    @FXML
    private TextField txt_rating;
    @FXML
    private TextField txt_prating;
    @FXML
    private TextField txt_name;
    @FXML
    private TextField txt_path;
    
    private Connection con = null;
    private PreparedStatement pst = null;
    private ResultSet rs = null;
    private ObservableList<MovieList> data;
            
    @FXML
    private TableView<MovieList> tableMovies;
    @FXML
    private TableColumn<?, ?> cName;
    @FXML
    private TableColumn<?, ?> cLength;
    @FXML
    private TableColumn<?, ?> cRating;
    @FXML
    private TableColumn<?, ?> cPRating;
    @FXML
    private TableColumn<?, ?> cPath;
    
    
    @FXML
    private void handleAddMovie(ActionEvent event) {
        String sql = "Insert into movies(name,length,rating,prating,path) Values(?,?,?,?,?)";
        String name = txt_name.getText();
        double length = Double.valueOf(txt_length.getText());
        double rating = Double.valueOf(txt_rating.getText());
        double prating = Double.valueOf(txt_prating.getText());
        String path = txt_path.getText();
        try {
            pst = con.prepareStatement(sql);
            pst.setString(1, name);
            pst.setDouble(2, length);
            pst.setDouble(3, rating);
            pst.setDouble(4, prating);
            pst.setString(5, path);
            
            int i = pst.executeUpdate();
            if(i == 1)
                System.out.println("Data inserted");
            setCellTable();
            loadDataFromDB();
                    
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        con = dba.DBConnection.dbConnection();
        data = FXCollections.observableArrayList();
        setCellTable();
        loadDataFromDB();
    }    
    private void setCellTable() {
        cName.setCellValueFactory(new PropertyValueFactory<>("name"));
        cLength.setCellValueFactory(new PropertyValueFactory<>("length"));
        cRating.setCellValueFactory(new PropertyValueFactory<>("rating"));
        cPRating.setCellValueFactory(new PropertyValueFactory<>("prating"));
        cPath.setCellValueFactory(new PropertyValueFactory<>("path"));
    }
    private void loadDataFromDB() {
        data.clear();
        try {
            pst = con.prepareStatement("Select * from movies");
            rs = pst.executeQuery();
            while(rs.next()){
                data.add(new MovieList(rs.getString(2), ""+rs.getDouble(3), ""+rs.getDouble(4), ""+rs.getDouble(5), rs.getString(6)));
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }
        tableMovies.setItems(data);
    }



    }