package Taxi;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 		Overview
 * 
 * 		外部请求类，用于从控制台读入外部指令，100ms一刷新，传给相应的其他类使用
 * */
public class People extends Thread{
	public TaxiGUI gui;
	public Map map;
	public Controller ctrl;
	public boolean shut=true;
	final public Scanner word=new Scanner(System.in);	
	final public ArrayList<Syntagm> maplist = new ArrayList<Syntagm>();
	final public ArrayList<Syntagm> calllist = new ArrayList<Syntagm>();
	final public ArrayList<Syntagm> findlist = new ArrayList<Syntagm>();
	
	/*不变式：
	 * 1	ctrl为Controller型变量,不为Null
	 * 2	gui为TaxiGUI型变量,不为Null
	 * 3	map为Map型变量,不为Null
	 * 4	time>=0
	 * 5	word为Scanner型变量，不为null
	 * 6	maplist为每一项均为不为Null的Syntagm型变量的ArrayList数组，其中每一项a满足
	 * 			1<=a.line<=80
	 * 			1<=a.list<=80
	 * 			1<=a.value<=4
	 * 7	calllist为每一项均为不为Null的Syntagm型变量的ArrayList数组，其中每一项a满足
	 * 			1<=a.line<=80
	 * 			1<=a.list<=80
	 * 			1<=a.orderline<=80
	 * 			1<=a.orderlist<=80
	 * 			0<=a.ordertime
	 * 8	findlist为每一项均为不为Null的Syntagm型变量的ArrayList数组，其中每一项a满足
	 * 			a.path.equals("Detail.txt")|| a.path.equals("History.txt")
	 * 		  ( a.findword.equals("Taxi") && 0<=a.findtaxi<=99)
	 * 		||( a.findword.equals(History0) && 0<=a.findtaxi<=99) 
	 * 		||( a.findword.equals(History1) && 0<=a.findtaxi<=99) 
	 * 		||( a.findword.equals("Status" && 0<=a.findtaxi<=3)
	 * 			
	 * */
	public boolean repOK(){
		try{
			/*@Effects: \result==invariant(this).*/
			if(gui==null 
			|| map==null || !map.repOK()
			|| ctrl==null || !ctrl.repOK()
			|| word==null
			|| maplist==null || calllist==null || findlist==null){
				return false;
			}
			if(!(gui instanceof TaxiGUI)
			|| !(map instanceof Map)
			|| !(ctrl instanceof Controller)
			|| !(word instanceof Scanner)
			|| !(maplist instanceof ArrayList)
			|| !(calllist instanceof ArrayList)
			|| !(findlist instanceof ArrayList)){
				return false;
			}
			for(int i=0;i<maplist.size();i++){
				if(maplist.get(i)==null){
					return false;
				}
				if(!(maplist.get(i) instanceof Syntagm)){
					return false;
				}
			}
			for(int i=0;i<calllist.size();i++){
				if(calllist.get(i) == null){
					return false;
				}
				if(!(calllist.get(i) instanceof Syntagm)){
					return false;
				}
			}
			for(int i=0;i<findlist.size();i++){
				if(findlist.get(i)==null){
					return false;
				}
				if(!(findlist.get(i) instanceof Syntagm)){
					return false;
				}
			}
			for(int i=0;i<maplist.size();i++){
				Syntagm s = maplist.get(i);
				if(s.line>80 || s.line<1 
				|| s.list>80 || s.list<1
				|| s.value<1 || s.value>4){
					return false;
				}
			}
			for(int i=0;i<calllist.size();i++){
				Syntagm s = calllist.get(i);
				if(s.line>80 || s.line<1 
				|| s.list>80 || s.list<1
				|| s.orderline>80 || s.orderline<1
				|| s.orderlist>80 || s.orderlist<1
				|| s.ordertime<0){
					return false;
				}
			}
			for(int i=0;i<findlist.size();i++){
				Syntagm s = findlist.get(i);
				if(!(s.path.equals("Detail.txt")
				  || s.path.equals("History.txt"))){
					return false;
				}
				if(s.findword.equals("Status") && s.findtaxi>=0 && s.findtaxi<=3){
					continue;
				}
				if(s.findword.equals("Taxi") && s.findtaxi>=0 && s.findtaxi<=99){
					continue;
				}
				if(s.findword.equals("History0") && s.findtaxi>=0 && s.findtaxi<=99){
					continue;
				}
				if(s.findword.equals("History1") && s.findtaxi>=0 && s.findtaxi<=99){
					continue;
				}
				return false;
			}
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	public void run(){
		/* @ REQUIRES: this属性全部非null &&	repOK()
		 * @ MODIFIES: this
		 * @ EFFECTS: 从控制台读入指令,共有三大类，五种指令请求
		 * 			1-叫车请求
		 * 			2-查询某出租车状态
		 * 			3-查询某状态所有出租车
		 * 			4-打开一条通路
		 * 			5-关闭一条通路
		 * 
		 * 			读入指令后，将其中合法的部分存入三个ArrayList中
		 * 
		 * 			100ms一处理合法请求(抛出给相应类使用)
		 */
		try{
			while(shut){
				String line = word.nextLine();
				
				String pattern = "\\[(CR),\\(([+-?]\\d*),([+-?]\\d*)\\),\\(([+-?]\\d*),([+-?]\\d*)\\)\\]|"
								+"\\[(FR),(Taxi|Status|History0|History1),([+-?]\\d*)\\]|"
								+"\\[(DR),(Open|Close),\\(([+-?]\\d*),([+-?]\\d*)\\),([+-?]\\d*)\\]";
				
				Pattern r = Pattern.compile(pattern);
				Matcher m = r.matcher(line);
				
				
				
				

				while(m.find()){
					do{
//						System.out.println(m.group(0));
						String w = m.group(0).replaceAll("\\[","").replaceAll("\\]","").replaceAll("\\(","").replaceAll("\\)","");
						String[] sarray = w.split(",");
						if(sarray[0].equals("CR")){//叫车指令
							try{
								double num1 = Double.parseDouble(sarray[1]);
								double num2 = Double.parseDouble(sarray[2]);
								double num3 = Double.parseDouble(sarray[3]);
								double num4 = Double.parseDouble(sarray[4]);
								
								int num5 = (int) num1;
								int num6 = (int) num2;
								int num7 = (int) num3;
								int num8 = (int) num4;
								
//								System.out.println(" "+num5+" "+num6+" "+num7+" "+num8);
								if(num5<=0 || num5>80 || num6<=0 || num6>80 || num7<=0 ||num7>80 ||num8 <=0 ||num8>80){
//									System.out.println("num1:"+num5+" num2:"+num6+" num3:"+num7+"num4:"+num8);
									System.err.println("Invalid input-"+m.group(0));
									break;
								}
								if(num5==num7 && num6==num8){
									System.err.println("Where are you going???");
									break;
								}
//								gui.RequestTaxi(new Point(num5-1,num6-1),new Point(num7-1,num8-1));
								int time = Controller.time/100;
								time*=100;
								Syntagm snew = new Syntagm(num5,num6,num7,num8,time);
								for(int i=0;i<calllist.size();i++){
									Syntagm sold = calllist.get(i);
									if(sold.line==snew.line 
									&& sold.list==snew.list
									&& sold.orderline==snew.orderline 
									&& sold.orderlist==snew.orderlist){
										System.out.println("Same "+m.group(0));
										calllist.remove(i);
										break;
									}
								}
								calllist.add(snew);
							}
							catch(Exception e){
								System.err.println("Invalid input-"+m.group(0));
								break;
							}				
						}
						else if(sarray[0].equals("FR")){//查询状态指令
							if(sarray[1].equals("Taxi")){
								try{
									double num1 = Double.parseDouble(sarray[2]);
									
									int num2 = (int) num1;
									if(num2>99 || num2<0){
										System.out.println("Invalid Taxi Number! [Taxi "+num2+"]");
										break;
									}
									findlist.add(new Syntagm("Taxi",(num2),"Detail.txt"));
								}
								catch(Exception f){
									System.err.println("Invalid Input ["+m.group(0)+"]");
									break;
								}
							}
							else if(sarray[1].equals("Status")){
								try{
									double num1 = Double.parseDouble(sarray[2]);
									int num2 = (int) num1;
									if(num2>3 || num2<0){
										System.out.println("Invalid State Number! [State "+num2+"]");
										break;
									}
									findlist.add(new Syntagm("Status",num2,"Detail.txt"));
								}
								catch(Exception f){
									System.err.println("Invalid Input ["+m.group(0)+"]");
									break;
								}
							}
							else if(sarray[1].equals("History0")){
								try{
									double num1 = Double.parseDouble(sarray[2]);

									int num2 = (int) num1;
									if(num2>99 || num2<0){
										System.out.println("Invalid Taxi Number! [Taxi "+num2+"]");
										break;
									}
									findlist.add(new Syntagm("History0",num2,"History.txt"));
								}
								catch(Exception f){
									System.err.println("Invalid Input ["+m.group(0)+"]");
									break;
								}
							}
							else{
								try{
									double num1 = Double.parseDouble(sarray[2]);

									int num2 = (int) num1;
									if(num2>99 || num2<0){
										System.out.println("Invalid Taxi Number! [Taxi "+num2+"]");
										break;
									}
									findlist.add(new Syntagm("History1",num2,"History.txt"));
								}
								catch(Exception f){
									System.err.println("Invalid Input ["+m.group(0)+"]");
									break;
								}
							}
						}
						else{//增减道路指令
							//[DR,(Open|Close),(x,y),0|1]
							try{
								double d_x = Double.parseDouble(sarray[2]);
								double d_y = Double.parseDouble(sarray[3]);
								double d_z = Double.parseDouble(sarray[4]);
								
								int x = (int) d_x;
								int y = (int) d_y;
								int z = (int) d_z;
								
								if(z>1 || z<0 || x>80 || x<1 || y>80 || y<1){
									System.err.println("Invalid Input ["+m.group(0)+"]");
									break;
								}
								if(sarray[1].equals("Open")){
									z+=1;
								}
								else{
									z+=3;
								}
								maplist.add(new Syntagm(x,y,z,1,1,1));
							}
							catch(Exception e){
								System.err.println("Invalid Input ["+m.group(0)+"]");
								break;
							}
						}
					}while(false);
					line=line.replaceFirst(pattern, "");
				}
				line=line.replaceAll(" ", "").replaceAll("\t","");
				if(line.length()!=0){
					System.err.println("Invalid Input ["+line+"]");
				}
				
				int leng = calllist.size();
				for(int i=0;i<leng;i++){
					for(int j=0;j<ctrl.request_list.size();j++){
						Syntagm sold = calllist.get(i);
						Syntagm snew = ctrl.request_list.get(j).get(0);
						if(sold.line==snew.line 
						&& sold.list==snew.list
						&& sold.orderline==snew.orderline 
						&& sold.orderlist==snew.orderlist
						&& sold.ordertime==snew.ordertime){
							System.out.println("Same [CR,("+sold.line+","+sold.list+"),("+sold.orderline+","+sold.orderlist+")]");
							calllist.remove(i);
							i--;
							break;
						}
					}
					ctrl.addr_l(calllist.get(i));
					gui.RequestTaxi(new Point(calllist.get(0).line-1,calllist.get(0).list-1),new Point(calllist.get(0).orderline-1,calllist.get(0).orderlist-1));
				}
				calllist.clear();
				leng = findlist.size();
				for(int i=0;i<leng;i++){
					ctrl.addf_l(findlist.get(i));
				}
				findlist.clear();
				leng = maplist.size();
				boolean flag=false;
				for(int i=0;i<leng;i++){
					Syntagm si = maplist.get(i);
					map.addmap(si.line,si.list,si.value);
					flag = true;
				} 
				if(flag){
					try{
						map.setmap();
					}
					catch(Exception e){
					}
				}
				maplist.clear();
				
				
			}
			
			
		}
		catch(Exception e){
			System.err.println("Something wrong with the people in the city!");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
