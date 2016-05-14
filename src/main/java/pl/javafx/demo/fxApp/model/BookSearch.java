package pl.javafx.demo.fxApp.model;

import java.util.ArrayList;
import java.util.List;

import javafx.beans.property.ListProperty;
import javafx.beans.property.LongProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import pl.javafx.demo.fxApp.dataprovider.data.BookVo;

public class BookSearch {
	
	private final LongProperty idBook = new SimpleLongProperty();
	private final StringProperty author = new SimpleStringProperty();
	private final StringProperty title = new SimpleStringProperty();
	private final ObjectProperty<BookStatus> status = new SimpleObjectProperty<>();
	private final ListProperty<BookVo> result = new SimpleListProperty<>(
			FXCollections.observableList(new ArrayList<>()));
	
	// REV: brak settera dla idBook
	public LongProperty idBookProperty() {
		return idBook;
	}
	
	public final Long getIdBook() {
		return idBook.get();
	}
	
	public StringProperty authorProperty() {
		return author;
	}
	
	public final String getAuthor() {
		return author.get();
	}
	
	public final void setAuthor(String value) {
		author.set(value);
	}
	
	public StringProperty titleProperty() {
		return title;
	}
	
	public final String getTitle() {
		return title.get();
	}
	
	public final void setTitle(String value) {
		title.set(value);
	}
	
	public final ObjectProperty<BookStatus> statusProperty() {
		return status;
	}
	
	public final BookStatus getStatus() {
		return status.get();
	}
	
	public final void setStatus(BookStatus value) {
		status.set(value);
	}
	
	public final ListProperty<BookVo> resultProperty() {
		return result;
	}
	
	public final List<BookVo> getResult() {
		return result.get();
	}
	
	public final void setResult(List<BookVo> value) {
		result.setAll(value);
	}

	@Override
	public String toString() {
		return "BookSearch [author=" + author + ", title=" + title + ", status=" + status + ", result=" + result + "]";
	}
	
	
	
	
	
	
	
	

}
