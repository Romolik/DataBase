package main.java.ru.nsu.shchiptsov.reports;

import javafx.fxml.Initializable;
import javafx.stage.Stage;

import java.sql.Connection;

public abstract class WindowReport implements Initializable {
	private Stage stage;
	private Connection connection = null;

	public void setStage(Stage stage) {
		this.stage = stage;
	}

	public Stage getStage() {
		return stage;
	}

	public Connection getConnection() {
		return connection;
	}

	public void setConnection(Connection connection) {
		this.connection = connection;
	}

	public void initializeInitialState() {

	}

}
