package Taxi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/*
 * 			Overview
 * 
 * 			调度器类，每100ms一处理请求，每200ms更新权重地图
 * 					调度判断某指令应该分配给哪台出租车
 * 					集中调度所有出租车的run操作
 * */
public class Controller extends Thread{
	public int request_num=0;
	public static int time=0;
	public Map map;
	public Driver[] driver_list;
	final Random random = new Random();
	final public boolean [] driver_free = new boolean[102];
	final public ArrayList<Syntagm> taxi_list = new ArrayList<Syntagm>();		//	可抢单出租车队列
	final public ArrayList<ArrayList<Syntagm>> request_list = new ArrayList<ArrayList<Syntagm>>();
	
	/*不变式：
	 * 1	request_num>=0
	 * 2	time>=0
	 * 3	map!=NULL && map.repOK()
	 * 4	driver_list!=null 且为Driver型数组，其中所有项均为Driver类型，均满足Driver.repOK()
	 * 5	random!=null，且为Random型变量，满足Random.repOK()
	 * 6	driver_free！=null且为布尔型数组
	 * 7		taxi_list不为Null，是子项全不为Null，且都是Syntagm类型变量的ArrayList
	 * 8		taxi_list的子项a全都满足
	 *				1<=a.line<=80
	 *				1<=a.list<=80
	 *				0<=a.name<=99
	 * 9		request_list不为Null，是子项全都是ArrayList类型数组的ArrayList
	 *			request_list的子项都不为Null，是子项全都是不为Null的Syntagm型变量的ArrayList
	 * 10		request_list的子项的子项a全都满足
	 *				1<=a.line<=80
	 *				1<=a.list<=80
	 *				1<=a.orderline<=80
	 *				1<=a.orderlist<=80
	 *				0<=a.ordertime
	 * */
	public boolean repOK(){
		/*@Effects: \result==invariant(this).*/
		try{

			if(null==map || null==driver_list
			|| null==driver_free || null==taxi_list || null==request_list){
				return false;
			}
			for(int i=0;i<taxi_list.size();i++){
				if(taxi_list.get(i)==null){
					return false;
				}
			}
			for(int i=0;i<request_list.size();i++){
				if(request_list.get(i)==null){
					return false;
				}
				for(int j=0;j<request_list.get(i).size();j++){
					if(request_list.get(i).get(j)==null){
						return false;
					}
				}
			}
			if(!(map instanceof Map)
			|| !(driver_list instanceof Driver[])
			|| !(driver_free instanceof boolean[])
			|| !(taxi_list instanceof ArrayList)
			|| !(request_list instanceof ArrayList)){
				return false;
			}
			for(int i=0;i<100;i++){
				if(!(driver_list[i] instanceof Driver)){
					return false;
				}
			}
			for(int i=0;i<taxi_list.size();i++){
				if(!(taxi_list.get(i) instanceof Syntagm)){
					return false;
				}
			}
			for(int i=0;i<request_list.size();i++){
				if(!(request_list.get(i) instanceof ArrayList)){
					return false;
				}
				for(int j=0;j<request_list.get(i).size();j++){
					if(!(request_list.get(i).get(j) instanceof Syntagm)){
						return false;
					}
				}
			}
			if(request_num<0){
				return false;
			}
			if(time<0){
				return false;
			}
			for(int i=0;i<100;i++){
				if(!driver_list[i].repOK()){
					return false;
				}
			}
			for(int i=0;i<taxi_list.size();i++){
				Syntagm s =taxi_list.get(i);
				if(s.line<1 || s.line>80
				|| s.list<1 || s.list>80
				|| s.name<0 || s.name>99){
					return false;
				}
			}
			for(int i=0;i<request_list.size();i++){
				for(int j=0;j<request_list.get(i).size();j++){
					Syntagm s = request_list.get(i).get(j);
					if(s.line>80 || s.line<1 
					|| s.list>80 || s.list<1
					|| s.orderline>80 || s.orderline<1
					|| s.orderlist>80 || s.orderlist<1
					|| s.ordertime<0){
						return false;
					}
				}
			}
			return true;
		}catch(Exception e){
			return false;
		}
	}
	
	public void run(){
		/* @ REQUIRES: this中的属性均满足相应类的repOK，均不为Null
		 * @ MODIFIES: Main.reset,this,map
		 * @ EFFECTS:  10ms一刷新,time记录的为10ms为单位的准确时间
		 * 				如果地图被修改(即Main.reset=true)==>所有出租车当前预存路线被重置
		 * 				每200ms==>更新地图权重
		 * 				每100ms==>更新指令(具体请见request_deal方法，大致如下)
		 * 						if(刚刚发出请求)==>输出4*4的出租车信息
		 * 				
		 * 						if(发出没到3s)==>判断有没有新的可以抢单的出租车，加入到a的队列中
		 * 						else==>从请求发出地点开始广度优先遍历，找到所有可以接单的出租车中信用最高的
		 * 								找到了	==>将该单摊派给该出租车
		 * 								没找到	==>输出叫车失败的信息
		 * 				每10ms==>更新出租车状态(具体请见两个出租车类中的run方法)
		 * 
		 * 				通过sleep睡眠补全10ms周期差值
		 */
		try{
			while(true){
				random.getsleeptime10();
				sleep(1);
				if(Main.reset){
					for(int i=0;i<100;i++){
						ArrayList<Syntagm> worklist = this.driver_list[i].worklist;
						if(this.driver_list[i].status==1 || this.driver_list[i].status==3){
							this.driver_list[i].taxi_flag=!this.driver_list[i].taxi_flag;
							for(int j=worklist.size()-1;j>0;j--){
								worklist.remove(j);
							}
						}
					}
					Main.reset=false;
				}
				
				if(time%200 == 0){
					map.refreshmapweight();
				}
				
				if(time%100 == 0){
					request_deal();
				}
				
				for(int i=0;i<100;i++){
					driver_list[i].run();
				}
				
				int sleeptime = random.getsleeptime10();
				if(sleeptime>0){
					sleep(sleeptime);
				}
				time+=10;
			}
		}
		catch(Exception e){
		}
	}
	
	public void addf_l(Syntagm request){
		/* @ REQUIRES: Syntagm request 
		 * 					&& request.path =="Detail.txt" 
		 * 					&& request.findword =="Taxi" 或  "Status" 
		 * 					&& 80>=request.findtaxi >=1	
		 * @ MODIFIES: none
		 * @ EFFECTS: (request.findword == "Taxi") ==> 输出查询的出租车x的信息到Detail.txt
		 * 			  (request.findword == "Status") ==> 输出所有处于x状态的出租车的信息到Detail.txt
		 */
		if(Controller.Create_File(request.path)==0){
			return;
		}
		
		if(request.findword.equals("Taxi")){
			Syntagm taxi = driver_list[request.findtaxi-1].get_taxi_information();
			
			String line1="The Detail of the Taxi No."+(request.findtaxi)+":\n";
			String line2="Time now is:"+(time)+"ms\n";
			String line3="The taxi is in ("+taxi.line+","+taxi.list+")\n";
			String line4="Status is:"+taxi.status+"\tMark is:"+taxi.mark+"\n\n";
			String s = line1 +line2+line3+line4;
			write(request,s);
		}
		else if(request.findword.equals("Status")){
			boolean flag=true;
			String s = "The taxis in Status "+request.findtaxi+" are:\nTime now is:"+(time)+"ms\n";
			write(request,s);
			for(int i=0;i<100;i++){
				Syntagm taxi = driver_list[i].get_taxi_information();
				if(taxi.status==request.findtaxi){
					flag=false;
					String line2 = "Taxi No."+(i)+":\t in ("+taxi.line+","+taxi.list+")\t";
					String line4="Status is:"+taxi.status+"\tMark is:"+taxi.mark+"\n";
					s = line2+line4;
					write(request,s);
				}
			}
			if(flag){
				s = "No Taxi in this Status!\n\n";
				write(request,s);
			}
			else{
				s="\n";
				write(request,s);
			}
		}
		else if(request.findword.equals("History0")){
			try{
				driver_list[request.findtaxi].History_Forword();
			}
			catch(Exception e){
			}
		}
		else if(request.findword.equals("History1")){
			try{
				driver_list[request.findtaxi].History_Reverse();;
			}
			catch(Exception e){
			}
		}
		else{
			return;
		}
	}
	public void addr_l(Syntagm s){
		/* @ REQUIRES: Syntagm s
		 * 			&& 1<=s.line<=80
		 * 			&& 1<=s.list<=80
		 * 			&& 1<=s.orderline<=80
		 * 			&& 1<=s.orderlist<=80
		 * 			&& 0<=s.ordertime
		 * @ MODIFIES: request_list
		 * @ EFFECTS: 把s作为一个新的动态数组的第0项，然后把这个动态数组加入到请求队列request_list末尾
		 */
		ArrayList<Syntagm> newlist = new ArrayList<Syntagm>();
		newlist.add(s); 
		request_list.add(newlist);
		request_num++;
		s.setpath(request_num);
	}
	
	
	
	
	public void setfreelist(){
		/* @ REQUIRES: none 
		 * @ MODIFIES: driver_free[]	taxi_list
		 * @ EFFECTS: 遍历所有的司机，存储所有没有任务且处于闲逛状态(Status=2)的出租车
		 * 			  driver_free[]从0到100存储出租车司机是否有空，true为有，false为无
		 * 			      为了方便其他方法调取使用，所以单独把有空的出租车司机存到taxi_list中
		 */
		try{
			taxi_list.clear();
			for(int i=0;i<100;i++){//存储所有空闲司机的当前状态
				Syntagm taxi = driver_list[i].get_taxi_information();
				if(taxi.status!=2 || !taxi.can_work){//如果正在忙就不用抢单了
					driver_free[i]=false;
					continue;
				}
				//如果不在忙就加入抢单队列
				driver_free[i]=true;
				taxi_list.add(new Syntagm(taxi.line,taxi.list,i));
			}
		}
		catch(Exception e){
			System.err.println("Something wrong with the set free list");
		}
		
	}
	
	
	
	
	
	
	/*request_list	0	指令1	抢单出租车1	出租车2	出租车3	……
	 * 
	 * 				1	指令2
	 * 
	 * 				2	指令3
	 *  
	 * */
	public void request_deal(){
		/* @ REQUIRES: any a == request_list.get(x).get(0) (x为合法的任意值)==>
		 * 					1<=a.line<=80
		 * 				&&	1<=a.list<=80
		 * 				&&	1<=a.orderline<=80
		 * 				&&	1<=a.orderlist<=80
		 * 				&&	0<=a.ordertime
		 * @ MODIFIES: request.list
		 * @ EFFECTS: any a == request_list.get(x).get(0) (x为合法的任意值)==>
		 * 				if(刚刚发出请求)==>输出4*4的出租车信息
		 * 				
		 * 				if(发出没到3s)==>判断有没有新的可以抢单的出租车，加入到a的队列中
		 * 				else==>从请求发出地点开始广度优先遍历，找到所有可以接单的出租车中信用最高的
		 * 						找到了	==>将该单摊派给该出租车
		 * 						没找到	==>输出叫车失败的信息
		 * 		
		 * 				以上所有请求处理信息将会被输出到Request_i.txt文件中，i为该请求为累计第几条请求
		 */
		setfreelist();//更新可抢单出租车队列
		for(int i=0;i<request_list.size();i++){//遍历所有指令
			
			//若刚刚发出请求，则输出周围4*4范围内出租车状态信息
			if(request_list.get(i).get(0).firstcall){
				Syntagm request = request_list.get(i).get(0);
				boolean flag=false;
				for(int j=0;j<100;j++){
					Syntagm taxi = driver_list[j].get_taxi_information();
					if(taxi.line>= request.line-2 && taxi.line<=request.line+2){
						if(taxi.list>= request.list-2 && taxi.list<=request.list+2){
							
							//写入操作
							flag=true;
								int file_right = Controller.Create_File(request.path);
								if(file_right==0){//文件不合法
									break;
								}
								else if(file_right == 1){//新建了文件
									String line1 = "The request "+request.ordernum+" is:\n\t	";
						            String line2 = "From ("+request.line+","+request.list+") To ("+request.orderline+","+request.orderlist+")\n";
									String line3 = "Calling time is:"+time+"ms\n\n";
									
						            String line4 = "At that time Taxis nearby:\n";
						            String line5 = "Driver No."+j+" is in ("+taxi.line+","+taxi.list+")\n"; 
									String line6 = "\tStatus:"+taxi.status+"\tMark:"+taxi.mark+"\n";
						            	 
						            String s=line1+line2+line3+line4+line5+line6;
						            
						            write(request,s);
								}
								else{//已有文件
									String line3 ="taxi No."+j+" is in ("+taxi.line+","+taxi.list+")\n"; 
									String line4 ="\tStatus:"+taxi.status+"\tMark:"+taxi.mark+"\n";
						            String s = line3+line4;
						            write(request,s);
								}
							
							
							
						}
					}
					
				}//end for
				if(!flag){
						int file_right = Controller.Create_File(request.path);
						if(file_right == 0){//文件不合法
						}
						else if(file_right == 1){//文件刚新建
							String line1 = "The request "+request.ordernum+" is:\n\t	";
				            String line2 = "From ("+request.line+","+request.list+") To ("+request.orderline+","+request.orderlist+")\n";
							String line3 = "Calling time is:"+time+"ms\n";
				            String line4 ="At that time Taxis nearby:\nNo car!\n\n"; 
				            String s=line1+line2+line3+line4;
				            write(request,s);
						}
						else{//文件已存在
							String line1 = "The request "+request.ordernum+" is:\n\t	";
				            String line2 = "From ("+request.line+","+request.list+") To ("+request.orderline+","+request.orderlist+")\n";
							String line3 = "Calling time is:"+time+"ms\n";
				            String line4 ="At that time Taxis nearby:\nNo car!\n\n"; 
				            String s=line1+line2+line3+line4;
				            
				            write(request,s);
						}
				}
				
				//输出抢单司机提示语
	            String line1 = "\nDriver hear the calling:\n";
	            String s=line1;
				write(request,s);
				
				
				request_list.get(i).get(0).firstcall=false;
			}//end a request first call!
			
			if(request_list.get(i).get(0).ordertime <= time-3000){//若指令r_l.get(i)已经发出请求3s
//				System.out.println("TIME "+time+" ordertime"+request_list.get(i).get(0).ordertime);
//				System.out.println("123???");
				boolean sat=false;
				int maxmark=-1;
				for(int j=1;j<request_list.get(i).size();j++){
					//遍历所有抢了此单的出租车,剔除正在工作的出租车,剔除信用较低的出租车
					int name = request_list.get(i).get(j).name;
					if(!driver_free[name]){
//						System.out.println("Because you are busy！ "+name);
						//如果出租车目前正在忙,则从队伍中剔除
						request_list.get(i).remove(j);
						j--;
						continue;
					}
//					else{
//						System.out.println("You are too lazy! "+name);
//					}
					Syntagm taxi = driver_list[name].get_taxi_information();
					if(taxi.mark<maxmark){
						request_list.get(i).remove(j);
						j--;
					}
					else if(taxi.mark>maxmark){
						maxmark=taxi.mark;
					}
				}
				
				for(int j=1;j<request_list.get(i).size();j++){
					//遍历所有抢单出租车，剔除所有信用低于最高信用的出租车
					int name = request_list.get(i).get(j).name;
					Syntagm taxi = driver_list[name].get_taxi_information();
					if(taxi.mark<maxmark){
						request_list.get(i).remove(j);
						j--;
					}
				}

				if(request_list.get(i).size()==1){
					//若没有一辆车应答叫号
					Syntagm request = request_list.get(i).get(0);

		            String line1 = "No driver hear the calling\n\n";
		            String s=line1;
					write(request,s);
					System.out.println("从("+request_list.get(i).get(0).line+","+request_list.get(i).get(0).list+
							")前往("+request_list.get(i).get(0).orderline+","+request_list.get(i).get(0).orderlist+")的乘客没打到车");
					request_list.remove(i);
					i--;
					continue;
				}
				
				int drivernum=-1;
				//遍历所有可抢单出租车到该点距离，找出最近距离，派单
				drivernum=bfs(request_list.get(i).get(0).line,request_list.get(i).get(0).list,request_list.get(i).size()-1,i);
//				System.out.println("the num "+drivernum);
				sat=(drivernum!=-1);
				
				if(!sat){
					System.out.println("从("+request_list.get(i).get(0).line+","+request_list.get(i).get(0).list+
							")前往("+request_list.get(i).get(0).orderline+","+request_list.get(i).get(0).orderlist+")的乘客没打到车");
					
					Syntagm request = request_list.get(i).get(0);
					
		            String line1 = "No driver recall answer the calling\n\n";
		            String s=line1;
					write(request,s);
					
				}
				request_list.remove(i);
				i--;
			}
			else{	//没到扔单时间，仍在不断扫描	
				
				for(int j=0;j<taxi_list.size();j++){
					Syntagm driverj = taxi_list.get(j);//第j号空闲司机的信息
					//如果司机正好在4*4格子里
					if(driverj.line>=request_list.get(i).get(0).line-2 &&	driverj.line<=request_list.get(i).get(0).line+2){
						if(driverj.list>=request_list.get(i).get(0).list-2 &&	driverj.list<=request_list.get(i).get(0).list+2){
							boolean judge=false;	
							//若未添加过该司机，则添加至i指令的队中
							for(int k=1;k<request_list.get(i).size();k++){
								if(request_list.get(i).get(k).name==driverj.name){
									judge = true;
									break;
								}
							}
							if(!judge){
//								System.out.println("the No."+driver.name+" add to the list!");

					            String line1 = "Driver No."+driverj.name+" add to the list!\n";
					            String s=line1;
								write(request_list.get(i).get(0),s);
								
								driver_list[driverj.name].hear_succeed();
								request_list.get(i).add(driverj);
							}
						}
					}
				}
				
			}
		}//end the biggest for
	}
	
	public int bfs(int line,int list,int taxinum,int rli){
		/* @ REQUIRES: 
		 * 			{
		 * 				(bfs之前已做的处理,我也不知道应该记录在哪里)
		 * 						遍历所有抢单出租车，找到空闲司机中信用最高的，将信用存储为临时变量max mark
		 * 						剔除所有信用不为max mark的出租车
		 * 						确保未被剔除的出租车至少有1辆
		 * 			}
		 * 			Point(x,y), Syntagm request 
		 * 				1<=line<=80
		 * 			&&	1<=list<=80
		 * 			&&	request_list.get(rli) is available
		 * 					a is available	==> 1<=a.line<=80
		 * 									&&	1<=a.list<=80
		 * 									&&	1<=a.orderline<=80
		 * 									&&	1<=a.orderlist<=80
		 * 									&&	0<=a.ordertime
		 * @ MODIFIES: none
		 * @ EFFECTS: 	
		 * 				
		 * 				从所有抢单出租车进行广度优先遍历，找到距离请求发出点最近距离，记为 min distance,车号记为min name
		 * 							==>	将该请求分配给min name
		 * 			
		 * 				分配成功	==> return 0
		 * 				分配失败	==>	return -1(出租车司机均另外有工作，出租车司机恰好开始休息等)
		 */
	
		if(taxinum<=0){
			return -1;
		}
		
		int min=6666;
		int minname=-1;
		
		int location_out = line*100+list;
		
		for(int i=1;i<request_list.get(rli).size();i++){
			Syntagm taxi = request_list.get(rli).get(i);
			if(min>driver_list[taxi.name].getdistance(location_out)){
				min = driver_list[taxi.name].getdistance(location_out);
				minname = taxi.name;
			}
		}
		if(minname == -1){
			return -1;
		}
		else{
			driver_list[minname].worklist.add(request_list.get(rli).get(0));
			return minname;
		}
	}
	
	
	
	synchronized public void write(Syntagm request,String s){
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
		try{
			
			FileWriter fileWritter = new FileWriter(myFilePath.getAbsoluteFile(),true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(s);
            bufferWritter.close();
            
		}catch(Exception e){
		}
	}
		
	
	public void setbfsmap(int bfsmap[][]){
		/* @ REQUIRES: bfsmap[x][y] 
		 * 				x>Readin.max
		 * 				y>Readin.max
		 * @ MODIFIES: bfsmap[][]
		 * @ EFFECTS: initial the bfsmap[][],把所有可能会被使用的项初始化为-1
		 */
		try{
			for(int i=1;i<=Readin.MAX;i++){
				for(int j=1;j<=Readin.MAX;j++){
					bfsmap[i][j]=-1;
				}
			}
		}catch(Exception e){
		}
	}
	
	public static int Create_File(String path){
		/* @ REQUIRES: String path
		 * @ MODIFIES: Some file
		 * @ EFFECTS: 核实path路径对应的文件是否合法
		 * 					若存在且为文件==>返回2
		 * 					若不存在==>新建该路径文件，并返回1
		 * 					若存在但为文件夹==>返回0
		 * 					其余情况(创建失败，读取失败等)==>返回0
		 */
		File myFilePath = new File(path);
		try{
			if(myFilePath.exists()){//若文件已存在
				if(myFilePath.isFile()){
					return 2;
				}
				else{
					System.err.println("文件夹 "+path+" 不可被写入值！");
					return 0;
				}
			}
			else{//文件不存在则创建一个文件
				try{
					myFilePath.createNewFile();
					return 1;
				}catch(Exception e){
					System.err.println("The File ["+path+"] create faild!");
					return 0;
				}
			}
		}catch(Exception e){
			return 0;
		}
	}
	
}
