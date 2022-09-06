package main.java.ru.nsu.shchiptsov.createUsers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class AddSellerController extends CreateUsersController {
	@FXML
	private Button exitButton;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		exitButton.setOnAction(event -> {
			getStage().close();
		});


	}
}
