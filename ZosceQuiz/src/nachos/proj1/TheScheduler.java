package nachos.proj1;

import nachos.threads.Scheduler;
import nachos.threads.ThreadQueue;

public class TheScheduler extends Scheduler{

	public TheScheduler() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public ThreadQueue newThreadQueue(boolean transferPriority) {
		return new Queue();
	}

}
