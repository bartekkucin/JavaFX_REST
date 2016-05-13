package pl.javafx.demo.fxApp;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pl.javafx.demo.fxApp.dataprovider.BooksDataProviderImpl;
import pl.javafx.demo.fxApp.dataprovider.data.BookStatusVo;
import pl.javafx.demo.fxApp.dataprovider.data.BookVo;
import pl.javafx.demo.fxApp.model.BookStatus;

/**
 * Hello world!
 *
 */
public class App extends Application
{
    public static void main( String[] args ) throws IOException 
    {
    	Application.launch(args);
    }
    
    @Override
	public void start(Stage primaryStage) throws Exception {
		/*
		 * Set the default locale based on the '--lang' startup argument.
		 */
		String langCode = getParameters().getNamed().get("lang");
		if (langCode != null && !langCode.isEmpty()) {
			Locale.setDefault(Locale.forLanguageTag(langCode));
		}

		primaryStage.setTitle("StarterKit-REST-JavaFX");

		/*
		 * Load screen from FXML file with specific language bundle (derived
		 * from default locale).
		 */
		Parent root = FXMLLoader.load(
				getClass().getResource("/view/bookSearch.fxml"), //
				ResourceBundle.getBundle("bundle/base"));

		Scene scene = new Scene(root);

		/*
		 * Set the style sheet(s) for application.
		 */
		scene.getStylesheets()
				.add(getClass().getResource("/css/standard.css").toExternalForm());

		primaryStage.setScene(scene);

		primaryStage.show();

	}

    
}
