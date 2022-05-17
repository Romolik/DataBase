package main.java.ru.nsu.shchiptsov.requests;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddSupplierController extends MainWindowProductRequestController{
	private Statement s = null;

	@FXML
	private Button exitButton;
	@FXML
	private Button addButton;
	@FXML
	private TextField nameSupplierTextField;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		exitButton.setOnAction(event -> {
			getStage().close();
		});
		addButton.setOnAction(event -> {
			String nameSupplier = nameSupplierTextField.getText();
			if (!Objects.equals(nameSupplier, "")) {
				try {
					s = getConnection().createStatement();
					ResultSet rs = s.executeQuery("SELECT * from Supplier" +
												  " where Name_Supplier = '" + nameSupplier + "'");
					if (rs.next()) {
						s.executeQuery("Update Supplier set Active = 1 where NAME_SUPPLIER = '" +
									   nameSupplier + "'");
					} else {
						s.executeQuery("INSERT into Supplier values (NULL,'" + nameSupplier +
									   "', 1)");
					}
					getConnection().commit();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}

}
