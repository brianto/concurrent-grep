import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

/**
 * Scans a file for matching lines and messages the result to a
 * {@link CollectionActor}.
 * 
 * @author Brian To
 * @author Matthew Metcalf
 */
public class ScanActor extends UntypedActor {
	private ActorRef collection;
	private String filepath;

	private boolean configured;

	/**
	 * Constructor
	 */
	public ScanActor() {
		this.configured = false;
	}

	/**
	 * Actions for when {@link ActorRef#tell} is called.
	 * 
	 * <table border>
	 * <thead>
	 * <tr>
	 * <th>Inbound Message</th>
	 * <th>Outbound Message</th>
	 * <th>Description</th>
	 * </tr>
	 * </thead> <tbody>
	 * <tr>
	 * <td>{@link CGrep} ({@link Configure})</td>
	 * <td>{@link CollectionActor} ({@link Found})</td>
	 * <td>Sets this actor's target file and regex, scans through all lines to
	 * collect matching lines, and messages the result.</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 */
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Configure)
			this.onConfigure((Configure) message);
	}

	/**
	 * @see ScanActor#onReceive(Object)
	 * @param message
	 */
	private void onConfigure(Configure message) {
		if (this.configured)
			throw new RuntimeException("this actor is already configured.");

		this.collection = message.getCollectionActor();
		this.filepath = message.getFilepath();

		this.configured = true;

		Found object = new Found(this.filepath, scan(message.getRegex()));

		this.collection.tell(object);

	}

	/**
	 * Reads the file denoted by the filepath in the class.
	 * 
	 * @return All lines as a {@link List} of {@link String}s.
	 */
	private List<String> readFile() {
		Scanner scanner = null;
		try {
			scanner = new Scanner(new File(filepath));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		List<String> lines = new ArrayList<String>();

		while (scanner.hasNext())
			lines.add(scanner.nextLine());

		scanner.close();

		return lines;
	}

	/**
	 * Filters a list of string lines by those which match the regex.
	 * 
	 * @param regex
	 * @return All string lines that match the regex.
	 */
	private List<String> scan(String regex) {
		List<String> results = new ArrayList<String>();
		List<String> something = readFile();

		Pattern pattern = Pattern.compile(regex);

		for (String some : something) {
			Matcher matcher = pattern.matcher(some);

			if (matcher.find())
				results.add(some);
		}

		return results;
	}
}
