package pl.javafx.demo.fxApp.dataprovider;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.List;

import pl.javafx.demo.fxApp.dataprovider.data.BookStatusVo;
import pl.javafx.demo.fxApp.dataprovider.data.BookVo;
import pl.javafx.demo.fxApp.model.BookStatus;

public interface IBooksDataProvider {

	IBooksDataProvider INSTANCE = new BooksDataProviderImpl();
	
	List<BookVo> findAllBooks() throws IOException;
	List<BookVo> findBooksByParams(String author, String title) throws IOException;
	BookVo saveBook(BookVo bookVo) throws MalformedURLException, IOException;
	BookVo updateBook(BookVo bookVo);
	// REV: do usuniecia ksiazki wystarczy id ksiazki
	void deleteBook(BookVo bookVo) throws ProtocolException, MalformedURLException, IOException;
	
	
	
	
}
