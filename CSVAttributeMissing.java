
public class CSVAttributeMissing extends Exception {

	public CSVAttributeMissing() {
		super("Error: Input row cannot be parsed due to missing information");
	}

	public CSVAttributeMissing(String errorMessage) {
		super(errorMessage);
	}

	public String getMessage() {
		return (super.getMessage());

	}
}
