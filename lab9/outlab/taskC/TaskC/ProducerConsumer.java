import java.util.LinkedList;
import java.util.Queue;

public class ProducerConsumer
{
	public static void main(String[] args)
	{
		int size = 5;
		
		Queue commonQueue = new LinkedList<Object>();
		
		Prod p = new Prod(commonQueue, size);
		Cons c = new Cons(commonQueue, size);
		
		
		Thread p1 = new Thread(p, "Producer");
		Thread c1 = new Thread(c, "Consumer");
		
		p1.start();
		c1.start();
	}
	
	private static class Prod implements Runnable
	{
		int bufLen;
		Queue Q1;
		
		Prod(Queue Q, int s)
		{
			bufLen = s;
			Q1 = Q;
		}

		@Override
		public void run() {
			while(true)
			{
				try
				{
						while(Q1.size() == bufLen)
						{
							synchronized(Q1)
							{
								System.out.println("Produced waiting");
								Q1.wait();
							}
						}
						
						synchronized(Q1)
						{
							Q1.notifyAll();
							Q1.add("hey");
							System.out.println("Produced: " + Q1.size());
						}
						
						// Inversely proportional to Production rate
						int ProductionRateInverse = 500;
						Thread.sleep(ProductionRateInverse);
				}			
				catch(InterruptedException e)
				{
					;
				}
			}
		}
	}
	
	private static class Cons implements Runnable
	{
		int bufLen;
		Queue Q1;
		
		Cons(Queue Q, int s)
		{
			bufLen = s;
			Q1 = Q;
		}

		@Override
		public void run() {
			while(true)
			{
				try
				{
					
						while(Q1.size() == 0)
						{
							synchronized(Q1)
							{
								System.out.println("Consumer waiting");
								Q1.wait();
							}
						}
						
						synchronized(Q1)
						{
							Q1.notifyAll();
							Q1.remove();
							System.out.println("Consumed: " + Q1.size());
						}
						
						// Inversely proportional to Consumption rate
						int ConsumptionRateInverse = 500; 
						Thread.sleep(ConsumptionRateInverse);
							
				}
				catch(InterruptedException e)
				{
					;
				}
			}
			
		}
	}
}
