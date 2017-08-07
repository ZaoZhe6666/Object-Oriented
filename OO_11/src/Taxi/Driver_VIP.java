package Taxi;

import java.awt.Point;
import java.util.ArrayList;
import java.util.ListIterator;
/*
 * 		Overview
 * 
 * 		模拟一台超级出租车,接受来自Controller摊派的指令
 * 		由于可以无视删除的道路，所以bfs等操作使用的地图是road_map_vip而非road_map
 * 
 * */
public class Driver_VIP extends Driver{
	private ArrayList<StringBuffer> history = new ArrayList<StringBuffer>();
	
	/*不变式：
	 *1	history !=null,且为ArrayList类型，其中所有的项都是不为null的StringBuffer类型
	 */
	public boolean repOK(){
		/*@Effects: \result==invariant(super).*/
		try{
			if(history == null || !(history instanceof ArrayList)){
				return false;
			}
			for(int i=0;i<history.size();i++){
				if(history.get(i)==null){
					return false;
				}
				if(!(history.get(i) instanceof StringBuffer)){
					return false;
				}
			}
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	Driver_VIP(TaxiGUI gui,int name,Map map,Light light){
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
		super.name=name;
		super.gui=gui;
		super.map=map;
		super.light=light;
		int line=random.get_random(80);
		int list=random.get_random(80);
		gui.SetTaxiStatus(name,new Point(line-1,list-1),4);
		super.location		=line*100+list;
		super.last_location	=line*100+list;
		super.status		=2;
		super.mark		=0;
		super.nogood		=0;
		super.moveto		=0;
		super.waittime	=0;
		super.taxi_flag	=true;
	}
	
	
	public void History_Forword(){
		/* @ REQUIRES: repOK() && 来自父类的repOK
		 * @ MODIFIES: 	File History.txt
		 * @ EFFECTS:  	通过迭代器，将此台超级出租车的已完成请求的路线正序输出在文件中
		 */
		Syntagm just_a_syntagm = new Syntagm("",-1,"History.txt");
		if(Controller.Create_File("History.txt") == 0){
			System.out.println("迭代失败");
			return;
		}
		if(history.size()==0){
			String line = "Super Taxi No."+name+" hasn't finished any orders\n\n";
			System.out.println(line);
			write(just_a_syntagm,line);
			return;
		}
		ListIterator<StringBuffer> listIterator = history.listIterator();
		int i=0;
		String line1 = "Super Taxi No."+name+" has finished these requests(Forward the direction):\n";
		write(just_a_syntagm,line1);
		while(listIterator.hasNext()){
			i++;
			String line2 = "No."+i+" order:\n";
			String word = listIterator.next().toString();
			String line = line2+word+"\n";
			write(just_a_syntagm,line);
		}
		write(just_a_syntagm,"\n\n");
	}
	
	public void History_Reverse(){
		/* @ REQUIRES: repOK() && 来自父类的repOK
		 * @ MODIFIES: 	File History.txt
		 * @ EFFECTS:  	通过迭代器，将此台超级出租车的已完成请求的路线倒序输出在文件中
		 */
		Syntagm just_a_syntagm = new Syntagm("",-1,"History.txt");
		if(Controller.Create_File("History.txt") == 0){
			System.out.println("迭代失败");
			return;
		}
		if(history.size()==0){
			String line = "Super Taxi No."+name+" hasn't finished any orders";
			System.out.println(line);
			write(just_a_syntagm,line);
			return;
		}
		ListIterator<StringBuffer> listIterator = history.listIterator();
		int i=0;
		String line1 = "Super Taxi No."+name+" has finished these requests(Reverse the direction):\n";
		write(just_a_syntagm,line1);
		while(listIterator.hasNext()){
			i++;
			listIterator.next();
		}
		while(listIterator.hasPrevious()){
			String line2 = "No."+i+" order:\n";
			String word = listIterator.previous().toString();
			String line = line2+word+"\n";
			write(just_a_syntagm,line);
			i--;
		}
		write(just_a_syntagm,"\n\n");
	}
	
	
	
	
	
	
	public void run(){
		/* @ REQUIRES: 	repOK && 父类repOK
		 * @ MODIFIES: 父类属性，子类属性
		 * @ EFFECTS: 	在移动过程中==>继续移动
		 * 				到达某点==>判断工作列表是否有工作
		 * 					有工作==>继续前行 或 工作状态改变(后者可能为从去接客人，到客人开始上车。或送客人到目的地，到到达目的地)
		 * 						遇到红绿灯==>判断是否需要等待
		 * 							需要==>等待
		 * 							不需要==>移动
		 * 						工作流程记录在history中
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
					int line	= super.location/100;
					int list	= super.location%100;
					boolean flag= super.taxi_flag;
					
					if(super.waittime!=0){//只要在等待，就减周期继续等待
						super.waittime-=10;
						if(super.waittime!=0){//仍在等待则不继续进行下面操作了
							return;
						}
					}
					
					if(super.nogood == 100){//如果感到了无聊想休息了
						super.waittime+=1000;
						super.nogood=0;
						super.status=0;
						super.last_location=super.location;
						gui.SetTaxiStatus(name,new Point(line-1,list-1), 4);
						return;
					}
//					System.out.println("End wait!");
					while(super.waittime==0){
						if(super.moveto!=0){//若已存有移动指令，则移动
							//若移动轨迹存在拖曳(即不是原地停留后再运动)，则运行红绿灯停留方法。
							if(super.last_location!=super.location){
								see_the_light();
								continue;//continue后等红绿灯与否都会将last location更新到location位置
							}
							int judge =super.moveto-super.location;
							if(judge!= 100 && judge!=-100 &&judge!=1 && judge!=-1){//飞车判断
								System.out.println("No."+name+" is flying! "+judge);
								break;
							}
							//移动操作，更新位置
							super.last_location=super.location;
							super.location=super.moveto;
							line=super.moveto/100;
							list=super.moveto%100; 
							super.moveto=0;
							gui.SetTaxiStatus(name,new Point(line-1,list-1),4);
						}//end if(存在移动指令)
						
						if(super.status==1){//服务乘客状态
//							System.out.println("serving "+name);
							super.nogood=0;
							if(!flag){
								super.taxi_flag=true;
								bfs(worklist.get(0),true,line,list,worklist);
								super.waittime+=200;
							}
							
							
							else if(worklist.size()>1){//服务乘客且未到达
								super.moveto=worklist.get(1).line*100+worklist.get(1).list;
								move(super.moveto);
								super.waittime+=200;
								worklist.remove(1);
								
								String s = "("+line+","+list+")->";
								write(worklist.get(0),s);
								history.get(history.size()-1).append(s);
							}
							
							
							else if(worklist.size()==1){//到达目的地，停车下人
								
								super.waittime+=1000;
								super.last_location=super.location;
								super.status= 0;
								String line1 = "("+line+","+list+")\n";
								String line2 = "Driver arrive  the destination and set the man\n\nRequest finished!";
								String s=line1+line2;
								write(worklist.get(0),s);
								history.get(history.size()-1).append(s+"\n");
								gui.SetTaxiStatus(name,new Point(line-1,list-1),4);
								super.worklist.clear();
							}
						}//end if(status==1) 
						
						else if(worklist.size()!=0){//抢到单去接客状态
//							System.out.println("to catch the man "+name);
							super.nogood=0;
							if(flag){
								super.taxi_flag=false;
								
//								System.out.println("No."+i+" got to catch the customer!");
								String line1 = "No."+name+" got to catch the customer!\n";
								String line2 = "The path is:\n";
								String s=line1+line2;
								write(worklist.get(0),s);
								
								StringBuffer line3 = new StringBuffer("\nFrom ("+worklist.get(0).line+","+worklist.get(0).list+")"+
																"("+worklist.get(0).orderline+","+worklist.get(0).orderlist+")\n"+line2);
								history.add(line3);
								
								super.status= 3;
								gui.SetTaxiStatus(name,new Point(line-1,list-1),4);
								bfs(worklist.get(0),false,line,list,worklist);
							}
							
							if(worklist.size()>1){//未到达用户，还在前往
								super.moveto=worklist.get(1).line*100+worklist.get(1).list;
								move(super.moveto);
								worklist.remove(1);
								super.waittime+=200;

								String s="("+line+","+list+")->";
								write(worklist.get(0),s);
//								System.out.println("here?"+history.size());
								history.get(history.size()-1).append(s);
//								System.out.println("yeah!");
								
								break;
							}
							if(worklist.size()==1){//到达用户那里，停车接人
								if(super.status== 0){//已接到人，准备出发
									String s = "Driver driving to the destination!\nthe path is:\n";
									write(worklist.get(0),s);
									history.get(history.size()-1).append(s);
									super.status=1;
									gui.SetTaxiStatus(name,new Point(line-1,list-1),4);
									continue;
								}
								super.waittime+=1000;
								super.status=0;
								String s="("+line+","+list+")\n\n";
								write(worklist.get(0),s);
								history.get(history.size()-1).append(s);
								
								s = "Driver stop to catch the man\n\n";
								write(worklist.get(0),s);
								history.get(history.size()-1).append(s);
								gui.SetTaxiStatus(name,new Point(line-1,list-1),4);
							}
						}//end if(worklist.size!=0)
						
						else{//没抢到单
//							System.out.println("just free "+name);
//							System.out.println("line "+line+" list"+list);
							if(super.status== 0){
								super.status=2;
								gui.SetTaxiStatus(name,new Point(line-1,list-1), 4);
							}
							ArrayList<Syntagm> seed = new ArrayList<Syntagm>();
							if(map.can_up(line, list,map.road_map_vip)){//如果可以往上走
//								System.out.println("UP	"+map.getmapweight(line-1, list,1));
//								System.out.println("can up");
								seed.add(new Syntagm(line-1,list,0,map.getmapweight(line-1, list,1),0,0));
							}
							if(map.can_down(line, list,map.road_map_vip)){//如果可以向下走
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
							if(map.can_left(line, list,map.road_map_vip)){//如果可以向左走
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
							if(map.can_right(line, list,map.road_map_vip)){//如果可以向右走		
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
							super.moveto=just_a_syntagm.line*100+just_a_syntagm.list;
							move(super.moveto);
							super.waittime+=200;
							super.nogood++;
							break;
							
						}//end else
					
					}
				
				
		}
		catch(Exception e){
			System.out.println("Something wrong with Driver No."+name+" "+e);
		}
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
		 * 			&&	repOK() && 父类repOK
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
				if(map.can_up(linei, listi , map.road_map_vip)){
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
				if(map.can_down(linei, listi,map.road_map_vip)){
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
				if(map.can_left(linei, listi,map.road_map_vip)){
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
				if(map.can_right(linei, listi,map.road_map_vip)){
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
			
			if(map.can_up(linei, listi,map.road_map_vip)){
//				System.out.println("CAN UP!");
				if(bfsmap[linei-1][listi]==bfsmap[linei][listi]-1){
					if(pipe_up+weightmap[linei-1][listi]==weightmap[linei][listi]){
						worklist.add(new Syntagm(linei-1,listi));
						linei--;
						continue;
					}
				}
			}
			if(map.can_down(linei, listi,map.road_map_vip)){
//				System.out.println("CAN DOWN!");
				if(bfsmap[linei+1][listi]==bfsmap[linei][listi]-1){
					if(pipe_down+weightmap[linei+1][listi]==weightmap[linei][listi]){
						worklist.add(new Syntagm(linei+1,listi));
						linei++;
						continue;
					}
				}
			}
			if(map.can_left(linei, listi,map.road_map_vip)){
//				System.out.println("CAN LEFT");
				if(bfsmap[linei][listi-1]==bfsmap[linei][listi]-1){
					if(pipe_left+weightmap[linei][listi-1]==weightmap[linei][listi]){
						worklist.add(new Syntagm(linei,listi-1));
						listi--;
						continue;
					}
				}
			}
			if(map.can_right(linei, listi,map.road_map_vip)){
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
		/* @ REQUIRES: 	repOK && 父类repOK
		 * 			&&	1<=location_out/100<=80
		 * 			&&	1<=location_out%100<=80
		 * @ MODIFIES: Null
		 * @ EFFECTS: 	从当前出租车所在位置向外部传入的坐标位置进行广度优先遍历
		 * 				返回广度优先遍历得到的最短距离
		 * 				由于超级出租车可以走人为截断点，所以重写了该方法
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
		int line = super.location/100;
		int list = super.location%100;

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
				if(map.can_up(linei, listi , map.road_map_vip)){
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
				if(map.can_down(linei, listi,map.road_map_vip)){
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
				if(map.can_left(linei, listi,map.road_map_vip)){
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
				if(map.can_right(linei, listi,map.road_map_vip)){
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
	
	
	
	
}
