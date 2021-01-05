package nachos.proj1;

import java.util.Vector;

import nachos.machine.FileSystem;
import nachos.machine.Machine;
import nachos.machine.OpenFile;
import nachos.threads.KThread;

public class MainSystem {

	private TheConsole console = new TheConsole();
	private FileSystem fileSystem = Machine.stubFileSystem();
	private Vector<Tweet> tweets = new Vector<>();
	
	public MainSystem() {
		
		int choose = 0;
		
		do {
			console.writeln("Welcome to ZOSCE. What would you like to do?");
			console.writeln("1. Check Timeline");
			console.writeln("2. Create Tweet");
			console.writeln("3. Delete Tweets");
			console.writeln("4. Logout");
			console.write("Choose : ");
			choose = console.readInt();
			
			switch(choose) {
			case 1:checkTimeLine();
				break;
			case 2: createTweet();
				break;
			case 3:deleteTweet();
				break;
			case 4:printSecond();
				break;
			}
		} while (choose != 4);
	}

	private void printSecond() {
		long durationInSeconds = (long) (Machine.timer().getTime() / 10e7);
		long minute = durationInSeconds / 60;
		console.writeln("You have been logged in for " + minute + " minute(s)");
		console.writeln("Good Bye");
	}

	private void deleteTweet() {
		String username = null;
		
		do {
			console.write("Input your username: ");
			username = console.readLine();
			
			if(username.isEmpty()) {
				console.writeln("Anonymous tweets cannot be deleted!");
			}
		} while (username.isEmpty());
		
		boolean flag = false;
		Vector<Tweet> thisUser = new Vector<>();
		
		for (Tweet tweet : tweets) {
			if(tweet.getUsername().equals(username)) {
				flag = true;
				thisUser.add(tweet);
			}
		}
		
		if(flag == false) {
			console.writeln("No tweet has been made with this username...");
		} else {
			int count = 1;
			for (Tweet tweet : thisUser) {
				console.writeln(count + ". " + tweet.getContent());
				count++;
			}
			
			count = count - 1;
			int choose = 0;
			do {
				console.write("Choose the tweet you want to delete [1-" + count + " | 0 to exit");
				choose = console.readInt();
				if(choose < 0 || choose > count) {
					console.writeln("Invalid index");
				}
			} while (choose < 0 || choose > count);
			
			if(choose == 0) {
				return;
			} else {
				tweets.remove(thisUser.elementAt(choose-1));
				console.write("Tweet deleted");
				
				OpenFile openFile = fileSystem.open("tweets.txt", true);
				String tweetPost = "";
				for (Tweet tweet : tweets) {
					tweetPost += tweet.toString();
					tweetPost += "\n";
				}
				
				byte[] data = tweetPost.getBytes();
				openFile.write(data, 0, data.length);
			}
		}
	}

	private void createTweet() {
		String content = null;
		
		do {
			console.writeln("Input your tweet [1-50 characters]:");
			content = console.readLine();
			
			if(content.length() < 1) {
				console.writeln("Tweet content cannot be empty!");
			} else if(content.length() > 50) {
				console.writeln("Tweet content cannot more than 50");
			}
			
		} while (content.length() < 1 || content.length() > 50 || content.contains("#"));
		
		String username = null;
		
		do {
			console.writeln("Input your username [20 characaters max | Leave empty to stay anonymous]: ");
			username = console.readLine();
			
			if(username.length() > 20) {
				console.writeln("Username cannot more than 20 characters");
			}
			
		} while (username.length() > 20 || username.contains("#"));
		
		
		if(username.isEmpty()) {
			username = "ANONYMOUS";
			String agree = null;
			
			do {
				console.write("Do you want to post this tweet? (Anonymous tweets cannot be deleted) [Y/N]: ");
				agree = console.readLine();
			} while (!agree.equalsIgnoreCase("Y") && !agree.equalsIgnoreCase("N"));
		}
		
		Tweet newTweet = new Tweet(content, username);
		tweets.add(newTweet);
		
		OpenFile openFile = fileSystem.open("tweets.txt", true);
		String tweetPost = "";
		for (Tweet tweet : tweets) {
			tweetPost += tweet.toString();
			tweetPost += "\n";
		}
		
		byte[] data = tweetPost.getBytes();
		openFile.write(data, 0, data.length);
		console.writeln("Tweet Posted!");
	}

	private void checkTimeLine() {
		OpenFile openFile = fileSystem.open("tweets.txt", false);
		
		if(openFile == null) {
			console.writeln("Sadly no one has tweeted yet.. Follow other users and create tweets to fill the timeline!");
			return;
		} else {
			byte[] theByte = new byte[9999];
			openFile.read(theByte, 0, openFile.length());
			
			String tweetPosts = new String(theByte);
			
			int count = 0;
			
			if (!tweets.isEmpty()) {
				for (int i = 0; i < tweets.size(); i++) {
					
					if(count == 3) {
						String agree = null;
						
						do {
							console.write("Do you want to show more tweets ? [Y/N]: ");
							agree = console.readLine();
						} while (!agree.equalsIgnoreCase("Y") && !agree.equalsIgnoreCase("N"));
						
						if(agree.equalsIgnoreCase("Y")) {
							count = 0;
						} else {
							break;
						}	
					}
					
					new KThread(tweets.get(i)).fork();
					count++;
				} 
			} else {
				console.writeln("Sadly no one has tweeted yet.. Follow other users and create tweets to fill the timeline!");
			}
			console.writeln("Press enter to go back home...");
			console.readLine();
		}
		
	}

}
