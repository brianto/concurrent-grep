import java.util.Collections;
import java.util.List;

public class Found {
	private final String filename;
	private final List<String> matchingEntries;
	
	public Found(String filename, List<String> matchingEntries) {
		this.filename = filename;
		this.matchingEntries = matchingEntries;
	}
	
	public String getFilename() {
		return this.filename;
	}
	
	public List<String> getMatchingEntries() {
		return Collections.unmodifiableList(this.matchingEntries);
	}
}
