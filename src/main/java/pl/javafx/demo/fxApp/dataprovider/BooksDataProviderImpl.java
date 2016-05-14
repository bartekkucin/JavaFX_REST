package pl.javafx.demo.fxApp.dataprovider;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import pl.javafx.demo.fxApp.controller.BookSearchController;
import pl.javafx.demo.fxApp.dataprovider.data.BookStatusVo;
import pl.javafx.demo.fxApp.dataprovider.data.BookVo;
import pl.javafx.demo.fxApp.mapper.BooksMapper;

public class BooksDataProviderImpl implements IBooksDataProvider {

	// REV: code conventions
	private static final String keyRequest = "Accept";
	private static final String valueRequest = "application/json";

	// REV: adres bazowy powinien byc pobrany z konfiguracji
	private static final String findAllURL = "http://localhost:8080/webstore/books/searchAll";
	private static final String saveBookURL = "http://localhost:8080/webstore/books/addBook";
	private static final String updateBookURL = "http://localhost:8080/webstore/books/book/updateBook";
	private static final String deleteBookURL = "http://localhost:8080/webstore/books/book/deleteBook";

	// REV: nie ma potrzeby przechowywania tych obiektów jako atrybutów klasy
	private static HttpURLConnection httpConnection;
	private static BufferedReader bufferedReader;

	private static final Logger LOG = Logger.getLogger(BookSearchController.class);

	public String getJson(String url) throws IOException {

		try {
			URL u = new URL(url);
			httpConnection = (HttpURLConnection) u.openConnection();
			httpConnection.setRequestMethod("GET");
			httpConnection.connect();
			int status = httpConnection.getResponseCode();
			switch (status) {
			case 200:
			case 201:
				bufferedReader = new BufferedReader(new InputStreamReader(httpConnection.getInputStream()));
				StringBuilder sb = new StringBuilder();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					sb.append(line + "\n");
				}
				bufferedReader.close();
				return sb.toString();
				// REV: gdy dostaniemy inny HTTP code warto zglosic blad
			}

		} catch (IOException ex) {
			// REV: obsluga wyjatkow
			
			// REV: masz już loggera pod zmienna LOG
			Logger.getLogger(getClass().getName()).log(Level.ERROR, null, ex);
		} finally {
			if (httpConnection != null) {
				try {
					httpConnection.disconnect();
				} catch (Exception ex) {
					// REV: j.w.
					Logger.getLogger(getClass().getName()).log(Level.ERROR, null, ex);
				}
			}
		}
		return null;
	}

	@Override
	public List<BookVo> findAllBooks() throws IOException {

		String json = getJson(findAllURL);
		if (json != null) {
			return BooksMapper.mapJson2BookVoList(json);
		}
		return null;
	}

	@Override
	public List<BookVo> findBooksByParams(String author, String title) throws IOException {

		List<BookVo> books = new ArrayList<>();

		try {

			// REV: j.w.
			String json = getJson(
					"http://localhost:8080/webstore/books/searchBooks?author=" + author + "&title=" + title);

			books = BooksMapper.mapJson2BookVoList(json);

		} catch (Exception e) {
			// REV: obsluga wyjatkow
			LOG.debug(e);
		}
		return books;
	}

	@Override
	public BookVo saveBook(BookVo bookVo) throws MalformedURLException, IOException {

		HttpURLConnection con = httpConnectionHelper(new URL(saveBookURL), "POST");
		bufferedReader = communicateByJson(bookVo, con);

		int HttpResult = con.getResponseCode();
		if (HttpResult == HttpURLConnection.HTTP_OK) {
			// REV: jesli serwer zwroci sformatowany JSON to lipa
			return BooksMapper.mapJson2BookVo(bufferedReader.readLine());
		} else {
			return null;
		}
		// REV: warto by zamknac polaczenie i readera
	}

	@Override
	public BookVo updateBook(BookVo bookVo) {
		// TODO Auto-generated method stub
		return null;
	}

	private BufferedReader communicateByJson(BookVo bookVo, HttpURLConnection con)
			throws IOException, UnsupportedEncodingException, MalformedURLException {
		OutputStream os = con.getOutputStream();

		try{
		os.write(generateJSON(bookVo).toString().getBytes("UTF-8"));
		InputStream is = new BufferedInputStream(con.getInputStream());
		bufferedReader = new BufferedReader(new InputStreamReader(is));
		}
		catch(Exception e)
		{
			// REV: obsluga wyjatkow
			LOG.debug(e);
		}
		finally {
			os.close();
		}
		
		return bufferedReader;
	}

	// REV: nazwa metody powinna byc czasownikiem
	private HttpURLConnection httpConnectionHelper(URL url, String requestMethod)
			throws MalformedURLException, IOException, ProtocolException {

		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setDoOutput(true);
		con.setDoInput(true);
		con.setRequestProperty("Content-Type", valueRequest);
		con.setRequestProperty(keyRequest, valueRequest);
		con.setRequestMethod(requestMethod);
		return con;
	}

	@Override
	public void deleteBook(BookVo bookVo) throws ProtocolException, MalformedURLException, IOException {
		
		HttpURLConnection con = httpConnectionHelper(new URL(deleteBookURL), "DELETE");

		OutputStream os = con.getOutputStream();
		os.write(generateJSON(bookVo).toString().getBytes("UTF-8"));
		// REV: trzeba by sprawdzic co zwrocil serwer
		os.close();
	}

	@SuppressWarnings("unchecked")
	public static JSONObject generateJSON(BookVo bookVo) {

		// REV: uzywasz Jackson'a, po co druga biblioteka do obslugi JSONow
		JSONObject json = new JSONObject();
		if (bookVo.getId() == null) {
			json.put("id", "");
		} else {
			json.put("id", bookVo.getId());
		}
		json.put("title", bookVo.getTitle());
		json.put("authors", bookVo.getAuthors());
		json.put("status", bookVo.getStatus().name());
		return json;

	}

}
