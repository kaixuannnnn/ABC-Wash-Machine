import java.util.*;
public class ABCWashMachine {
	public enum status { free, busy}
	static int maxDur=0;
	static int numCar=0; 
	static int totalWait=0;
	static int totalWork=0;
	public static void main(String[] args) {
		Clock carArrival;
		Queue <Clock> waitQueue= new Queue <>();
		Queue <Clock> arrivalQueue = new Queue<>();
		//enum status {free, busy} machineWash;
		status machineWash;
		//observation data
		Clock totalWaitTime= new Clock();
		Clock maxWaitTime = new Clock();
		Clock totalServiceTime = new Clock();
		Clock startTime= new Clock();
		Clock endTime= new Clock();
		Clock washEnd = new Clock();
		Clock washStart = new Clock();
		Clock i;
		int nextArrival=0;
		Random rand = new Random();
		
		startTime.setTime(8,0,0);
		endTime.setTime(8,30,0); // can change to 12 pm
		for (i=startTime.getCopy();i.lessThan(endTime); ){	//create random time and store in arrivalQueue
			nextArrival = rand.nextInt(10); //nextArrival in the range 0 to 9
			i.addTimeMinute(nextArrival);
			if(i.lessThan(endTime)) {
				arrivalQueue.enqueue(i.getCopy());
				System.out.println("car arrival: " + i.toString() + " < " + 
						endTime.toString());
			}
		}
		System.out.println();
		
		//start the simulation
		machineWash=status.free;
		if (!arrivalQueue.isEmpty()) {
			startTime=arrivalQueue.peek();
			//System.out.println(startTime);
			washEnd=startTime.getCopy();
			washEnd.addTimeMinute(5);	//each round 5 mins
		} else
			startTime=endTime.getCopy();

		Clock del;
		for
		(i=startTime;(i.lessThan(endTime)||(!waitQueue.isEmpty())||(!arrivalQueue.isEmpty()));) 
		{
			if (!arrivalQueue.isEmpty())	//if car arrival event occur 
				if (i.equalTime(arrivalQueue.peek())) {
					waitQueue.enqueue(i.getCopy());
					del=arrivalQueue.dequeue();
				}
			if ((machineWash==status.busy) && (i.equalTime(washEnd))) {	//if machine finish washing
				washEnd.setTime(14,0,0); 
				machineWash=status.free;
			}
			if ((machineWash==status.free) && !(waitQueue.isEmpty())) {	//when machine finish washing, update machine state
				washStart=i.getCopy();
				washEnd=i.getCopy(); 
				washEnd.addTimeMinute(5);
				
				doAnalysis(i,waitQueue.peek(),washEnd); // call doAnalysis method
				del=waitQueue.dequeue();
				machineWash=status.busy;
			}
			if ((machineWash==status.free) && (waitQueue.isEmpty())) {	//machine is available and car in waiting
				washEnd.setTime(14,0,0);
				
			}
			//jump to next event.
			if (!arrivalQueue.isEmpty()) {		//machine is available but no car is waiting
				if (washEnd.lessThan(arrivalQueue.peek())) {
					i=washEnd.getCopy();
				}
				else {
					i=arrivalQueue.peek().getCopy();
				}
			}
			else {
				i=washEnd.getCopy();
			}

		} 

		//report
		System.out.print("REPORT\n");
		System.out.print("Number of customer arrive by 8.30 am: " + numCar + "\n");
		System.out.print("Longest waiting time: " + maxDur + " minutes\n");
		System.out.print(String.format("Average waiting time: %.2f minutes", 
				totalWait/(float)numCar));
	}
	public static void doAnalysis(Clock waitStop, Clock start, Clock washStop) {
		int carWait,machineWork;
		carWait=start.durationSec(waitStop.getCopy());
		machineWork=waitStop.durationSec(washStop.getCopy());
		numCar++;
		totalWait+= carWait;	//use to calculate the average
		totalWork+=machineWork;	//
		
		if (maxDur<carWait) 
			maxDur=carWait;
		System.out.println("Customer: "+waitStop+" waitQueue: "+start+" washEnd: "+washStop);
		System.out.println("carWait: "+carWait+" maxDur: "+maxDur);
		System.out.println("TotalWait: "+ totalWait);
		System.out.println("TotalWork: "+totalWork);
		System.out.println();
	}
}