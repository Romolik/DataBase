package main.java.ru.nsu.shchiptsov.reports;

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
import main.java.ru.nsu.shchiptsov.AccessLevelErrorController;
import main.java.ru.nsu.shchiptsov.MainWindowController;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainWindowReportsController extends MainWindowController {
	@FXML
	private Button cancelButton;
	@FXML
	private Button report1Button;
	@FXML
	private Button report2Button;
	@FXML
	private Button report3Button;
	@FXML
	private Button report4Button;
	@FXML
	private Button report5Button;
	@FXML
	private Button report6Button;
	@FXML
	private Button report7Button;
	@FXML
	private Button report8Button;
	@FXML
	private Button report9Button;
	@FXML
	private Button report10Button;
	@FXML
	private Button report11Button;
	@FXML
	private Button report12Button;
	@FXML
	private Button report13Button;
	@FXML
	private Button report14Button;
	@FXML
	private Button report15Button;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		cancelButton.setOnAction(event -> {
			loadMainMenuWindow(getNameRole(), getIdEmployee());
		});
		report1Button.setOnAction(event -> {
			loadReportWindow("1");
		});
		report2Button.setOnAction(event -> {
			loadReportWindow("2");
		});
		report3Button.setOnAction(event -> {
			loadReportWindow("3");

		});
		report4Button.setOnAction(event -> {
			loadReportWindow("4");
		});
		report5Button.setOnAction(event -> {
			loadReportWindow("5");
		});
		report6Button.setOnAction(event -> {
			loadReportWindow("6");
		});
		report7Button.setOnAction(event -> {
			loadReportWindow("7");
		});
		report8Button.setOnAction(event -> {
			loadReportWindow("8");
		});
		report9Button.setOnAction(event -> {
			loadReportWindow("9");
		});
		report10Button.setOnAction(event -> {
			loadReportWindow("10");
		});
		report11Button.setOnAction(event -> {
			loadReportWindow("11");
		});
		report12Button.setOnAction(event -> {
			loadReportWindow("12");
		});
		report13Button.setOnAction(event -> {
			loadReportWindow("13");
		});
		report14Button.setOnAction(event -> {
			loadReportWindow("14");
		});
		report15Button.setOnAction(event -> {
			loadReportWindow("15");
		});
	}

	private void loadReportWindow(String numberReport) {
		Stage onTop = new Stage();
		onTop.initOwner(getStage().getScene().getWindow());
		onTop.initModality(Modality.WINDOW_MODAL);
		onTop.setTitle("Report#" + numberReport);
		onTop.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		FXMLLoader loader = new FXMLLoader ();
		loader.setLocation (getClass ().getResource ("/reports/WindowReport" + numberReport + ".fxml"));
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
		WindowReport windowReport = loader.getController();
		windowReport.setStage(onTop);
		windowReport.setConnection(getConnection());
		windowReport.initializeInitialState();
		Parent root = loader.getRoot();
		Scene scene = new Scene (root);
		scene.getStylesheets().add(Objects.requireNonNull(
				getClass().getResource("/application.css")).toExternalForm());
		onTop.setScene (scene);
		onTop.show();
	}

}
