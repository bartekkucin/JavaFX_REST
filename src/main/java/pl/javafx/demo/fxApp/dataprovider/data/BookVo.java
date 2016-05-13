package pl.javafx.demo.fxApp.dataprovider.data;

public class BookVo {
    private Long id;
    private String title;
    private String authors;
    private BookStatusVo status;
    
    public BookVo() {
    }

    public BookVo(Long id, String title, String authors, BookStatusVo status) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.setStatus(status);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAuthors() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors = authors;
    }

	public BookStatusVo getStatus() {
		return status;
	}

	public void setStatus(BookStatusVo status) {
		this.status = status;
	}
}

