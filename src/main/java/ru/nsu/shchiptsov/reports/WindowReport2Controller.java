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
import java.util.Objects;
import java.util.ResourceBundle;

public class WindowReport2Controller extends WindowReport {
	@FXML
	private Button exitButton;
	@FXML
	private ChoiceBox typeChoiceBox;
	@FXML
	private Label countBuyer;
	@FXML
	private Button findButton;
	@FXML
	private Button findButton1;
	@FXML
	private TextField articulProductTextField;
	@FXML
	private TextField productVolumeTextField;
	@FXML
	private TableView<DatabaseFields> mainTableView;
	@FXML
	private DatePicker datePickerStart;
	@FXML
	private DatePicker datePickerEnd;

	private TableColumn<DatabaseFields, String> firstName = new TableColumn<>("Имя");
	private TableColumn<DatabaseFields, String> lastName = new TableColumn<>("Фамилия");

	private final ObservableList<DatabaseFields>
			usersData = FXCollections.observableArrayList();
	private Statement s = null;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		exitButton.setOnAction(event -> {
			getStage().close();
		});
		findButton.setOnAction(event -> {
			resetColumns();
			String typeProduct = (String) typeChoiceBox.getSelectionModel().getSelectedItem();
			LocalDate dateStart = datePickerStart.getValue();
			int day = dateStart.getDayOfMonth();
			String month = dateStart.getMonth().toString();
			int year = dateStart.getYear();
			String competitionDateStart = year + "-" + month + "-" + day;
			LocalDate dateEnd = datePickerEnd.getValue();
			day = dateEnd.getDayOfMonth();
			month = dateEnd.getMonth().toString();
			year = dateEnd.getYear();
			String competitionDateEnd = year + "-" + month + "-" + day;
			try {
				String request = "SELECT purchase.ID_purchase,\n" +
								 "       product.ARTICLE_NUMBER,\n" +
								 "       product.ID_Product,\n" +
								 "       purchase.ID_Product,\n" +
								 "       invoice.Id_Invoice,\n" +
								 "       invoice.\"Date\",\n" +
								 "       purchase.Id_Invoice,\n" +
								 "       purchase.Id_Buyer,\n" +
								 "       Buyer.Id_Buyer,\n" +
								 "       Buyer.First_Name,\n" +
								 "       Buyer.Last_Name,\n" +
								 "       TYPE_PRODUCT.ID_TYPE_PRODUCT,\n" +
								 "       TYPE_PRODUCT.NAME_TYPE_PRODUCT\n" +
								 "FROM purchase\n" +
								 "           INNER JOIN Buyer ON  purchase.Id_Buyer = Buyer.Id_Buyer\n" +
								 "           INNER JOIN invoice ON  invoice.Id_Invoice = purchase.Id_Invoice\n" +
								 "           INNER JOIN Product ON  product.ID_Product = purchase.ID_Product\n" +
								 "           INNER JOIN TYPE_PRODUCT ON PRODUCT.ID_TYPE_PRODUCT = TYPE_PRODUCT.ID_TYPE_PRODUCT\n" +
								 "        Where  TYPE_PRODUCT.NAME_TYPE_PRODUCT = '" + typeProduct + "' AND \"Date\" between  to_date('" +
								 competitionDateStart +"', 'yyyy-mm-dd') and to_date('" + competitionDateEnd + "', 'yyyy-mm-dd')\n" +
								 "group by Buyer.Id_Buyer, purchase.ID_purchase, product.ARTICLE_NUMBER, product.ID_Product, purchase.ID_Product, invoice.Id_Invoice, invoice.NUMBER_OF_PIECES, invoice.\"Quantity(weight_in_grams)\", purchase.Id_Invoice, purchase.Id_Buyer, Buyer.First_Name,\n" +
								 "         Buyer.Last_Name, invoice.\"Date\", TYPE_PRODUCT.ID_TYPE_PRODUCT, TYPE_PRODUCT.NAME_TYPE_PRODUCT";
				ResultSet rs = s.executeQuery(request);
				Integer count = 0;
				while (rs.next()) {
					DatabaseFields tmp = new DatabaseFields();
					tmp.setFirstName(rs.getString("First_Name"));
					tmp.setLastName(rs.getString("Last_Name"));
					usersData.add(tmp);
					count++;
				}
				countBuyer.setText(count.toString());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			mainTableView.getColumns().get(0).getColumns().add(firstName);
			mainTableView.getColumns().get(1).getColumns().add(lastName);
		});
		findButton1.setOnAction(event -> {
			resetColumns();
			String articul = articulProductTextField.getText();
			String volumeProduct = productVolumeTextField.getText();
			if (!Objects.equals(articul, "") && !Objects.equals(volumeProduct, "")) {
				try {
					s = getConnection().createStatement();
					String request = "SELECT \tpurchase.ID_purchase,\n" +
									 "       product.ARTICLE_NUMBER,\n" +
									 "       product.ID_Product,\n" +
									 "       purchase.ID_Product,\n" +
									 "       invoice.Id_Invoice,\n" +
									 "       invoice.NUMBER_OF_PIECES,\n" +
									 "       invoice.\"Quantity(weight_in_grams)\",\n" +
									 "       purchase.Id_Invoice,\n" +
									 "       purchase.Id_Buyer,\n" +
									 "       Buyer.Id_Buyer,\n" +
									 "       Buyer.First_Name,\n" +
									 "       Buyer.Last_Name\n" +
									 "FROM purchase\n" +
									 "           INNER JOIN Buyer ON  purchase.Id_Buyer = Buyer.Id_Buyer\n" +
									 "           INNER JOIN invoice ON  invoice.Id_Invoice = purchase.Id_Invoice\n" +
									 "           INNER JOIN Product ON  product.ID_Product = purchase.ID_Product\n" +
									 "        Where  product.ARTICLE_NUMBER = '" + articul + "'" +
									 "group by Buyer.Id_Buyer, purchase.ID_purchase, product.ARTICLE_NUMBER, product.ID_Product, purchase.ID_Product, invoice.Id_Invoice, invoice.NUMBER_OF_PIECES, invoice.\"Quantity(weight_in_grams)\", purchase.Id_Invoice, purchase.Id_Buyer, Buyer.First_Name,\n" +
									 "         Buyer.Last_Name\n" +
									 "Having SUM(invoice.NUMBER_OF_PIECES) > " + volumeProduct + "or SUM(invoice.\"Quantity(weight_in_grams)\") > " + volumeProduct;
					Integer count = 0;
					ResultSet rs = s.executeQuery(request);
					while (rs.next()) {
						DatabaseFields tmp = new DatabaseFields();
						tmp.setFirstName(rs.getString("First_Name"));
						tmp.setLastName(rs.getString("Last_Name"));
						usersData.add(tmp);
						count++;
					}
				countBuyer.setText(count.toString());
				} catch (SQLException e) {
					e.printStackTrace();
				}
				mainTableView.getColumns().get(0).getColumns().add(firstName);
				mainTableView.getColumns().get(1).getColumns().add(lastName);
			}
		});
	}

	public void initializeInitialState() {
		firstName.setPrefWidth(300);
		lastName.setPrefWidth(300);
		mainTableView.setItems(usersData);
		firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));

		try {
			s = getConnection().createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM Type_Product");
			ArrayList<String> listTypeProduct = new ArrayList<>	();
			listTypeProduct.add(" ");
			while (rs.next()) {
				listTypeProduct.add(rs.getString("NAME_TYPE_PRODUCT"));
			}
			ObservableList<String> list = FXCollections.observableArrayList(listTypeProduct);
			typeChoiceBox.setItems(list);
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
