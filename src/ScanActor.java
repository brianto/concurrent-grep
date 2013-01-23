import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import akka.actor.ActorRef;
import akka.actor.UntypedActor;

public class ScanActor extends UntypedActor {
	private ActorRef collection;
	private String filepath;
	private boolean configured;
	
	public ScanActor() {
		this.configured = false;
	}
	
	public ScanActor useCollectionActor(ActorRef actor) {
		return this;
	}
	
	@Override
	public void onReceive(Object message) throws Exception {
		if (message instanceof Configure)
			this.onConfigure((Configure) message);
	}
	
	public void onConfigure(Configure message) {
		if (this.configured)
			throw new RuntimeException("this actor is already configured.");
		
		this.collection = message.getCollectionActor();
		this.filepath = message.getFilepath();
		
		this.configured = true;
		
		Found object = new Found(this.filepath, scan(message.getRegex()));
		
		this.collection.tell(object);
		
	}
	
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
		
		return lines;
	}
	
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
