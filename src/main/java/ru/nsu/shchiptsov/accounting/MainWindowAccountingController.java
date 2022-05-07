package main.java.ru.nsu.shchiptsov.accounting;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.java.ru.nsu.shchiptsov.MainWindowController;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowAccountingController extends MainWindowController {
	@FXML
	private Button cancelButton;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		cancelButton.setOnAction(event -> {
			loadMainMenuWindow(getNameRole());
		});
	}
}