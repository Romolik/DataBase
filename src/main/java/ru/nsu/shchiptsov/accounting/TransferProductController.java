package main.java.ru.nsu.shchiptsov.accounting;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import main.java.ru.nsu.shchiptsov.DatabaseFields;
import org.apache.ibatis.annotations.Update;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class TransferProductController implements Initializable {
	private Stage stage;
	private Connection connection;
	private Integer idEmployee;
	private Integer idPointOfSale;
	private Statement s = null;

	@FXML
	private ChoiceBox choiceBox;
	@FXML
	private TextField articulProductTextField;
	@FXML
	private TextField volumePieceTextField;
	@FXML
	private TextField volume100gTextField;
	@FXML
	private TextField newArticulProductTextField;
	@FXML
	private Button exitButton;
	@FXML
	private Button changeButton1;
	@FXML
	private Button changeButton2;

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setIdPointOfSale(Integer idPointOfSale) {
		this.idPointOfSale = idPointOfSale;
	}


	public void setIdEmployee(Integer idEmployee) {
		this.idEmployee = idEmployee;
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		exitButton.setOnAction(event -> {
			stage.close();
		});
		changeButton1.setOnAction(event -> {
			String articul = articulProductTextField.getText();
			String namePointOfSale = (String) choiceBox.getSelectionModel().getSelectedItem();
			String volumePiece = volumePieceTextField.getText();
			if (Objects.equals(articul, "") ||
				choiceBox.getSelectionModel().isEmpty() ||
				Objects.equals(volumePiece, "")) {
				return;
			}
			DatabaseFields databaseFields = new DatabaseFields();

			String request = "Select NAME_PRODUCT, NUMBER_OF_PIECES, ID_TYPE_PRODUCT " +
							 " from product where ARTICLE_NUMBER = '" + articul + "'";
			try {
				s = connection.createStatement();
				ResultSet rs = s.executeQuery(request);
				rs.next();
				databaseFields.setNameProduct(rs.getString("NAME_PRODUCT"));
				databaseFields.setNumberOfPieces(rs.getInt("NUMBER_OF_PIECES"));
				databaseFields.setIdTypeProduct(rs.getInt("ID_TYPE_PRODUCT"));

				Integer newVolume = databaseFields.getNumberOfPieces() - Integer.parseInt(volumePiece);
				if (newVolume < 0) {
					return;
				}
				request = "UPDATE Product set NUMBER_OF_PIECES = " + newVolume + " " +
						  "where ARTICLE_NUMBER = '" + articul + "'";
				s.executeQuery(request);
				if (!Objects.equals(namePointOfSale, "списать")) {
					request = "SELECT ARTICLE_NUMBER, NUMBER_OF_PIECES from product Inner Join " +
							  "Point_of_Sale on product.ID_POINT_OF_SALE = point_of_Sale.ID_POINT_OF_SALE where" +
							  " name_product = '" + databaseFields.getNameProduct() + "' And " +
							  "ARTICLE_NUMBER <> '" + articul + "' And NAME_POINT_OF_SALE = '" + namePointOfSale + "'";
					rs = s.executeQuery(request);
					if (rs.next()) {
						databaseFields.setArticleNumber(rs.getString("ARTICLE_NUMBER"));
						databaseFields.setNumberOfPieces(rs.getInt("Number_OF_PIECES"));
						newVolume = Integer.parseInt(volumePiece) + databaseFields.getNumberOfPieces();
						s.executeQuery("UPDATE product set NUMBER_OF_PIECES = " + newVolume + " " +
									   "where ARTICLE_NUMBER = '" + databaseFields.getArticleNumber() + "'");
						connection.commit();
					} else {
						String newArticul = newArticulProductTextField.getText();
						if (Objects.equals(newArticul, "")) {
							connection.rollback();
							return;
						}
						rs = s.executeQuery("Select ID_POINT_OF_SALE from Point_OF_sale " +
											"where Name_Point_Of_Sale = '" + namePointOfSale + "'");
						rs.next();
						databaseFields.setIdPointOfSale(rs.getInt("Id_Point_of_Sale"));
						newVolume = Integer.parseInt(volumePiece);
						s.executeQuery("INSERT into product values (NULL, " + databaseFields.getIdTypeProduct() +
									   ", " +  databaseFields.getIdPointOfSale() + ", '" + databaseFields.getNameProduct() + "', " +
									   "'" + newArticul + "'," + newVolume + ", 0, 0, 0)");
						connection.commit();
					}
				} else {
					connection.commit();
				}
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
				e.printStackTrace();
			}
		});
		changeButton2.setOnAction(event -> {
			String articul = articulProductTextField.getText();
			String namePointOfSale = (String) choiceBox.getSelectionModel().getSelectedItem();
			String volumePiece = volume100gTextField.getText();
			if (Objects.equals(articul, "") ||
				choiceBox.getSelectionModel().isEmpty() ||
				Objects.equals(volumePiece, "")) {
				return;
			}
			DatabaseFields databaseFields = new DatabaseFields();

			String request = "Select NAME_PRODUCT, \"Quantity(weight_in_grams)\", ID_TYPE_PRODUCT " +
							 " from product where ARTICLE_NUMBER = '" + articul + "'";
			try {
				s = connection.createStatement();
				ResultSet rs = s.executeQuery(request);
				rs.next();
				databaseFields.setNameProduct(rs.getString("NAME_PRODUCT"));
				databaseFields.setQuantity(rs.getDouble("\"Quantity(weight_in_grams)\""));
				databaseFields.setIdTypeProduct(rs.getInt("ID_TYPE_PRODUCT"));

				Double newVolume = databaseFields.getQuantity() - Double.parseDouble(volumePiece);
				if (newVolume < 0) {
					return;
				}
				request = "UPDATE Product set \"Quantity(weight_in_grams)\" = " + newVolume + " " +
						  "where ARTICLE_NUMBER = '" + articul + "'";
				s.executeQuery(request);
				request = "SELECT ARTICLE_NUMBER, \"Quantity(weight_in_grams)\" from product Inner Join " +
						  "Point_of_Sale on product.ID_POINT_OF_SALE = point_of_Sale.ID_POINT_OF_SALE where" +
						  " name_product = '" + databaseFields.getNameProduct() + "' And " +
						  "ARTICLE_NUMBER <> '" + articul + "' And NAME_POINT_OF_SALE = '" + namePointOfSale + "'";
				rs = s.executeQuery(request);
				if (rs.next()) {
					databaseFields.setArticleNumber(rs.getString("ARTICLE_NUMBER"));
					databaseFields.setNumberOfPieces(rs.getInt("Number_OF_PIECES"));
					newVolume = Double.parseDouble(volumePiece) + databaseFields.getQuantity();
					s.executeQuery("UPDATE product set \"Quantity(weight_in_grams)\" = " + newVolume + " " +
								   "where ARTICLE_NUMBER = '" + databaseFields.getArticleNumber() + "'");
					connection.commit();
				} else {
					String newArticul = newArticulProductTextField.getText();
					if (Objects.equals(newArticul, "")) {
						connection.rollback();
						return;
					}
					rs = s.executeQuery("Select ID_POINT_OF_SALE from Point_OF_sale " +
										"where Name_Point_Of_Sale = '" + namePointOfSale + "'");
					rs.next();
					databaseFields.setIdPointOfSale(rs.getInt("Id_Point_of_Sale"));
					newVolume = Double.parseDouble(volumePiece);
					s.executeQuery("INSERT into product values (NULL, " + databaseFields.getIdTypeProduct() +
								   ", " +  databaseFields.getIdPointOfSale() + ", '" + databaseFields.getNameProduct() + "', " +
								   "'" + newArticul + "', 0, " + newVolume + ", 0, 0)");
					connection.commit();
				}
			} catch (SQLException e) {
				try {
					connection.rollback();
				} catch (SQLException ex) {
					ex.printStackTrace();
				}
				e.printStackTrace();
				return;
			}
		});

	}

	public void initializeInitialState() {
		try {
			s = connection.createStatement();
			ResultSet rs = s.executeQuery("SELECT * FROM Point_of_sale where Id_Point_Of_Sale" +
										  " <> " + idPointOfSale);
			ArrayList<String> listNamePointOfSale = new ArrayList<>	();
			while (rs.next()) {
				listNamePointOfSale.add(rs.getString("NAME_POINT_OF_SALE"));
			}
			listNamePointOfSale.add("списать");
			ObservableList<String>
					list = FXCollections.observableArrayList(listNamePointOfSale);
			choiceBox.setItems(list);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}


}
