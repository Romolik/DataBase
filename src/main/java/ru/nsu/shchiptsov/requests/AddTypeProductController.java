package main.java.ru.nsu.shchiptsov.requests;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;

public class AddTypeProductController extends MainWindowProductRequestController {
	private Statement s = null;

	@FXML
	private Button exitButton;
	@FXML
	private Button addButton;
	@FXML
	private TextField nameTypeProductTextField;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		exitButton.setOnAction(event -> {
			getStage().close();
		});
		addButton.setOnAction(event -> {
			String nameTypeProduct = nameTypeProductTextField.getText();
			if (!Objects.equals(nameTypeProduct, "")) {
				try {
					s = getConnection().createStatement();
					s.executeQuery("INSERT into Type_Product values (NULL,'" +
									   nameTypeProduct + "')");
					getConnection().commit();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		});
	}
}
