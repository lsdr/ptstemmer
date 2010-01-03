package ptstemmer.exceptions;

public class PTStemmerException extends Exception {
	
	public PTStemmerException(String message, Throwable cause)
	{
		super(message,cause);
	}
	
	public PTStemmerException(String message)
	{
		super(message);
	}
}
