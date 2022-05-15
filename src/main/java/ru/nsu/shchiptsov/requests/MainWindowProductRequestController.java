package main.java.ru.nsu.shchiptsov.requests;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.java.ru.nsu.shchiptsov.MainWindowController;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowProductRequestController extends MainWindowController {
	@FXML
	private Button cancelButton;
	@FXML
	private Button createRequest;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		cancelButton.setOnAction(event -> {
			loadMainMenuWindow(getNameRole(), getIdEmployee());
		});
		createRequest.setOnAction(event -> {

		});
	}
}
