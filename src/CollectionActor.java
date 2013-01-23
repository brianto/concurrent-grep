import akka.actor.Actors;
import akka.actor.UntypedActor;

public class CollectionActor extends UntypedActor {
	private int filesScanned;
	private int filesToScan;

	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof FileCount)
			this.onFileCount((FileCount) message);

		if (message instanceof Found)
			this.onFound((Found) message);
	}

	public void onFileCount(FileCount message) {
		// The first, of class FileCount (which should be received exactly
		// once), contains a count of the number of files being scanned.
		this.filesToScan = message.getCount();
	}

	// Sent from ScanActor
	public void onFound(Found message) {
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
