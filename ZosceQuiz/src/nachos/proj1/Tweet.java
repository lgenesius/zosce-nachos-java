package nachos.proj1;

public class Tweet implements Runnable{
	private String content, username;
	
	

	public Tweet(String content, String username) {
		super();
		this.content = content;
		this.username = username;
	}



	public String getContent() {
		return content;
	}



	public void setContent(String content) {
		this.content = content;
	}



	public String getUsername() {
		return username;
	}



	public void setUsername(String username) {
		this.username = username;
	}



	public Tweet() {
		
	}



	@Override
	public void run() {
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		System.out.println("User : " + this.username);
		System.out.println(this.content);
	}
	
	public String toString() {
		return this.content + "#" + this.username;
	}

}
