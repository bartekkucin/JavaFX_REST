package pl.javafx.demo.fxApp.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import javafx.beans.property.ReadOnlyLongWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.javafx.demo.fxApp.dataprovider.IBooksDataProvider;
import pl.javafx.demo.fxApp.dataprovider.data.BookVo;
import pl.javafx.demo.fxApp.model.BookSearch;
import pl.javafx.demo.fxApp.model.BookStatus;

public class BookSearchController {

	private static final Logger LOG = Logger.getLogger(BookSearchController.class);

	@FXML
	ResourceBundle resources;

	@FXML
	URL location;

	@FXML
	TextField titleField;

	@FXML
	TextField authorField;

	@FXML
	Button searchBookButton;

	@FXML
	Button deleteBookButton;

	@FXML
	Button addBookButton;

	@FXML
	TableView<BookVo> resultTable;

	@FXML
	TableColumn<BookVo, String> idColumn;

	@FXML
	TableColumn<BookVo, String> titleColumn;

	@FXML
	TableColumn<BookVo, String> authorsColumn;

	@FXML
	TableColumn<BookVo, String> statusColumn;

	@FXML
	AnchorPane panel;

	private Parent root;

	private final BookSearch model = new BookSearch();

	private final IBooksDataProvider bookDataProvider = IBooksDataProvider.INSTANCE;
	
	public BookSearchController() {
		LOG.debug("Constructor: nameField = " + titleField);

	}

	@FXML
	private void initialize() throws IOException {
		LOG.debug("initialize(): nameField = " + titleField);

		initializeResultTable();

		/*
		 * Bind controls properties to model properties.
		 */

		resultTable.itemsProperty().bind(model.resultProperty());
	}

	private void initializeResultTable() {
		/*
		 * Define what properties of BookVO will be displayed in different
		 * columns.
		 */
		idColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getId().toString()));
		titleColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTitle()));
		authorsColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getAuthors()));
		statusColumn.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getStatus().name()));
		resultTable.setPlaceholder(new Label(resources.getString("table.emptyText")));
	}

	@FXML
	private void searchBook(ActionEvent event) {

		LOG.debug("'Search' button clicked");
		/*
		 * Used task to execute the potentially long running call in background
		 * (separate thread), so that the JavaFX Application Thread is not
		 * blocked.
		 */
		Task<Collection<BookVo>> backgroundTask = new Task<Collection<BookVo>>() {

			@Override
			protected Collection<BookVo> call() throws Exception {
				LOG.debug("call() called");
				/*
				 * Get the data.
				 */
				Collection<BookVo> result = bookDataProvider.findBooksByParams(authorField.getText(),
						titleField.getText());
				return result;
			}

			@Override
			protected void succeeded() {
				LOG.debug("succeeded() called");
				/*
				 * Copy the result to model.
				 */
				model.setResult(new ArrayList<BookVo>(getValue()));
				/*
				 * Reset the table.
				 */
				resultTable.getSortOrder().clear();
			}
		};

		new Thread(backgroundTask).start();
	}

	
	@FXML
	public void openAddBookModalWindow(ActionEvent event) throws IOException {
		Stage primaryStage = new Stage();
		primaryStage.setTitle(ResourceBundle.getBundle("bundle/base").getString("window.add"));

		root = FXMLLoader.load(getClass().getResource("/view/bookAdd.fxml"), ResourceBundle.getBundle("bundle/base"));

		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/css/standard.css").toExternalForm());
		primaryStage.setScene(scene);
		primaryStage.initModality(Modality.WINDOW_MODAL);
		primaryStage.initOwner(((Node) event.getSource()).getScene().getWindow());
		primaryStage.show();
	}

	@FXML
	public void deleteBook(ActionEvent event) throws IOException {
		LOG.debug("'Delete' button clicked");

		Task<Collection<BookVo>> backgroundTask = new Task<Collection<BookVo>>() {
			private Boolean deleteResult;

			@Override
			protected Collection<BookVo> call() throws Exception {
				LOG.debug("call() called");
				/*
				 * Get the data.
				 */
				BookVo book2delete = resultTable.getSelectionModel().getSelectedItem();
				bookDataProvider.deleteBook(book2delete);
				Collection<BookVo> result = bookDataProvider.findBooksByParams(authorField.getText(),
						titleField.getText());
				return result;
			}

			@Override
			protected void succeeded() {
				LOG.debug("succeeded() called");
				/*
				 * Copy the result to model.
				 */
				model.setResult(new ArrayList<BookVo>(getValue()));
				/*
				 * Reset the table.
				 */
				resultTable.getSortOrder().clear();
			}
		};

		new Thread(backgroundTask).start();
	}

}
