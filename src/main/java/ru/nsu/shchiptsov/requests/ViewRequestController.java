package main.java.ru.nsu.shchiptsov.requests;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class ViewRequestController extends MainWindowProductRequestController {
	private Statement s = null;

	@FXML
	private Button exitButton;
	@FXML
	private Button orderButton1;
	@FXML
	private Button orderButton2;
	@FXML
	private TextField numberPieceTextField;
	@FXML
	private TextField quntityTextField;
	@FXML
	private ChoiceBox requestChoiceBox;
	@FXML
	private ChoiceBox nameSupplierChoiceBox;
	@FXML
	private DatePicker dateOrder;
	@FXML
	private Button removeButton;

	String nameProduct;
	String numberPiece;
	String quantity;
	String idSupplier;
	String numberRequest;
	Integer idPointOfSale = null;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		exitButton.setOnAction(event -> {
			getStage().close();
		});
		requestChoiceBox.setOnAction(event -> {
			String request = (String) requestChoiceBox.getSelectionModel().getSelectedItem();
			ArrayList partsRequest = parseRequest(request);
			nameProduct = (String) partsRequest.get(0);
			numberPiece = (String) partsRequest.get(2);
			quantity = (String) partsRequest.get(4);
			numberRequest = (String) partsRequest.get(partsRequest.size() - 1);
			showSupplier();
		});
		removeButton.setOnAction(event -> {
			if (requestChoiceBox.getSelectionModel().isEmpty()) {
				return;
			}
			try {
				s = getConnection().createStatement();
				s.executeQuery("Update Request set Status = 'refused' where " +
							   "NUMBER_REQUEST = " + numberRequest);
				initializeInitialState();
				getConnection().commit();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
		orderButton1.setOnAction(event -> {
			order(true);
		});
		orderButton2.setOnAction(event -> {
			order(false);
		});
	}

	private void order(boolean piece) {
		LocalDate dateOrderValue = dateOrder.getValue();
		if (nameSupplierChoiceBox.getSelectionModel().isEmpty() || dateOrderValue == null) {
			return;
		}
		ArrayList<String> supplier = parseRequest(
				(String) nameSupplierChoiceBox.getSelectionModel().getSelectedItem());
		Double volumeSupplier;
		Double volumeRequest;
		if (piece) {
			volumeSupplier = Double.parseDouble(supplier.get(2));
			volumeRequest = Double.parseDouble(numberPieceTextField.getText());
		} else {
			volumeSupplier = Double.parseDouble(supplier.get(6));
			volumeRequest = Double.parseDouble(quntityTextField.getText());
		}
		Double newVolume = volumeSupplier - volumeRequest;
		if (newVolume < 0) {
			return;
		}
		int day = dateOrderValue.getDayOfMonth();
		int month = dateOrderValue.getMonth().getValue();
		int year = dateOrderValue.getYear();
		String competitionDateOrder = year + "-" + month + "-" + day;
		String volume = ", " + volumeRequest + ", 0)";
		String article = supplier.get(supplier.size() - 1);
		try {
			s = getConnection().createStatement();
			s.executeQuery("INSERT into INVOICE values(Null, " + getIdEmployee()  +
						   ", to_date('" + competitionDateOrder + "', 'yyyy-mm-dd')" + volume);
			String request = "SELECT max(ID_INVOICE) as maxIdInvoice from Invoice " +
							 "where id_employee = " + getIdEmployee();
			ResultSet rs = s.executeQuery(request);
			if (!rs.next()) {
				return;
			}
			String idInvoice = rs.getString("maxIdInvoice");
			Double cost;
			if (piece) {
				cost = volumeRequest * Double.parseDouble(supplier.get(4));
			} else {
				cost = volumeRequest * Double.parseDouble(supplier.get(8));
			}
			rs = s.executeQuery("Select Id_Product from Product where " +
								"ARTICLE_NUMBER = '" + article + "'");
			if (!rs.next()) {
				return;
			}
			s.executeQuery("INSERT into Order_Shop (ID_Order_shop, ID_Point_of_Sale," +
						   "ID_Product_Supplier, Id_Product, ID_Invoice, ID_Supplier, \"Cost($)\", " +
						   "Status) values (Null, " + getIdPointOfSale() + ", " +
						   getIdProductSupplier() + ", " + rs.getString("Id_Product") +
						   ", " + idInvoice + ", " + getIdSupplier(supplier.get(0)) +
						   ", " + cost + ",'Considered')");
			s.executeQuery("UPDATE request set STATUS = 'Done' where Number_Request = " +
						   numberRequest);
			getConnection().commit();
		} catch (SQLException e) {
			try {
				getConnection().rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public void initializeInitialState() {
		try {
			s = getConnection().createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM Request where STATUS = 'Considered'");
			ArrayList<String> listRequest = new ArrayList<>();
			while (rs.next()) {
				String numberPiece = rs.getString("Number_of_Pieces");
				String quantity = rs.getString("\"Quantity(weight_in_grams)\"");
				String idProduct = rs.getString("ID_PRODUCT");
				String numberRequest = rs.getString("Number_Request");
				Statement s2 = getConnection().createStatement();
				ResultSet rs2 = s2.executeQuery("Select NAME_PRODUCT from product where ID_PRODUCT = " +
							   idProduct);
				if (rs2.next()) {
					listRequest.add(rs2.getString("NAME_PRODUCT") + " , " +
									numberPiece + " шт, " + quantity +
									" грамм, Номер заявки: " + numberRequest);
				}
			}
			ObservableList<String> list = FXCollections.observableArrayList(listRequest);
			requestChoiceBox.setItems(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private ArrayList<String> parseRequest(String request) {
		return new ArrayList<>(Arrays.asList(request.split(" ")));
	}

	private void showSupplier() {
		ResultSet rs = null;
		try {
			rs = s.executeQuery("SELECT NAME_SUPPLIER," +
								"product_Supplier.NUMBER_OF_PIECES," +
								"product_Supplier.\"Quantity(weight_in_grams)\"," +
								"product_Supplier.\"Сost per piece\"," +
								"product_Supplier.Article_Number," +
								"product_Supplier.\"Cost per 100g\"" +
								" FROM Supplier Inner Join" +
								" product_Supplier on product_Supplier.ID_SUPPLIER = " +
								"Supplier.ID_SUPPLIER where Active = 1 And NAME_PRODUCT = '" +
								nameProduct + "'");
			ArrayList<String> listNameSupplier = new ArrayList<>();
			while (rs.next()) {
				listNameSupplier.add(rs.getString("NAME_SUPPLIER") + " , " +
									 rs.getString("NUMBER_OF_PIECES") + " шт, " +
									 rs.getString("\"Сost per piece\"") + " $/шт, " +
									 rs.getString("\"Quantity(weight_in_grams)\"") + " г, " +
									 rs.getString("\"Cost per 100g\"") + " $/100г, " +
									 "Артикль: " + rs.getString("Article_Number"));
			}
			ObservableList<String>
					list = FXCollections.observableArrayList(listNameSupplier);
			nameSupplierChoiceBox.setItems(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private Integer getIdPointOfSale() {
		if (idPointOfSale == null) {
			try {
				s = getConnection().createStatement();
				String request = "SELECT ID_POINT_OF_SALE \n" +
								 "  FROM Employee " +
								 " Where  Id_Employee = " + getIdEmployee();
				ResultSet rs = s.executeQuery(request);
				rs.next();
				idPointOfSale = rs.getInt("ID_POINT_OF_SALE");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return idPointOfSale;
	}

	private Integer getIdProductSupplier() {
		Integer idProductSupplier = null;
		try {
			s = getConnection().createStatement();
			String request = "SELECT ID_Product_Supplier \n" +
							 "  FROM Product_Supplier " +
							 " Where  Name_Product = '" + nameProduct + "'";
			ResultSet rs = s.executeQuery(request);
			rs.next();
			idProductSupplier = rs.getInt("ID_Product_Supplier");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return idProductSupplier;
	}

	private Integer getIdSupplier(String nameSupplier) {
		Integer IdSupplier = null;
		try {
			s = getConnection().createStatement();
			String request = "SELECT ID_SUPPLIER \n" +
							 "  FROM Supplier " +
							 " Where  NAME_SUPPLIER = '" + nameSupplier + "'";
			ResultSet rs = s.executeQuery(request);
			rs.next();
			IdSupplier = rs.getInt("ID_SUPPLIER");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return IdSupplier;
	}

}
