package main.java.ru.nsu.shchiptsov.requests;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.KeyCombination;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import main.java.ru.nsu.shchiptsov.MainWindowController;
import main.java.ru.nsu.shchiptsov.accounting.TransferProductController;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainWindowProductRequestController extends MainWindowController {
	@FXML
	private Button cancelButton;
	@FXML
	private Button createRequestButton;
	@FXML
	private Button viewRequestButton;
	@FXML
	private Button addTypeProductButton;
	@FXML
	private Button addSupplierProductButton;
	@FXML
	private Button addSupplierButton;
	@FXML
	private Button removeSupplierButton;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		cancelButton.setOnAction(event -> {
			loadMainMenuWindow(getNameRole(), getIdEmployee());
		});
		viewRequestButton.setOnAction(event -> {
			loadFxml("View Request", "/requests/ViewRequest.fxml");
		});
		createRequestButton.setOnAction(event -> {
			loadFxml("Create Request", "/requests/CreateRequest.fxml");
		});
		addSupplierProductButton.setOnAction(event -> {
			loadFxml("Add Product Supplier", "/requests/AddSupplierProduct.fxml");
		});
		addSupplierButton.setOnAction(event -> {
			loadFxml("Add Supplier", "/requests/AddSupplier.fxml");
		});
		addTypeProductButton.setOnAction(event -> {
			loadFxml("Add Type Product", "/requests/AddTypeProduct.fxml");
		});
		removeSupplierButton.setOnAction(event -> {
			loadFxml("Remove Supplier", "/requests/RemoveSupplier.fxml");
		});
	}

	public void loadFxml(String titleWindow, String pathFxml) {
		Stage onTop = new Stage();
		onTop.initOwner(getStage().getScene().getWindow());
		onTop.initModality(Modality.WINDOW_MODAL);
		onTop.setTitle(titleWindow);
		onTop.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		FXMLLoader loader = new FXMLLoader ();
		loader.setLocation (getClass ().getResource (pathFxml));
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
		MainWindowProductRequestController
				mainWindowProductRequestController = loader.getController();
		mainWindowProductRequestController.setStage(onTop);
		mainWindowProductRequestController.setConnection(getConnection());
		mainWindowProductRequestController.initializeInitialState();
		mainWindowProductRequestController.setIdEmployee(getIdEmployee());
		Parent root = loader.getRoot();
		Scene scene = new Scene (root);
		scene.getStylesheets().add(
				Objects.requireNonNull(getClass().getResource("/application.css")).toExternalForm());
		onTop.setScene (scene);
		onTop.show();
	}

	public void initializeInitialState() {

	}
}
