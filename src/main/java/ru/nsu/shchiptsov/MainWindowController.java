package main.java.ru.nsu.shchiptsov;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.sql.Connection;
import java.util.Objects;

public abstract class MainWindowController implements Initializable {
	private Stage stage;
	private Connection connection = null;
	private ChooseRoleController.NameRole nameRole;

	public void setNameRole(ChooseRoleController.NameRole nameRole) {
		this.nameRole = nameRole;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Connection getConnection() {
		return connection;
	}

	public ChooseRoleController.NameRole getNameRole() {
		return nameRole;
	}

	public Stage getStage() {
		return stage;
	}

	public void showStage(FXMLLoader loader) {
		Parent root = loader.getRoot();
		Scene scene = new Scene (root);
		scene.getStylesheets().add(Objects.requireNonNull(
				getClass().getResource("/application.css")).toExternalForm());
		stage.setScene (scene);
		stage.show ();
	}

}
