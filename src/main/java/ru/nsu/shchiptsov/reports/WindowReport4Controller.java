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

public class WindowReport4Controller extends WindowReport {
	@FXML
	private Button exitButton;
	@FXML
	private ChoiceBox nameChoiceBox;
	@FXML
	private ChoiceBox typeChoiceBox;
	@FXML
	private Button findButton;
	@FXML
	private TextField articulProductTextField;
	@FXML
	private TableView<DatabaseFields> mainTableView;

	private TableColumn<DatabaseFields, String> nameProduct = new TableColumn<>("Название");
	private TableColumn<DatabaseFields, Integer> numberOfPieces = new TableColumn<>("Кол-во(шт)");
	private TableColumn<DatabaseFields, Double> quantity = new TableColumn<>("Масса(граммы)");
	private TableColumn<DatabaseFields, Double> costPiece = new TableColumn<>("Цена(шт)");
	private TableColumn<DatabaseFields, Double> cost100g = new TableColumn<>("Цена(100г)");
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
								 "       product.ARTICLE_NUMBER,\n" +
								 "       product.NAME_PRODUCT, \n" +
								 "       product.\"Сost per piece\",\n" +
								 "       product.\"Cost per 100g\",\n" +
								 "       product.NUMBER_OF_PIECES, \n" +
								 "       product.\"Quantity(weight_in_grams)\",\n" +
								 "       point_of_sale.NAME_POINT_OF_SALE,\n" +
								 "       type_point_of_sale.ID_TYPE_POINT_OF_SALE,\n" +
								 "       point_of_sale.ID_TYPE_POINT_OF_SALE,\n" +
								 "       TYPE_POINT_OF_SALE.NAME_TYPE_POINT_OF_SALE\n" +
								 "  FROM Product" +
								 " INNER JOIN Point_Of_Sale ON product.ID_POINT_OF_SALE = point_of_sale.ID_POINT_OF_SALE\n" +
								 " INNER JOIN Type_Point_of_Sale ON type_point_of_sale.ID_TYPE_POINT_OF_SALE = point_of_sale.ID_TYPE_POINT_OF_SALE\n" +
								 " Where  product.ARTICLE_NUMBER = '" + articulProductTextField.getText() + "'";
				String tmpReq = "";
				if ((!typeChoiceBox.getSelectionModel().isEmpty()) && (typeChoiceBox.getSelectionModel().getSelectedItem() != " ")) {
					tmpReq = " AND  type_point_of_sale.NAME_TYPE_POINT_OF_SALE = '" + typeChoiceBox.getSelectionModel().getSelectedItem() + "'";
				}
				if (!nameChoiceBox.getSelectionModel().isEmpty() && nameChoiceBox.getSelectionModel().getSelectedItem() != " ") {
					tmpReq = " AND  point_of_sale.NAME_POINT_OF_SALE = '" + nameChoiceBox.getSelectionModel().getSelectedItem() + "'";
				}
				request += tmpReq;
				ResultSet rs = s.executeQuery(request);
				while (rs.next()) {
					DatabaseFields tmp = new DatabaseFields();
					tmp.setNameProduct(rs.getString("NAME_PRODUCT"));
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
			mainTableView.getColumns().get(1).getColumns().add(numberOfPieces);
			mainTableView.getColumns().get(2).getColumns().add(quantity);
			mainTableView.getColumns().get(3).getColumns().add(costPiece);
			mainTableView.getColumns().get(4).getColumns().add(cost100g);
		});
	}

	public void initializeInitialState() {
		nameProduct.setPrefWidth(120);
		numberOfPieces.setPrefWidth(120);
		quantity.setPrefWidth(120);
		cost100g.setPrefWidth(120);
		costPiece.setPrefWidth(120);
		mainTableView.setItems(usersData);
		nameProduct.setCellValueFactory(new PropertyValueFactory<>("nameProduct"));
		numberOfPieces.setCellValueFactory(new PropertyValueFactory<>("numberOfPieces"));
		quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
		costPiece.setCellValueFactory(new PropertyValueFactory<>("costPiece"));
		cost100g.setCellValueFactory(new PropertyValueFactory<>("cost100g"));

		try {
			s = getConnection().createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM Point_of_sale");
			ArrayList<String> listNamePointOfSale = new ArrayList<>	();
			listNamePointOfSale.add(" ");
			while (rs.next()) {
				listNamePointOfSale.add(rs.getString("NAME_POINT_OF_SALE"));
			}
			ObservableList<String>
					list = FXCollections.observableArrayList(listNamePointOfSale);
			nameChoiceBox.setItems(list);
			rs = s.executeQuery("SELECT * FROM Type_Point_of_sale");
			ArrayList<String> listTypePointOfSale = new ArrayList<>	();
			listTypePointOfSale.add(" ");
			while (rs.next()) {
				listTypePointOfSale.add(rs.getString("NAME_TYPE_POINT_OF_SALE"));
			}
			ObservableList<String>
					list2 = FXCollections.observableArrayList(listTypePointOfSale);
			typeChoiceBox.setItems(list2);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void resetColumns() {
		usersData.clear();
		for (TableColumn<DatabaseFields, ?> column : mainTableView.getColumns()) {
			if (!column.getColumns().isEmpty()) {
				column.getColumns().remove(0);
			}
		}
	}

}
