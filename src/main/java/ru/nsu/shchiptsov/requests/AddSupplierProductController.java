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

public class AddSupplierProductController extends MainWindowProductRequestController {
	private Statement s = null;

	@FXML
	private Button exitButton;
	@FXML
	private Button addButton1;
	@FXML
	private Button addButton2;
	@FXML
	private TextField articulProductTextField;
	@FXML
	private TextField nameProductTextField;
	@FXML
	private TextField costPieceTextField;
	@FXML
	private TextField numberPieceTextField;
	@FXML
	private TextField cost100gTextField;
	@FXML
	private TextField quntityTextField;
	@FXML
	private ChoiceBox typeProductChoiceBox;
	@FXML
	private ChoiceBox nameSupplierChoiceBox;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		exitButton.setOnAction(event -> {
			getStage().close();
		});
		addButton1.setOnAction(event -> {
			Double cost = Double.parseDouble(costPieceTextField.getText());
			Double volume = Double.parseDouble(numberPieceTextField.getText());
			if (Objects.equals(costPieceTextField.getText(), "") ||
				Objects.equals(numberPieceTextField.getText(), "")) {
				return;
			}
			addProduct(cost, volume, true);
		});
		addButton2.setOnAction(event -> {
			Double cost = Double.parseDouble(cost100gTextField.getText());
			Double volume = Double.parseDouble(quntityTextField.getText());
			if (Objects.equals(cost100gTextField.getText(), "") ||
				Objects.equals(quntityTextField.getText(), "")) {
				return;
			}
			addProduct(cost, volume, false);
		});
	}

	private void addProduct(Double cost, Double volume, boolean piece) {
		String articul = articulProductTextField.getText();
		String nameProduct = nameProductTextField.getText();
		String typeProduct = (String) typeProductChoiceBox.getSelectionModel().getSelectedItem();
		String nameSupplier = (String) nameSupplierChoiceBox.getSelectionModel().getSelectedItem();
		if (nameProduct.equals("")  || typeProductChoiceBox.getSelectionModel().isEmpty() ||
			nameSupplierChoiceBox.getSelectionModel().isEmpty()) {
			return;
		}
		try {
			ResultSet rs = s.executeQuery("SELECT ID_SUPPLIER from Supplier where " +
										  "Name_Supplier = '" + nameSupplier + "'");
			rs.next();
			String idSupplier = rs.getString("ID_SUPPLIER");
			rs = s.executeQuery("SELECT ID_TYPE_PRODUCT from Type_Product where " +
								"NAME_TYPE_PRODUCT = '" + typeProduct + "'");
			rs.next();
			typeProduct = rs.getString("ID_TYPE_PRODUCT");
			boolean update = false;
			if (s.executeQuery("Select * from Product_Supplier where Article_Number = " +
							  articul).next()) {
				update = true;
			}
			if (update && articul.equals("")) {
				return;
			}
			String request;
			if (piece) {
				if (!update) {
					request = "Insert into Product_Supplier(ID_Product_Supplier,ID_Type_Product," +
							  "ID_Supplier,Name_Product,\n" +
							  " Number_of_Pieces,\"Quantity(weight_in_grams)\",\n" +
							  " \"Сost per piece\",\"Cost per 100g\")" +
							  " values (Null, " +
							  typeProduct + ", " +
							  idSupplier + ", '" + nameProduct + "', " +
							  volume + ", 0, " + cost + ", 0)";
				} else {
					request = "UPDATE Product_Supplier set ID_TYPE_PRODUCT = " + typeProduct + "," +
							  " ID_SUPPLIER = " + idSupplier + ", NUMBER_OF_PIECES = " + volume + ", " +
							  "\"Сost per piece\" = " + cost + " where Article_Number = " + articul;
				}
			} else {
				if (!update) {
					request = "Insert into Product_Supplier(ID_Product_Supplier,ID_Type_Product," +
							  "ID_Supplier,Name_Product,\n" +
							  "Number_of_Pieces,\"Quantity(weight_in_grams)\",\n" +
							  "\"Сost per piece\",\"Cost per 100g\") values (Null, " +
							  typeProduct + ", " +
							  idSupplier + ", '" + nameProduct + "', 0, " +
							  volume + ", 0, " + cost + ")";
				} else {
					request = "UPDATE Product_Supplier set ID_TYPE_PRODUCT = " + typeProduct + "," +
							  " ID_SUPPLIER = " + idSupplier + ", \"Quantity(weight_in_grams)\" = " + volume + ", " +
							  "\"Cost per 100g\" = " + cost + " where Article_Number = " + articul;
				}
			}
			s.executeQuery(request);
			getConnection().commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


	public void initializeInitialState() {
		try {
			s = getConnection().createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM Supplier where Active = 1");
			ArrayList<String> listNameSupplier = new ArrayList<>();
			while (rs.next()) {
				listNameSupplier.add(rs.getString("NAME_SUPPLIER"));
			}
			ObservableList<String>
					list = FXCollections.observableArrayList(listNameSupplier);
			nameSupplierChoiceBox.setItems(list);
			rs = s.executeQuery("SELECT * FROM Type_Product");
			ArrayList<String> listTypeProduct = new ArrayList<>();
			while (rs.next()) {
				listTypeProduct.add(rs.getString("NAME_TYPE_PRODUCT"));
			}
			ObservableList<String>
					list2 = FXCollections.observableArrayList(listTypeProduct);
			typeProductChoiceBox.setItems(list2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
