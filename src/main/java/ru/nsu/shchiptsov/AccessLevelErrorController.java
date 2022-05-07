package main.java.ru.nsu.shchiptsov;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class AccessLevelErrorController implements Initializable {
	@FXML
	private Button okButton;

	private Stage stage;

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		okButton.setOnAction(event -> {
			stage.close();
		});
	}

}
