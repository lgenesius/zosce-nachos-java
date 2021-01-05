package nachos.proj1;

import nachos.machine.Machine;
import nachos.machine.SerialConsole;
import nachos.threads.Semaphore;

public class TheConsole {
	private SerialConsole console = Machine.console();
	private Semaphore semaphore = new Semaphore(0);
	private char character;
	
	public TheConsole() {
		Runnable receive = new Runnable() {
			
			@Override
			public void run() {
				character = (char) console.readByte();
				semaphore.V();
			}
		};
		
		Runnable send = new Runnable() {
			
			@Override
			public void run() {
				semaphore.V();
			}
		};
		
		console.setInterruptHandlers(receive, send);
	}
	
	public String readLine() {
		String string = "";
		
		do {
			semaphore.P();
			if(character != '\n') {
				string += character;
			}
		} while(character != '\n');
		
		return string;
	}
	
	public int readInt() {
		int result = 0;
		
		try {
			result = Integer.parseInt(readLine());
		} catch (NumberFormatException e) {
			result = -1;
		}
		
		return result;
	}
	
	public void write(String string) {
		for (int i = 0; i < string.length(); i++) {
			console.writeByte(string.charAt(i));
			semaphore.P();
		}
	}
	
	public void writeln(String string) {
		write(string + '\n');
	}

}
