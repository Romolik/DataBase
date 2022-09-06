package main.java.ru.nsu.shchiptsov.createUsers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import main.java.ru.nsu.shchiptsov.MainWindowController;

import java.net.URL;
import java.util.ResourceBundle;

public class CreateUsersController extends MainWindowController {
	@FXML
	private Button cancelButton;
	@FXML
	private Button addSellerButton;
	@FXML
	private Button changeSellerButton;
	@FXML
	private Button removeSellerButton;
	@FXML
	private Button addManagerButton;
	@FXML
	private Button changeManagerButton;
	@FXML
	private Button removeManagerButton;
	@FXML
	private Button addSupplierButton;
	@FXML
	private Button changeSupplierButton;
	@FXML
	private Button removeSupplierButton;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		cancelButton.setOnAction(event -> {
			loadMainMenuWindow(getNameRole(), getIdEmployee());
		});

	}

}
