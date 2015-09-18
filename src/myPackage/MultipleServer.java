package myPackage;

import java.util.Random;

public class MultipleServer {
	int Queue_size = 1000;
	int next_event_type;
	int num_custs_delayed1;
	int num_custs_delayed2;
	int num_events=3;
	int num_in_queue1;
	int num_in_queue2;
	int server_status1;
	int server_status2;
	double area_num_in_queue1;
	double area_num_in_queue2;
	double area_server_status1;
	double area_server_status2;
	double sim_time;
	double time_last_event1;
	double time_last_event2;
	double total_of_delays1;
	double total_of_delays2;
	double mean_interarrival;
	double server1_service_up;
	double server1_service_down;
	double server2_service_up;
	double server2_service_down;
	  
	double[] time_arrival1 = new double[Queue_size];
	double[] time_arrival2 = new double[Queue_size];
	double[] time_next_event=new double[4];
	 
	Random random = new Random(10000);
	Random r = new Random();
	 
	void initialize()
	{
		sim_time = 0.0;
		server_status1 = 0;
		server_status2 = 0;
		num_in_queue1 = 0;
		num_in_queue2 = 0;
		time_last_event1 = 0.0;
		time_last_event2 = 0.0;
		num_custs_delayed1 = 0;
		num_custs_delayed2 = 0;
		total_of_delays1 = 0.0;
		total_of_delays2 = 0.0;
		area_num_in_queue1 = 0.0;
		area_num_in_queue2 = 0.0;
		area_server_status1 = 0.0;
		area_server_status2 = 0.0;
	 
		time_next_event[1] = sim_time + expon(mean_interarrival) ;
		time_next_event[2] = 1.0e+30;
		time_next_event[3] = 1.0e+30;
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
	 
	public void arrive1()
	{
		double delay;
		time_next_event[1]= sim_time+expon(mean_interarrival);
		
	 
		if (server_status1 == 1)
		{
			++num_in_queue1;
			
			if(num_in_queue1>Queue_size)
			{
				System.out.println("Over flow of the queue time arrival at "+sim_time);
				System.exit(2);
				
			}
			time_arrival1[num_in_queue1] = sim_time;

		}
	 
		else
		{
			delay = 0;
			total_of_delays1 += delay;
			server_status1 = 1;
			time_next_event[2] = sim_time + uniform(server1_service_down,server1_service_up);
		}
	}
	 
	public void depart1()
	{
		double delay;
		if (num_in_queue1 == 0)
		{
			server_status1 = 0;
			time_next_event[2] = 1.0e+30;
		}
		else
		{
			--num_in_queue1;
			delay = sim_time-time_arrival1[1];
			total_of_delays1 += delay;
			
			time_next_event[2] = sim_time + uniform(server1_service_down,server1_service_up);
			
			for (int i = 1; i <= num_in_queue1; ++i)
			{
				time_arrival1[i] = time_arrival1[i+1];
			}
		}
		++num_custs_delayed1;
		
		if(r.nextDouble() <= 0.3)
		{
			arrive2(sim_time);
		}	
	}
	
	public void arrive2(double Time)
	{
		double delay;
		if (server_status2 == 1)
		{
			++num_in_queue2;
			
			if(num_in_queue2>Queue_size)
			{
				System.out.println("Over flow of the queue time arrival at "+sim_time);
				System.exit(2);
				
			}
			time_arrival2[num_in_queue2] = Time;

		}
	 
		else
		{
			delay = 0;
			total_of_delays2 += delay;
			server_status2 = 1;
			time_next_event[3] = sim_time + uniform(server2_service_down,server2_service_up);
			
		}
		update_time_avg_stats2();
	}
	
	public  void depart2()
	{

		double delay;
		if (num_in_queue2 == 0)
		{
			server_status2 = 0;
			time_next_event[3] = 1.0e+30;
		}
		else
		{
			--num_in_queue2;
			delay = sim_time - time_arrival2[1];
			total_of_delays2  += delay;
			//System.out.println("total delay 2= " +total_of_delays2);
			
			time_next_event[3] = sim_time +  uniform(server2_service_down,server2_service_up);
			
			for (int i = 1; i <= num_in_queue2; ++i)
			{
				time_arrival2[i] = time_arrival2[i+1];
			}
		}
		++num_custs_delayed2;
	}
	 
	
	public void update_time_avg_stats1()
	{
		double time_since_last_event;
		 
		time_since_last_event = sim_time - time_last_event1;
		time_last_event1 = sim_time;
	 
		area_num_in_queue1 += num_in_queue1 * time_since_last_event;
	 
		area_server_status1 += server_status1 * time_since_last_event;
	}
	
	public  void update_time_avg_stats2()
	{
		double time_since_last_event;

		time_since_last_event = sim_time - time_last_event2;
		time_last_event2 = sim_time;

		area_num_in_queue2 += num_in_queue2 * time_since_last_event;

		area_server_status2 += server_status2 * time_since_last_event;
	}
	
	public double expon(double  mean)
	{
		return -mean * Math.log(random.nextDouble());
	}
	
	public double uniform(double a, double b)
	{
		return (double) (a+((b-a)*r.nextDouble()));
	}
	
	public void reportForServer1()
	{
		System.out.println( "Total customer uses Server1 = " + num_custs_delayed1 + "\n");
		System.out.println( "Average delay in queue1 = " + total_of_delays1 / num_custs_delayed1 + "\n");
		System.out.println( "Average number in queue1  = " + area_num_in_queue1 / sim_time + "\n");
		System.out.println( "Server1 utilization  = " + area_server_status1 / sim_time + "\n");
	}
	
	public void reportForServer2()
	{
		System.out.println( "Total customer uses Server2 = " + num_custs_delayed2 + "\n");
		System.out.println( "Average delay in queue2 = " + total_of_delays2 / num_custs_delayed2 + "\n");
		System.out.println( "Average number in queue2  = " + area_num_in_queue2 / sim_time + "\n");
		System.out.println( "Server2 utilization  = " + area_server_status2 / sim_time + "\n");
	}
}
