package Taxi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class Controller extends Thread{
	public int request_num=0;
	public int time=0;
	public Map map;
	public boolean [] driver_free = new boolean[102];
	public ArrayList<Driver> driver_list = new ArrayList<Driver>();	//	所有出租车队列
	public ArrayList<Syntagm> taxi_list = new ArrayList<Syntagm>();		//	抢单出租车队列
	public ArrayList<ArrayList<Syntagm>> request_list = new ArrayList<ArrayList<Syntagm>>();
	
	
	
	
	public void addf_l(Syntagm request){
		/* @ REQUIRES: Syntagm request
		 * @ MODIFIES: none
		 * @ EFFECTS: for searching the order Status or order taxi's stasus 
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
			Driver driver = driver_list.get(request.findtaxi-1);
			String line1="The Detail of the Taxi No."+(request.findtaxi+1)+":\n";
			String line2="Time now is:"+(time*100)+"ms\n";
			String line3="The taxi is in ("+driver.line+","+driver.list+")\n";
			String line4="Status is:"+driver.status+"\tMark is:"+driver.mark+"\n\n";
			String s = line1 +line2+line3+line4;
			write(request,s);
		}
		else if(request.findword.equals("Status")){
			boolean flag=true;
			String s = "The taxis in Status "+request.findtaxi+" are:\nTime now is:"+(time*100)+"ms\n";
			write(request,s);
			for(int i=0;i<100;i++){
				Driver driver = driver_list.get(i);
				if(driver.status==request.findtaxi){
					flag=false;
					String line2 = "Taxi No."+(i+1)+":\t in ("+driver.line+","+driver.list+")\t";
					String line4="Status is:"+driver.status+"\tMark is:"+driver.mark+"\n";
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
		 * @ MODIFIES: request_list
		 * @ EFFECTS: add a new character to the request_list
		 */
		ArrayList<Syntagm> newlist = new ArrayList<Syntagm>();
		newlist.add(s); 
		request_list.add(newlist);
		request_num++;
		s.setpath(request_num);
	}
	public void run(){
		/* @ REQUIRES: none
		 * @ MODIFIES: request_list
		 * @ EFFECTS: deal the request_list per 100ms.
		 * 			  throw the correct request to the right driver thread
		 * 			  modify to call the taxi while the request have been called 3s ago
		 */
		try{ 
			Random random = new Random();
			while(true){//每100s处理一次请求
				if(Main.reset){
					for(int i=0;i<100;i++){
						Driver driver = driver_list.get(i);
						if(driver.status==1 || driver.status == 3){
							System.out.println("No."+driver.name+" in status "+driver.status);
							driver.flag1=!driver.flag1;
							for(int j=driver.worklist.size()-1;j>1;j--){
								driver.worklist.remove(j);
							}
						}
					}
					Main.reset=false;
				}
				request_deal();
				int sleeptime=random.getsleeptime100();
				if(sleeptime<0){
					System.out.println("foolish calculator!");
				}
				else{
					sleep(sleeptime);
				}
				time=(int)(System.currentTimeMillis()-Main.time)/100;
//				System.out.println(time);
			}
			
			
			
			
			
		}
		catch(Exception e){
			System.err.println("Something wrong with the controller");
		}
	}
	
	
	
	
	public void setfreelist(){
		/* @ REQUIRES: none 
		 * @ MODIFIES: driver_free[]	taxi_list
		 * @ EFFECTS: search all the drivers to make sure which one is free to get a new work
		 */
		try{
			taxi_list.clear();
			for(int i=0;i<100;i++){//存储所有空闲司机的当前状态
				if(driver_list.get(i).working){//如果正在忙就不用抢单了
					driver_free[i+1]=false;
					continue;
				}
				//如果不在忙就加入抢单队列
				driver_free[i+1]=true;
				taxi_list.add(new Syntagm(driver_list.get(i).line,driver_list.get(i).list,i+1));
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
		/* @ REQUIRES: none 
		 * @ MODIFIES: none
		 * @ EFFECTS: deal with the request and throw/save it to the correct taxi
		 */
		setfreelist();//更新可抢单出租车队列
		for(int i=0;i<request_list.size();i++){//遍历所有指令
			
			//若刚刚发出请求，则输出周围4*4范围内出租车状态信息
			if(request_list.get(i).get(0).firstcall){
				Syntagm request = request_list.get(i).get(0);
				boolean flag=false;
				for(int j=0;j<driver_list.size();j++){
					Driver driver=driver_list.get(j);
					if(driver.line>= request.line-2 && driver.line<=request.line+2){
						if(driver.list>= request.list-2 && driver.list<=request.list+2){
							
							//写入操作
							flag=true;
							File myFilePath = new File(request.path);
							try{
								if(myFilePath.exists()){//若文件已存在
									if(myFilePath.isFile()){
										
										String line3 ="Driver No."+driver.name+" is in ("+driver.line+","+driver.list+")\n"; 
										String line4 ="\tStatus:"+driver.status+"\tMark:"+driver.mark+"\n";
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
						            String line5 = "Driver No."+driver.name+" is in ("+driver.line+","+driver.list+")\n"; 
									String line6 = "\tStatus:"+driver.status+"\tMark:"+driver.mark+"\n";
						            	 
						            String s=line1+line2+line3+line4+line5+line6;
						            
						            write(request,s);
						            
								}
							}catch(Exception e){
							}
							
							
							
						}
					}
					
				}
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
			}
			if(request_list.get(i).get(0).ordertime <= time-30){//若指令r_l.get(i)已经发出请求3s
//				System.out.println("123???");
				boolean sat=false;
				int maxmark=-1;
				if(request_list.get(i).size()==1){
					//若没有一辆车应答叫号
					Syntagm request = request_list.get(i).get(0);

		            String line1 = "No driver hear the calling\n\n";
		            String s=line1;
					write(request,s);
					request_list.remove(i);
					i--;
					continue;
				}
				for(int j=1;j<request_list.get(i).size();j++){
					//遍历所有抢了此单的出租车
					int name = request_list.get(i).get(j).name;
					if(!driver_free[name]){
						//如果出租车目前正在忙,则从队伍中剔除
						request_list.get(i).remove(j);
						j--;
						continue;
					}
					if(driver_list.get(name-1).mark<maxmark){
						driver_list.get(name-1).relax=true;
						request_list.get(i).remove(j);
						j--;
					}
					else if(driver_list.get(name-1).mark>maxmark){
						maxmark=driver_list.get(name-1).mark;
					}
				}
				for(int j=1;j<request_list.get(i).size();j++){
					int name = request_list.get(i).get(j).name;
					if(driver_list.get(name-1).mark<maxmark){
						driver_list.get(name-1).relax=true;
						request_list.get(i).remove(j);
						j--;
					}
				}
				
				int drivernum=-1;
				//从呼叫点request_list.get(i).get(0).getline   request_list.get(i).get(0).getlist开始bfs找到可搭乘出租车
				drivernum=bfs(request_list.get(i).get(0).line,request_list.get(i).get(0).list,request_list.get(i).size()-1,i);
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
					Syntagm driver = taxi_list.get(j);//第j号空闲司机的信息
					//如果司机正好在4*4格子里
					if(driver.line>=request_list.get(i).get(0).line-2 &&	driver.line<=request_list.get(i).get(0).line+2){
						if(driver.list>=request_list.get(i).get(0).list-2 &&	driver.list<=request_list.get(i).get(0).list+2){
							boolean judge=false;	
							//若未添加过该司机，则添加至i指令的队中
							for(int k=1;k<request_list.get(i).size();k++){
								if(request_list.get(i).get(k).name==driver.name){
									judge = true;
									break;
								}
							}
							if(!judge){
//								System.out.println("the No."+driver.name+" add to the list!");

					            String line1 = "Driver No."+driver.name+" add to the list!\n";
					            String s=line1;
								write(request_list.get(i).get(0),s);
								
								driver_list.get(driver.name-1).mark++;
								driver_list.get(driver.name-1).relax=false;
								driver_list.get(driver.name-1).feel_no_good=0;
								request_list.get(i).add(driver);
							}
						}
					}
				}
				
			}
		}
	}
	
	public int bfs(int line,int list,int taxinum,int rli){
		/* @ REQUIRES: Point(x,y), Syntagm request 
		 * @ MODIFIES: none
		 * @ EFFECTS: BFS form the calling point to far;
		 * 				if there is a taxi's mark ==max mark && it is free
		 * 				then throw the request to the taxi,return 0
		 */
		//rli 此单的序号
		//bfsold 用来存上次的所有点
		//bfsnew 用来存新增的所有点
		int [][] bfsmap=new int [Readin.MAX+5][Readin.MAX+5];
		ArrayList<Syntagm> bfsold = new ArrayList<Syntagm>();
		ArrayList<Syntagm> bfsnew = new ArrayList<Syntagm>();
		ArrayList<Driver> rightdriver = new ArrayList<Driver>();
		setbfsmap(bfsmap);
		int value=0;
		bfsmap[line][list]=0;
		bfsold.add(new Syntagm(line,list,0,1,1,1));
		boolean sat=false;
		if(taxinum<=0){
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
							Syntagm driver = request_list.get(rli).get(x);//第x个抢到该请求的车
							if(driver_free[driver.name]){//若该司机很清闲
								if(driver.line!=linei-1 || driver.list!=listi){
									continue;
								}
								rightdriver.add(driver_list.get(driver.name-1));
								sat=true;
							}
						}
					}
				}
				if(map.can_down(linei, listi)){//can down
					if(bfsmap[linei+1][listi]==-1){//若下方的点尚未走过
						bfsmap[linei+1][listi]=value;//下方点距line list距离为value
						bfsnew.add(new Syntagm(linei+1,listi,value,1,1,1));//添加到下个循环中
						for(int x=1;x<request_list.get(rli).size();x++){
							Syntagm driver = request_list.get(rli).get(x);//第x个抢到该请求的车
							if(driver_free[driver.name]){//若该司机很清闲
								if(driver.line!=linei+1 || driver.list!=listi){
									continue;
								}
								rightdriver.add(driver_list.get(driver.name-1));
								sat=true;
							}
						}
					}
				}
				if(map.can_left(linei, listi)){//can left
					if(bfsmap[linei][listi-1]==-1){//若左边的点尚未走过
						bfsmap[linei][listi-1]=value;//左边点距line list距离为value
						bfsnew.add(new Syntagm(linei,listi-1,value,1,1,1));//添加到下个循环中
						for(int x=1;x<request_list.get(rli).size();x++){
							Syntagm driver = request_list.get(rli).get(x);//第x个抢到该请求的车
							if(driver_free[driver.name]){//若该司机很清闲
								if(driver.line!=linei || driver.list!=listi-1){
									continue;
								}
								rightdriver.add(driver_list.get(driver.name-1));
								sat=true;
							}
						}
					}
				}
				if(map.can_right(linei, listi)){//can right
					if(bfsmap[linei][listi+1]==-1){//若right的点尚未走过
						bfsmap[linei][listi+1]=value;//right点距line list距离为value
						bfsnew.add(new Syntagm(linei,listi+1,value,1,1,1));//添加到下个循环中
						for(int x=1;x<request_list.get(rli).size();x++){
							Syntagm driver = request_list.get(rli).get(x);//第x个抢到该请求的车
							if(driver_free[driver.name]){//若该司机很清闲
								if(driver.line!=linei || driver.list!=listi+1){
									continue;
								}
								rightdriver.add(driver_list.get(driver.name-1));
								sat=true;
							}
						}
					}
				}
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
			driver_free[rightdriver.get(0).name]=false;
			rightdriver.get(0).working=true;
			rightdriver.get(0).addlist(request_list.get(rli).get(0));
			return 0;
		}
			
		return -1;
	}
	
	
	synchronized public void write(Syntagm request,String s){
		/* @ REQUIRES: Syntagm request String s 
		 * @ MODIFIES: none
		 * @ EFFECTS: print the word s into the request's path
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
		/* @ REQUIRES: bfs[][] 
		 * @ MODIFIES: bfsmap[][]
		 * @ EFFECTS: initial the bfsmap[][]
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
	public void getd_l(Driver driver){
		/* @ REQUIRES: Driver d 
		 * @ MODIFIES: driver_list
		 * @ EFFECTS: initial the driver_list
		 */
		driver_list.add(driver);
	}
	
	
	
	
	
	
	
	
	
}
