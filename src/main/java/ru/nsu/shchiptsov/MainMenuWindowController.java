package main.java.ru.nsu.shchiptsov;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import main.java.ru.nsu.shchiptsov.sellers.MainWindowSellersController;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import main.java.ru.nsu.shchiptsov.ChooseRoleController.NameRole;

public class MainMenuWindowController extends MainWindowController {
	@FXML
	private Label roleLabel;
	@FXML
	private Button cancelButton;
	@FXML
	private Button sellersButton;
	@FXML
	private Button ordersButton;

	private NameRole nameRole;

	private HashMap<NameRole, ArrayList<String>> accessRights = new HashMap<>();

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		initializeAccessesRole();
		cancelButton.setOnAction(event -> {
			showStageTable("/ChooseRole.fxml");
		});
		sellersButton.setOnAction(event -> {
			showStageTable("/MainWindowSellers.fxml");
		});

	}

	private void showStageTable(String nameFxmlFile) {
		FXMLLoader loader = new FXMLLoader ();
		loader.setLocation (getClass ().getResource (nameFxmlFile));
		try {
			loader.load ();
		} catch (IOException e) {
			e.printStackTrace();
		}
		MainMenuWindowController mainWindowController = loader.getController();
		mainWindowController.setStage(getStage());
		mainWindowController.setConnection(getConnection());
		showStage(loader);
	}

	public void setRoleLabel() {
		roleLabel.setText(getNameRole().name);
	}

	private void initializeAccessesRole() {
		nameRole = getNameRole();
		ArrayList<String> listAvailableTabs1 = new ArrayList<>();
		listAvailableTabs1.add("MainWindowSellers");
		ArrayList<String> listAvailableTabs2 = new ArrayList<>();
		listAvailableTabs2.add("MainWindowSellers");
		ArrayList<String> listAvailableTabs3 = new ArrayList<>();
		accessRights.put(NameRole.Administrator, new ArrayList<>(listAvailableTabs1));
		accessRights.put(NameRole.Seller, new ArrayList<>(listAvailableTabs2));
		accessRights.put(NameRole.Manager, new ArrayList<>(listAvailableTabs3));
	}

}
