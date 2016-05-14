package pl.javafx.demo.fxApp.mapper;

import java.io.IOException;
import java.util.List;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;

import pl.javafx.demo.fxApp.dataprovider.data.BookVo;

public class BooksMapper {
	
	// REV: metody statyczne sprawiaja problemy
	
		public static List<BookVo> mapJson2BookVoList(String jsonString) throws JsonParseException, JsonMappingException, IOException{
			// REV: ObjectMapper powiwnien byc utworzony tylko raz
			return new ObjectMapper().readValue(jsonString, new TypeReference<List<BookVo>>(){});
		}

		public static BookVo mapJson2BookVo(String jsonString) throws JsonParseException, JsonMappingException, IOException{
			// REV: j.w.
			return new ObjectMapper().readValue(jsonString, BookVo.class);
		}
	}

