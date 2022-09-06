package main.java.ru.nsu.shchiptsov;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;
import java.util.ResourceBundle;


public class ChooseRoleController extends MainWindowController {
	@FXML
	private TextField loginTextField;
	@FXML
	private TextField passwordTextField;
	@FXML
	private Button cancelButton;
	@FXML
	private Button nextButton;
	@FXML
	private Label errorLoginLabel;

	@FXML
	private ComboBox<String> chooseRoleComboBox;

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		chooseRoleComboBox.setStyle("-fx-font: Italic 14pt Georgia;");
		initializeMenuRole();
		cancelButton.setOnAction(event -> {
			FXMLLoader loader = new FXMLLoader ();
			loader.setLocation (getClass ().getResource ("/LoginToServer.fxml"));
			try {
				loader.load ();
			} catch (IOException e) {
				e.printStackTrace ();
			}
			LoginToServerController loginToServerController = loader.getController();
			loginToServerController.setStage(getStage());
			showStage(loader);
		});
		nextButton.setOnAction(event -> {
			if (Objects.equals(chooseRoleComboBox.getSelectionModel().getSelectedItem(),
							   "Администратор") && Objects.equals(loginTextField.getText(),
								"admin") && Objects.equals(passwordTextField.getText(), "admin")) {
						loadMainMenuWindow(NameRole.Administrator, 0);
			} else if (Objects.equals(chooseRoleComboBox.getSelectionModel().getSelectedItem(),
									  "Продавец")) {
				try {
					Statement s = getConnection().createStatement();
					ResultSet rs = s.executeQuery("SELECT * FROM Users where Login = '" +
												  loginTextField.getText() + "'");
					if (rs.next() && rs.getString("password").equals(passwordTextField.getText()) &&
						rs.getString("Role").equals("seller")) {
						loadMainMenuWindow(NameRole.Seller, Integer.parseInt(loginTextField.getText()));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (Objects.equals(chooseRoleComboBox.getSelectionModel().getSelectedItem(), "Менеджер")) {
				try {
					Statement s = getConnection().createStatement();
					ResultSet rs = s.executeQuery("SELECT * FROM Users where Login = '" +
												  loginTextField.getText() + "'");
					if (rs.next() && rs.getString("password").equals(passwordTextField.getText()) &&
						rs.getString("Role").equals("manager")) {
						loadMainMenuWindow(NameRole.Manager, Integer.parseInt(loginTextField.getText()));
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			} else if (Objects.equals(chooseRoleComboBox.getSelectionModel().getSelectedItem(), "Поставщик")) {
				try {
					Statement s = getConnection().createStatement();
					ResultSet rs = s.executeQuery("SELECT * FROM Users where Login = '" +
												  loginTextField.getText() + "'");
					if (rs.next() && rs.getString("password").equals(passwordTextField.getText()) &&
						rs.getString("Role").equals("supplier")) {
						rs = s.executeQuery("select ID_SUPPLIER from supplier where NAME_SUPPLIER = '" +
									   loginTextField.getText() + "'");
						if (rs.next()) {
							loadMainMenuWindow(NameRole.Supplier, rs.getInt("ID_SUPPLIER"));
						}
					}
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			errorLoginLabel.setText("Login or password incorrect");
		});
	}

	public enum NameRole {
		Administrator("Администратор"), Seller("Продавец"),
		Manager("Менеджер"), Supplier("Поставщик");
		protected final String name;

		NameRole(String s) {
			name = s;
		}};

	private void initializeMenuRole() {
		MenuItem administrator = new MenuItem(ChooseRoleController.NameRole.Administrator.name);
		MenuItem seller = new MenuItem(ChooseRoleController.NameRole.Seller.name);
		MenuItem manager = new MenuItem(ChooseRoleController.NameRole.Manager.name);
		MenuItem supplier = new MenuItem(ChooseRoleController.NameRole.Supplier.name);
		chooseRoleComboBox.getItems().addAll(administrator.getText(), seller.getText(),
											 manager.getText(), supplier.getText());
	}

}
