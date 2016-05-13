package pl.javafx.demo.fxApp.controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import javafx.beans.binding.Bindings;
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
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;
import pl.javafx.demo.fxApp.dataprovider.IBooksDataProvider;
import pl.javafx.demo.fxApp.dataprovider.data.BookStatusVo;
import pl.javafx.demo.fxApp.dataprovider.data.BookVo;
import pl.javafx.demo.fxApp.model.BookSearch;
import pl.javafx.demo.fxApp.model.BookStatus;

public class BookAddController {

	private static final Logger LOG = Logger.getLogger(BookAddController.class);

	private final IBooksDataProvider bookDataProvider = IBooksDataProvider.INSTANCE;	

	@FXML
	private ResourceBundle resources;
	
	@FXML
	ComboBox<BookStatus> statusField;

	@FXML
	private URL location;

	@FXML
	TextField titleBook;

	@FXML
	TextField authorsBook;

	@FXML
	Button addBookButton;
	
	@FXML
	Button returnButton;

	@FXML
	Button backButton;
	
	private final BookSearch model = new BookSearch();

	public BookAddController() {
		LOG.debug("Constructor: titleBook = " + titleBook + ", " + "authorsBook = " + authorsBook);
	}
	
	private void initializeBookStatusField() {

		for (BookStatus bookStatus : BookStatus.values()) {
			statusField.getItems().add(bookStatus);
		}

		statusField.setCellFactory(new Callback<ListView<BookStatus>, ListCell<BookStatus>>() {

			@Override
			public ListCell<BookStatus> call(ListView<BookStatus> param) {
				return new ListCell<BookStatus>() {

					@Override
					protected void updateItem(BookStatus item, boolean empty) {
						super.updateItem(item, empty);
						if (empty) {
							return;
						}
						String text = getInternationalizedText(item);
						setText(text);
					}
				};
			}
		});

		statusField.setConverter(new StringConverter<BookStatus>() {

			@Override
			public String toString(BookStatus object) {
				return getInternationalizedText(object);
			}

			@Override
			public BookStatus fromString(String string) {
				return null;
			}
		});
	}
	
	private String getInternationalizedText(BookStatus bookStatus) {
		return resources.getString("status." + bookStatus.name());
	}

	@FXML
	private void initialize() {

		initializeBookStatusField();
		statusField.valueProperty().bindBidirectional(model.statusProperty());
		
		addBookButton.disableProperty()
				.bind(titleBook.textProperty().isEmpty().or 
							(authorsBook.textProperty().isEmpty()).or 
							(statusField.valueProperty().isNull()));
	}

	@FXML
	public void addBookButtonAction(ActionEvent event) {
		Task<Void> backgroundTask = new Task<Void>() {
			@Override
			protected Void call() throws Exception {
				System.out.println("Call starter");
				bookDataProvider.saveBook(new BookVo(null, titleBook.getText(), authorsBook.getText(), statusField.getValue().toBookStatusVo()));
				return null;
			}

			@Override
			protected void succeeded() {;
				System.out.println("Call starter succeed");
				titleBook.clear();
				authorsBook.clear();
			}
		};
		new Thread(backgroundTask).start();
	}

	
	@FXML
	public void returnButtonAction(ActionEvent event) throws IOException {
	    // get a handle to the stage
	    Stage stage = (Stage) returnButton.getScene().getWindow();
	    // do what you have to do
	    stage.close();
	}

}
