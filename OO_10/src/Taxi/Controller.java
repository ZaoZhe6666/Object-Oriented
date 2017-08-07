package Taxi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

/*
 * 			Overview
 * 
 * 			调度器类，每100ms将过去100ms中请求发送给司机类
 * 			发送请求前进行摊派审查，即判断是否有合适的出租车可完成摊派
 * */
public class Controller{
	public int request_num=0;
	public int time=0;
	public Map map;
	public Driver driver;
	final public boolean [] driver_free = new boolean[102];
	final public ArrayList<Syntagm> taxi_list = new ArrayList<Syntagm>();		//	可抢单出租车队列
	final public ArrayList<ArrayList<Syntagm>> request_list = new ArrayList<ArrayList<Syntagm>>();
	
	/*不变式：
	 *1 	request>=0
	 *2 	time>=0
	 *3		map是Map类型变量,不为Null
	 *4		driver是Driver类型变量,不为Null
	 *5		driver_free是布尔型数组,不为Null
	 *6		taxi_list不为Null，是子项全不为Null，且都是Syntagm类型变量的ArrayList
	 *7		taxi_list的子项a全都满足
	 *				1<=a.line<=80
	 *				1<=a.list<=80
	 *				0<=a.name<=99
	 *8		request_list不为Null，是子项全都是ArrayList类型数组的ArrayList
	 *		request_list的子项都不为Null，是子项全都是不为Null的Syntagm型变量的ArrayList
	 *9		request_list的子项的子项a全都满足
	 *				1<=a.line<=80
	 *				1<=a.list<=80
	 *				1<=a.orderline<=80
	 *				1<=a.orderlist<=80
	 *				0<=a.ordertime
	 * */
	public boolean repOK(){
		/*@Effects: \result==invariant(this).*/
		try{
			if(null==map || null==driver 
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
			|| !(driver instanceof Driver)
			|| !(driver_free instanceof boolean[])
			|| !(taxi_list instanceof ArrayList)
			|| !(request_list instanceof ArrayList)){
				return false;
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
	
	
	public void addf_l(Syntagm request){
		/* @ REQUIRES: Syntagm request 
		 * 					&& request.path =="Detail.txt" 
		 * 					&& request.findword =="Taxi" 或  "Status" 
		 * 					&& 80>=request.findtaxi >=1	
		 * @ MODIFIES: none
		 * @ EFFECTS: (request.findword == "Taxi") ==> 输出查询的出租车x的信息到Detail.txt
		 * 			  (request.findword == "Status") ==> 输出所有处于x状态的出租车的信息到Detail.txt
		 */
		File myFilePath = new File(request.path);
		try{
			if(!myFilePath.exists()){//若文件不存在则创建一个文件
				try{
					myFilePath.createNewFile();
				}catch(Exception e){
					System.err.println("The File ["+request.path+"] create faild!");
				}
			}
			else if(myFilePath.isDirectory()){
				System.err.println("存在同名文件夹，不可写入操作");
				return;
			}
		}catch(Exception e){
		}
		
		if(request.findword.equals("Taxi")){
			Syntagm taxi = driver.get_taxi_information(request.findtaxi-1);
			
			String line1="The Detail of the Taxi No."+(request.findtaxi)+":\n";
			String line2="Time now is:"+(time*100)+"ms\n";
			String line3="The taxi is in ("+taxi.line+","+taxi.list+")\n";
			String line4="Status is:"+taxi.status+"\tMark is:"+taxi.mark+"\n\n";
			String s = line1 +line2+line3+line4;
			write(request,s);
		}
		else if(request.findword.equals("Status")){
			boolean flag=true;
			String s = "The taxis in Status "+request.findtaxi+" are:\nTime now is:"+(time*100)+"ms\n";
			write(request,s);
			for(int i=0;i<100;i++){
				Syntagm taxi = driver.get_taxi_information(i);
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
				Syntagm taxi = driver.get_taxi_information(i);
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
		/* @ REQUIRES: any a in request_list==>
		 * 					1<=a.line<=80
		 * 				&&	1<=a.list<=80
		 * 				&&	1<=a.orderline<=80
		 * 				&&	1<=a.orderlist<=80
		 * 				&&	0<=a.ordertime
		 * @ MODIFIES: request.list
		 * @ EFFECTS: any a in request.list==>
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
		time+=1;
		for(int i=0;i<request_list.size();i++){//遍历所有指令
			
			//若刚刚发出请求，则输出周围4*4范围内出租车状态信息
			if(request_list.get(i).get(0).firstcall){
				Syntagm request = request_list.get(i).get(0);
				boolean flag=false;
				for(int j=0;j<100;j++){
					Syntagm taxi = driver.get_taxi_information(j);
					if(taxi.line>= request.line-2 && taxi.line<=request.line+2){
						if(taxi.list>= request.list-2 && taxi.list<=request.list+2){
							
							//写入操作
							flag=true;
							File myFilePath = new File(request.path);
							try{
								if(myFilePath.exists()){//若文件已存在
									if(myFilePath.isFile()){
										
										String line3 ="taxi No."+j+" is in ("+taxi.line+","+taxi.list+")\n"; 
										String line4 ="\tStatus:"+taxi.status+"\tMark:"+taxi.mark+"\n";
							            String s = line3+line4;
							            write(request,s);
									}
									else{
										System.err.println("["+request.path+"]文件夹不可被写入值！");
									}
								}
								else{//文件不存在则创建一个文件
									try{
										myFilePath.createNewFile();
									}catch(Exception e){
										System.err.println("The File ["+request.path+"] create faild!");
										continue;
									}
									
									String line1 = "The request "+request.ordernum+" is:\n\t	";
						            String line2 = "From ("+request.line+","+request.list+") To ("+request.orderline+","+request.orderlist+")\n";
									String line3 = "Calling time is:"+time*100+"ms\n\n";
									
						            String line4 = "At that time Taxis nearby:\n";
						            String line5 = "Driver No."+j+" is in ("+taxi.line+","+taxi.list+")\n"; 
									String line6 = "\tStatus:"+taxi.status+"\tMark:"+taxi.mark+"\n";
						            	 
						            String s=line1+line2+line3+line4+line5+line6;
						            
						            write(request,s);
						            
								}
							}catch(Exception e){
							}
							
							
							
						}
					}
					
				}//end for
				if(!flag){
					File myFilePath = new File(request.path);
					try{
						if(myFilePath.exists()){//若文件已存在
							if(myFilePath.isFile()){
								
					            String line1 = "The request "+request.ordernum+" is:\n\t	";
					            String line2 = "From ("+request.line+","+request.list+") To ("+request.orderline+","+request.orderlist+")\n";
								String line3 = "Calling time is:"+time*100+"ms\n";
					            String line4 ="At that time Taxis nearby:\nNo car!\n\n"; 
					            String s=line1+line2+line3+line4;
					            
					            write(request,s);
							}
							else{
								System.err.println("文件夹不可被写入值！");
							}
						}
						else{//文件不存在则创建一个文件
							try{
								myFilePath.createNewFile();
							}catch(Exception e){
								System.err.println("The File ["+request.path+"] create faild!");
							}
							
				            String line1 = "The request "+request.ordernum+" is:\n\t	";
				            String line2 = "From ("+request.line+","+request.list+") To ("+request.orderline+","+request.orderlist+")\n";
							String line3 = "Calling time is:"+time*100+"ms\n";
				            String line4 ="At that time Taxis nearby:\nNo car!\n\n"; 
				            String s=line1+line2+line3+line4;
				            write(request,s);
				            
						}
					}catch(Exception e){
					}
				}
				
				//输出抢单司机提示语
	            String line1 = "\nDriver hear the calling:\n";
	            String s=line1;
				write(request,s);
				
				
				request_list.get(i).get(0).firstcall=false;
			}//end a request first call!
			
			if(request_list.get(i).get(0).ordertime <= time-30){//若指令r_l.get(i)已经发出请求3s
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
					Syntagm taxi = driver.get_taxi_information(name);
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
					Syntagm taxi = driver.get_taxi_information(name);
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
				//从呼叫点request_list.get(i).get(0).getline   request_list.get(i).get(0).getlist开始bfs找到可搭乘出租车
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
								
								driver.hear_succeed(driverj.name);
								request_list.get(i).add(driverj);
							}
						}
					}
				}
				
			}
		}//end the biggest for
	}
	
	public int bfs(int line,int list,int taxinum,int rli){
		/* @ REQUIRES: Point(x,y), Syntagm request 
		 * 				1<=line<=80
		 * 			&&	1<=list<=80
		 * 			&&	request_list.get(rli) is available
		 * 					a is available	==> 1<=a.line<=80
		 * 									&&	1<=a.list<=80
		 * 									&&	1<=a.orderline<=80
		 * 									&&	1<=a.orderlist<=80
		 * 									&&	0<=a.ordertime
		 * @ MODIFIES: none
		 * @ EFFECTS: 	遍历所有抢单出租车，找到空闲司机中信用最高的，将信用存储为临时变量max mark
		 * 				从请求发出的点进行广度优先遍历，若找到抢单出租车x，且信用为max mark
		 * 							==>	将该请求分配给x
		 * 			
		 * 				分配成功	==> return 0
		 * 				分配失败	==>	return -1(出租车司机另外有工作，出租车司机恰好开始休息等)
		 */
		//rli 此单的序号
		//bfsold 用来存上次的所有点
		//bfsnew 用来存新增的所有点
		int [][] bfsmap=new int [Readin.MAX+5][Readin.MAX+5];
		ArrayList<Syntagm> bfsold = new ArrayList<Syntagm>();
		ArrayList<Syntagm> bfsnew = new ArrayList<Syntagm>();
		ArrayList<Syntagm> rightdriver = new ArrayList<Syntagm>();
		setbfsmap(bfsmap);
		int value=0;
		bfsmap[line][list]=0;
		bfsold.add(new Syntagm(line,list,0,1,1,1));
		boolean sat=false;
		if(taxinum<=0){
//			System.out.println("RETURN -1");
			return -1;
		}
		while(true){
			bfsnew=new ArrayList<Syntagm>(); 
			for(int t=0;t<bfsold.size();t++){
				value++;
				//main point当前bfs到点的详细信息
				int linei = bfsold.get(t).line;
				int listi = bfsold.get(t).list;
				if(map.can_up(linei, listi)){//该点能往上走
					if(bfsmap[linei-1][listi]==-1){//若上方的点尚未走过
						bfsmap[linei-1][listi]=value;//上方点距line list距离为value
						bfsnew.add(new Syntagm(linei-1,listi,value,1,1,1));//添加到下个循环中
						for(int x=1;x<request_list.get(rli).size();x++){
							Syntagm driverx = request_list.get(rli).get(x);//第x个抢到该请求的车
							if(driver_free[driverx.name]){//若该司机很清闲
								if(driverx.line!=linei-1 || driverx.list!=listi){
									continue;
								}
//								System.out.println("driverx.name "+driverx.name);
								rightdriver.add(driver.get_taxi_information(driverx.name));
								sat=true;
							}
							else{
								request_list.get(rli).remove(x);
								x--;
							}
						}
					}
				}
				if(map.can_down(linei, listi)){//can down
					if(bfsmap[linei+1][listi]==-1){//若下方的点尚未走过
						bfsmap[linei+1][listi]=value;//下方点距line list距离为value
						bfsnew.add(new Syntagm(linei+1,listi,value,1,1,1));//添加到下个循环中
						for(int x=1;x<request_list.get(rli).size();x++){
							Syntagm driverx = request_list.get(rli).get(x);//第x个抢到该请求的车
							if(driver_free[driverx.name]){//若该司机很清闲
								if(driverx.line!=linei+1 || driverx.list!=listi){
									continue;
								}
//								System.out.println("driverx.name "+driverx.name);
								rightdriver.add(driver.get_taxi_information(driverx.name));
								sat=true;
							}
							else{
								request_list.get(rli).remove(x);
								x--;
							}
						}
					}
				}
				if(map.can_left(linei, listi)){//can left
					if(bfsmap[linei][listi-1]==-1){//若左边的点尚未走过
						bfsmap[linei][listi-1]=value;//左边点距line list距离为value
						bfsnew.add(new Syntagm(linei,listi-1,value,1,1,1));//添加到下个循环中
						for(int x=1;x<request_list.get(rli).size();x++){
							Syntagm driverx = request_list.get(rli).get(x);//第x个抢到该请求的车
							if(driver_free[driverx.name]){//若该司机很清闲
								if(driverx.line!=linei || driverx.list!=listi-1){
									continue;
								}
//								System.out.println("driverx.name "+driverx.name);
								rightdriver.add(driver.get_taxi_information(driverx.name));
								sat=true;
							}
							else{
								request_list.get(rli).remove(x);
								x--;
							}
						}
					}
				}
				if(map.can_right(linei, listi)){//can right
					if(bfsmap[linei][listi+1]==-1){//若right的点尚未走过
						bfsmap[linei][listi+1]=value;//right点距line list距离为value
						bfsnew.add(new Syntagm(linei,listi+1,value,1,1,1));//添加到下个循环中
						for(int x=1;x<request_list.get(rli).size();x++){
							Syntagm driverx = request_list.get(rli).get(x);//第x个抢到该请求的车
							if(driver_free[driverx.name]){//若该司机很清闲
								if(driverx.line!=linei || driverx.list!=listi+1){
									continue;
								}
//								System.out.println("driverx.name "+driverx.name);
								rightdriver.add(driver.get_taxi_information(driverx.name));
								sat=true;
							}
							else{
								request_list.get(rli).remove(x);
								x--;
							}
						}
					}
				}
			}
			if(bfsold.size()==0){
				break;
			}
			bfsold.clear();
			bfsold=bfsnew;
			if(sat){
				break;
			}
		}
		if(sat){
			
            String line1 = "\nDriver NO."+rightdriver.get(0).name+" catch the work!\n\n";
            String s=line1;
			write(request_list.get(rli).get(0),s);
			
//			System.out.println("the NO."+rightdriver.get(0).name+" catch the work!");
//			System.out.println(rightdriver.get(0).name+" is not free!");
			driver_free[rightdriver.get(0).name]=false;
			driver.getwork(request_list.get(rli).get(0), rightdriver.get(0).name);
			return 0;
		}
			
		return -1;
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
	
	
}
