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

public class WindowReport8Controller extends WindowReport {
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

	private TableColumn<DatabaseFields, String> firstName = new TableColumn<>("Имя");
	private TableColumn<DatabaseFields, String> lastName = new TableColumn<>("Фамилия");
	private TableColumn<DatabaseFields, Double> salary = new TableColumn<>("Зарплата");
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
				String request = "SELECT Employee.ID_POINT_OF_SALE, \n" +
								 "       Employee.First_Name,\n" +
								 "       Employee.Last_Name, \n" +
								 "       Employee.Salary,\n" +
								 "       Employee.ID_Post,\n" +
								 "       Post.NAME_POST,\n" +
								 "       point_of_sale.NAME_POINT_OF_SALE,\n" +
								 "       type_point_of_sale.ID_TYPE_POINT_OF_SALE,\n" +
								 "       point_of_sale.ID_TYPE_POINT_OF_SALE,\n" +
								 "       TYPE_POINT_OF_SALE.NAME_TYPE_POINT_OF_SALE\n" +
								 "  FROM Employee" +
								 " INNER JOIN Point_Of_Sale ON Employee.ID_POINT_OF_SALE = point_of_sale.ID_POINT_OF_SALE\n" +
								 " INNER JOIN Type_Point_of_Sale ON type_point_of_sale.ID_TYPE_POINT_OF_SALE = point_of_sale.ID_TYPE_POINT_OF_SALE\n" +
								 " INNER JOIN Post ON Post.ID_Post = Employee.ID_Post\n" +
								 " Where post.Name_Post = 'Seller'";
				String tmpReq = "";
				if ((!typeChoiceBox.getSelectionModel().isEmpty()) && (typeChoiceBox.getSelectionModel().getSelectedItem() != " ")) {
					tmpReq = " AND type_point_of_sale.NAME_TYPE_POINT_OF_SALE = '" + typeChoiceBox.getSelectionModel().getSelectedItem() + "'";
				}
				if (!nameChoiceBox.getSelectionModel().isEmpty() && nameChoiceBox.getSelectionModel().getSelectedItem() != " ") {
					tmpReq = " AND point_of_sale.NAME_POINT_OF_SALE = '" + nameChoiceBox.getSelectionModel().getSelectedItem() + "'";
				}
				request += tmpReq;
				ResultSet rs = s.executeQuery(request);
				while (rs.next()) {
					DatabaseFields tmp = new DatabaseFields();
					tmp.setFirstName(rs.getString("First_Name"));
					tmp.setLastName(rs.getString("Last_Name"));
					tmp.setSalary(rs.getDouble("Salary"));
					usersData.add(tmp);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			mainTableView.getColumns().get(0).getColumns().add(firstName);
			mainTableView.getColumns().get(1).getColumns().add(lastName);
			mainTableView.getColumns().get(2).getColumns().add(salary);
		});
	}

	public void initializeInitialState() {
		firstName.setPrefWidth(200);
		lastName.setPrefWidth(200);
		salary.setPrefWidth(200);
		mainTableView.setItems(usersData);
		firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		salary.setCellValueFactory(new PropertyValueFactory<>("salary"));

		try {
			s = getConnection().createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM Point_of_sale");
			ArrayList<String> listNamePointOfSale = new ArrayList<>	();
			listNamePointOfSale.add(" ");
			while (rs.next()) {
				listNamePointOfSale.add(rs.getString("NAME_POINT_OF_SALE"));
			}
			ObservableList<String> list = FXCollections.observableArrayList(listNamePointOfSale);
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
