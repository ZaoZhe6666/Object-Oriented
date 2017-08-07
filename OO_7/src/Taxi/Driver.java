package Taxi;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class Driver extends Thread{
	private SafeFile sf;
	private TaxiGUI gui;
	private Map map;
	private boolean relax=true;
	private int mark=0;
	private int name=0;
	private int line=0;
	private int list=0;
	private int feel_no_good=0;
	private int status=2;
	private Syntagm just_a_rest;
	private ArrayList<Syntagm> worklist = new ArrayList<Syntagm>();
	private boolean shut=true;
	private boolean working=false;
	/*	status value
	 * 		0	stop				(停车)
	 * 		2	waiting for serve	(空车跑)
	 * 		1	serving				(拉客)
	 * 		3	going to the man	(去接客人)
	 * */
	Driver(int name,int line,int list,Map map){
		this.name=name;
		this.line=line;
		this.list=list;
		this.map=map;
	}
	public void setgui(TaxiGUI gui){
		this.gui=gui;
	}
	public void write(Syntagm request,String s){
		File myFilePath = new File(request.getpath());
		synchronized(myFilePath){
			try{
				
				FileWriter fileWritter = new FileWriter(myFilePath.getAbsoluteFile(),true);
	            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
	            bufferWritter.write(s);
	            bufferWritter.close();
	            
			}catch(Exception e){
			}
		}
	}
	public void run(){
		try{
			Random random = new Random();
			boolean flag1=true;
			while(shut){
				random.getsleeptime200();
				sleep(10);
				if(status ==1){//服务乘客状态
					setrelax(false);
					feel_no_good=0;
					if(!flag1){
						flag1=true;
						bfs(worklist.get(0),true);
						String s = "("+line+","+list+")->";
						write(worklist.get(0),s);
//						System.out.println("No."+name+" bfs to the finish point succeed!");
						for(int i=0;i<worklist.size();i++){
							System.out.println(i+"---"+worklist.get(i).getline()+","+worklist.get(i).getlist());
						}
					}
					
					working=true;
					
					if(worklist.size()>2){
						line = worklist.get(1).getline();
						list = worklist.get(1).getlist();
						
						String s = "("+line+","+list+")->";
						write(worklist.get(0),s);
						int sleeptime=random.getsleeptime200();
						if(sleeptime<0){
							System.out.println("foolish work!");
						}
						else{
							sleep(sleeptime);
						}
						
						gui.SetTaxiStatus(name,new Point(line-1,list-1),status);
						worklist.remove(1);
						continue;
					}
					if(worklist.size()==2){//到达目的地，停车下人
						line = worklist.get(1).getline();
						list = worklist.get(1).getlist();
						System.out.println("No."+name+" got to the order!");
						status = 0;
						
						String line1 = "("+line+","+list+")\n";
						String line2 = "Driver arrive  the destination and set the man\n\nRequest finished!";
						String s=line1+line2;
						int sleeptime=random.getsleeptime200();
						if(sleeptime<0){
							System.out.println("foolish work!");
						}
						else{
							sleep(sleeptime);
						}

						write(worklist.get(0),s);
						gui.SetTaxiStatus(name,new Point(line-1,list-1),status);
						mark+=3;
						worklist.clear();
						int sleep_time=random.getsleeptime1000();
						if(sleep_time<0){
							System.out.println("foolish work!");
						}
						else{
							sleep(sleep_time);
						}
						status=2;
						System.out.println("No."+name+" feel free!");
						setrelax(true);
						gui.SetTaxiStatus(name,new Point(line-1,list-1),status);
						working=false;
					}
					continue;
				}
				if(worklist.size()!=0){//抢到单去接乘客状态
					setrelax(false);
					feel_no_good=0;
					if(flag1){
						flag1=false;
						System.out.println("No."+name+" got to catch the customer!");
						String line1 = "No."+name+" got to catch the customer!\n";
						String line2 = "The path is:\n("+line+","+list+")->";
						String s=line1+line2;
						write(worklist.get(0),s);
						status = 3;
						gui.SetTaxiStatus(name,new Point(line-1,list-1),status);
						bfs(worklist.get(0),false);

						for(int i=0;i<worklist.size();i++){
							System.out.println(i+"---"+worklist.get(i).getline()+","+worklist.get(i).getlist());
						}

						System.out.println("No."+name+" bfs the man's point succeed!");
					}
					working = true;
					if(worklist.size()>2){
						line = worklist.get(1).getline();
						list = worklist.get(1).getlist();
						String s="("+line+","+list+")->";

						int sleeptime=random.getsleeptime200();
						if(sleeptime<0){
							System.out.println("foolish work!");
						}
						else{
							sleep(sleeptime);
						}
						write(worklist.get(0),s);
						gui.SetTaxiStatus(name,new Point(line-1,list-1),status);
						worklist.remove(1);
						continue;
					}
					if(worklist.size()==2){//到达用户那里，停车接人
						line = worklist.get(0).getline();
						list = worklist.get(0).getlist();
						int sleeptime=random.getsleeptime200();
						if(sleeptime<0){
							System.out.println("foolish work!");
						}
						else{
							sleep(sleeptime);
						}
						System.out.println("No."+name+" catching the customer!");
						String s="("+line+","+list+")\n\n";
						
						write(worklist.get(0),s);
						status = 0;
						s = "Driver stop to catch the man\n\n";
						write(worklist.get(0),s);
						gui.SetTaxiStatus(name,new Point(line-1,list-1),status);
						worklist.remove(1);
						int sleep_time=random.getsleeptime1000();
						if(sleep_time<0){
							System.out.println("foolish work!");
						}
						else{
							sleep(sleep_time);
						}

//						System.out.println("No."+name+" begin to go to the finish point!");
						s = "Driver driving to the destination!\nthe path is:\n";
						write(worklist.get(0),s);
						status=1;
						gui.SetTaxiStatus(name,new Point(line-1,list-1),status);
					}
					
					
					

				}
				else{//若尚没有抢到单
					if(status ==0){
						status=2;
						gui.SetTaxiStatus(name,new Point(line-1,list-1),status);
					}
					working=false;
					/*枚举类型1
					while(true){//枚举前进
						sleep(10);
						int chess=random.get_random(4);
						if(chess==1 && map.can_up(line,list)){
							just_a_rest=new Syntagm(line-1,list);
							break;
						}
						else if(chess==2 && map.can_down(line,list)){
							just_a_rest=new Syntagm(line+1,list);
							break;
						}
						else if(chess==3 && map.can_left(line,list)){
							just_a_rest=new Syntagm(line,list-1);
							break;
						}
						else if(chess==4 && map.can_right(line,list)){
							just_a_rest=new Syntagm(line,list+1);
							break;
						}
					}*/
					//枚举类型2
					int seed = (map.can_up(line, list)?1:0)+
								(map.can_down(line, list)?1:0)+
								(map.can_left(line, list)?1:0)+
								(map.can_right(line, list)?1:0);
					int chess=random.get_random(seed);
					if(map.can_up(line,list)){
						chess--;
						if(chess==0){
							just_a_rest=new Syntagm(line-1,list);
						}
					}
					if(map.can_down(line,list)){
						chess--;
						if(chess==0){
							just_a_rest=new Syntagm(line+1,list);
						}
					}
					if(map.can_left(line,list)){
						chess--;
						if(chess==0){
							just_a_rest=new Syntagm(line,list-1);
						}
					}
					if(map.can_right(line,list)){
						chess--;
						if(chess==0){
							just_a_rest=new Syntagm(line,list+1);								
						}
					}
					if(chess>0){
						System.err.println("Isolated Island?");
						break;
					}
					
					

//					if(name==25)System.out.println(name+" 1 time: "+(System.currentTimeMillis()-Main.time)/100);
					line 	=	just_a_rest.getline();
					list	=	just_a_rest.getlist();
					
					int sleeptime=random.getsleeptime200();
					if(sleeptime<0){
						System.out.println("foolish work!");
					}
					else{
						sleep(sleeptime);
					}

					gui.SetTaxiStatus(name,new Point(line-1,list-1),status);
					

					feel_no_good++;
					if(feel_no_good==100 && relax){
// 						if(name==25)System.out.println(name+" 2 time: "+(System.currentTimeMillis()-Main.time)/100);
						feel_no_good=0;
						status =0;
						gui.SetTaxiStatus(name,new Point(line-1,list-1),status);
						working=false;
						sleeptime = random.getsleeptime1000();
						sleep(sleeptime);
					}
				}
				
				
			}
		}
		catch(Exception e){
			System.out.println("Something wrong with the driver No."+name+"!Perhaps He/She went to the toilet");
		}
	}
	public int getline(){
		return line;
	}
	public int getlist(){
		return list;
	}
	public void setshut(){
		this.shut=false;
	}
	public void setsf(SafeFile sf){
		this.sf=sf;
	}
	public void setmark(){
		this.mark++;
	}
	public void setrelax(boolean relax){
		feel_no_good=0;
		this.relax=relax;
	}
	public int getmark(){
		return mark;
	}
	public boolean get_working(){
		return working;
	}
	public void set_working(boolean working){
		this.working=working;
	}
	public void addlist(Syntagm work){
		worklist.add(work);
		working=true;
	}
	public int getname(){
		return name;
	}
	public int getstatus(){
		return status;
	}
	
	private void bfs(Syntagm order,boolean ctrl){
//		System.out.println("No."+name+" is in ("+line+","+list+") now!");
		int bfsmap[][]= new int[Readin.MAX+5][Readin.MAX+5];
		for(int i=0;i<=Readin.MAX;i++){
			for(int j=0;j<=Readin.MAX;j++){
				bfsmap[i][j]=-3;
			}
		}
		
		ArrayList<Syntagm> oldlist = new ArrayList<Syntagm>();
		ArrayList<Syntagm> newlist = new ArrayList<Syntagm>();
		
		
//		System.out.println("BFS FIRST SET!");
		int olinei,olisti;
		if(ctrl){
			olinei= order.get_orderline();
			olisti= order.get_orderlist();
		}
		else{
			olinei= order.getline();
			olisti= order.getlist();
		}

		oldlist.add(new Syntagm(olinei,olisti));
		System.out.println("No."+name+" is to ("+olinei+","+olisti+")");
		int value=0;
		bfsmap[olinei][olisti]=0;
		boolean flag=true;
		while(flag){
//			System.out.println("value:"+value);
			value++;
			if(line == olinei && list == olisti){
				flag=false;
				break;
			}
			newlist = new ArrayList<Syntagm>();
//			System.out.println("oldlist length:"+oldlist.size());
			for(int i=0;i<oldlist.size();i++){
				Syntagm point = oldlist.get(i);
				int linei = point.getline();
				int listi = point.getlist();
//				System.out.println("now ("+linei+","+listi+")");
				if(map.can_up(linei, listi)){
					if(bfsmap[linei-1][listi]==-3){
						bfsmap[linei-1][listi]=value;
						newlist.add(new Syntagm(linei-1,listi));
						if(line == linei-1 && list == listi){
							flag=false;
							break;
						}
					}
				}
				if(map.can_down(linei, listi)){
					if(bfsmap[linei+1][listi]==-3){
						bfsmap[linei+1][listi]=value;
						newlist.add(new Syntagm(linei+1,listi));
						if(line == linei+1 && list == listi){
							flag=false;
							break;
						}
					}
				}
				if(map.can_left(linei, listi)){
					if(bfsmap[linei][listi-1]==-3){
						bfsmap[linei][listi-1]=value;
						newlist.add(new Syntagm(linei,listi-1));
						if(line == linei && list == listi-1){
							flag=false;
							break;
						}
					}
				}
				if(map.can_right(linei, listi)){
					if(bfsmap[linei][listi+1]==-3){
						bfsmap[linei][listi+1]=value;
						newlist.add(new Syntagm(linei,listi+1));
						if(line == linei && list == listi+1){
							flag=false;
							break;
						}
					}
				}
			}
			oldlist.clear();
			oldlist=newlist;
		}
//		System.out.println("finish while flag");
		flag=true;
		int linei=line;
		int listi=list;
		while(flag){
//			System.out.println("linei "+linei+" listi "+listi);
			if(linei == olinei && listi == olisti){
				flag=false;
				break;
			}
			if(map.can_up(linei, listi)){
//				System.out.println("CAN UP!");
				if(bfsmap[linei-1][listi]==bfsmap[linei][listi]-1){
					worklist.add(new Syntagm(linei-1,listi));
					linei--;
					continue;
				}
			}
			if(map.can_down(linei, listi)){
//				System.out.println("CAN DOWN!");
				if(bfsmap[linei+1][listi]==bfsmap[linei][listi]-1){
					worklist.add(new Syntagm(linei+1,listi));
					linei++;
					continue;
				}
			}
			if(map.can_left(linei, listi)){
//				System.out.println("CAN LEFT");
				if(bfsmap[linei][listi-1]==bfsmap[linei][listi]-1){
					worklist.add(new Syntagm(linei,listi-1));
					listi--;
					continue;
				}
			}
			if(map.can_right(linei, listi)){
//				System.out.println("CAN RIGHT");
				if(bfsmap[linei][listi+1]==bfsmap[linei][listi]-1){
					worklist.add(new Syntagm(linei,listi+1));
					listi++;
					continue;
				}
			}
		}
		
		return;
		
	}
	
	
}
