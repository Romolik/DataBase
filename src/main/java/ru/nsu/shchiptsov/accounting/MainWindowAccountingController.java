package main.java.ru.nsu.shchiptsov.accounting;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCombination;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.ru.nsu.shchiptsov.DatabaseFields;
import main.java.ru.nsu.shchiptsov.MainWindowController;
import main.java.ru.nsu.shchiptsov.sellers.CreateCardBuyerController;

import java.io.IOException;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainWindowAccountingController extends MainWindowController {
	@FXML
	private Button cancelButton;
	@FXML
	private Button changePriceButton;
	@FXML
	private Button transferProductButton;
	@FXML
	private Button assortmentButton;
	@FXML
	private TableView<DatabaseFields> mainTableView;
	@FXML
	private Button addArticulProductButton;

	private TableColumn<DatabaseFields, String> nameProduct = new TableColumn<>("Название");
	private TableColumn<DatabaseFields, String> articleNumber = new TableColumn<>("Артикул");
	private TableColumn<DatabaseFields, Integer> numberOfPieces = new TableColumn<>("Кол-во(шт)");
	private TableColumn<DatabaseFields, Double> quantity = new TableColumn<>("Масса(граммы)");
	private TableColumn<DatabaseFields, Double> costPerPiece = new TableColumn<>("Стоимость(шт)");
	private TableColumn<DatabaseFields, Double> costPer100g = new TableColumn<>("Стоимость(100г)");

	private final ObservableList<DatabaseFields> usersData = FXCollections.observableArrayList();
	private Statement s = null;
	private Integer idPointOfSale = null;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		initializeInitialState();
		cancelButton.setOnAction(event -> {
			loadMainMenuWindow(getNameRole(), getIdEmployee());
		});
		assortmentButton.setOnAction(event -> {
			resetColumns();
			try {
				s = getConnection().createStatement();
				String request = "SELECT product.ID_POINT_OF_SALE, \n" +
								 "       product.NAME_PRODUCT, \n" +
								 "       product.ARTICLE_NUMBER,\n" +
								 "       product.NUMBER_OF_PIECES, \n" +
								 "       product.\"Quantity(weight_in_grams)\",\n" +
								 "       product.\"Сost per piece\",\n" +
								 "       product.\"Cost per 100g\",\n" +
								 "       employee.Id_Point_Of_Sale" +
								 "  FROM Product INNER JOIN Employee\n" +
								 "    ON product.ID_POINT_OF_SALE = employee.Id_Point_Of_Sale\n" +
								 " Where  employee.Id_Employee = " + getIdEmployee();
				ResultSet rs = s.executeQuery(request);
				while (rs.next()) {
					DatabaseFields tmp = new DatabaseFields();
					tmp.setNameProduct(rs.getString("NAME_PRODUCT"));
					tmp.setArticleNumber(rs.getString("ARTICLE_NUMBER"));
					tmp.setNumberOfPieces(rs.getInt("NUMBER_OF_PIECES"));
					tmp.setQuantity(rs.getDouble("Quantity(weight_in_grams)"));
					tmp.setCostPiece(rs.getDouble("Сost per piece"));
					tmp.setCost100g(rs.getDouble("Cost per 100g"));
					usersData.add(tmp);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			mainTableView.getColumns().get(0).getColumns().add(nameProduct);
			mainTableView.getColumns().get(1).getColumns().add(articleNumber);
			mainTableView.getColumns().get(2).getColumns().add(numberOfPieces);
			mainTableView.getColumns().get(3).getColumns().add(quantity);
			mainTableView.getColumns().get(4).getColumns().add(costPerPiece);
			mainTableView.getColumns().get(5).getColumns().add(costPer100g);
		});
		changePriceButton.setOnAction(event -> {
			getIdPointOfSale();
			Stage onTop = new Stage();
			onTop.initOwner(getStage().getScene().getWindow());
			onTop.initModality(Modality.WINDOW_MODAL);
			onTop.setTitle("Change price");
			onTop.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
			FXMLLoader loader = new FXMLLoader ();
			loader.setLocation (getClass ().getResource ("/accounting/ChangeCostProduct.fxml"));
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
			ChangeCostProductController changeCostProductController = loader.getController();
			changeCostProductController.setStage(onTop);
			changeCostProductController.setIdEmployee(getIdEmployee());
			changeCostProductController.setConnection(getConnection());
			changeCostProductController.setIdPointOfSale(idPointOfSale);
			Parent root = loader.getRoot();
			Scene scene = new Scene (root);
			scene.getStylesheets().add(
					Objects.requireNonNull(getClass().getResource("/application.css")).toExternalForm());
			onTop.setScene (scene);
			onTop.show();
		});
		transferProductButton.setOnAction(event -> {
			getIdPointOfSale();
			Stage onTop = new Stage();
			onTop.initOwner(getStage().getScene().getWindow());
			onTop.initModality(Modality.WINDOW_MODAL);
			onTop.setTitle("Change price");
			onTop.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
			FXMLLoader loader = new FXMLLoader ();
			loader.setLocation (getClass ().getResource ("/accounting/TransferProduct.fxml"));
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
			TransferProductController transferProductController = loader.getController();
			transferProductController.setStage(onTop);
			transferProductController.setIdEmployee(getIdEmployee());
			transferProductController.setConnection(getConnection());
			transferProductController.setIdPointOfSale(idPointOfSale);
			transferProductController.initializeInitialState();
			Parent root = loader.getRoot();
			Scene scene = new Scene (root);
			scene.getStylesheets().add(
					Objects.requireNonNull(getClass().getResource("/application.css")).toExternalForm());
			onTop.setScene (scene);
			onTop.show();
		});
		addArticulProductButton.setOnAction(event -> {
			getIdPointOfSale();
			Stage onTop = new Stage();
			onTop.initOwner(getStage().getScene().getWindow());
			onTop.initModality(Modality.WINDOW_MODAL);
			onTop.setTitle("Change price");
			onTop.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
			FXMLLoader loader = new FXMLLoader ();
			loader.setLocation (getClass ().getResource ("/accounting/AddArticulProduct.fxml"));
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
			AddArticulProductController addArticulProductController = loader.getController();
			addArticulProductController.setStage(onTop);
			addArticulProductController.setConnection(getConnection());
			addArticulProductController.setIdPointOfSale(idPointOfSale);
			addArticulProductController.initializeInitialState();
			Parent root = loader.getRoot();
			Scene scene = new Scene (root);
			scene.getStylesheets().add(
					Objects.requireNonNull(getClass().getResource("/application.css")).toExternalForm());
			onTop.setScene (scene);
			onTop.show();
		});
	}

	private void resetColumns() {
		usersData.clear();
		for (TableColumn<DatabaseFields, ?> column : mainTableView.getColumns()) {
			if (!column.getColumns().isEmpty()) {
				column.getColumns().remove(0);
			}
		}
	}

	private void getIdPointOfSale() {
		if (idPointOfSale == null) {
			try {
				s = getConnection().createStatement();
				String request = "SELECT product.ID_POINT_OF_SALE, \n" +
								 "       employee.Id_Point_Of_Sale" +
								 "  FROM Product INNER JOIN Employee\n" +
								 "    ON product.ID_POINT_OF_SALE = employee.Id_Point_Of_Sale\n" +
								 " Where  employee.Id_Employee = " + getIdEmployee();
				ResultSet rs = s.executeQuery(request);
				rs.next();
				idPointOfSale = rs.getInt("ID_POINT_OF_SALE");
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	private void initializeInitialState() {
		nameProduct.setPrefWidth(133);
		articleNumber.setPrefWidth(133);
		numberOfPieces.setPrefWidth(133);
		quantity.setPrefWidth(133);
		costPer100g.setPrefWidth(133);
		costPerPiece.setPrefWidth(133);
		nameProduct.setCellValueFactory(new PropertyValueFactory<>("nameProduct"));
		articleNumber.setCellValueFactory(new PropertyValueFactory<>("articleNumber"));
		numberOfPieces.setCellValueFactory(new PropertyValueFactory<>("numberOfPieces"));
		quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		costPerPiece.setCellValueFactory(new PropertyValueFactory<>("costPiece"));
		costPer100g.setCellValueFactory(new PropertyValueFactory<>("cost100g"));
		mainTableView.setItems(usersData);
	}
}
