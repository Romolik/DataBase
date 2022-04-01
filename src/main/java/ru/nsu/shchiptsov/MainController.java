package main.java.ru.nsu.shchiptsov;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;

import java.sql.*;

import static java.lang.System.exit;

public class MainController {
	private int flagCommit = -1;
	Connection connection;
	Statement s = null;

	private ObservableList<DatabaseFields>
			usersData = FXCollections.observableArrayList();
	@FXML
	Button exitButton;
	@FXML
	MenuButton menuButton;
	@FXML
	Button addButton;
	@FXML
	TableView<DatabaseFields> mainTableView;

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	private MenuItem menuItemBuyer;
	private MenuItem menuItemEmployee;
	private MenuItem menuItemInvoice;
	private MenuItem menuItemOrderShop;
	private MenuItem menuItemPointOfSale;
	private MenuItem menuItemPost;
	private MenuItem menuItemProduct;
	private MenuItem menuItemPurchase;
	private MenuItem menuItemSupplier;
	private MenuItem menuItemTypePointOfSale;
	private MenuItem menuItemTypeProduct;
	private TableColumn<DatabaseFields, String> idBuyer = new TableColumn<>("idBuyer");
	private TableColumn<DatabaseFields, String> firstName = new TableColumn<>("firstName");
	private TableColumn<DatabaseFields, String> lastName = new TableColumn<>("lastName");
	private TableColumn<DatabaseFields, Double> moneySpent = new TableColumn<>("moneySpent");
	private TableColumn<DatabaseFields, String> idEmployee = new TableColumn<>("idEmployee");
	private TableColumn<DatabaseFields, String> idPost = new TableColumn<>("idPost");
	private TableColumn<DatabaseFields, Double> salary = new TableColumn<>("salary");//double
	private TableColumn<DatabaseFields, String> numberPhone = new TableColumn<>("numberPhone");
	private TableColumn<DatabaseFields, String> idInvoice = new TableColumn<>("idInvoice");
	private TableColumn<DatabaseFields, Date> date = new TableColumn<>("date");
	private TableColumn<DatabaseFields, String> idCompositionInvoice = new TableColumn<>("idCompositionInvoice");
	private TableColumn<DatabaseFields, String> idPointSale = new TableColumn<>("idPointSale");
	private TableColumn<DatabaseFields, String> idProduct = new TableColumn<>("idProduct");
	private TableColumn<DatabaseFields, String> idSupplier = new TableColumn<>("idSupplier");
	private TableColumn<DatabaseFields, Double> cost = new TableColumn<>("cost");
	private enum nameTable {Buyer, Employee, Invoice, OrderShop, PointSale, Post,
							Product, Purchase, Supplier, TypePointSale, TypeProduct};
	private enum typeOperation {Insert, Update}

	@FXML
	void initialize() {
		initializeTable();

		menuItemBuyer.setOnAction(event -> {
			resetColumns();
			try {
				s = connection.createStatement();
				//connection.setAutoCommit(false);
				ResultSet rs = s.executeQuery("SELECT * FROM BUYER");
				while (rs.next()) {
					DatabaseFields tmp = new DatabaseFields();
					tmp.setIdBuyer(rs.getString("ID_Buyer"));
					tmp.setFirstName(rs.getString("First_Name"));
					tmp.setLastName(rs.getString("Last_Name"));
					tmp.setMoneySpent(rs.getDouble("Money_Spent($)"));
					usersData.add(tmp);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			mainTableView.getColumns().get(0).getColumns().add(idBuyer);
			mainTableView.getColumns().get(1).getColumns().add(firstName);
			mainTableView.getColumns().get(2).getColumns().add(lastName);
			mainTableView.getColumns().get(3).getColumns().add(moneySpent);
		});
		///При смене таблицы делать commit
		menuItemEmployee.setOnAction(event -> {
			resetColumns();
			//Statement s = null;
			try {
				s = connection.createStatement();
				ResultSet rs = s.executeQuery("SELECT * FROM Employee");
				while (rs.next()) {
					DatabaseFields tmp = new DatabaseFields();
					tmp.setIdEmployee(rs.getString("ID_Employee"));
					tmp.setIdPost(rs.getString("ID_Post"));
					tmp.setSalary(rs.getDouble("Salary"));
					tmp.setFirstName(rs.getString("First_Name"));
					tmp.setLastName(rs.getString("Last_Name"));
					tmp.setNumberPhone(rs.getString("Number_Phone"));
					usersData.add(tmp);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			mainTableView.getColumns().get(0).getColumns().add(idEmployee);
			mainTableView.getColumns().get(1).getColumns().add(idPost);
			mainTableView.getColumns().get(2).getColumns().add(salary);
			mainTableView.getColumns().get(3).getColumns().add(firstName);
			mainTableView.getColumns().get(4).getColumns().add(lastName);
			mainTableView.getColumns().get(5).getColumns().add(numberPhone);
		});
		menuItemInvoice.setOnAction(event -> {
			resetColumns();
			try {
				s = connection.createStatement();
				ResultSet rs = s.executeQuery("SELECT * FROM Invoice");
				while (rs.next()) {
					DatabaseFields tmp = new DatabaseFields();
					tmp.setIdInvoice(rs.getString("ID_Invoice"));
					tmp.setIdEmployee(rs.getString("ID_Employee"));
					tmp.setDate(rs.getDate("Date"));
					usersData.add(tmp);
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			mainTableView.getColumns().get(0).getColumns().add(idInvoice);
			mainTableView.getColumns().get(1).getColumns().add(idEmployee);
			mainTableView.getColumns().get(2).getColumns().add(date);
		});
		exitButton.setOnAction(event -> {
			exit(0);
		});
		addButton.setOnAction(event -> {
			mainTableView.getItems().add(new DatabaseFields());
		});
	}

	public void resetColumns() {
		//commitInDataBase();
		usersData.clear();
		for (TableColumn<DatabaseFields, ?> column : mainTableView.getColumns()) {
			if (!column.getColumns().isEmpty()) {
				column.getColumns().remove(0);
			}
		}
	}

	private void commitInDataBase() {
		try {
			System.out.println("commit");
			connection.commit();
			flagCommit = -1;
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	private void initializeTable() {
		mainTableView.setEditable(true);
		menuItemBuyer = new MenuItem("Buyer");
		menuItemEmployee = new MenuItem("Employee");
		menuItemInvoice = new MenuItem("Invoice");
		menuItemOrderShop = new MenuItem("OrderShop");
		menuItemPointOfSale = new MenuItem("PointOfSale");
		menuItemPost = new MenuItem("Post");
		menuItemProduct = new MenuItem("Product");
		menuItemPurchase = new MenuItem("Purchase");
		menuItemSupplier = new MenuItem("Supplier");
		menuItemTypePointOfSale = new MenuItem("TypePointOfSale");
		menuItemTypeProduct = new MenuItem("TypeProduct");
		menuButton.getItems().addAll(menuItemBuyer, menuItemEmployee, menuItemInvoice,
									 menuItemOrderShop, menuItemPointOfSale, menuItemPost,
									 menuItemProduct, menuItemPurchase, menuItemSupplier,
									 menuItemTypePointOfSale, menuItemTypeProduct);

		idBuyer.setCellValueFactory(new PropertyValueFactory<>("idBuyer"));
		idBuyer.setCellFactory(TextFieldTableCell.forTableColumn());
		idBuyer.setEditable(true);
		idBuyer.setOnEditCommit((TableColumn.CellEditEvent<DatabaseFields, String> event) -> {
			TablePosition<DatabaseFields, String> pos = event.getTablePosition();
			String newIdBuyer = event.getNewValue();
			Integer row = pos.getRow();
			DatabaseFields databaseFields = event.getTableView().getItems().get(row);
			if (!(row.equals(flagCommit)) && -1 != flagCommit) {
				try {
					s.executeQuery("UPDATE BUYER set ID_Buyer = '" + mainTableView.getItems().get(flagCommit).getIdBuyer() +
							"' where " +
							"ID_Buyer = " + mainTableView.getItems().get(flagCommit).getLastIdBuyer());
				} catch (SQLException e) {
					e.printStackTrace();
			}
				commitInDataBase();
			}
//			try {
//				flagCommit = row;
//				s.executeQuery("UPDATE BUYER set ID_Buyer = '" + newIdBuyer +
//							   "' where " +
//							   "ID_Buyer = " + databaseFields.getIdBuyer());
//			} catch (SQLException e) {
//				e.printStackTrace();
//			}
			flagCommit = row;
			databaseFields.setLastIdBuyer(databaseFields.getIdBuyer());
			databaseFields.setIdBuyer(newIdBuyer);
		});
		firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
		firstName.setCellFactory(TextFieldTableCell.forTableColumn());
		firstName.setEditable(true);
		firstName.setOnEditCommit((TableColumn.CellEditEvent<DatabaseFields, String> event) -> {
			TablePosition<DatabaseFields, String> pos = event.getTablePosition();
			String newFirstName = event.getNewValue();
			Integer row = pos.getRow();
			DatabaseFields databaseFields = event.getTableView().getItems().get(row);
			if (!(row.equals(flagCommit)) && -1 != flagCommit) {
				commitInDataBase();
			}
			try {
				flagCommit = row;
//					s.executeQuery("UPDATE BUYER set First_Name = '" + newFirstName + "' where " +
//								   "ID_Buyer = " + databaseFields.getIdBuyer());
				s.executeQuery(
						"UPDATE BUYER set First_Name = '" + newFirstName +
						"' where " +
						"ID_Buyer = " + databaseFields.getIdBuyer());
			} catch (SQLException e) {
				e.printStackTrace();
			}
			databaseFields.setFirstName(newFirstName);
		});
		lastName.setCellValueFactory(new PropertyValueFactory<>("lastName"));
		moneySpent.setCellValueFactory(new PropertyValueFactory<>("moneySpent"));
		idEmployee.setCellValueFactory(new PropertyValueFactory<>("idEmployee"));
		idPost.setCellValueFactory(new PropertyValueFactory<>("idPost"));
		salary.setCellValueFactory(new PropertyValueFactory<>("salary"));
		numberPhone.setCellValueFactory(new PropertyValueFactory<>("numberPhone"));
		idInvoice.setCellValueFactory(new PropertyValueFactory<>("idInvoice"));
		date.setCellValueFactory(new PropertyValueFactory<>("date"));
		idCompositionInvoice.setCellValueFactory(new PropertyValueFactory<>("idCompositionInvoice"));
		idPointSale.setCellValueFactory(new PropertyValueFactory<>("idPointSale"));
		idProduct.setCellValueFactory(new PropertyValueFactory<>("idProduct"));
		idSupplier.setCellValueFactory(new PropertyValueFactory<>("idSupplier"));
		cost.setCellValueFactory(new PropertyValueFactory<>("cost"));
		mainTableView.setItems(usersData);
	}


}