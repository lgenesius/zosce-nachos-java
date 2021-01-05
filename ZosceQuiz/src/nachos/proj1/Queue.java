package nachos.proj1;

import java.util.Vector;

import nachos.threads.KThread;
import nachos.threads.ThreadQueue;

public class Queue extends ThreadQueue{
	private Vector<KThread> waitingQueue;
	
	public Queue() {
		waitingQueue = new Vector<>();
	}

	@Override
	public void waitForAccess(KThread thread) {
		waitingQueue.add(thread);
		
	}

	@Override
	public KThread nextThread() {
		if(waitingQueue.isEmpty()) {
			return null;
		}
		
		KThread thread = waitingQueue.remove(0);
		return thread;
	}

	@Override
	public void acquire(KThread thread) {
		
		
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub
		
	}

}
