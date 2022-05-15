package main.java.ru.nsu.shchiptsov.accounting;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.java.ru.nsu.shchiptsov.MainWindowController;

import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowAccountingController extends MainWindowController {
	@FXML
	private Button cancelButton;
	@FXML
	private Button changePriceButton;
	@FXML
	private Button transferProductButton;
	@FXML
	private Button assortmentButton;


	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		cancelButton.setOnAction(event -> {
			loadMainMenuWindow(getNameRole(), getIdEmployee());
		});
		assortmentButton.setOnAction(event -> {
			
		});
		changePriceButton.setOnAction(event -> {

		});
		transferProductButton.setOnAction(event -> {

		});
	}
}
