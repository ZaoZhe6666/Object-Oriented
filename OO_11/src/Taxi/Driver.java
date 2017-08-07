package Taxi;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
/*
 * 		Overview
 * 
 * 		模拟一台普通出租车,接受来自Controller摊派的指令
 * 		由于不可以无视删除的道路，所以bfs等操作使用的地图是road_map而非road_map_vip
 * 
 * */
public class Driver{
	public int name=-1;
	public Light light;
	public Map map;
	public TaxiGUI gui;
	public Random random = new Random();
	public boolean shut 		= true;
	public int location 		= 0;
	public int last_location 	= 0;
	public int status			= 0;
	public int mark				= 0;
	public int nogood			= 0;
	public int moveto			= 0;
	public int waittime 		= 0;
	public boolean  taxi_flag 	= true;
	final public ArrayList<Syntagm> worklist = new ArrayList<Syntagm>();
	
	/*不变式：
	 * 1	light为Light型变量,不为Null
	 * 2	map为Map型变量,不为Null
	 * 3	name>=-1
	 * 4	gui为TaxiGUI型变量,不为Null
	 * 5	random为Random型变量,不为Null
	 * 6	location满足
	 * 				1<=location/100<=80
	 * 				1<=location%100<=80
	 * 7	last_location满足
	 * 				1<=last_location/100<=80
	 * 				1<=last_location%100<=80
	 * 8	status满足
	 * 				0<=status<=3
	 * 9	mark满足
	 * 				0<=mark
	 * 10	nogood满足
	 * 				0<=nogood<=100
	 * 11	moveto满足
	 * 				1<=moveto/100<=80
	 * 				1<=moveto%100<=80
	 * 12	waittime满足
	 * 				0<=waittime<=1000
	 * 13	work_list不为Null,是子项都是Syntagm型变量的ArrayList
	 *		work_list的第一个子项a满足
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
			if(light == null || !light.repOK()
			|| map == null || map.repOK()
			|| gui==null
			|| random == null || random.repOK()
			|| worklist==null){
				return false;
			}
			if(name<-1){
				return false;
			}
			if(location/100 <1 || location/100>80
			|| location%100 <1 || location%100>80){
				return false;
			}
			if(last_location/100 <1 || last_location/100>80
			|| last_location%100 <1 || last_location%100>80){
				return false;
			}
			if(status<0 || status>3
			|| mark<0
			|| nogood<0 || nogood>100
			|| waittime<0 || waittime>1000){
				return false;
			}
			if(moveto/100 <1 || moveto/100>80
			|| moveto%100 <1 || moveto%100>80){
				return false;
			}
			
			for(int i=0;i<worklist.size();i++){
				Syntagm s = worklist.get(i);
				if(i==0){
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
			
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	Driver(){
	}
	Driver(TaxiGUI gui,int name,Map map,Light light){
		/* @ REQUIRES: gui.repOK() && map.repOK() && light.repOK() 
		 * 				&& 0<=name<100
		 * @ MODIFIES: 	this  源自父类的属性
		 * @ EFFECTS:  	初始化所有的属性
		 * 				初始化Driver中的gui(用于后期和gui协调显示位置之类的操作)
		 * 				随机产生一个合法坐标(line,list)	
		 * 				1<=line<=80
		 * 				1<=list<=80
		 * 				location和last location均初始化为line*100+list
		 * 				出租车状态均初始化为2
		 * 				创建每个出租车的工作列表worklist
		 * 				其余数组均初始化为0或真
		 * 				在gui上显示点初始位置
		 */
		this.name=name;
		this.gui=gui;
		this.map=map;
		this.light=light;
		int line=random.get_random(80);
		int list=random.get_random(80);
		gui.SetTaxiStatus(name,new Point(line-1,list-1),2);
		this.location		=line*100+list;
		this.last_location	=line*100+list;
		this.status		=2;
		this.mark		=0;
		this.nogood		=0;
		this.moveto		=0;
		this.waittime	=0;
		this.taxi_flag	=true;
	}
	
	
	public void write(Syntagm request,String s){
		/* @ REQUIRES: Syntagm request String s 
		 * 				request.path!=""
		 * 				File "request.path" is exists
		 * 				&&	repOK()
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
	
	
	public void hear_succeed(){
		/* @ REQUIRES: repOK()
		 * @ MODIFIES: mark
		 * @ EFFECTS: 抢到单的出租车的信用值加一，即mark++
		 */
		this.mark++;
	}
	
	
	public void getwork(Syntagm request){
		/* @ REQUIRES: 		1<=request.line<=80
		 * 				&&	1<=request.list<=80
		 * 				&&	1<=request.orderline<=80
		 * 				&&	1<=request.orderlist<=80
		 * 				&&	0<=request.ordertime
		 * 				&&	0<=i<=99
		 * 				&&	work_list!=null
		 * 				&&	repOK()
		 * @ MODIFIES: work list
		 * @ EFFECTS: 将请求request加入到出租车的工作队列中
		 */
		System.out.println("The "+name+" get the work!");
		this.worklist.add(request);
	}
	
	
	
	public void move(int location_out){//////////////////////////////////////////////////////////////
		/* @ REQUIRES: 	1<=location_out/100<=80
		 * 			&&	1<=location_out%100<=80
		 * 			&&	map!=null
		 * 			&&	map中的weight_map可装下180*80的内容
		 * 			&&	第i辆出租车所在的位置和location所指向的位置的距离为1，且直接可达
		 * 			&&	repOK()
		 * @ MODIFIES: map中的weight_map[][]
		 * @ EFFECTS: 出租车通过，更新道路权重
		 */
		try{
			int line0 = this.location/100;
			int list0 = this.location%100;
			int line1 = location_out/100;
			int list1 = location_out%100;
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
		 * 				work list中每一子项a均为有效
		 * 					其中若a为第一项需满足
		 * 					1<=a.line<=80
		 * 				&&	1<=a.list<=80
		 * 				&&	1<=a.orderline<=80
		 * 				&&	1<=a.orderlist<=80
		 * 				&&	0<=a.ordertime
		 * 					否则a需要满足
		 * 					1<=a.line<=80
		 * 				&&	1<=a.list<=80
		 * 				&&	repOK()
		 * @ MODIFIES: this
		 * @ EFFECTS: 	在移动过程中==>继续移动
		 * 				到达某点==>判断工作列表是否有工作
		 * 					有工作==>继续前行 或 工作状态改变(后者可能为从去接客人，到客人开始上车。或送客人到目的地，到到达目的地)
		 * 						遇到红绿灯==>判断是否需要等待
		 * 							需要==>等待
		 * 							不需要==>移动
		 * 								
		 * 					没有工作==>判断是否到20s了该休息了
		 * 						是==>休息，疲劳值清零
		 * 						不是
		 * 							==>疲劳值增加
		 * 							==>随机获得前进位置
		 * 							==>判断是否遇到了红灯需要等待
		 * 							是==>等待
		 * 							不是==>移动
		 */							
		try{
					int line	= this.location/100;
					int list	= this.location%100;
					boolean flag= this.taxi_flag;
					
					if(this.waittime!=0){//只要在等待，就减周期继续等待
						this.waittime-=10;
						if(this.waittime!=0){//仍在等待则不继续进行下面操作了
							return;
						}
					}
					
					if(this.nogood == 100){//如果感到了无聊想休息了
						this.waittime+=1000;
						this.nogood=0;
						this.status=0;
						this.last_location=this.location;
						gui.SetTaxiStatus(name,new Point(line-1,list-1), 0);
						return;
					}
					
					while(this.waittime==0){
						if(this.moveto!=0){//若已存有移动指令，则移动
							//若移动轨迹存在拖曳(即不是原地停留后再运动)，则运行红绿灯停留方法。
							if(this.last_location!=this.location){
								see_the_light();
								continue;//continue后等红绿灯与否都会将last location更新到location位置
							}
							int judge =this.moveto-this.location;
							if(judge!= 100 && judge!=-100 &&judge!=1 && judge!=-1){//飞车判断
								System.out.println("No."+name+" is flying! "+judge);
								break;
							}

							//移动操作，更新位置
							this.last_location=this.location;
							this.location=this.moveto;
							line=this.moveto/100;
							list=this.moveto%100; 
							this.moveto=0;
							gui.SetTaxiStatus(name,new Point(line-1,list-1),this.status);
						}//end if(存在移动指令)
						
						if(this.status==1){//服务乘客状态
//							System.out.println("serving "+name);
							this.nogood=0;
							if(!flag){
								this.taxi_flag=true;
								bfs(worklist.get(0),true,line,list,worklist);
								this.waittime+=200;
							}
							
							
							else if(worklist.size()>1){//服务乘客且未到达
								this.moveto=worklist.get(1).line*100+worklist.get(1).list;
								move(this.moveto);
								this.waittime+=200;
								worklist.remove(1);
								
								String s = "("+line+","+list+")->";
								write(worklist.get(0),s);
							}
							
							
							else if(worklist.size()==1){//到达目的地，停车下人
								
								this.waittime+=1000;
								this.last_location=this.location;
								this.status= 0;
								String line1 = "("+line+","+list+")\n";
								String line2 = "Driver arrive  the destination and set the man\n\nRequest finished!";
								String s=line1+line2;
								write(worklist.get(0),s);
								gui.SetTaxiStatus(name,new Point(line-1,list-1),0);
								this.worklist.clear();
							}
						}//end if(status==1) 
						
						else if(worklist.size()!=0){//抢到单去接客状态
//							System.out.println("to catch the man "+name);
							this.nogood=0;
							if(flag){
								this.taxi_flag=false;
								
//								System.out.println("No."+i+" got to catch the customer!");
								String line1 = "No."+name+" got to catch the customer!\n";
								String line2 = "The path is:\n";
								String s=line1+line2;
								write(worklist.get(0),s);
								
								this.status= 3;
								gui.SetTaxiStatus(name,new Point(line-1,list-1),3);
								bfs(worklist.get(0),false,line,list,worklist);
							}
							
							if(worklist.size()>1){//未到达用户，还在前往
								this.moveto=worklist.get(1).line*100+worklist.get(1).list;
								move(this.moveto);
								worklist.remove(1);
								this.waittime+=200;

								String s="("+line+","+list+")->";
								write(worklist.get(0),s);
								
								break;
							}
							if(worklist.size()==1){//到达用户那里，停车接人
								if(this.status== 0){//已接到人，准备出发
									String s = "Driver driving to the destination!\nthe path is:\n";
									write(worklist.get(0),s);
									this.status=1;
									gui.SetTaxiStatus(name,new Point(line-1,list-1),1);
									continue;
								}
								this.waittime+=1000;
								this.status=0;
								String s="("+line+","+list+")\n\n";
								write(worklist.get(0),s);
								
								s = "Driver stop to catch the man\n\n";
								write(worklist.get(0),s);
								gui.SetTaxiStatus(name,new Point(line-1,list-1),0);
							}
						}//end if(worklist.size!=0)
						
						else{//没抢到单
//							System.out.println("line "+line+" list"+list);
							if(this.status== 0){
								this.status=2;
								gui.SetTaxiStatus(name,new Point(line-1,list-1), 2);
							}
							ArrayList<Syntagm> seed = new ArrayList<Syntagm>();
							if(map.can_up(line, list,map.road_map)){//如果可以往上走
//								System.out.println("UP	"+map.getmapweight(line-1, list,1));
//								System.out.println("can up");
								seed.add(new Syntagm(line-1,list,0,map.getmapweight(line-1, list,1),0,0));
							}
							if(map.can_down(line, list,map.road_map)){//如果可以向下走
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
							if(map.can_left(line, list,map.road_map)){//如果可以向左走
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
							if(map.can_right(line, list,map.road_map)){//如果可以向右走
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
								System.out.println("Driver No."+name+" in a isolated island?");
								break;
							}
							int random_seed = random.get_random(seed.size())-1;
							Syntagm just_a_syntagm = seed.get(random_seed);
							this.moveto=just_a_syntagm.line*100+just_a_syntagm.list;
							move(this.moveto);
							this.waittime+=200;
							this.nogood++;
							break;
							
						}//end else
					
					}
				
				
		}
		catch(Exception e){
			System.out.println("Something wrong with Driver No."+name+" "+e);
		}
	}
	
	
	public void see_the_light(){////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		/* @ REQUIRES: 	light.light_map可装下80*80的int型信息
		 * 			&&	出租车上一位置(last_location)和当前位置(location)距离为1或0且直接可达
		 * 			&&	出租车目标移动位置(move to)和当前位置(location)距离为1且直接可达
		 * 			&&	repOK()
		 * @ MODIFIES: waittime,last_location
		 * @ EFFECTS:	根据出租车上一位置(last_location)，当前位置(location),目标移动位置(move to)的关系
		 * 				如果出租车上一位置(last_location)和当前位置(location)距离为0，即相同，则直接返回
		 * 				如果在前行方向恰好有红灯，或者要左拐，但是前行方向为绿灯，则waittime+=红绿灯变化周期
		 * 				否则不做变化
		 * 
		 * 				所有操作结束后，last_location均更新到location位置上
		 */
		int line0 = this.last_location/100;
		int list0 = this.last_location%100;
		int line1 = this.location/100;
		int list1 = this.location%100;
		int line2 = this.moveto/100;
		int list2 = this.moveto%100;
		if(light.light_map[line1][list1]==0){
			this.last_location=this.location;
			return;
		}
		else if(light.clock==true){//南北方向存在禁行
			if(list0==list1 && list0==list2){//纯粹的南北方向运动
				this.waittime+=Light.light_change_time;
			}
			else if(list0-1==list1 && line1+1==line2){//东->西方向前行欲左拐
				this.waittime+=Light.light_change_time;
			}
			else if(list0+1==list1 && line1-1==line2){//西->东方向前行欲左拐
				this.waittime+=Light.light_change_time;
			}
		}
		else{//东西方向存在禁行
			if(line0==line1 && line0==line2){//纯粹的东西方向运动
				this.waittime+=Light.light_change_time;
			}
			else if(line0+1==line1 && list1+1==list2){//南->北方向前行欲左拐
				this.waittime+=Light.light_change_time;
			}
			else if(line0-1==line1 && list1-1==list2){//北->南方向前行欲左拐
				this.waittime+=Light.light_change_time;
			}
		}
		this.last_location=this.location;
		return;
	}
	
	
	
	public void bfs(Syntagm order,boolean ctrl,int line,int list,ArrayList<Syntagm> worklist){
		/* @ REQUIRES:	1<=order.line<=80
		 * 			&&	1<=order.list<=80
		 * 			&&	1<=order.orderline<=80
		 * 			&&	1<=order.orderlist<=80
		 * 			&&	map!=null
		 * 			&&  map.repOK
		 * 			&&	map中weight_map可存储180*80的integer类型信息
		 * 			&&	1<=line<=80
		 * 			&&	1<=list<=80
		 * 			&&	worklist!=null
		 * 			&&	repOK()
		 * @ MODIFIES: worklist
		 * @ EFFECTS:	ctrl==true	==>	从叫车的点开始广度优先遍历，找到到达出租车的最优路径，存在worklist中
		 * 				ctrl==false ==> 从叫车的点开始广度优先遍历，找到到达目的地的最优路径，存在worklist中
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
				if(map.can_up(linei, listi , map.road_map)){
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
				if(map.can_down(linei, listi,map.road_map)){
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
				if(map.can_left(linei, listi,map.road_map)){
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
				if(map.can_right(linei, listi,map.road_map)){
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
			
			if(map.can_up(linei, listi,map.road_map)){
//				System.out.println("CAN UP!");
				if(bfsmap[linei-1][listi]==bfsmap[linei][listi]-1){
					if(pipe_up+weightmap[linei-1][listi]==weightmap[linei][listi]){
						worklist.add(new Syntagm(linei-1,listi));
						linei--;
						continue;
					}
				}
			}
			if(map.can_down(linei, listi,map.road_map)){
//				System.out.println("CAN DOWN!");
				if(bfsmap[linei+1][listi]==bfsmap[linei][listi]-1){
					if(pipe_down+weightmap[linei+1][listi]==weightmap[linei][listi]){
						worklist.add(new Syntagm(linei+1,listi));
						linei++;
						continue;
					}
				}
			}
			if(map.can_left(linei, listi,map.road_map)){
//				System.out.println("CAN LEFT");
				if(bfsmap[linei][listi-1]==bfsmap[linei][listi]-1){
					if(pipe_left+weightmap[linei][listi-1]==weightmap[linei][listi]){
						worklist.add(new Syntagm(linei,listi-1));
						listi--;
						continue;
					}
				}
			}
			if(map.can_right(linei, listi,map.road_map)){
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
	
	public int getdistance(int location_out){
		/* @ REQUIRES: 	repOK
		 * 			&&	1<=location_out/100<=80
		 * 			&&	1<=location_out%100<=80
		 * @ MODIFIES: Null
		 * @ EFFECTS: 	从当前出租车所在位置向外部传入的坐标位置进行广度优先遍历
		 * 				返回广度优先遍历得到的最短距离
		 */		
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

		olinei = location_out/100;
		olisti = location_out%100;
		int line = this.location/100;
		int list = this.location%100;

		oldlist.add(new Syntagm(olinei,olisti));

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
				return -1;
			}
			newlist = new ArrayList<Syntagm>();
			for(int i=0;i<oldlist.size();i++){
				Syntagm point = oldlist.get(i);
				int linei = point.line;
				int listi = point.list;
				if(map.can_up(linei, listi , map.road_map)){
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
				if(map.can_down(linei, listi,map.road_map)){
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
				if(map.can_left(linei, listi,map.road_map)){
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
				if(map.can_right(linei, listi,map.road_map)){
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
		
		
		return value;
	}
	
	public void History_Forword(){
		/* @ REQUIRES: 	repOK()
		 * @ MODIFIES: Null
		 * @ EFFECTS: 	由于普通出租车不支持迭代器访问，所以仅输出提示语
		 */	
		System.out.println("Taxi No."+name+" is a simple Taxi! He is Tired! But you do not care!");
		return;
	}
	
	public void History_Reverse(){
		/* @ REQUIRES: 	repOK()
		 * @ MODIFIES: Null
		 * @ EFFECTS: 	由于普通出租车不支持迭代器访问，所以仅输出提示语
		 */	
		System.out.println("Taxi No."+name+" is a simple Taxi! He is Tired! But you do not care!");
		return;
	}
	
	public Syntagm get_taxi_information(){
		/* @ REQUIRES: repOK()
		 * @ MODIFIES: none
		 * @ EFFECTS:	/result =Syntagm(name,line,list,mark,status,working)
		 * 				用于外部查询出租车状态使用，将出租车的基本信息(代号,所处坐标,信用值,状态,是否有工作)放在一个结构体中传出
		 */
		int line = this.location/100;
		int list = this.location%100;
		int mark = this.mark;
		int status = this.status;
		int name = this.name;
		boolean working = (this.status!=2 || this.worklist.size()!=0);
		Syntagm just_a_syntagm = new Syntagm(name,line,list,mark,status,working);
		return just_a_syntagm;
	}
	
}
