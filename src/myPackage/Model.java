package myPackage;

import java.util.Random;

public class Model
{
	double sim_time ;
	int Queue_size = 10000;
	int next_event_type;
	int []num_custs_delayed = new int[5];
	int num_events=5;
	double mean_interarrival;
	int []num_in_queue = new int[5];
	int []server_status = new int[5];
	double []area_num_in_queue = new double[5];
	double []area_server_status = new double[5];
	double []time_last_event = new double[5];
	double []total_of_delays = new double[5];
	double []server_service_up= new double[5];//{0.0,5.0,5.0,5.0,5.0};
	double []server_service_down = new double[5];//{0.0,1.0,1.0,1.0,1.0};
	double[][] time_arrival = new double[num_in_queue.length][Queue_size];
	double[] time_next_event=new double[num_events+1];
	
	Random random = new Random(10000);
	Random r = new Random();
	
	 
	void initialize()
	{
		sim_time = 0.0;
		time_next_event[1]= sim_time + expon(mean_interarrival) ;
		for(int i=2;i<=num_events;++i)
		{
			time_next_event[i] = 1.0e+30;
		}
	}
	 
	public void timing()
	{
		int   i;
		double min_time_next_event = 1.0e+29;
		next_event_type=0;
		
		for(i=1;i<=num_events;++i)
		{
			if (time_next_event[i] < min_time_next_event)
				{
					min_time_next_event=time_next_event[i];
					next_event_type=i;
				}
		}
			
		if(next_event_type==0)
		{
			System.out.println("Event List is Empty");
			System.exit(1);
		}
			
	 
		sim_time = min_time_next_event;
	}
	
	public void arrive(int common, double time)
	{
		double delay;
		if(common==1)
		{
			time_next_event[1]= sim_time +expon(mean_interarrival);
		}
		
	 
		if (server_status[common] == 1)
		{
			++num_in_queue[common];
//			System.out.println("Queue1 = "+num_custs_delayed[1]);
//			System.out.println("Queue2 = "+num_custs_delayed[2]);
//			System.out.println("Queue3 = "+num_custs_delayed[3]);
//			System.out.println("Queue4 = "+num_custs_delayed[4]);
			
			if(num_in_queue[common]>Queue_size)
			{
				System.out.println("Over flow of the queue time arrival at "+sim_time);
				System.exit(2);
				
			}
			System.out.println("Simulation time = "+sim_time+" time next event "+time);
			time_arrival[common][num_in_queue[common]] = sim_time;
		}
	 
		else
		{
			delay = 0;
			total_of_delays[common] += delay;
			server_status[common] = 1;
			time_next_event[common+1] = sim_time + uniform(server_service_down[common],server_service_up[common]);
		}
	}
	
	 
	public void depart(int common)
	{
		double delay;
		if (num_in_queue[common] == 0)
		{
			server_status[common] = 0;
			time_next_event[common+1] = 1.0e+30;
		}
		else
		{
			--num_in_queue[common];
			delay = sim_time-time_arrival[common][1];
			total_of_delays[common] += delay;
			
			time_next_event[common+1] = sim_time + uniform(server_service_down[common],server_service_up[common]);
			
			for (int i = 1; i <= num_in_queue[common]; ++i)
			{
				
				System.out.println("i======= "+i+ "  time arrival ===== " + time_arrival[common][i]);
				time_arrival[common][i] = time_arrival[common][i+1];
			}
		}
		++num_custs_delayed[common];
		
			
	}
	
	public void update_time_avg_stats(int common)
	{
		double time_since_last_event;
		 
		time_since_last_event = sim_time - time_last_event[common];
		time_last_event[common] = sim_time;
	 
		area_num_in_queue[common] += num_in_queue[common] * time_since_last_event;
	 
		area_server_status[common] += server_status[common] * time_since_last_event;
	} 
	
	
	public double uniform(double down, double up)
	{
		return (double) (down+((up-down)*r.nextDouble()));
	}
	
	public double expon(double  mean)
	{
		return -mean * Math.log(random.nextDouble());
	}
	
	public void reportForServer(int serverNumber)
	{
		System.out.println( "Total customer uses Server"+serverNumber+" = " + num_custs_delayed[serverNumber]);
		System.out.println( "Average delay in queue"+serverNumber+" = " + total_of_delays[serverNumber] / num_custs_delayed[serverNumber]);
		System.out.println( "Average number in queue"+serverNumber+" = " + area_num_in_queue[serverNumber] / sim_time + "\n");
		System.out.println( "Server"+serverNumber+ " utilization  = " + area_server_status[serverNumber] / sim_time + "\n");
	}
}
