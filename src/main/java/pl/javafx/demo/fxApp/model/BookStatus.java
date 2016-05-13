package pl.javafx.demo.fxApp.model;

import pl.javafx.demo.fxApp.dataprovider.data.BookStatusVo;

public enum BookStatus {
	
	FREE, LOAN, MISSING, ANY;
	
	public static BookStatus fromBookStatusVO(BookStatusVo status) {
		return BookStatus.valueOf(status.name());
	}

	public BookStatusVo toBookStatusVo() {
		if (this == ANY) {
			return null;
		}
		return BookStatusVo.valueOf(this.name());
	}

}
