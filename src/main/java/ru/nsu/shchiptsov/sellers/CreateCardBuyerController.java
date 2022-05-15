package main.java.ru.nsu.shchiptsov.sellers;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class CreateCardBuyerController implements Initializable {
	@FXML
	private TextField firstNameTextField;
	@FXML
	private TextField lastNameTextField;
	@FXML
	private Button createButton;
	@FXML
	private Button exitButton;

	private Stage stage;
	private Connection connection;
	private Statement s = null;

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		exitButton.setOnAction(event -> {
			stage.close();
		});
		createButton.setOnAction(event -> {
			String firstName = firstNameTextField.getText();
			String lastName = lastNameTextField.getText();
			if (Objects.equals(firstName, "") || Objects.equals(lastName, "")) {
				return;
			}
			try {
				s = connection.createStatement();
				s.executeQuery("insert into Buyer (ID_Buyer,First_Name,Last_Name,\"Money_Spent($)\")" +
											  " values (NULL, '" + firstName +"', '" + lastName+ "', 0)");
				connection.commit();
				firstNameTextField.clear();
				lastNameTextField.clear();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		});
	}

}
