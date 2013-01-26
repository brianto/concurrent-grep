import java.util.Collections;
import java.util.List;

/**
 * Message: reports all lines that match a regex.
 * 
 * @author Brian To
 * @author Matthew Metcalf
 */
public class Found {
	private final String filename;
	private final List<String> matchingEntries;
	
	/**
	 * Constructor
	 * 
	 * @param filename name of file
	 * @param matchingEntries list of all matching lines
	 */
	public Found(String filename, List<String> matchingEntries) {
		this.filename = filename;
		this.matchingEntries = Collections.unmodifiableList(matchingEntries);
	}
	
	public String getFilename() {
		return this.filename;
	}
	
	public List<String> getMatchingEntries() {
		return this.matchingEntries;
	}
}
