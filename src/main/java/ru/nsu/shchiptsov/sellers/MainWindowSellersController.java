package main.java.ru.nsu.shchiptsov.sellers;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCombination;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.ru.nsu.shchiptsov.AccessLevelErrorController;
import main.java.ru.nsu.shchiptsov.DatabaseFields;
import main.java.ru.nsu.shchiptsov.MainWindowController;
import org.apache.ibatis.annotations.Update;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainWindowSellersController extends MainWindowController {
	@FXML
	private Button cancelButton;
	@FXML
	private DatePicker dateSale;
	@FXML
	private TextField articulProduct;
	@FXML
	private TextField numberCard;
	@FXML
	private TextField volumeProduct;
	@FXML
	private Button sellButton;
	@FXML
	private Button createCardBuyer;

	private Statement s = null;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		cancelButton.setOnAction(event -> {
			loadMainMenuWindow(getNameRole(), getIdEmployee());
		});
		sellButton.setOnAction(event -> {
			try {
				String articul = articulProduct.getText();
				Double volume= Double.parseDouble(volumeProduct.getText());
				LocalDate dateSaleValue = dateSale.getValue();
				if (articul == null || volume <= 0 || dateSaleValue == null) {
					showSellErrorWindow();
					return;
				}
				int day = dateSaleValue.getDayOfMonth();
				int month = dateSaleValue.getMonth().getValue();
				int year = dateSaleValue.getYear();
				String competitionDateSale = year + "-" + month + "-" + day;
				s = getConnection().createStatement();
				String typeProduct = "";
				String request  = "Select NUMBER_OF_PIECES, " +
								  "\"Quantity(weight_in_grams)\"," +
								  "Id_Product, " +
								  "\"Сost per piece\"," +
								  "\"Cost per 100g\"," +
								  "product.ID_POINT_OF_SALE" +
								  " from Product" +
								  " Inner Join Employee on product.ID_POINT_OF_SALE = employee.ID_POINT_OF_SALE" +
								  " Where product.ARTICLE_NUMBER = '" + articul + "' AND " +
								  "employee.ID_Employee = " + getIdEmployee();
				ResultSet rs = s.executeQuery(request);
				rs.next();
				DatabaseFields tmp = new DatabaseFields();
				tmp.setNumberOfPieces(rs.getInt("NUMBER_OF_PIECES"));
				tmp.setIdPointOfSale(rs.getInt("ID_POINT_OF_SALE"));
				tmp.setQuantity(rs.getDouble("\"Quantity(weight_in_grams)\""));
				tmp.setIdProduct(rs.getInt("Id_Product"));
				tmp.setCost100g(rs.getDouble("\"Cost per 100g\""));
				tmp.setCostPiece(rs.getDouble("Сost per piece"));
				if ((tmp.getQuantity() <= 0 && tmp.getNumberOfPieces() <= 0)) {
					showSellErrorWindow();
					return;
				}
				Double newVolume;
				Double cost;
				String quantity;
				Double idBuyer = 1.0;
				if (tmp.getNumberOfPieces() > 0) {
					newVolume = tmp.getNumberOfPieces() - volume;
					if (newVolume < 0) {
						showSellErrorWindow();
						return;
					}
					cost = tmp.getCostPiece();
					typeProduct = "NUMBER_OF_PIECES";
					quantity = ", " + volume + ",0)";
				} else  {
					newVolume = tmp.getNumberOfPieces() - volume;
					if (newVolume < 0) {
						showSellErrorWindow();
						return;
					}
					cost = tmp.getCost100g();
					typeProduct = "\"Quantity(weight_in_grams)\"";
					quantity = ", 0, " + volume + ")";
				}
				cost *= volume;
				s = getConnection().createStatement();
				request = "UPDATE Product set " + typeProduct + " = " + newVolume +
				" where Id_Product = " + tmp.getIdProduct();
				s.executeQuery(request);
				if (numberCard.getText() != "") {
					Double lastMoneySpent;
					rs = s.executeQuery("SELECT \"Money_Spent($)\", id_Buyer from Buyer " +
								   "where buyer.NUMBER_CARD = " + numberCard.getText());
					rs.next();
					idBuyer = rs.getDouble("id_Buyer");
					lastMoneySpent = rs.getDouble("\"Money_Spent($)\"");
					lastMoneySpent += cost;
					s.executeQuery("UPDATE Buyer set \"Money_Spent($)\" = " + lastMoneySpent +
								   "where buyer.NUMBER_CARD = " + numberCard.getText());
				}
				request = "INSERT into INVOICE values(Null, " + getIdEmployee()  +
						  ", to_date('" + competitionDateSale + "', 'yyyy-mm-dd')" + quantity;
				s.executeQuery(request);
				request = "SELECT max(ID_INVOICE) as maxIdInvoice from Invoice " +
						  "where id_employee = " + getIdEmployee();
				rs = s.executeQuery(request);
				rs.next();
				String idInvoice = rs.getString("maxIdInvoice");
				request = "insert into Purchase values (Null, " + tmp.getIdPointOfSale() + ", " + tmp.getIdProduct() + "," +
						  " " + idInvoice + ", " + idBuyer + ", " + cost + ")";
				s.executeQuery(request);
				getConnection().commit();

			} catch (SQLException e) {
				try {
					getConnection().rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
				e.printStackTrace();
			}
		});
		createCardBuyer.setOnAction(event -> {
			Stage onTop = new Stage();
			onTop.initOwner(getStage().getScene().getWindow());
			onTop.initModality(Modality.WINDOW_MODAL);
			onTop.setTitle("Create card buyer");
			onTop.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
			FXMLLoader loader = new FXMLLoader ();
			loader.setLocation (getClass ().getResource ("/sellers/CreateCardBuyer.fxml"));
			try {
				loader.load ();
			} catch (IOException e) {
				e.printStackTrace ();
			}
			onTop.setOnCloseRequest (new EventHandler<WindowEvent>() {
				@Override
				public void handle (WindowEvent event) {
					event.consume ();
				}
			});
			CreateCardBuyerController createCardBuyerController = loader.getController();
			createCardBuyerController.setStage(onTop);
			createCardBuyerController.setConnection(getConnection());
			Parent root = loader.getRoot();
			Scene scene = new Scene (root);
			scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/application.css")).toExternalForm());
			onTop.setScene (scene);
			onTop.show();
		});
	}

	private void showSellErrorWindow() {
		Stage onTop = new Stage();
		onTop.initOwner(getStage().getScene().getWindow());
		onTop.initModality(Modality.WINDOW_MODAL);
		onTop.setTitle("Sell error");
		onTop.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		FXMLLoader loader = new FXMLLoader ();
		loader.setLocation (getClass ().getResource ("/sellers/ProductSellError.fxml"));
		try {
			loader.load ();
		} catch (IOException e) {
			e.printStackTrace ();
		}
		onTop.setOnCloseRequest (new EventHandler<WindowEvent>() {
			@Override
			public void handle (WindowEvent event) {
				event.consume ();
			}
		});
		ProductSellError productSellError = loader.getController();
		productSellError.setStage(onTop);
		Parent root = loader.getRoot();
		Scene scene = new Scene (root);
		scene.getStylesheets().add(Objects.requireNonNull(
				getClass().getResource("/application.css")).toExternalForm());
		onTop.setScene (scene);
		onTop.show();
	}

}
