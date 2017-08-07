package Taxi;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;

public class Controller extends Thread{
	private SafeFile sf;
	private int request_num=0;
	private int time=0;
	private Map map;
	private boolean [] driver_free = new boolean[102];
	private ArrayList<Driver> driver_list = new ArrayList<Driver>();	//	所有出租车队列
	public ArrayList<Syntagm> taxi_list = new ArrayList<Syntagm>();		//	抢单出租车队列
	public ArrayList<ArrayList<Syntagm>> request_list = new ArrayList<ArrayList<Syntagm>>();
	public void addf_l(Syntagm request){
		File myFilePath = new File(request.getpath());
		try{
			if(!myFilePath.exists()){//若文件不存在则创建一个文件
				try{
					myFilePath.createNewFile();
				}catch(Exception e){
					System.err.println("The File ["+request.getpath()+"] create faild!");
				}
			}
			else if(myFilePath.isDirectory()){
				System.err.println("存在同名文件夹，不可写入操作");
				return;
			}
		}catch(Exception e){
		}
		
		if(request.getfindword().equals("Taxi")){
			Driver driver = driver_list.get(request.getfindtaxi()-1);
			String line1="The Detail of the Taxi No."+(request.getfindtaxi()+1)+":\n";
			String line2="Time now is:"+(time*100)+"ms\n";
			String line3="The taxi is in ("+driver.getline()+","+driver.getlist()+")\n";
			String line4="Status is:"+driver.getstatus()+"\tMark is:"+driver.getmark()+"\n\n";
			String s = line1 +line2+line3+line4;
			write(request,s);
		}
		else if(request.getfindword().equals("Status")){
			boolean flag=true;
			String s = "The taxis in Status "+request.getfindtaxi()+" are:\nTime now is:"+(time*100)+"ms\n";
			write(request,s);
			for(int i=0;i<100;i++){
				Driver driver = driver_list.get(i);
				if(driver.getstatus()==request.getfindtaxi()){
					flag=false;
					String line2 = "Taxi No."+(i+1)+":\t in ("+driver.getline()+","+driver.getlist()+")\t";
					String line4="Status is:"+driver.getstatus()+"\tMark is:"+driver.getmark()+"\n";
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
		ArrayList<Syntagm> newlist = new ArrayList<Syntagm>();
		newlist.add(s); 
		request_list.add(newlist);
		request_num++;
		s.setpath(request_num);
	}
	public void run(){
		try{ 
			Random random = new Random();
			while(true){//每100s处理一次请求
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
		try{
			taxi_list.clear();
			for(int i=0;i<100;i++){//存储所有空闲司机的当前状态
				if(driver_list.get(i).get_working()){//如果正在忙就不用抢单了
					driver_free[i]=false;
					continue;
				}
				//如果不在忙就加入抢单队列
				driver_free[i]=true;
				taxi_list.add(new Syntagm(driver_list.get(i).getline(),driver_list.get(i).getlist(),i+1));
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
		setfreelist();//更新可抢单出租车队列
		for(int i=0;i<request_list.size();i++){//遍历所有指令
			
			//若刚刚发出请求，则输出周围4*4范围内出租车状态信息
			if(request_list.get(i).get(0).getfirstcall()){
				Syntagm request = request_list.get(i).get(0);
				boolean flag=false;
				for(int j=0;j<driver_list.size();j++){
					Driver driver=driver_list.get(j);
					if(driver.getline()>= request.getline()-2 && driver.getline()<=request.getline()+2){
						if(driver.getlist()>= request.getlist()-2 && driver.getlist()<=request.getlist()+2){
							
							//写入操作
							flag=true;
							File myFilePath = new File(request.getpath());
							try{
								if(myFilePath.exists()){//若文件已存在
									if(myFilePath.isFile()){
										
										String line3 ="Driver No."+driver.getname()+" is in ("+driver.getline()+","+driver.getlist()+")\n"; 
										String line4 ="\tStatus:"+driver.getstatus()+"\tMark:"+driver.getmark()+"\n";
							            String s = line3+line4;
							            write(request,s);
									}
									else{
										System.err.println("["+request.getpath()+"]文件夹不可被写入值！");
									}
								}
								else{//文件不存在则创建一个文件
									try{
										myFilePath.createNewFile();
									}catch(Exception e){
										System.err.println("The File ["+request.getpath()+"] create faild!");
										continue;
									}
									
									String line1 = "The request "+request.get_order()+" is:\n\t	";
						            String line2 = "From ("+request.getline()+","+request.getlist()+") To ("+request.get_orderline()+","+request.get_orderlist()+")\n";
									String line3 = "Calling time is:"+time*100+"ms\n\n";
									
						            String line4 = "At that time Taxis nearby:\n";
						            String line5 = "Driver No."+driver.getname()+" is in ("+driver.getline()+","+driver.getlist()+")\n"; 
									String line6 = "\tStatus:"+driver.getstatus()+"\tMark:"+driver.getmark()+"\n";
						            	 
						            String s=line1+line2+line3+line4+line5+line6;
						            
						            write(request,s);
						            
								}
							}catch(Exception e){
							}
							
							
							
						}
					}
					
				}
				if(!flag){
					File myFilePath = new File(request.getpath());
					try{
						if(myFilePath.exists()){//若文件已存在
							if(myFilePath.isFile()){
								
					            String line1 = "The request "+request.get_order()+" is:\n\t	";
					            String line2 = "From ("+request.getline()+","+request.getlist()+") To ("+request.get_orderline()+","+request.get_orderlist()+")\n";
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
								System.err.println("The File ["+request.getpath()+"] create faild!");
							}
							
				            String line1 = "The request "+request.get_order()+" is:\n\t	";
				            String line2 = "From ("+request.getline()+","+request.getlist()+") To ("+request.get_orderline()+","+request.get_orderlist()+")\n";
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
				
				
				request_list.get(i).get(0).setfirstcall();
			}
			if(request_list.get(i).get(0).gettime() <= time-30){//若指令r_l.get(i)已经发出请求3s
//				System.out.println("123???");
				boolean sat=false;
				int maxmark=-1;
				if(request_list.get(i).size()==1){
					//若没有一辆车应答叫号
					Syntagm request = request_list.get(i).get(0);

		            String line1 = "No driver hear the calling\n\n";
		            String s=line1;
					write(request,s);
					
				}
				for(int j=1;j<request_list.get(i).size();j++){
					//遍历所有抢了此单的出租车
					int name = request_list.get(i).get(j).getname();
					if(!driver_free[name]){
						//如果出租车目前正在忙,则从队伍中剔除
						request_list.get(i).remove(j);
						j--;
					}
					if(driver_list.get(name-1).getmark()<maxmark){
						driver_list.get(name-1).setrelax(true);
						request_list.get(i).remove(j);
						j--;
					}
					else if(driver_list.get(name-1).getmark()>maxmark){
						maxmark=driver_list.get(name-1).getmark();
					}
				}
				for(int j=1;j<request_list.get(i).size();j++){
					int name = request_list.get(i).get(j).getname();
					if(driver_list.get(name-1).getmark()<maxmark){
						driver_list.get(name-1).setrelax(true);
						request_list.get(i).remove(j);
						j--;
					}
				}
				
				int drivernum=-1;
				//从呼叫点request_list.get(i).get(0).getline   request_list.get(i).get(0).getlist开始bfs找到可搭乘出租车
				drivernum=bfs(request_list.get(i).get(0).getline(),request_list.get(i).get(0).getlist(),request_list.get(i).size()-1,i);
				sat=(drivernum!=-1);
				
				if(!sat){
					System.out.println("从("+request_list.get(i).get(0).getline()+","+request_list.get(i).get(0).getlist()+
							")前往("+request_list.get(i).get(0).get_orderline()+","+request_list.get(i).get(0).get_orderlist()+")的乘客没打到车");
					
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
					if(driver.getline()>=request_list.get(i).get(0).getline()-2 &&	driver.getline()<=request_list.get(i).get(0).getline()+2){
						if(driver.getlist()>=request_list.get(i).get(0).getlist()-2 &&	driver.getlist()<=request_list.get(i).get(0).getlist()+2){
							boolean judge=false;	
							//若未添加过该司机，则添加至i指令的队中
							for(int k=1;k<request_list.get(i).size();k++){
								if(request_list.get(i).get(k).getname()==driver.getname()){
									judge = true;
									break;
								}
							}
							if(!judge){
								System.out.println("the No."+driver.getname()+" add to the list!");

					            String line1 = "Driver No."+driver.getname()+" add to the list!\n";
					            String s=line1;
								write(request_list.get(i).get(0),s);
								
								driver_list.get(driver.getname()-1).setmark();
								driver_list.get(driver.getname()-1).setrelax(false);
								request_list.get(i).add(driver);
							}
						}
					}
				}
				
			}
		}
	}
	
	public int bfs(int line,int list,int taxinum,int rli){
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
		bfsold.add(new Syntagm(line,list,0,1));
		boolean sat=false;
		if(taxinum<=0){
			return -1;
		}
		while(true){
			bfsnew=new ArrayList<Syntagm>(); 
			for(int t=0;t<bfsold.size();t++){
				value++;
				//main point当前bfs到点的详细信息
				int linei = bfsold.get(t).getline();
				int listi = bfsold.get(t).getlist();
				if(map.can_up(linei, listi)){//该点能往上走
					if(bfsmap[linei-1][listi]==-1){//若上方的点尚未走过
						bfsmap[linei-1][listi]=value;//上方点距line list距离为value
						bfsnew.add(new Syntagm(linei-1,listi,value,1));//添加到下个循环中
						for(int x=1;x<request_list.get(rli).size();x++){
							Syntagm driver = request_list.get(rli).get(x);//第x个抢到该请求的车
							if(driver_free[driver.getname()]){//若该司机很清闲
								if(driver.getline()!=linei-1 || driver.getlist()!=listi){
									continue;
								}
								rightdriver.add(driver_list.get(driver.getname()-1));
								sat=true;
							}
						}
					}
				}
				if(map.can_down(linei, listi)){//can down
					if(bfsmap[linei+1][listi]==-1){//若下方的点尚未走过
						bfsmap[linei+1][listi]=value;//下方点距line list距离为value
						bfsnew.add(new Syntagm(linei+1,listi,value,1));//添加到下个循环中
						for(int x=1;x<request_list.get(rli).size();x++){
							Syntagm driver = request_list.get(rli).get(x);//第x个抢到该请求的车
							if(driver_free[driver.getname()]){//若该司机很清闲
								if(driver.getline()!=linei+1 || driver.getlist()!=listi){
									continue;
								}
								rightdriver.add(driver_list.get(driver.getname()-1));
								sat=true;
							}
						}
					}
				}
				if(map.can_left(linei, listi)){//can left
					if(bfsmap[linei][listi-1]==-1){//若左边的点尚未走过
						bfsmap[linei][listi-1]=value;//左边点距line list距离为value
						bfsnew.add(new Syntagm(linei,listi-1,value,1));//添加到下个循环中
						for(int x=1;x<request_list.get(rli).size();x++){
							Syntagm driver = request_list.get(rli).get(x);//第x个抢到该请求的车
							if(driver_free[driver.getname()]){//若该司机很清闲
								if(driver.getline()!=linei || driver.getlist()!=listi-1){
									continue;
								}
								rightdriver.add(driver_list.get(driver.getname()-1));
								sat=true;
							}
						}
					}
				}
				if(map.can_right(linei, listi)){//can right
					if(bfsmap[linei][listi+1]==-1){//若right的点尚未走过
						bfsmap[linei][listi+1]=value;//right点距line list距离为value
						bfsnew.add(new Syntagm(linei,listi+1,value,1));//添加到下个循环中
						for(int x=1;x<request_list.get(rli).size();x++){
							Syntagm driver = request_list.get(rli).get(x);//第x个抢到该请求的车
							if(driver_free[driver.getname()]){//若该司机很清闲
								if(driver.getline()!=linei || driver.getlist()!=listi+1){
									continue;
								}
								rightdriver.add(driver_list.get(driver.getname()-1));
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
			
            String line1 = "\nDriver NO."+rightdriver.get(0).getname()+" catch the work!\n\n";
            String s=line1;
			write(request_list.get(rli).get(0),s);
			
			System.out.println("the NO."+rightdriver.get(0).getname()+" catch the work!");
			driver_free[rightdriver.get(0).getname()]=false;
			rightdriver.get(0).set_working(true);
			rightdriver.get(0).addlist(request_list.get(rli).get(0));
			return 0;
		}
			
		return -1;
	}
	
	
	synchronized public void write(Syntagm request,String s){
		File myFilePath = new File(request.getpath());
		try{
			
			FileWriter fileWritter = new FileWriter(myFilePath.getAbsoluteFile(),true);
            BufferedWriter bufferWritter = new BufferedWriter(fileWritter);
            bufferWritter.write(s);
            bufferWritter.close();
            
		}catch(Exception e){
		}
	}
		
		
		
		
		
		
		
		
		
		
		
		
		
		/*//就近找人接，同样近找老司机
		while(i<taxinum){
			bfsnew=new ArrayList<Syntagm>(); 
			for(int t=0;t<bfsold.size();t++){
				value++;
				int judge=0;
				//main point当前bfs到点的详细信息
				Syntagm mainpoint = bfsold.get(t);
				judge=map.getmap(bfsold.get(t).getline(),bfsold.get(t).getlist());
				int linei = bfsold.get(t).getline();
				int listi = bfsold.get(t).getlist();
				if((judge >=4 && judge<=9) || judge==13 || judge==15){//该点能往上走
					if(bfsmap[linei-1][listi]==-1){//若上方的点尚未走过
						bfsmap[linei-1][listi]=value;//上方点距line list距离为value
						bfsnew.add(new Syntagm(linei-1,listi,value,1));//添加到下个循环中
						for(int x=1;x<request_list.get(rli).size();x++){
							Syntagm driver = request_list.get(rli).get(x);//第x个抢到该请求的车
							if(driver_free[driver.getname()]){//若该司机很清闲
								if(driver.getline()!=linei-1 || driver.getlist()!=listi){
									continue;
								}
								rightdriver.add(driver_list.get(driver.getname()));
								sat=true;
							}
						}
					}
				}
				if((judge >=1 && judge<=6) || judge==10 || judge==15){//can down
					if(bfsmap[linei+1][listi]==-1){//若下方的点尚未走过
						bfsmap[linei+1][listi]=value;//下方点距line list距离为value
						bfsnew.add(new Syntagm(linei+1,listi,value,1));//添加到下个循环中
						for(int x=1;x<request_list.get(rli).size();x++){
							Syntagm driver = request_list.get(rli).get(x);//第x个抢到该请求的车
							if(driver_free[driver.getname()]){//若该司机很清闲
								if(driver.getline()!=linei+1 || driver.getlist()!=listi){
									continue;
								}
								rightdriver.add(driver_list.get(driver.getname()));
								sat=true;
							}
						}
					}
				}
				if(judge ==2 || judge ==3 || judge==5 || judge==6 || judge==8 ||judge ==9 || judge==12 || judge==14){//can left
					if(bfsmap[linei][listi-1]==-1){//若左边的点尚未走过
						bfsmap[linei][listi-1]=value;//左边点距line list距离为value
						bfsnew.add(new Syntagm(linei,listi-1,value,1));//添加到下个循环中
						for(int x=1;x<request_list.get(rli).size();x++){
							Syntagm driver = request_list.get(rli).get(x);//第x个抢到该请求的车
							if(driver_free[driver.getname()]){//若该司机很清闲
								if(driver.getline()!=linei || driver.getlist()!=listi-1){
									continue;
								}
								rightdriver.add(driver_list.get(driver.getname()));
								sat=true;
							}
						}
					}
				}
				if(judge ==2 || judge ==1 || judge==5 || judge==4 || judge==8 ||judge ==7 || judge==11 || judge==14){//can right
					if(bfsmap[linei][listi+1]==-1){//若tight的点尚未走过
						bfsmap[linei][listi+1]=value;//right点距line list距离为value
						bfsnew.add(new Syntagm(linei,listi+1,value,1));//添加到下个循环中
						for(int x=1;x<request_list.get(rli).size();x++){
							Syntagm driver = request_list.get(rli).get(x);//第x个抢到该请求的车
							if(driver_free[driver.getname()]){//若该司机很清闲
								if(driver.getline()!=linei || driver.getlist()!=listi+1){
									continue;
								}
								rightdriver.add(driver_list.get(driver.getname()));
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
			int min=9999999;
			for(i=0;i<rightdriver.size();i++){
				if(rightdriver.get(i).getmark()<min){
					min=rightdriver.get(i).getmark();
				}
			}
			for(i=0;i<rightdriver.size();i++){
				if(rightdriver.get(i).getmark()==min){
					rightdriver.get(i).addlist(request_list.get(rli).get(0));
					break;
				}
			}
			
			return 0;
		}
		
		return -1;
	}*/
	
	public void setbfsmap(int bfsmap[][]){
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
		driver_list.add(driver);
	}
	public void setmap(Map map){
		this.map=map;
	}
	public void setsf(SafeFile sf){
		this.sf=sf;
	}
	
	
	
	
	
	
	
	
	
	
}
