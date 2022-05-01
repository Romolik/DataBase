package main.java.ru.nsu.shchiptsov;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.*;
import java.util.Objects;

public class Main extends Application {
	public static void main (String[] args) {
		launch (args);
	}

	@Override
	public void start(Stage primaryStage) {
		primaryStage.setTitle ("DataBase");
		FXMLLoader loader = new FXMLLoader ();
		loader.setLocation (getClass ().getResource ("/LoginToServer.fxml"));
		try {
			loader.load ();
		} catch (IOException e) {
			e.printStackTrace ();
		}
		LoginToServerController loginToServerController = loader.getController();
		loginToServerController.setStage(primaryStage);
		primaryStage.setOnCloseRequest (new EventHandler<WindowEvent>() {
			@Override
			public void handle (WindowEvent event) {
				event.consume ();
			}
		});
		Parent root = loader.getRoot();
		primaryStage.setResizable (false);
		primaryStage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
		Scene scene = new Scene (root);
		scene.getStylesheets().add(Objects.requireNonNull(
				getClass().getResource("/application.css")).toExternalForm());
		primaryStage.setScene (scene);
		primaryStage.show ();
	}

}
