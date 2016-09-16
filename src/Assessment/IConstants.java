package Assessment;

public interface IConstants {
	public static final byte END = '\n';
	public static final byte END_OF_LIST = '\r';
	public static final byte LIST_FILES = 'L';
	public static final byte SEND_FILE = 'F';
	public static final byte FILE_EXISTS = 'Y';
	public static final byte FILE_DOESNT_EXIST = 'N';
	
	// DOM constant to properly format and indent xml tags.
	public static final String HTTP_INDENT = "{http://xml.apache.org/xslt}indent-amount";
}
