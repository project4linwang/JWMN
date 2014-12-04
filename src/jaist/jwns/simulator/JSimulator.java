package jaist.jwns.simulator;




import jaist.jwns.entity.Link;
import jaist.jwns.entity.NetWorkThroughPut;
import jaist.jwns.entity.Node;
import jaist.jwns.entity.NodeChain;
import jaist.jwns.entity.ProcessDevice;
import jaist.jwns.entity.ThroughPut;
import jaist.jwns.entity.HelloTable.HTable;
import jaist.jwns.template.LinkThroughPut;
import jaist.jwns.template.NodeData;
import jaist.jwns.template.SDPair;
import jaist.jwns.template.Template;
import jaist.jwns.util.Configuration;
import jaist.jwns.util.IPAddress;
import jaist.jwns.util.IniReader;
import jaist.jwns.util.LogNetworkThroughput;
import jaist.jwns.util.SerialXMLUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/***
 * JSimulator is the main class in JNS. It contains the main loop that will
 * generate the elements of the network. 
 * @author lin
 *
 */
public class JSimulator {
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			if(args==null){
				System.out.println("Please input the config file and scenario file");
			}
			else{
				if(args.length>=2){
					Configuration config=Configuration.getConfig();
					String confile=args[0];
					String scenfile=args[1];
					if(args.length==3){
						String para=args[2];
						if(para.equals("-rt")){
							config.setPrintRoutingTable(true);
						}
						else if(para.equals("-lost")){
							config.setPrintLostPackets(true);
						}
						else{
							PrintHelp();
						}
					}
					else if(args.length==4){
						String para1=args[2];
						String para2=args[3];
						if(para1.equals("-rt") || para2.equals("-rt")){
							config.setPrintRoutingTable(true);
						}
						else{
							PrintHelp();
						}
						if(para1.equals("-lost") || para2.equals("-lost")){
							config.setPrintLostPackets(true);
						}
						else{
							PrintHelp();
						}
					}
					
				    ReadConfigFile(confile);//"result/config.ini"
					Template tm= (Template)SerialXMLUtil.load(scenfile);//"result/configexfinal.xml"
					int packet_size=config.getSend_packet_size();
					NodeChain chain=NodeChain.getNodeChain();
					ArrayList<Node> nodelist=new ArrayList<Node>();
					ArrayList<NodeData> nodes= tm.getNodes();
			        ArrayList<LinkThroughPut> lkputs= tm.getOutPut();
			        ArrayList<SDPair> pairs=tm.getSDPairs();
		            
			        if(nodes!=null){
			        	int nd_seq=0;
			        	for(NodeData node:nodes){
			        		nd_seq++;
			        		String ipaddr=node.getIPAddress();
			        		String[] sp=ipaddr.split(",");
			        		IPAddress ip=new IPAddress(Integer.parseInt(sp[0]),Integer.parseInt(sp[1]),Integer.parseInt(sp[2]),Integer.parseInt(sp[3]));
			        		Node nd=new Node(nd_seq,node.getName(),ip,node.getQueueSize());
			        		nd.setRange(node.getRange());
			        		nd.setX(node.getX());
			        		nd.setY(node.getY());
			        		nd.setDelay_time(node.getDelay());
			        		nd.setDefault_queueStatus(node.getQueueStatus());
			        		ProcessDevice pd=new ProcessDevice(node.getProcessTime());
			        		nd.addProcessDevice(pd);
		        		
			        		nd.init();
			        		nodelist.add(nd);
			        		chain.addNode(nd); 
			        	}
			        	chain.startRegister();
			        	System.out.println("Initializing...");
			        }
			        try {
						Thread.sleep(10000);

						ArrayList<Link> linklist=new ArrayList<Link>();
						//set links for each node
						if(nodelist!=null){
							
							for(Node nd:nodelist){
							   ArrayList<HTable> htables= nd.getHelloTable();
							   if(htables!=null){
								   for(HTable ht:htables){
									  Node next_node= FindNodeByIP(nodelist, ht.getNext());
									  if(next_node!=null){
										  if(!Linklist_exist(linklist,nd,next_node)){
											  Link link=new Link(config.getLink_bandwidth()); 					  
											  nd.addLink(link);
											  link.addNode(nd.getSrc().toString());
											  next_node.addLink(link); 
											  link.addNode(next_node.getSrc().toString());
											  linklist.add(link);
										  }
									  }							 
									  
								   }
							   }
							}
						}
						if(nodelist!=null){
							for(Node nd:nodelist){
								nd.init();
							}
						}
						//
						System.out.println("Begin Send Data");
						
						if(linklist!=null){
							for(Link lk:linklist){
								if(lkputs!=null){
									for(LinkThroughPut lkput:lkputs){
										String start_addr=lkput.getStartAddress();
										String[] start_sp=start_addr.split(",");
										String end_addr=lkput.getEndAddress();
										String[] end_sp=end_addr.split(",");
										IPAddress ip_end=new IPAddress(Integer.parseInt(end_sp[0]),Integer.parseInt(end_sp[1]),Integer.parseInt(end_sp[2]),Integer.parseInt(end_sp[3]));
						        		IPAddress ip_start=new IPAddress(Integer.parseInt(start_sp[0]),Integer.parseInt(start_sp[1]),Integer.parseInt(start_sp[2]),Integer.parseInt(start_sp[3]));
						        		List<String> nodepair= lk.getNodePair();
										if(nodepair.contains(ip_start.toString()) && nodepair.contains(ip_end.toString())){
											
											lk.openTracer();
											
										}
									}
								}
								
							}
						}
						if(pairs!=null){
							for(SDPair pair:pairs){
								String start_addr=pair.getSrcAddress();
								String[] start_sp=start_addr.split(",");
								String end_addr=pair.getDstAddress();
								String[] end_sp=end_addr.split(",");
								IPAddress ip_end=new IPAddress(Integer.parseInt(end_sp[0]),Integer.parseInt(end_sp[1]),Integer.parseInt(end_sp[2]),Integer.parseInt(end_sp[3]));
								IPAddress ip_start=new IPAddress(Integer.parseInt(start_sp[0]),Integer.parseInt(start_sp[1]),Integer.parseInt(start_sp[2]),Integer.parseInt(start_sp[3]));
								Node sender=FindNodeByIP(nodelist,ip_start.toString() );						
								Node receiver=FindNodeByIP(nodelist, ip_end.toString());
								sender.sendPacket(packet_size, sender.getDelay_time(), receiver.getSrc());
								Thread.sleep(100);
							}
						}		
						
						Thread.sleep(config.getRREP_live_interval()+1000);
						if(config.isPrintRoutingTable()){
							PrintRoutingTable(nodelist);
						}
						
						Scanner scan = new Scanner(System.in);
						System.out.println("Please input command \"finish\" when finished");
						
						while(true){
							String command=scan.next();
							if(command.equals("finish")){
								if(linklist!=null){
									for(Link lk:linklist){
										lk.sendFinished(false);
									}
								}
								if(nodelist!=null){
									for(Node nd:nodelist){
										nd.Close();
									}
								}
								LogNetworkThroughput log=new LogNetworkThroughput();
								log.writeToFile("ThroughPut.txt");
								break;
							}
							else{
								System.out.println("Please input command \"finish\" when finished");
							}
						}
						System.out.println("The program has been closed");
						System.exit(0);
						
						
						
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else{
					System.out.println("Please input the config file and scenario file");
				}
				
			}			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	private static boolean Linklist_exist(ArrayList<Link> linklist, Node nd,
			Node nextNode) {
		// TODO Auto-generated method stub
		if(linklist!=null){
			for(Link lk:linklist){
			   ArrayList<String> nodes=	lk.getNodePair();
			   if(nodes!=null){
				   if(nodes.contains(nd.getSrc().toString()) && nodes.contains(nextNode.getSrc().toString())){
					   return true;
				   }
			   }
			}
		}
		
		return false;
	}
	
	private static void PrintRoutingTable(ArrayList<Node> nodes){
		if(nodes!=null){
			for(Node nd:nodes){
			   nd.printRT();
			}
		}
	}
	private static Node FindNodeByIP(ArrayList<Node> nodes,String name){
		if(nodes!=null){
			for(Node nd:nodes){
				if(nd.getSrc().toString().equals(name)){
					return nd;
				}
			}
		}
		return null;
	}
	private static String ConvertIPString(String ip){
		String addr=ip;
		String[] sp=addr.split(",");
		IPAddress ip_end=new IPAddress(Integer.parseInt(sp[0]),Integer.parseInt(sp[1]),Integer.parseInt(sp[2]),Integer.parseInt(sp[3]));
		return ip_end.toString();
	}
	private static void PrintHelp(){
		System.out.println("Usage: java - jar jwns.jar [config file] [scenario file] [-options] !");
		System.out.println("where options includes:");
		System.out.println("-rt"+"    "+"print the routing table");
		System.out.println("-lost"+"    "+"print the lost packet list");
		System.exit(0);
	}
	private static void ReadConfigFile(String filename){
		IniReader reader;
		try {
			reader = new IniReader(filename);
			String s1= reader.getValue("JWNS", "LPacket_Length");
			String s2= reader.getValue("JWNS", "trace_time");
			String s3= reader.getValue("JWNS", "queue_period");
			String s4= reader.getValue("JWNS", "hello_time_interval");
			String s5= reader.getValue("JWNS", "hello_entry_expirationtime");
			String s6= reader.getValue("JWNS", "RREQ_len");
			String s7= reader.getValue("JWNS", "RREP_len");
			String s8= reader.getValue("JWNS", "RREQ_time_interval");
			String s9= reader.getValue("JWNS", "RoutingTable_expiration");
			String s10= reader.getValue("JWNS", "RREP_live_interval");
			String s11= reader.getValue("JWNS", "WND_threshold");
			String s12= reader.getValue("JWNS", "WND_max");
			String s13= reader.getValue("JWNS", "WND_initial");
			String s14= reader.getValue("JWNS", "wnd_algorithm");
			String s15= reader.getValue("JWNS", "real");
			String s16=reader.getValue("JWNS", "RREQ_hop_max");
			String s17=reader.getValue("JWNS", "send_packet_size");
			String s18=reader.getValue("JWNS", "link_bandwidth");
			int lp_length=Integer.parseInt(s1);
			long trace_time=Long.parseLong(s2);
			long queue_period=Long.parseLong(s3);
			long hello_interval=Long.parseLong(s4);
			int rreq_len=Integer.parseInt(s6);
			int rrep_len=Integer.parseInt(s7);
			long rrep_live_interval=Long.parseLong(s10);
			int wnd_threshold=Integer.parseInt(s11);
			int wnd_max=Integer.parseInt(s12);
			int wnd_initial=Integer.parseInt(s13);
			int wnd_algorithm=Integer.parseInt(s14);
			int real=Integer.parseInt(s15);
			int rreq_hop_max=Integer.parseInt(s16);
			int packet_size=Integer.parseInt(s17);
			int bandwidth=Integer.parseInt(s18);
			Configuration config=Configuration.getConfig();
			config.setLPacketLength(lp_length);
			config.setQueuePeriod(queue_period);
			config.setTraceTime(trace_time);
			config.setHelloTimeInterval(hello_interval);
			config.setRREQLength(rreq_len);
			config.setRREPLength(rrep_len);
			config.setWNDThreshold(wnd_threshold);
			config.setWNDMax(wnd_max);
			config.setWNDInitial(wnd_initial);
			config.setWNDAlgorithm(wnd_algorithm);
			config.setReal(real);
			config.setRREP_live_interval(rrep_live_interval);
			config.setRREQ_hop_max(rreq_hop_max);
			config.setSend_packet_size(packet_size);
			config.setLink_bandwidth(bandwidth);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
