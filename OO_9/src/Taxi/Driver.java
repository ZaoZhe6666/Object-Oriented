package Taxi;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class Driver extends Thread{
	public Random random = new Random();
	public TaxiGUI gui;
	public Map map;
	public boolean relax=true;
	public boolean flag1=true;
	public int mark=0;
	public int name=0;
	public int line=0;
	public int list=0;
	public int feel_no_good=0;
	public int status=2;
	public ArrayList<Syntagm> worklist = new ArrayList<Syntagm>();
	public boolean shut=true;
	public boolean working=false;
	/*	status value
	 * 		0	stop				(停车)
	 * 		2	waiting for serve	(空车跑)
	 * 		1	serving				(拉客)
	 * 		3	going to the man	(去接客人)
	 * */
	Driver(int name,int line,int list,Map map){
		/* @ REQUIRES: Point(x,y) Map map
		 * @ MODIFIES: none
		 * @ EFFECTS: initial the driver thread
		 */
		this.name=name;
		this.line=line;
		this.list=list;
		this.map=map;
	}
	public void move(int line,int list){
		/* @ REQUIRES: Point(x,y) 
		 * @ MODIFIES: none
		 * @ EFFECTS: modify the taxi's moving and sleep 200ms
		 */
		try{
			
			if(this.line==line){
				if(this.list>list){//如果往右边走
					map.setmapweight(line, list, 0, 1);
				}
				else if(this.list<list){//如果往左边走
					map.setmapweight(line, list-1, 0, 1);
				}
				else{//如果在原地不动
					//WTF?
				}
			}
			else{
				if(this.line<line){//向下走
					map.setmapweight(line, list, 1, 1);
				}
				else{//向上走
					map.setmapweight(line-1, list, 1, 1);
				}
			}
			int sleeptime=random.getsleeptime200();
			if(sleeptime<0){
				System.out.println("foolish work!");
			}
			else{
				sleep(sleeptime);
			}
			if(this.line==line){
				if(this.list>list){//如果往右边走
					map.setmapweight(line, list, 0, -1);
				}
				else if(this.list<list){//如果往左边走
					map.setmapweight(line, list-1, 0, -1);
				}
				else{//如果在原地不动
				}
			}
			else{
				if(this.line<line){//向下走
					map.setmapweight(line, list, 1, -1);
				}
				else{//向上走
					map.setmapweight(line-1, list, 1, -1);
				}
			}
			
			this.line=line;
			this.list=list;
		}
		catch(Exception e){
		}
	}
	public void write(Syntagm request,String s){
		/* @ REQUIRES: Syntagm request String s 
		 * @ MODIFIES: none
		 * @ EFFECTS: print the word s into the request's path
		 */
		File myFilePath = new File(request.path);
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
		/* @ REQUIRES: none
		 * @ MODIFIES: worklist
		 * @ EFFECTS: modify the taxi's working ststus.
		 * 				change it's status and moving to delete the worklist's character
		 */
		try{
			while(shut){
				random.getsleeptime200();
				sleep(10);
				if(status ==1){//服务乘客状态
					relax=false;
					feel_no_good=0;
					if(!flag1){
						flag1=true;
						bfs(worklist.get(0),true);
						String s = "("+line+","+list+")->";
						write(worklist.get(0),s);
//						System.out.println("No."+name+" bfs to the finish point succeed!");
//						for(int i=0;i<worklist.size();i++){
//							System.out.println(i+"---"+worklist.get(i).line+","+worklist.get(i).list);
//						}
					}
					
					working=true;
					
					if(worklist.size()>2){
						move(worklist.get(1).line,worklist.get(1).list);
						
						String s = "("+line+","+list+")->";
						write(worklist.get(0),s);
						
						gui.SetTaxiStatus(name,new Point(line-1,list-1),status);
						worklist.remove(1);
						continue;
					}
					if(worklist.size()==2){//到达目的地，停车下人

						move(worklist.get(1).line,worklist.get(1).list);
//						System.out.println("No."+name+" got to the order!");
						status = 0;
						
						String line1 = "("+line+","+list+")\n";
						String line2 = "Driver arrive  the destination and set the man\n\nRequest finished!";
						String s=line1+line2;

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
//						System.out.println("No."+name+" feel free!");
						relax=true;
						gui.SetTaxiStatus(name,new Point(line-1,list-1),status);
						working=false;
					}
					continue;
				}
				if(worklist.size()!=0){//抢到单去接乘客状态
					relax=false;
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

//						for(int i=0;i<worklist.size();i++){
//							System.out.println(i+"---"+worklist.get(i).line+","+worklist.get(i).list);
//						}

//						System.out.println("No."+name+" bfs the man's point succeed!");
					}
					working = true;
					if(worklist.size()>2){

						move(worklist.get(1).line,worklist.get(1).list);
						String s="("+line+","+list+")->";
						write(worklist.get(0),s);
						gui.SetTaxiStatus(name,new Point(line-1,list-1),status);
						worklist.remove(1);
						continue;
					}
					if(worklist.size()==2){//到达用户那里，停车接人
						move(worklist.get(0).line,worklist.get(0).list);
//						System.out.println("No."+name+" catching the customer!");
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
					
					//枚举前进

					ArrayList<Syntagm> seed = new ArrayList<Syntagm>();
					
					if(map.can_up(line, list)){//如果可以往上走
						seed.add(new Syntagm(line-1,list,0,guigv.GetFlow(line-1, list-1, line-2, list-1),0,0));
//						if(name==50)						System.out.println("up No."+name+" __"+seed.size());
					}
					if(map.can_down(line, list)){//如果可以向下走
						boolean flag=true;
						for(int i=0;i<seed.size();i++){
							if(seed.get(i).weight<guigv.GetFlow(line-1, list-1, line, list-1)){
								flag = false;
								break;
							}
							else if(seed.get(i).weight>guigv.GetFlow(line-1, list-1, line, list-1)){
								seed.remove(i);
								i--;
							}
						}
						if(flag){
							seed.add(new Syntagm(line+1,list,0,guigv.GetFlow(line-1, list-1, line, list-1),0,0));
						}
//						if(name==50)						System.out.println("down No."+name+" __"+seed.size());
					}
					if(map.can_left(line, list)){//如果可以向左走
						boolean flag=true;
						for(int i=0;i<seed.size();i++){
							if(seed.get(i).weight<guigv.GetFlow(line-1, list-1, line-1, list-2)){
								flag = false;
								break;
							}
							else if(seed.get(i).weight>guigv.GetFlow(line-1, list-1, line-1, list-2)){
								seed.remove(i);
								i--;
							}
						}
						if(flag){
							seed.add(new Syntagm(line,list-1,0,guigv.GetFlow(line-1, list-1, line-1, list-2),0,0));
						}

//						if(name==50)						System.out.println("left No."+name+" __"+seed.size());
					}
					if(map.can_right(line, list)){//如果可以向右走
						boolean flag=true;
						for(int i=0;i<seed.size();i++){
							if(seed.get(i).weight<guigv.GetFlow(line-1, list-1, line-1, list)){
								flag = false;
								break;
							}
							else if(seed.get(i).weight>guigv.GetFlow(line-1, list-1, line-1, list)){
								seed.remove(i);
								i--;
							}
						}
						if(flag){
							seed.add(new Syntagm(line,list+1,0,guigv.GetFlow(line-1, list-1, line-1, list),0,0));
						}

//						if(name==50)						System.out.println("right No."+name+" __"+seed.size());
					}
					if(seed.size()==0){
						System.out.println("Driver No."+name+" in a isolated island?");
						break;
					}
					int random_seed = random.get_random(seed.size())-1;
					
					Syntagm just_a_syntagm = seed.get(random_seed);
					
					move(just_a_syntagm.line,just_a_syntagm.list);
					gui.SetTaxiStatus(name,new Point(line-1,list-1),status);
					

					feel_no_good++;
					if(feel_no_good==100 && relax){
						feel_no_good=0;
						status =0;
						gui.SetTaxiStatus(name,new Point(line-1,list-1),status);
						working=false;
						int sleeptime = random.getsleeptime1000();
						sleep(sleeptime);
					}
				}
				
				
			}
		}
		catch(Exception e){
			System.out.println("Something wrong with the driver No."+name+"!Perhaps He/She went to the toilet");
		}
	}
	public void addlist(Syntagm work){
		/* @ REQUIRES: Syntagm work
		 * @ MODIFIES: worklist
		 * @ EFFECTS: add something to the worklist
		 */
		worklist.add(work);
		working=true;
	}
	
	public void bfs(Syntagm order,boolean ctrl){
		/* @ REQUIRES: Syntagm order,boolean ctrl 
		 * @ MODIFIES: worklist
		 * @ EFFECTS: if(ctrl==true) from the callint point to bfs and get way to the taxi's point
		 * 			  else from the calling point to bfs and get the way to order point
		 */			
//		System.out.println("BFS BEFORE");
		int bfsmap[][]= new int[Readin.MAX+5][Readin.MAX+5];
		int weightmap[][] = new int[Readin.MAX+5][Readin.MAX+5];
		for(int i=0;i<=Readin.MAX;i++){
			for(int j=0;j<=Readin.MAX;j++){
				bfsmap[i][j]=-3;
				weightmap[i][j]=6666;
			}
		}
		
		ArrayList<Syntagm> oldlist = new ArrayList<Syntagm>();
		ArrayList<Syntagm> newlist = new ArrayList<Syntagm>();
		
		
		int olinei,olisti;
		if(ctrl){
			olinei= order.orderline;
			olisti= order.orderlist;
		}
		else{
			olinei= order.line;
			olisti= order.list;
		}

		oldlist.add(new Syntagm(olinei,olisti));
//		System.out.println("No."+name+" is to ("+olinei+","+olisti+")");
		int value=0;
		bfsmap[olinei][olisti]=0;
		weightmap[olinei][olisti]=0;
		boolean flag=true;
		while(flag){
			value++;
			if(line == olinei && list == olisti){
				flag=false;
				break;
			}
			if(oldlist.size()==0){
				System.out.println("Do not Connected Gragh?");
				return;
			}
			newlist = new ArrayList<Syntagm>();
			for(int i=0;i<oldlist.size();i++){
				Syntagm point = oldlist.get(i);
				int linei = point.line;
				int listi = point.list;
				if(map.can_up(linei, listi)){
					int weight1 = weightmap[linei][listi];
					int weight2 = guigv.GetFlow(linei-1,listi-1,linei-2,listi-1);
					int weight3 = weightmap[linei-1][listi];
					
					if(bfsmap[linei-1][listi]==bfsmap[linei][listi]+1 || bfsmap[linei-1][listi]==-3){
						weightmap[linei-1][listi]=(weight1+weight2>weight3)?(weight3):(weight1+weight2);
					}
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
					int weight1 = weightmap[linei][listi];
					int weight2 = guigv.GetFlow(linei-1,listi-1,linei,listi-1);
					int weight3 = weightmap[linei+1][listi];

					if(bfsmap[linei+1][listi]==bfsmap[linei][listi]+1 || bfsmap[linei+1][listi]==-3){
						weightmap[linei+1][listi]=(weight1+weight2>weight3)?(weight3):(weight1+weight2);
					}
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
					int weight1 = weightmap[linei][listi];
					int weight2 = guigv.GetFlow(linei-1,listi-1,linei-1,listi-2);
					int weight3 = weightmap[linei][listi-1];
					

					if(bfsmap[linei][listi-1]==bfsmap[linei][listi]+1 || bfsmap[linei][listi-1]==-3){
						weightmap[linei][listi-1]=(weight1+weight2>weight3)?(weight3):(weight1+weight2);
					}
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
					int weight1 = weightmap[linei][listi];
					int weight2 = guigv.GetFlow(linei-1,listi-1,linei-1,listi);
					int weight3 = weightmap[linei][listi+1];
					
					if(bfsmap[linei][listi+1]==bfsmap[linei][listi]+1 || bfsmap[linei][listi+1]==-3){
						weightmap[linei][listi+1]=(weight1+weight2>weight3)?(weight3):(weight1+weight2);
					}
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
		flag=true;
		int linei=line;
		int listi=list;
		while(flag){
			if(linei == olinei && listi == olisti){
				flag=false;
				break;
			}
			
			int pipe_up		= 	guigv.GetFlow(linei-1,listi-1,linei-2,listi-1);
			int pipe_down 	= 	guigv.GetFlow(linei-1,listi-1,linei  ,listi-1);
			int pipe_left 	= 	guigv.GetFlow(linei-1,listi-1,linei-1,listi-2);
			int pipe_right 	= 	guigv.GetFlow(linei-1,listi-1,linei-1,listi  );
			
			if(map.can_up(linei, listi)){
//				System.out.println("CAN UP!");
				if(bfsmap[linei-1][listi]==bfsmap[linei][listi]-1){
					if(pipe_up+weightmap[linei-1][listi]==weightmap[linei][listi]){
						worklist.add(new Syntagm(linei-1,listi));
						linei--;
						continue;
					}
				}
			}
			if(map.can_down(linei, listi)){
//				System.out.println("CAN DOWN!");
				if(bfsmap[linei+1][listi]==bfsmap[linei][listi]-1){
					if(pipe_down+weightmap[linei+1][listi]==weightmap[linei][listi]){
						worklist.add(new Syntagm(linei+1,listi));
						linei++;
						continue;
					}
				}
			}
			if(map.can_left(linei, listi)){
//				System.out.println("CAN LEFT");
				if(bfsmap[linei][listi-1]==bfsmap[linei][listi]-1){
					if(pipe_left+weightmap[linei][listi-1]==weightmap[linei][listi]){
						worklist.add(new Syntagm(linei,listi-1));
						listi--;
						continue;
					}
				}
			}
			if(map.can_right(linei, listi)){
//				System.out.println("CAN RIGHT");
				if(bfsmap[linei][listi+1]==bfsmap[linei][listi]-1){
					if(pipe_right+weightmap[linei][listi+1]==weightmap[linei][listi]){
						worklist.add(new Syntagm(linei,listi+1));
						listi++;
						continue;
					}
				}
			}
		}
//		System.out.println("BFS CHENGGONEL");
		return;
		
	}
	
	
}
