package main.java.ru.nsu.shchiptsov.reports;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import main.java.ru.nsu.shchiptsov.DatabaseFields;
import org.w3c.dom.Text;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WindowReport12Controller extends WindowReport {
	@FXML
	private Button exitButton;
	@FXML
	private TextField numberOrderTextField;
	@FXML
	private Button findButton;
	@FXML
	private TableView<DatabaseFields> mainTableView;

	private Statement s = null;

	private TableColumn<DatabaseFields, String> nameProduct = new TableColumn<>("Название");
	private TableColumn<DatabaseFields, Double> cost = new TableColumn<>("Стоимость");
	private TableColumn<DatabaseFields, String> status = new TableColumn<>("Статус");

	private final ObservableList<DatabaseFields> usersData = FXCollections.observableArrayList();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		exitButton.setOnAction(event -> {
			getStage().close();
		});
		findButton.setOnAction(event -> {
			resetColumns();
			String numberOrder = numberOrderTextField.getText();
			if (numberOrder.equals("")) {
				return;
			}
			try {
				s = getConnection().createStatement();
				ResultSet rs = s.executeQuery("Select order_shop.STATUS, order_shop.\"Cost($)\"," +
											  "product.NAME_PRODUCT from order_shop " +
											  "Inner Join Product on order_shop.Id_Product = " +
											  "product.Id_Product where NUMBER_ORDER_SHOP = " + numberOrder);
				while (rs.next()) {
					DatabaseFields tmp = new DatabaseFields();
					tmp.setNameProduct(rs.getString("NAME_PRODUCT"));
					tmp.setStatus(rs.getString("STATUS"));
					tmp.setCost(rs.getDouble("\"Cost($)\""));
					usersData.add(tmp);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			mainTableView.getColumns().get(0).getColumns().add(nameProduct);
			mainTableView.getColumns().get(1).getColumns().add(cost);
			mainTableView.getColumns().get(2).getColumns().add(status);
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
		nameProduct.setPrefWidth(200);
		cost.setPrefWidth(200);
		status.setPrefWidth(200);
		mainTableView.setItems(usersData);
		nameProduct.setCellValueFactory(new PropertyValueFactory<>("nameProduct"));
		cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
		status.setCellValueFactory(new PropertyValueFactory<>("status"));
	}

}
