class Chat {
   boolean flag = false;
   Integer BUFFER_SIZE=5;
   Integer numFull=0;
   Integer numEmpty=BUFFER_SIZE;

   public synchronized void Produce() {
   	numEmpty--;
	if (numEmpty==0) {
	 	try {
			wait();
		}catch (InterruptedException e){;}
	}
	numFull++;
	System.out.println("Produce " + numFull.toString());
	try{
		Thread.sleep(400);
	}
	catch(InterruptedException e){;}
	notify();
   }

   public synchronized void Consume() {
	numFull--;
	if (numFull==0) {
		try {
	    	wait();
		}catch (InterruptedException e) {;}
	}
	numEmpty++;
	System.out.println("Consume "+ numFull.toString());
	try{
		Thread.sleep(1000);
	}
	catch(InterruptedException e){;}
	notify();
   }
}


class T1 implements Runnable {
   Chat m;

   public T1(Chat m1) {
      this.m = m1;
      new Thread(this, "Produce").start();
   }

   public void run() {
   while(true) m.Produce();
   }
}


class T2 implements Runnable {
   Chat m;
   String[] s2 = { "Hi", "I am good, what about you?", "Great!" };

   public T2(Chat m2) {
      this.m = m2;
      new Thread(this, "Consume").start();
   }

   public void run() {
      while(true) m.Consume();
   }
}


public class ProducerConsumer {
   public static void main(String[] args) {
      Chat m = new Chat();
      new T1(m);
      new T2(m);
   }
}