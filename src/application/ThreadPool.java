package application;

import java.util.LinkedList;

public class ThreadPool {
	private WorkerThread[] threads;
	private LinkedList<Runnable> tasksQueue;
	private boolean isStopped = false;
	private static ThreadPool oneInstance = null;

	private ThreadPool() {
		tasksQueue = new LinkedList<Runnable>();
		threads = new WorkerThread[10]; // Number of worker threads
		for (int i = 0; i < threads.length; i++) {
			threads[i] = new WorkerThread();
			threads[i].start();
		}
		System.out.println("Thread Pool alive");
	}
	
	public static ThreadPool getInstance () {
		if (oneInstance == null) {
			oneInstance = new ThreadPool();
		}
		return oneInstance;
	}
	
	public boolean isStopped() {
		return this.isStopped;
	}
	
	public void enqueue(Runnable r) {
		if (!isStopped) {
			synchronized (tasksQueue) {
				tasksQueue.addLast(r);
				tasksQueue.notify();
			}
		}
	}
	
    public synchronized void stop(){
        this.isStopped = true;
        for(WorkerThread thread : threads){
           thread.pleaseStop();
        }
    }
    
	public class WorkerThread extends Thread {
		private boolean isStopped = false;
		
		public void run() {
			while (!isStopped) {
				Runnable r;
				synchronized (tasksQueue) {
					while (tasksQueue.isEmpty()) {
						try {
							tasksQueue.wait();
						} catch (InterruptedException e) {
							e.printStackTrace();
						}
					}
					r = (Runnable) tasksQueue.removeFirst();					
				}
				
				try {
					long number = 1000 +((long)(Math.random()*(5000-1000)));
					sleep(number);
					r.run();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		
	    public synchronized void pleaseStop() {
	        isStopped = true;
//	        this.interrupt();
	    }
	}
}
