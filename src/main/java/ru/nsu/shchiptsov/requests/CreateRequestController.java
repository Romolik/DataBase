package main.java.ru.nsu.shchiptsov.requests;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;

import javax.xml.transform.Result;
import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class CreateRequestController extends MainWindowProductRequestController {
	private Statement s = null;

	@FXML
	private Button exitButton;
	@FXML
	private Button addButton1;
	@FXML
	private Button addButton2;
	@FXML
	private TextField articulProductTextField;
	@FXML
	private TextField numberPieceTextField;
	@FXML
	private TextField quntityTextField;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		exitButton.setOnAction(event -> {
			getStage().close();
		});
		addButton1.setOnAction(event -> {
			String volume = numberPieceTextField.getText();
			createRequest(volume, true);

		});
		addButton2.setOnAction(event -> {
			String volume = quntityTextField.getText();
			createRequest(volume, false);
		});
	}

	private void createRequest(String volume, boolean piece) {
		String articul = articulProductTextField.getText();
		if (Objects.equals(articul, "") || Objects.equals(volume, "")) {
			return;
		}
		try {
			s = getConnection().createStatement();
			ResultSet rs = s.executeQuery("SELECT ID_POINT_OF_SALE from Employee where " +
										  "ID_EMPLOYEE = " + getIdEmployee());
			rs.next();
			String idPointOfSale = rs.getString("ID_POINT_OF_SALE");
			rs = s.executeQuery("SELECT ID_PRODUCT from Product " +
								"where ARTICLE_NUMBER = '" + articul + "' And " +
								"ID_POINT_OF_SALE = " + idPointOfSale);
			rs.next();
			String idProduct = rs.getString("ID_PRODUCT");
			String request;
			if (piece) {
				request = "Insert into Request(ID_Request, ID_Point_of_Sale," +
						  " ID_Product,Number_of_Pieces, \"Quantity(weight_in_grams)\"," +
						  "Status) values (Null, " + idPointOfSale + ", " +
						  idProduct + ", " + volume + ", 0, 'Considered')";
			} else {
				request = "Insert into Request(ID_Request, ID_Point_of_Sale," +
						  " ID_Product,Number_of_Pieces, \"Quantity(weight_in_grams)\"," +
						  "Status) values (Null, " + idPointOfSale + ", " +
						  idProduct + ", 0," + volume + "'Considered')";
			}
			s.executeQuery(request);
			getConnection().commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
