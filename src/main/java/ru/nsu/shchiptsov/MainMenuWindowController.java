package main.java.ru.nsu.shchiptsov;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCombination;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.ru.nsu.shchiptsov.sellers.MainWindowSellersController;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
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
	@FXML
	private Button accountingButton;
	@FXML
	private Button requestButton;
	@FXML
	private Button reportsButton;
	@FXML
	private Button createUsersButton;

	private HashMap<NameRole, ArrayList<String>> accessRights;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		initializeAccessesRole();
		cancelButton.setOnAction(event -> {
			showStageTable("/ChooseRole.fxml");
		});
		sellersButton.setOnAction(event -> {
			if (accessRights.get(getNameRole()).contains("MainWindowSellers")) {
				showStageTable("/sellers/MainWindowSellers.fxml");
			} else {
				showAccessLevelErrorWindow();
			}
		});
		ordersButton.setOnAction(event -> {
			if (accessRights.get(getNameRole()).contains("MainWindowOrders")) {
				showStageTable("/orders/MainWindowOrders.fxml");
			} else {
				showAccessLevelErrorWindow();
			}
		});
		accountingButton.setOnAction(event -> {
			if (accessRights.get(getNameRole()).contains("MainWindowAccounting")) {
				showStageTable("/accounting/MainWindowAccounting.fxml");
			} else {
				showAccessLevelErrorWindow();
			}
		});
		requestButton.setOnAction(event -> {
			if (accessRights.get(getNameRole()).contains("MainWindowProductRequest")) {
				showStageTable("/requests/MainWindowProductRequest.fxml");
			} else {
				showAccessLevelErrorWindow();
			}
		});
		reportsButton.setOnAction(event -> {
			if (accessRights.get(getNameRole()).contains("MainWindowReports")) {
				showStageTable("/reports/MainWindowReports.fxml");
			} else {
				showAccessLevelErrorWindow();
			}
		});
		createUsersButton.setOnAction(event -> {
			if (accessRights.get(getNameRole()).contains("CreateUsers")) {
				showStageTable("/createUsers/CreateUsers.fxml");
			} else {
				showAccessLevelErrorWindow();
			}
		});


	}

	public void setRoleLabel() {
		roleLabel.setText(getNameRole().name);
	}

	private void initializeAccessesRole() {
		accessRights = new HashMap<>();
		ArrayList<String> listAvailableTabsAdministrator = new ArrayList<>();
		listAvailableTabsAdministrator.add("MainWindowSellers");
		listAvailableTabsAdministrator.add("MainWindowAccounting");
		listAvailableTabsAdministrator.add("MainWindowProductRequest");
		listAvailableTabsAdministrator.add("MainWindowReports");
		listAvailableTabsAdministrator.add("CreateUsers");
		ArrayList<String> listAvailableTabsSeller = new ArrayList<>();
		listAvailableTabsSeller.add("MainWindowSellers");
		listAvailableTabsSeller.add("MainWindowProductRequest");
		ArrayList<String> listAvailableTabsManager = new ArrayList<>();
		listAvailableTabsManager.add("MainWindowAccounting");
		listAvailableTabsManager.add("MainWindowProductRequest");
		listAvailableTabsAdministrator.add("MainWindowReports");
		ArrayList<String> listAvailableTabsSupplier = new ArrayList<>();
		listAvailableTabsSupplier.add("MainWindowOrders");
		accessRights.put(NameRole.Administrator, new ArrayList<>(listAvailableTabsAdministrator));
		accessRights.put(NameRole.Seller, new ArrayList<>(listAvailableTabsSeller));
		accessRights.put(NameRole.Manager, new ArrayList<>(listAvailableTabsManager));
		accessRights.put(NameRole.Supplier, new ArrayList<>(listAvailableTabsSupplier));
	}

	private void showAccessLevelErrorWindow() {
		Stage onTop = new Stage();
		onTop.initOwner(getStage().getScene().getWindow());
		onTop.initModality(Modality.WINDOW_MODAL);
		onTop.setTitle("Access rights error");
		onTop.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		FXMLLoader loader = new FXMLLoader ();
		loader.setLocation (getClass ().getResource ("/AccessLevelError.fxml"));
		try {
			loader.load ();
		} catch (IOException e) {
			e.printStackTrace ();
		}
		onTop.setOnCloseRequest (new EventHandler<WindowEvent>() {
			@Override
			public void handle (WindowEvent event) {
				event.consume ();
			}
		});
		AccessLevelErrorController accessLevelErrorController = loader.getController();
		accessLevelErrorController.setStage(onTop);
		Parent root = loader.getRoot();
		Scene scene = new Scene (root);
		scene.getStylesheets().add(Objects.requireNonNull(
				getClass().getResource("/application.css")).toExternalForm());
		onTop.setScene (scene);
		onTop.show();
	}

}
