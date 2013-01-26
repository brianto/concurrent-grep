import akka.actor.Actors;
import akka.actor.UntypedActor;

/**
 * Actor: Aggregates and prints scan results.
 * 
 * @author Brian TO
 * @author Matthew Metcalf
 */
public class CollectionActor extends UntypedActor {
	private int filesScanned;
	private int filesToScan;

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
	 * <td>{@link CGrep} ({@link FileCount})</td>
	 * <td><emph>none</emph></td>
	 * <td>Sets the number of scanned file messages to expect before shutdown.</td>
	 * </tr>
	 * <tr>
	 * <td>{@link ScanActor} ({@link Found})</td>
	 * <td><emph>none</emph></td>
	 * <td>Prints out matching lines. If this is the last expected message,
	 * {@link CollectionActor} shuts down all actors in the registry.</td>
	 * </tr>
	 * </tbody>
	 * </table>
	 */
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof FileCount)
			this.onFileCount((FileCount) message);

		if (message instanceof Found)
			this.onFound((Found) message);
	}

	/**
	 * @see CollectionActor#onReceive(Object)
	 * @param message
	 *            {@link FileCount} message
	 */
	private void onFileCount(FileCount message) {
		// The first, of class FileCount (which should be received exactly
		// once), contains a count of the number of files being scanned.
		this.filesToScan = message.getCount();
	}

	/**
	 * @see CollectionActor#onReceive(Object)
	 * @param message
	 */
	private void onFound(Found message) {
		// The remaining messages are Found objects, which, upon receipt, are
		// printed by the CollectionActor.
		for (String s : message.getMatchingEntries())
			System.out.println(s);

		// When all the Found messages have been processed, the CollectionActor
		// shuts down all actors using Actors.registry().shutdown()
		if (++this.filesScanned == this.filesToScan)
			Actors.registry().shutdownAll();
	}
}
