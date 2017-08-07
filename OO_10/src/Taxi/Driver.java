package Taxi;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
/*
 * 		Overview
 * 
 * 		模拟一百台出租车，接收存储由controller传输进的合法请求
 * 		每10ms遍历一次出租车队列并更新状态。所有出租车以减少自己工作区工作为目标
 * 
 * */
public class Driver extends Thread{
	public Light light;
	public Map map;
	public Controller ctrl;
	public static int time = 0;
	public TaxiGUI gui;
	public Random random = new Random();
	public boolean shut = true;
	final public int [] taxi_location = new int [105];
	final public int [] taxi_last_location = new int [105];
	final public int [] taxi_status	= new int [105];
	final public int [] taxi_mark		= new int [105];
	final public int [] taxi_nogood	= new int [105];
	final public int [] taxi_moveto	= new int [105];
	final public int [] taxi_waittime = new int [105];
	final public boolean [] taxi_flag = new boolean [105];
	final public ArrayList<ArrayList<Syntagm>> work_list = new ArrayList<ArrayList<Syntagm>>();
	
	/*不变式：
	 * 1	light为Light型变量,不为Null
	 * 2	map为Map型变量,不为Null
	 * 3	time>=0
	 * 4	ctrl为Controller型变量,不为Null
	 * 5	gui为TaxiGUI型变量,不为Null
	 * 6	random为Random型变量,不为Null
	 * 7	taxi_location不为Null，其中所有元素a均满足
	 * 				1<=a/100<=80
	 * 				1<=a%100<=80
	 * 8	taxi_last_location不为Null，其中所有元素a均满足
	 * 				1<=a/100<=80
	 * 				1<=a%100<=80
	 * 9	taxi_status不为Null，其中所有元素a均满足
	 * 				0<=a<=3
	 * 10	taxi_mark不为Null，其中所有元素a均满足
	 * 				0<=a
	 * 11	taxi_nogood不为Null，其中所有元素a均满足
	 * 				0<=a<=100
	 * 12	taxi_moveto不为Null，其中所有元素a均满足
	 * 				1<=a/100<=80
	 * 				1<=a%100<=80
	 * 13	taxi_waittime不为Null，其中所有元素a均满足
	 * 				0<=a<=1000
	 * 14	taxi_flag不为Null
	 * 15	work_list不为Null,是子项都是ArrayList类型数组的ArrayList
	 *		work_list的子项都不为Null,都是子项全都是Syntagm型变量的ArrayList
	 *		work_list的子项的第一个子项a满足
	 *				1<=a.line<=80
	 *				1<=a.list<=80
	 *				1<=a.orderline<=80
	 *				1<=a.orderlist<=80
	 *				0<=a.ordertime
	 *				0<=a.ordernum
	 *				a.path.equals("Request_"+a.ordernum+".txt")
	 *		其余项满足
	 *				1<=a.line<=80
	 *				1<=a.list<=80
	 */
	public boolean repOK(){
		/*@Effects: \result==invariant(this).*/
		try{
			if(light==null || map==null || ctrl==null || gui==null || random==null
			|| taxi_location==null || taxi_last_location==null || taxi_moveto==null
			|| taxi_status==null || taxi_mark==null || taxi_nogood==null 
			|| taxi_waittime==null || taxi_flag==null || work_list==null){
				return false;
			}
			for(int i=0;i<work_list.size();i++){
				if(null==work_list.get(i)){
					return false;
				}
				for(int j=0;j<work_list.get(i).size();j++){
					if(work_list.get(i).get(j)==null){
						return false;
					}
				}
			}
			if(!(light instanceof Light)
			|| !(map instanceof Map)
			|| !(ctrl instanceof Controller)
			|| !(gui instanceof TaxiGUI)
			|| !(random instanceof Random)
			|| !(taxi_location instanceof int[])
			|| !(taxi_last_location instanceof int[])
			|| !(taxi_status instanceof int[])
			|| !(taxi_mark instanceof int[])
			|| !(taxi_nogood instanceof int[])
			|| !(taxi_moveto instanceof int[])
			|| !(taxi_waittime instanceof int[])
			|| !(taxi_flag instanceof boolean[])
			|| !(work_list instanceof ArrayList)){
				return false;
			}
			for(int i=0;i<work_list.size();i++){
				if(!(work_list.get(i) instanceof ArrayList)){
					return false;
				}
				for(int j=0;j<work_list.get(i).size();j++){
					if(!(work_list.get(i).get(j) instanceof Syntagm)){
						return false;
					}
				}
			}
			if(time<0){
				return false;
			}
			for(int i=0;i<100;i++){
				int line;
				int list;
				line = taxi_location[i]/100;
				list = taxi_location[i]%100;
				if(line<1 || line>80 || list<1 || list>80){
					return false;
				}
				line = taxi_last_location[i]/100;
				list = taxi_last_location[i]%100;
				if(line<1 || line>80 || list<1 || list>80){
					return false;
				}
				if(taxi_status[i]>3 || taxi_status[i]<0){
					return false;
				}
				if(taxi_mark[i]<0){
					return false;
				}
				if(taxi_nogood[i]<0 || taxi_nogood[i]>100){
					return false;
				}
				line = taxi_moveto[i]/100;
				list = taxi_moveto[i]%100;
				if(taxi_moveto[i]!=0 && (line<1 || line>80 || list<1 || list>80)){
					return false;
				}
				if(taxi_waittime[i]<0 || taxi_waittime[i]>1000){
					return false;
				}
			}
			for(int i=0;i<work_list.size();i++){
				for(int j=0;j<work_list.get(i).size();j++){
					Syntagm s = work_list.get(i).get(j);
					if(j==0){
						if(s.line>80 || s.line<1 
						|| s.list>80 || s.list<1
						|| s.orderline>80 || s.orderline<1
						|| s.orderlist>80 || s.orderlist<1
						|| s.ordertime<0
						|| s.ordernum<0
						|| !(s.path.equals("Request_"+s.ordernum+".txt"))){
							return false;
						}
					}
					else{
						if(s.line>80 || s.line<1 
						|| s.list>80 || s.list<1){
							return false;
						}
					}
				}
			}
			
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	Driver(TaxiGUI gui){
		/* @ REQUIRES: TaxiGUI gui ==> gui!=null,下述的数组均不为null
		 * @ MODIFIES: 	gui 
		 * 				taxi_location[]
		 * 				taxi_last_location[]
		 * 				taxi_status[]
		 * 				taxi_mark[]
		 * 				taxi_nogood[]
		 * 				taxi_moveto[]
		 * 				taxi_waittime[]
		 * 				taxi_flag[]
		 * 				work_list
		 * @ EFFECTS: 初始化上述的这些数组，初始化Driver中的gui(用于后期和gui协调显示位置之类的操作)
		 * 				随机产生一个合法坐标(line,list)	
		 * 				location和last location均初始化为line*100+list
		 * 				出租车状态均初始化为2，添加每个出租车的工作列表到总的工作列表work_list中
		 * 				其余数组均初始化为0或真
		 * 				在gui上显示所有点初始位置
		 */
		this.gui=gui;
		for(int i=0;i<100;i++){
			int line=random.get_random(80);
			int list=random.get_random(80);
			taxi_location[i]	=line*100+list;
			taxi_last_location[i]=line*100+list;
			taxi_status[i]		=2;
			taxi_mark[i]		=0;
			taxi_nogood[i]		=0;
			taxi_moveto[i]		=0;
			taxi_waittime[i]	=0;
			taxi_flag[i]		=true;
			ArrayList<Syntagm> worklisti = new ArrayList<Syntagm>();
			work_list.add(worklisti);
		}
		for(int i=0;i<100;i++){
			int line = this.taxi_location[i]/100;
			int list = this.taxi_location[i]%100;
			gui.SetTaxiStatus(i,new Point(line-1,list-1), 2);
		}
	}
	public void write(Syntagm request,String s){
		/* @ REQUIRES: Syntagm request String s 
		 * 				request.path!=""
		 * 				File "request.path" is exists
		 * @ MODIFIES: none
		 * @ EFFECTS: 将字符串s写入到request所指向的路径中，所以只要求request所指向的路径合法，且该文件存在
		 * @ THREAD REQUIRES:	NONE
		 * @ THREAD EFFECTS:	锁住request.path所对应的文件(一次只有一个线程在修改他)
		 * 						\lock(File(request.path))
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
	
	
	public void hear_succeed(int i){
		/* @ REQUIRES: int i==> 
		 * 				0<=i<=99
		 * 			&&	taxi_mark[]!=null
		 * @ MODIFIES: taxi_mark[]
		 * @ EFFECTS: 抢到单的出租车的信用值加一，即taxi_mark[i]++
		 */
		this.taxi_mark[i]++;
	}
	
	
	public void getwork(Syntagm request,int i){
		/* @ REQUIRES: 		1<=request.line<=80
		 * 				&&	1<=request.list<=80
		 * 				&&	1<=request.orderline<=80
		 * 				&&	1<=request.orderlist<=80
		 * 				&&	0<=request.ordertime
		 * 				&&	0<=i<=99
		 * 				&&	work_list!=null
		 * @ MODIFIES: work_list
		 * @ EFFECTS: 将请求request加入到出租车i的工作队列中
		 */
		System.out.println("The "+i+" get the work!");
		this.work_list.get(i).add(request);
	}
	
	
	
	public void move(int i,int location){
		/* @ REQUIRES: 	1<=i<=80
		 * 			&&	101<=location<=8080
		 * 			&&  map!=null
		 * 			&&	map中的weight_map可装下180*80的内容
		 * 			&&	第i辆出租车所在的位置和location所指向的位置的距离为1，且直接可达
		 * @ MODIFIES: map中的weight_map[][]
		 * @ EFFECTS: 将第i辆出租车的位置由i更新为location
		 */
		try{
			int line0 = this.taxi_location[i]/100;
			int list0 = this.taxi_location[i]%100;
			int line1 = location/100;
			int list1 = location%100;
			if(line0==line1){
				if(list1>list0){//如果往右边走
					map.setmapweight(line0, list0, 0, 1);
				}
				else if(list1<list0){//如果往左边走
					map.setmapweight(line1, list1, 0, 1);
				}
				else{//如果在原地不动
					//WTF?
				}
			}
			else{
				if(line0<line1){//向下走
					map.setmapweight(line0, list0, 1, 1);
				}
				else{//向上走
					map.setmapweight(line1, list1, 1, 1);
				}
			}
		}
		catch(Exception e){
		}
	}
	
	
	
	public void run(){
		/* @ REQUIRES: 	类属性均不为null
		 * 				work_list中每一子项的每一子项a均为有效
		 * 					其中若a为第一项需满足
		 * 					1<=a.line<=80
		 * 				&&	1<=a.list<=80
		 * 				&&	1<=a.orderline<=80
		 * 				&&	1<=a.orderlist<=80
		 * 				&&	0<=a.ordertime
		 * 					否则a需要满足
		 * 					1<=a.line<=80
		 * 				&&	1<=a.list<=80
		 * 
		 * @ MODIFIES: work_list，time
		 * @ EFFECTS: 	每200ms更新map中流量地图
		 * 				每100ms获取所有前100ms间controller中所收到请求指令
		 * 				每次刷新时判断是否有道路修改，若有，则更新所有的有工作的出租车
		 * 
		 * 				遍历所有出租车
		 * 					在移动过程中==>继续移动
		 * 					到达某点==>判断工作列表是否有工作
		 * 						有工作==>继续前行 或 工作状态改变(后者可能为从去接客人，到客人开始上车。或送客人到目的地，到到达目的地)
		 * 							遇到红绿灯==>判断是否需要等待
		 * 								需要==>等待
		 * 								不需要==>移动
		 * 									
		 * 						没有工作==>判断是否到20s了该休息了
		 * 							是==>休息，疲劳值清零
		 * 							不是
		 * 								==>疲劳值增加
		 * 								==>随机获得前进位置
		 * 								==>判断是否遇到了红灯需要等待
		 * 								是==>等待
		 * 								不是==>移动
		 * 				time+=10
		 */							
		try{
			while(shut){
//				System.out.println("Time is "+time);
				random.getsleeptime10();
				sleep(1);
				if(Main.reset){
					for(int i=0;i<100;i++){
						ArrayList<Syntagm> worklist = this.work_list.get(i);
						if(this.taxi_status[i]==1 || this.taxi_status[i]==3){
							this.taxi_flag[i]=!this.taxi_flag[i];
							for(int j=worklist.size()-1;j>0;j--){
								worklist.remove(j);
							}
						}
					}
					Main.reset=false;
				}
				if(time%200 == 0){
					map.refreshmaweight();
				}
				if(time%100 == 0){
					ctrl.request_deal();
				}
				for(int i=0;i<100;i++){
//					System.out.println(i+" is dealing");
//					System.out.println("No."+i+" Status "+this.taxi_status[i]);
					int line	= this.taxi_location[i]/100;
					int list	= this.taxi_location[i]%100;
					boolean flag= this.taxi_flag[i];
					ArrayList<Syntagm> worklist = this.work_list.get(i);
					if(this.taxi_waittime[i]!=0){//只要在等待，就减周期继续等待
						this.taxi_waittime[i]-=10;
						if(this.taxi_waittime[i]!=0){
							continue;
						}
					}
					if(this.taxi_nogood[i] == 100){
						this.taxi_waittime[i]+=1000;
						this.taxi_nogood[i]=0;
						this.taxi_status[i]=0;
						this.taxi_last_location[i]=this.taxi_location[i];
						gui.SetTaxiStatus(i,new Point(line-1,list-1), 0);
						continue;
					}
					while(this.taxi_waittime[i]==0){
						if(this.taxi_moveto[i]!=0){//若已存有移动指令，则移动
							//若移动轨迹存在拖曳(即不是原地停留后再运动)，则运行红绿灯停留方法。
							if(this.taxi_last_location[i]!=this.taxi_location[i]){
								see_the_light(i);
								continue;
							}
							int judge =this.taxi_moveto[i]-this.taxi_location[i];
							if(judge!= 100 && judge!=-100 &&judge!=1 && judge!=-1){
								System.out.println("No."+i+" is flying! "+judge);
								break;
							}
							this.taxi_last_location[i]=this.taxi_location[i];
							this.taxi_location[i]=this.taxi_moveto[i];
							line=this.taxi_moveto[i]/100;
							list=this.taxi_moveto[i]%100; 
							this.taxi_moveto[i]=0;
							gui.SetTaxiStatus(i,new Point(line-1,list-1),this.taxi_status[i]);
						}
						if(this.taxi_status[i]==1){//服务乘客状态
							this.taxi_nogood[i]=0;
							if(!flag){
								this.taxi_flag[i]=true;
								bfs(worklist.get(0),true,line,list,worklist);
								this.taxi_waittime[i]+=200;
								this.work_list.set(i, worklist);
								
								
							}
							
							
							else if(worklist.size()>1){//服务乘客且未到达
								this.taxi_moveto[i]=worklist.get(1).line*100+worklist.get(1).list;
								move(i,this.taxi_moveto[i]);
								this.taxi_waittime[i]+=200;
								worklist.remove(1);
								this.work_list.set(i,worklist);
								
								String s = "("+line+","+list+")->";
								write(worklist.get(0),s);
							}
							
							
							else if(worklist.size()==1){//到达目的地，停车下人
								
								this.taxi_waittime[i]+=1000;
								this.taxi_last_location[i]=this.taxi_location[i];
								this.taxi_status[i] = 0;
								String line1 = "("+line+","+list+")\n";
								String line2 = "Driver arrive  the destination and set the man\n\nRequest finished!";
								String s=line1+line2;
								write(worklist.get(0),s);
								gui.SetTaxiStatus(i,new Point(line-1,list-1),0);
								this.work_list.get(i).clear();
							}
						}//end if(status==1) 
						
						else if(worklist.size()!=0){//抢到单去接客状态
							this.taxi_nogood[i]=0;
							if(flag){
								this.taxi_flag[i]=false;
								
//								System.out.println("No."+i+" got to catch the customer!");
								String line1 = "No."+i+" got to catch the customer!\n";
								String line2 = "The path is:\n";
								String s=line1+line2;
								write(worklist.get(0),s);
								
								this.taxi_status[i] = 3;
								gui.SetTaxiStatus(i,new Point(line-1,list-1),3);
								bfs(worklist.get(0),false,line,list,worklist);
							}
							
							if(worklist.size()>1){//未到达用户，还在前往
								this.taxi_moveto[i]=worklist.get(1).line*100+worklist.get(1).list;
								move(i,this.taxi_moveto[i]);
								worklist.remove(1);
								this.work_list.set(i, worklist);
								this.taxi_waittime[i]+=200;

								String s="("+line+","+list+")->";
								write(worklist.get(0),s);
								
								break;
							}
							if(worklist.size()==1){//到达用户那里，停车接人
								if(this.taxi_status[i] == 0){//已接到人，准备出发
									String s = "Driver driving to the destination!\nthe path is:\n";
									write(worklist.get(0),s);
									this.taxi_status[i]=1;
									gui.SetTaxiStatus(i,new Point(line-1,list-1),1);
									continue;
								}
								this.taxi_waittime[i]+=1000;
								this.taxi_status[i]=0;
								String s="("+line+","+list+")\n\n";
								write(worklist.get(0),s);
								
								s = "Driver stop to catch the man\n\n";
								write(worklist.get(0),s);
								gui.SetTaxiStatus(i,new Point(line-1,list-1),0);
							}
						}//end if(worklist.size!=0)
						
						else{//没抢到单
//							System.out.println("line "+line+" list"+list);
							if(this.taxi_status[i] == 0){
								this.taxi_status[i]=2;
								gui.SetTaxiStatus(i,new Point(line-1,list-1), 2);
							}
							ArrayList<Syntagm> seed = new ArrayList<Syntagm>();
							
							if(map.can_up(line, list)){//如果可以往上走
//								System.out.println("UP	"+map.getmapweight(line-1, list,1));
//								System.out.println("can up");
								seed.add(new Syntagm(line-1,list,0,map.getmapweight(line-1, list,1),0,0));
							}
							if(map.can_down(line, list)){//如果可以向下走
//								System.out.println("DOWN	"+map.getmapweight(line, list,1));
//								System.out.println("can down");
								boolean flag1=true;
								for(int x=0;x<seed.size();x++){
									if(seed.get(x).weight<map.getmapweight(line, list,1)){
										flag1 = false;
										break;
									}
									else if(seed.get(x).weight>map.getmapweight(line, list,1)){
										seed.remove(x);
										x--;
									}
								}
								if(flag1){
									seed.add(new Syntagm(line+1,list,0,map.getmapweight(line, list,1),0,0));
								}
							}
							if(map.can_left(line, list)){//如果可以向左走
//								System.out.println("LEFT	"+map.getmapweight(line, list-1,0));
//								System.out.println("can left");
								boolean flag2=true;
								for(int x=0;x<seed.size();x++){
									if(seed.get(x).weight<map.getmapweight(line, list-1,0)){
										flag2 = false;
										break;
									}
									else if(seed.get(x).weight>map.getmapweight(line, list-1,0)){
										seed.remove(x);
										x--;
									}
								}
								if(flag2){
									seed.add(new Syntagm(line,list-1,0,map.getmapweight(line, list-1,0),0,0));
								}
							}
							if(map.can_right(line, list)){//如果可以向右走
//								System.out.println("right	"+map.getmapweight(line, list,0));
//								System.out.println("can right");
								boolean flag3=true;
								for(int x=0;x<seed.size();x++){
									if(seed.get(x).weight<map.getmapweight(line, list,0)){
										flag3 = false;
										break;
									}
									else if(seed.get(x).weight>map.getmapweight(line, list,0)){
										seed.remove(x);
										x--;
									}
								}
								if(flag3){
									seed.add(new Syntagm(line,list+1,0,map.getmapweight(line, list,0),0,0));
								}
							}
							if(seed.size()==0){
								System.out.println("Driver No."+i+" in a isolated island?");
								break;
							}
							int random_seed = random.get_random(seed.size())-1;
							
							Syntagm just_a_syntagm = seed.get(random_seed);
							this.taxi_moveto[i]=just_a_syntagm.line*100+just_a_syntagm.list;
							move(i,this.taxi_moveto[i]);
							this.taxi_waittime[i]+=200;

							this.taxi_nogood[i]++;
							break;
							
						}//end else
					
					}
				}//end for
				
				
				int sleeptime=random.getsleeptime10();
				if(sleeptime<=0){
				}
				else{
					sleep(sleeptime);
				}
				time+=10;
			}
		}
		catch(Exception e){
		}
	}
	
	
	public void see_the_light(int i){
		/* @ REQUIRES: int i==> 
		 * 				0<=i<=99
		 * 			&&	taxi_last_location[]!=null
		 * 			&&	taxi_location[]!=null
		 * 			&&	taxi_moveto[]!=null
		 * 			&&	light.light_map可装下80*80的int型信息
		 * 			&&	出租车上一位置(last_location)和当前位置(location)距离为1或0且直接可达
		 * 			&&	出租车目标移动位置(move to)和当前位置(location)距离为1且直接可达
		 * @ MODIFIES: taxi_waittime[],taxi_last_location[]
		 * @ EFFECTS:	根据出租车上一位置(last_location)，当前位置(location),目标移动位置(move to)的关系
		 * 				如果出租车上一位置(last_location)和当前位置(location)距离为0，即相同，则直接返回
		 * 				如果在前行方向恰好有红灯，或者要左拐，但是前行方向为绿灯，则taxi_waittime[i]+=红绿灯变化周期
		 * 				否则不做变化
		 * 
		 * 				所有操作结束后，last_location均更新到location位置上
		 */
		int line0 = this.taxi_last_location[i]/100;
		int list0 = this.taxi_last_location[i]%100;
		int line1 = this.taxi_location[i]/100;
		int list1 = this.taxi_location[i]%100;
		int line2 = this.taxi_moveto[i]/100;
		int list2 = this.taxi_moveto[i]%100;
		if(light.light_map[line1][list1]==0){
			this.taxi_last_location[i]=this.taxi_location[i];
			return;
		}
		if(light.clock){//南北方向存在禁行
			if(list0==list1 && list0==list2){//纯粹的南北方向运动
				this.taxi_waittime[i]+=Light.light_change_time;
			}
			else if(list0-1==list1 && line1+1==line2){//东->西方向前行欲左拐
				this.taxi_waittime[i]+=Light.light_change_time;
			}
			else if(list0+1==list1 && line1-1==line2){//西->东方向前行欲左拐
				this.taxi_waittime[i]+=Light.light_change_time;
			}
		}
		else{//东西方向存在禁行
			if(line0==line1 && line0==line2){//纯粹的东西方向运动
				this.taxi_waittime[i]+=Light.light_change_time;
			}
			else if(line0+1==line1 && list1+1==list2){//南->北方向前行欲左拐
				this.taxi_waittime[i]+=Light.light_change_time;
			}
			else if(line0-1==line1 && list1-1==list2){//北->南方向前行欲左拐
				this.taxi_waittime[i]+=Light.light_change_time;
			}
		}
		this.taxi_last_location[i]=this.taxi_location[i];
		return;
	}
	
	
	
	public void bfs(Syntagm order,boolean ctrl,int line,int list,ArrayList<Syntagm> worklist){
		/* @ REQUIRES:	1<=order.line<=80
		 * 			&&	1<=order.list<=80
		 * 			&&	1<=order.orderline<=80
		 * 			&&	1<=order.orderlist<=80
		 * 			&&	map!=null
		 * 			&&	map中weight_map可存储180*80的integer类型信息
		 * 			&&	1<=line<=80
		 * 			&&	1<=list<=80
		 * 			&&	worklist!=null
		 * @ MODIFIES: worklist
		 * @ EFFECTS:	ctrl==true	==>	从叫车的点开始广度优先遍历，找到到达接单出租车x的最优路径，存在x的worklist中
		 * 				ctrl==false ==> 从叫车的点开始广度优先遍历，找到到达目的地的最优路径，存在x的worklist中
		 * 
		 * 				若找到==>正常返回
		 * 				没找到==>输出疑似非连通图后返回
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
					int weight2 = map.getmapweight(linei-1, listi, 1);
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
					int weight2 = map.getmapweight(linei, listi, 1);
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
					int weight2 = map.getmapweight(linei, listi-1, 0);
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
					int weight2 = map.getmapweight(linei, listi, 0);
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
			
			int pipe_up		= 	map.getmapweight(linei-1, listi  , 1);
			int pipe_down 	= 	map.getmapweight(linei  , listi  , 1);
			int pipe_left 	= 	map.getmapweight(linei  , listi-1, 0);
			int pipe_right 	= 	map.getmapweight(linei  , listi  , 0);
			
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
	
	
	

	public Syntagm get_taxi_information(int n){
		/* @ REQUIRES: int n==> 
		 * 				0<=n<=99
		 * 			&&	taxi_location[n]!=null
		 * 			&&	taxi_mark[n]!=null
		 * 			&&	taxi_status[n]!=null
		 * 			&&	work_list.get(n) !=null
		 * @ MODIFIES: none
		 * @ EFFECTS:	/result =Syntagm(name,line,list,mark,status,working)
		 * 				用于外部查询出租车状态使用，将出租车的基本信息(代号,所处坐标,信用值,状态,是否有工作)放在一个结构体中传出
		 */
		int line = this.taxi_location[n]/100;
		int list = this.taxi_location[n]%100;
		int mark = this.taxi_mark[n];
		int status = this.taxi_status[n];
		int name = n;
		boolean working = (this.taxi_status[n]!=2 || this.work_list.get(n).size()!=0);
		Syntagm just_a_syntagm = new Syntagm(name,line,list,mark,status,working);
		return just_a_syntagm;
	}
	
}
