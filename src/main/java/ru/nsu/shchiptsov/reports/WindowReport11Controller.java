package main.java.ru.nsu.shchiptsov.reports;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

import java.net.URL;
import java.util.ResourceBundle;

public class WindowReport11Controller extends WindowReport {
	@FXML
	private Button exitButton;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		exitButton.setOnAction(event -> {
			getStage().close();
		});
	}

}
