package main.java.ru.nsu.shchiptsov.orders;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import main.java.ru.nsu.shchiptsov.MainWindowController;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.ResourceBundle;

public class MainWindowOrdersController extends MainWindowController {
	private Statement s = null;

	@FXML
	private Button cancelButton;
	@FXML
	private Button confirmButton;
	@FXML
	private Button notConfirmButton;
	@FXML
	private ChoiceBox ordersChoiceBox;

	private boolean piece;


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		cancelButton.setOnAction(event -> {
			loadMainMenuWindow(getNameRole(), getIdEmployee());
		});
		confirmButton.setOnAction(event -> {
			if (ordersChoiceBox.getSelectionModel().isEmpty()) {
				return;
			}
			ArrayList<String> order = parseOrder(
					(String) ordersChoiceBox.getSelectionModel().getSelectedItem());
			String numberOrder = order.get(order.size() - 1);
			String nameProduct = order.get(0);
			String articul = order.get(7);
			try {
				s = getConnection().createStatement();
				if (piece) {
					ResultSet rs = s.executeQuery("Select NUMBER_OF_PIECES" +
												  " from product_supplier where " +
												  "Article_Number = " + articul);
					if (rs.next()) {
						Integer volume = rs.getInt("NUMBER_OF_PIECES");
						Integer newVolume = volume - Integer.parseInt(order.get(2));
						if (newVolume < 0) {
							s.executeQuery("UPDATE ORDER_shop set STATUS = 'Refused' where" +
										   " NUMBER_ORDER_SHOP = " + numberOrder);
							return;
						}
						s.executeQuery("UPDATE product_Supplier set NUMBER_OF_PIECES = " +
									   newVolume + " where Article_Number = " + articul);
						s.executeQuery("UPDATE ORDER_shop set STATUS = 'Done' where" +
									   "  NUMBER_ORDER_SHOP = " + numberOrder);
						rs = s.executeQuery("Select product.ID_PRODUCT, product.NUMBER_OF_PIECES from Product " +
											"Inner JOIN order_Shop on product.Id_Product = " +
											"order_Shop.Id_Product where NUMBER_ORDER_SHOP = " + numberOrder);
						rs.next();

						s.executeQuery("UPDATE Product set NUMBER_OF_PIECES = " +
									(rs.getInt("NUMBER_OF_PIECES") + Integer.parseInt(order.get(2)))  +
									   "where Id_Product = " + rs.getString("ID_PRODUCT"));
						getConnection().commit();
					}
				} else {
					ResultSet rs = s.executeQuery("Select \"Quantity(weight_in_grams)\"" +
												  " from product_supplier where " +
												  "Article_Number = " + articul);
					if (rs.next()) {
						Double volume = rs.getDouble("\"Quantity(weight_in_grams)\"");
						Double newVolume = volume - Double.parseDouble(order.get(2));
						if (newVolume < 0) {
							s.executeQuery("UPDATE ORDER_shop set STATUS = 'Refused' where" +
										   " NUMBER_ORDER_SHOP = " + numberOrder);
							return;
						}
						s.executeQuery("UPDATE product_Supplier set \"Quantity(weight_in_grams)\" = " +
									   newVolume + " where Article_Number = " + articul);
						s.executeQuery("UPDATE ORDER_shop set STATUS = 'Done' where" +
									   "  NUMBER_ORDER_SHOP = " + numberOrder);
						rs = s.executeQuery("Select order_shop.ID_PRODUCT, \"Quantity(weight_in_grams)\" from Product " +
											"Inner JOIN order_Shop on product.Id_Product = " +
											"order_Shop.Id_Product where NUMBER_ORDER_SHOP = " + numberOrder);
						rs.next();
						s.executeQuery("UPDATE Product set \"Quantity(weight_in_grams)\" = " +
									   (rs.getDouble("\"Quantity(weight_in_grams)\"")
										+ Double.parseDouble(order.get(2))) + "where Id_Product = " +
									   rs.getString("ID_PRODUCT"));
						getConnection().commit();
					}
				}
				initializeInitialState();
			} catch (SQLException e) {
				try {
					getConnection().rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
				e.printStackTrace();
			}
		});
		notConfirmButton.setOnAction(event -> {
			if (ordersChoiceBox.getSelectionModel().isEmpty()) {
				return;
			}
			ArrayList<String> order = parseOrder(
					(String) ordersChoiceBox.getSelectionModel().getSelectedItem());
			String numberOrder = order.get(order.size() - 1);
			try {
				s = getConnection().createStatement();
				s.executeQuery("UPDATE Order_Shop set STATUS = 'Refused' where " +
							   "NUMBER_ORDER_SHOP = " + numberOrder);
				getConnection().commit();
				initializeInitialState();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

	public void initializeInitialState() {
		try {
			s = getConnection().createStatement();
			ResultSet rs =
					s.executeQuery("SELECT order_shop.ID_PRODUCT_SUPPLIER,\n" +
								   " product_supplier.NAME_PRODUCT,\n" +
								   " invoice.NUMBER_OF_PIECES,\n" +
								   " invoice.\"Quantity(weight_in_grams)\",\n" +
								   " order_shop.\"Cost($)\",\n" +
								   " PRODUCT_SUPPLIER.Article_Number,\n" +
								   " order_shop.NUMBER_ORDER_SHOP\n" +
								   " FROM Order_Shop" +
								   " INNER JOIN PRODUCT_SUPPLIER ON" +
								   " order_shop.ID_PRODUCT_SUPPLIER = product_supplier.ID_PRODUCT_SUPPLIER" +
								   " Inner Join Invoice on invoice.ID_INVOICE = order_shop.ID_INVOICE" +
								   " where order_shop.STATUS = 'Considered' And " +
								   "order_shop.ID_SUPPLIER = " +
								   getIdEmployee());
			ArrayList<String> listOrders = new ArrayList<>();
			while (rs.next()) {
				piece = true;
				if (rs.getInt("NUMBER_OF_PIECES") <
					rs.getDouble("Quantity(weight_in_grams)")) {
					piece = false;
				}
				if (piece) {
					listOrders.add(rs.getString("NAME_PRODUCT") + " , " +
								   rs.getString("NUMBER_OF_PIECES") + " шт, " +
								   rs.getString("\"Cost($)\"") + " $, Артикль: " +
								   rs.getString("Article_Number") +
								   " Номер заказа: " + rs.getString("NUMBER_ORDER_SHOP"));
				} else {
					listOrders.add(rs.getString("NAME_PRODUCT") + " , " +
								   rs.getString("\"Quantity(weight_in_grams)\"") + " г, " +
								   rs.getString("\"Cost($)\"") + " $, Артикль: " +
								   rs.getString("Article_Number") +
								   " Номер заказа: " + rs.getString("NUMBER_ORDER_SHOP"));
				}
			}
			ObservableList<String>
					list = FXCollections.observableArrayList(listOrders);
			ordersChoiceBox.setItems(list);

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	private ArrayList<String> parseOrder(String request) {
		return new ArrayList<>(Arrays.asList(request.split(" ")));
	}
}
