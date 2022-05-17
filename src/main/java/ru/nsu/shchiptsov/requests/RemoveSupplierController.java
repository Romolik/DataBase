package main.java.ru.nsu.shchiptsov.requests;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class RemoveSupplierController extends MainWindowProductRequestController {
	private Statement s = null;

	@FXML
	private Button exitButton;
	@FXML
	private Button removeButton;
	@FXML
	private ChoiceBox choiceBox;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		exitButton.setOnAction(event -> {
			getStage().close();
		});
		removeButton.setOnAction(event -> {
			if (!choiceBox.getSelectionModel().isEmpty()) {
				String nameSupplier = (String) choiceBox.getSelectionModel().getSelectedItem();
				try {
					s = getConnection().createStatement();
					s.executeQuery("Update Supplier set Active = 0 where NAME_SUPPLIER = '" +
								   nameSupplier + "'");
					getConnection().commit();
					initializeInitialState();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}

	public void initializeInitialState() {
		try {
			s = getConnection().createStatement();
			ResultSet
					rs = s.executeQuery("SELECT * FROM Supplier where Active = 1");
			ArrayList<String> listNameSupplier = new ArrayList<>	();
			while (rs.next()) {
				listNameSupplier.add(rs.getString("NAME_SUPPLIER"));
			}
			ObservableList<String>
					list = FXCollections.observableArrayList(listNameSupplier);
			choiceBox.setItems(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
