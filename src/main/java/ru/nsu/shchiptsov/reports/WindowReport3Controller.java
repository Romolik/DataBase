package main.java.ru.nsu.shchiptsov.reports;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.ru.nsu.shchiptsov.DatabaseFields;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WindowReport3Controller extends WindowReport {
	@FXML
	private Button exitButton;
	@FXML
	private Button findButton;
	@FXML
	private TableView<DatabaseFields> mainTableView;
	@FXML
	private ChoiceBox choiceBox;

	private TableColumn<DatabaseFields, String> nameProduct = new TableColumn<>("Название");
	private TableColumn<DatabaseFields, String> articleNumber = new TableColumn<>("Артикул");
	private TableColumn<DatabaseFields, Integer> numberOfPieces = new TableColumn<>("Кол-во(шт)");
	private TableColumn<DatabaseFields, Double> quantity = new TableColumn<>("Масса(граммы)");

	private final ObservableList<DatabaseFields> usersData = FXCollections.observableArrayList();

	private Statement s = null;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		exitButton.setOnAction(event -> {
			getStage().close();
		});
		findButton.setOnAction(event -> {
			resetColumns();
			try {
				s = getConnection().createStatement();
				String request = "SELECT product.ID_POINT_OF_SALE, \n" +
								 "       product.NAME_PRODUCT, \n" +
								 "       product.ARTICLE_NUMBER,\n" +
								 "       product.NUMBER_OF_PIECES, \n" +
								 "       product.\"Quantity(weight_in_grams)\",\n" +
								 "       point_of_sale.NAME_POINT_OF_SALE\n" +
								 "  FROM Product INNER JOIN Point_Of_Sale\n" +
								 "    ON product.ID_POINT_OF_SALE = point_of_sale.ID_POINT_OF_SALE\n" +
								 " Where  point_of_sale.NAME_POINT_OF_SALE = '" + choiceBox.getSelectionModel().getSelectedItem() + "'";

				ResultSet rs = s.executeQuery(request);
				while (rs.next()) {
					DatabaseFields tmp = new DatabaseFields();
					tmp.setNameProduct(rs.getString("NAME_PRODUCT"));
					tmp.setArticleNumber(rs.getString("ARTICLE_NUMBER"));
					tmp.setNumberOfPieces(rs.getInt("NUMBER_OF_PIECES"));
					tmp.setQuantity(rs.getDouble("Quantity(weight_in_grams)"));
					usersData.add(tmp);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			mainTableView.getColumns().get(0).getColumns().add(nameProduct);
			mainTableView.getColumns().get(1).getColumns().add(articleNumber);
			mainTableView.getColumns().get(2).getColumns().add(numberOfPieces);
			mainTableView.getColumns().get(3).getColumns().add(quantity);
		});
	}

	public void resetColumns() {
		usersData.clear();
		for (TableColumn<DatabaseFields, ?> column : mainTableView.getColumns()) {
			if (!column.getColumns().isEmpty()) {
				column.getColumns().remove(0);
			}
		}
	}

	public void initializeInitialState() {
		nameProduct.setPrefWidth(150);
		articleNumber.setPrefWidth(150);
		numberOfPieces.setPrefWidth(150);
		quantity.setPrefWidth(150);
		mainTableView.setItems(usersData);
		nameProduct.setCellValueFactory(new PropertyValueFactory<>("nameProduct"));
		articleNumber.setCellValueFactory(new PropertyValueFactory<>("articleNumber"));
		numberOfPieces.setCellValueFactory(new PropertyValueFactory<>("numberOfPieces"));
		quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		try {
			s = getConnection().createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM Point_of_sale");
			ArrayList<String> listNamePointOfSale = new ArrayList<>	();
			while (rs.next()) {
				listNamePointOfSale.add(rs.getString("NAME_POINT_OF_SALE"));
			}
			ObservableList<String> list = FXCollections.observableArrayList(listNamePointOfSale);
			choiceBox.setItems(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}