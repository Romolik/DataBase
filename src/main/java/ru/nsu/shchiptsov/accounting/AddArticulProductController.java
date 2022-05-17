package main.java.ru.nsu.shchiptsov.accounting;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddArticulProductController implements Initializable {
	private Stage stage;
	private Connection connection;
	private Integer idPointOfSale;
	private Statement s = null;

	@FXML
	private Button exitButton;
	@FXML
	private TextField articulProductTextField;
	@FXML
	private TextField nameProductTextField;
	@FXML
	private ChoiceBox typeProductChoiceBox;
	@FXML
	private Button addButton;

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setIdPointOfSale(Integer idPointOfSale) {
		this.idPointOfSale = idPointOfSale;
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		exitButton.setOnAction(event -> {
			stage.close();
		});
		addButton.setOnAction(event -> {
			String nameProduct = nameProductTextField.getText();
			String articul = articulProductTextField.getText();
			String typeProduct = (String) typeProductChoiceBox.getSelectionModel().getSelectedItem();
			if (Objects.equals(nameProduct, "") || Objects.equals(articul, "") ||
				typeProductChoiceBox.getSelectionModel().isEmpty()) {
				return;
			}
			try {
				s = connection.createStatement();
				ResultSet rs = s.executeQuery("SELECT ID_TYPE_PRODUCT from Type_Product where" +
							   " NAME_TYPE_PRODUCT = '" + typeProduct + "'");
				rs.next();
				Integer idTypeProduct = rs.getInt("ID_TYPE_PRODUCT");
				s.executeQuery("insert into Product values (NULL, " + idTypeProduct + ", " +
							   idPointOfSale + ", '" + nameProduct + "', '" + articul + "', 0," +
							   "0,0,0)");
				connection.commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}

		});
	}

	public void initializeInitialState() {
		try {
			s = connection.createStatement();
			ResultSet
					rs = s.executeQuery("SELECT * FROM Type_Product");
			ArrayList<String> listNameTypeProduct = new ArrayList<>	();
			while (rs.next()) {
				listNameTypeProduct.add(rs.getString("NAME_TYPE_PRODUCT"));
			}
			ObservableList<String>
					list = FXCollections.observableArrayList(listNameTypeProduct);
			typeProductChoiceBox.setItems(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
