package main.java.ru.nsu.shchiptsov.accounting;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import main.java.ru.nsu.shchiptsov.MainWindowController;

import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class ChangeCostProductController implements Initializable {
	@FXML
	private Button exitButton;
	@FXML
	private Button changeButton1;
	@FXML
	private Button changeButton2;
	@FXML
	private TextField articulProductTextField;
	@FXML
	private TextField newCostPieceTextField;
	@FXML
	private TextField newCost100gTextField;

	private Stage stage;
	private Connection connection;
	private Integer idEmployee;
	private Integer idPointOfSale;
	private Statement s = null;

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setIdPointOfSale(Integer idPointOfSale) {
		this.idPointOfSale = idPointOfSale;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
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
			changeCost("\"Сost per piece\"");
		});
		changeButton2.setOnAction(event -> {
			changeCost("\"Cost per 100g\"");
		});
	}

	private void changeCost(String typeProduct) {
		Double cost;
		if (typeProduct.equals("\"Сost per piece\"")) {
			if (Objects.equals(articulProductTextField.getText(), "") ||
				Objects.equals(newCostPieceTextField.getText(), "")) {
				return;
			}
			try {
				cost = Double.parseDouble(newCostPieceTextField.getText());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return;
			}
		} else {
			if (Objects.equals(articulProductTextField.getText(), "") ||
				Objects.equals(newCost100gTextField.getText(), "")) {
				return;
			}
			try {
				cost = Double.parseDouble(newCost100gTextField.getText());
			} catch (NumberFormatException e) {
				e.printStackTrace();
				return;
			}
		}
		String request = "Update Product set " + typeProduct + " = " + cost + " " +
						 "where ARTICLE_NUMBER = '" + articulProductTextField.getText() + "' AND " +
						 "ID_POINT_OF_SALE = " + idPointOfSale;
		try {
			s = connection.createStatement();
			s.executeQuery(request);
			connection.commit();
		} catch (SQLException e) {
			try {
				connection.rollback();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
			e.printStackTrace();
		}
	}

}
