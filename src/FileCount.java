/**
 * Message: expectation of total expected found messages.
 * 
 * @author Brian To
 * @author Matthew Metcalf
 */
public class FileCount {
	private final int count; 
	
	/**
	 * Constructor
	 * 
	 * @param count number of found messages to expect
	 */
	public FileCount(int count) {
		this.count = count;
	}
	
	public int getCount() {
		return this.count;
	}
}
