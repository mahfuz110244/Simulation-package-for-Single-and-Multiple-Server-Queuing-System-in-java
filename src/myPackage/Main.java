
package myPackage;

import java.util.Scanner;

public class Main 
{
	public static SingleServer ss;
	public static MultipleServer ms;
	public static Model m;
	static Scanner sc = new Scanner(System.in);
	public static void main(String[] args) 
	{
		ss = new SingleServer();
		ms = new MultipleServer();
		m = new Model();
		
		
		System.out.println("Choose the Server System No");
		System.out.println("Choose 1 for Single Server Queueing System");
		System.out.println("Choose 2 for Multiple Server Queueing System");
		System.out.println("Choose 3 for Multiple Server Queueing System For Model");
		int n = sc.nextInt();
		switch (n)
		{
			case 1: singleServerInput();
			break;
			case 2: multipleServerInput();
			break;
			case 3: ModelInput();
			break;
		}
	}

	

	private static void singleServerInput()
	{
		System.out.println("Single Server Queueing System");
		System.out.println("Enter mean inter arrival time = ");
		ss.mean_interarrival=sc.nextDouble();
		
		System.out.println("\nEnter mean service time of server  = ");
		ss.mean_service=sc.nextDouble();
		System.out.println("\nEnter number of customer  = ");
		ss.num_of_customer= sc.nextInt();
	    ss.initialize();
		
		while(ss.num_custs_delayed<ss.num_of_customer)
		{
			ss.timing();
			ss.update_time_avg_stats();
			 
			switch (ss.next_event_type)
			{
				case 1: ss.arrive();
				break;
				case 2: ss.depart();
				break;
			}	
		}
		ss.print();
	}
	
	private static void multipleServerInput()
	{
		System.out.println("Multiple Server Queueing System");
		System.out.println("Enter mean inter arrival time = ");
		ms.mean_interarrival=sc.nextDouble();
		System.out.println("\nEnter uniform upper service time of server1  = ");
		ms.server1_service_up=sc.nextDouble();
		System.out.println("\nEnter uniform down service time of server1  = ");
		ms.server1_service_down=sc.nextDouble();
		System.out.println("\nEnter uniform upper service time of server2  = ");
		ms.server2_service_up=sc.nextDouble();
		System.out.println("\nEnter uniform down service time of server2  = ");
		ms.server2_service_down=sc.nextDouble();
		
		
		System.out.println("\nEnter simulation time = ");
		double sim_time = sc.nextDouble();
		
		
		 
		System.out.println("Multiple-server queueing system\n");
		System.out.println("Mean interarrival time = "+ms.mean_interarrival+" minutes\n");
		System.out.println("Upper service time of server1 = "+ms.server1_service_up+" minutes\n");
		System.out.println("down service time of server1 = "+ms.server1_service_down+" minutes\n");
		System.out.println("Upper service time of server2 = "+ms.server2_service_up+" minutes\n");
		System.out.println("down service time of server2= "+ms.server2_service_down+" minutes\n");
		System.out.println("Simulation time = "+sim_time);
		
		ms.initialize();
		while(ms.sim_time<sim_time)
		{
			ms.timing();
			switch (ms.next_event_type)
			{
				case 1: ms.arrive1();
				ms.update_time_avg_stats1();
				break;
				case 2: ms.depart1();
				ms.update_time_avg_stats1();
				break;
				case 3: ms.depart2();
				ms.update_time_avg_stats2();
				break;
			}
			
			
		}
		
		while(ms.num_in_queue1>0 || ms.num_in_queue2>0)
		{
			ms.timing();
			switch (ms.next_event_type)
			{	case 1:
				break;
				case 2: ms.depart1();
				ms.update_time_avg_stats1();
				break;
				case 3: ms.depart2();
				ms.update_time_avg_stats2();
				break;
			}
		}
		
		System.out.println("\n\nReport for server 1:\n--------------------\n");
		ms.reportForServer1();
		System.out.println("\n\nReport for server 2:\n--------------------\n");
		ms.reportForServer2();
		System.out.println("Simulation time ended = "+ms.sim_time);
	}
	

	private static void ModelInput()
	{
		System.out.println("Multiple Server Queueing System For Model");
		System.out.println("\nEnter Mean Service time of Server1  = ");
		m.mean_interarrival=sc.nextDouble();
		System.out.println("\nEnter uniform down & upper service time of server  = ");
		//System.out.println("server up "+m.server_service_up.length);
		for(int i=1; i<5; i++)
		{
			m.server_service_down[i]=sc.nextDouble();
			m.server_service_up[i]=sc.nextDouble();
		}
		
		System.out.println("\nEnter simulation time = ");
		double sim_time = sc.nextDouble();
		
		
		 
		/*System.out.println("Multiple Server Queueing System For Model\n");
		System.out.println("Mean interarrival time = "+sv.mean_interarrival+" minutes\n");
		System.out.println("Upper service time of server1 = "+sv.server1_service_up+" minutes\n");
		System.out.println("down service time of server1 = "+sv.server1_service_down+" minutes\n");
		System.out.println("Upper service time of server2 = "+sv.server2_service_up+" minutes\n");
		System.out.println("down service time of server2= "+sv.server2_service_down+" minutes\n");
		System.out.println("Simulation time = "+sim_time);*/
		
		m.initialize();
		while(m.sim_time<sim_time)
		{
			m.timing();
			switch (m.next_event_type)
			{
				case 1: 
					m.arrive(1,m.time_next_event[1]);
					m.update_time_avg_stats(1);
					break;
					
				case 2: 
					m.depart(1);
					m.update_time_avg_stats(1);
					if(m.r.nextDouble() >= 0.0 && m.r.nextDouble()<0.4)
					{
						m.arrive(2,m.sim_time);
						m.update_time_avg_stats(2);
					}
					else if(m.r.nextDouble() >= 0.4 && m.r.nextDouble()<0.7)
					{
						m.arrive(3,m.sim_time);
						m.update_time_avg_stats(3);
					}
					else
					{
						m.arrive(4,m.sim_time);
						m.update_time_avg_stats(4);
					}
					break;
					
				case 3: 
					m.depart(2);
					m.update_time_avg_stats(2);
					break;

				case 4: 
					m.depart(3);
					m.update_time_avg_stats(3);
					break;

				case 5: 
					m.depart(4);
					m.update_time_avg_stats(4);
					break;
			}
			
//			System.out.println("Queue1 = "+m.num_in_queue[1]);
//			System.out.println("Queue2 = "+m.num_in_queue[2]);
//			System.out.println("Queue3 = "+m.num_in_queue[3]);
//			System.out.println("Queue4 = "+m.num_in_queue[4]);
			
			
		}
		
		m.time_next_event[1] = 1.0e+30;
		while(m.num_in_queue[1]>0 || m.num_in_queue[2]>0 ||m.num_in_queue[3]>0 || m.num_in_queue[4]>0)
		{
			m.timing();
			switch (m.next_event_type)
			{
				
					
				case 2: 
					m.depart(1);
					m.update_time_avg_stats(1);
					if(m.r.nextDouble() >= 0.0 && m.r.nextDouble()<0.4)
					{
						m.arrive(2,m.sim_time);
						m.update_time_avg_stats(2);
					}
					else if(m.r.nextDouble() >= 0.4 && m.r.nextDouble()<0.7)
					{
						m.arrive(3,m.sim_time);
						m.update_time_avg_stats(3);
					}
					else
					{
						m.arrive(4,m.sim_time);
						m.update_time_avg_stats(4);
					}
					break;
					
				case 3: 
					m.depart(2);
					m.update_time_avg_stats(2);
					break;

				case 4: 
					m.depart(3);
					m.update_time_avg_stats(3);
					break;

				case 5: 
					m.depart(4);
					m.update_time_avg_stats(4);
					break;
			}
		}
		
		System.out.println("\n\nReport for Checking Server 1:\n---------------------------------------------------------\n");
		m.reportForServer(1);
		System.out.println("\n\nReport for Money Transfer Counter 1 Server 2:\n---------------------------------------------------------\n");
		m.reportForServer(2);
		System.out.println("\n\nReport for Cash Withdraw Counter 2 Server 3:\n---------------------------------------------------------\n");
		m.reportForServer(3);
		System.out.println("\n\nReport for Depositr Counter 3 Server 4:\n---------------------------------------------------------\n");
		m.reportForServer(4);
		System.out.println("Simulation time ended = "+m.sim_time);
		
//		System.out.println("Queue1 = "+m.num_in_queue[1]);
//		System.out.println("Queue2 = "+m.num_in_queue[2]);
//		System.out.println("Queue3 = "+m.num_in_queue[3]);
//		System.out.println("Queue4 = "+m.num_in_queue[4]);
	}


}
