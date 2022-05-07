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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class WindowReport15Controller extends WindowReport {
	@FXML
	private Button exitButton;
	@FXML
	private ChoiceBox nameChoiceBox;
	@FXML
	private ChoiceBox typeChoiceBox;
	@FXML
	private Button findButton;
	@FXML
	private TableView<DatabaseFields> mainTableView;
	@FXML
	private DatePicker datePickerStart;
	@FXML
	private DatePicker datePickerEnd;
	private TableColumn<DatabaseFields, Double> tradeTurnover = new TableColumn<>("Товарооборот($)");
	private final ObservableList<DatabaseFields> usersData = FXCollections.observableArrayList();
	private Statement s = null;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		exitButton.setOnAction(event -> {
			getStage().close();
		});
		findButton.setOnAction(event -> {
			resetColumns();
			LocalDate dateStart = datePickerStart.getValue();
			LocalDate dateEnd = datePickerEnd.getValue();
			if (dateStart == null || dateEnd == null) {
				return;
			}
			int day = dateStart.getDayOfMonth();
			String month = dateStart.getMonth().toString();
			int year = dateStart.getYear();
			String competitionDateStart = year + "-" + month + "-" + day;
			day = dateEnd.getDayOfMonth();
			month = dateEnd.getMonth().toString();
			year = dateEnd.getYear();
			String competitionDateEnd = year + "-" + month + "-" + day;
			try {
				s = getConnection().createStatement();
				String request = "SELECT SUM(PURCHASE.\"Cost($)\") as tradeTurnover,\n" +
								 "       Point_of_Sale.ID_POINT_OF_SALE\n" +
								 "FROM POINT_OF_SALE\n" +
								 "           INNER JOIN PURCHASE ON  Point_of_Sale.ID_POINT_OF_SALE = PURCHASE.ID_POINT_OF_SALE\n" +
								 "           INNER JOIN invoice ON  invoice.Id_Invoice = purchase.Id_Invoice\n" +
								 "           INNER JOIN Product ON  product.ID_Product = purchase.ID_Product\n" +
								 "           INNER JOIN TYPE_POINT_OF_SALE ON  POINT_OF_SALE.ID_TYPE_POINT_OF_SALE = TYPE_POINT_OF_SALE.ID_TYPE_POINT_OF_SALE\n" +
								 "        Where  \"Date\" between  to_date('" + competitionDateStart + "', 'yyyy-mm-dd')" +
								 " and to_date('"+ competitionDateEnd + "', 'yyyy-mm-dd')\n";
				String tmpReq = "";
				if ((!typeChoiceBox.getSelectionModel().isEmpty()) && (typeChoiceBox.getSelectionModel().getSelectedItem() != " ")) {
					tmpReq = " AND  type_point_of_sale.NAME_TYPE_POINT_OF_SALE = '" + typeChoiceBox.getSelectionModel().getSelectedItem() + "'\n";
				}
				if (!nameChoiceBox.getSelectionModel().isEmpty() && nameChoiceBox.getSelectionModel().getSelectedItem() != " ") {
					tmpReq = " AND  point_of_sale.NAME_POINT_OF_SALE = '" + nameChoiceBox.getSelectionModel().getSelectedItem() + "'\n";
				}
				tmpReq += "group by Point_of_Sale.ID_POINT_OF_SALE";
				request += tmpReq;
				ResultSet rs = s.executeQuery(request);
				while (rs.next()) {
					DatabaseFields tmp = new DatabaseFields();
					tmp.setTradeTurnover(rs.getDouble("TradeTurnover"));

					usersData.add(tmp);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			mainTableView.getColumns().get(0).getColumns().add(tradeTurnover);
		});
	}
	public void initializeInitialState() {
		tradeTurnover.setPrefWidth(600);

		mainTableView.setItems(usersData);
		tradeTurnover.setCellValueFactory(new PropertyValueFactory<>("tradeTurnover"));


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
