package Taxi;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class People extends Thread{
	public TaxiGUI gui;
	public Map map;
	public Controller ctrl;
	public boolean shut=true;
	public int time=0;
	public Scanner word=new Scanner(System.in);
	public ArrayList<Syntagm> maplist = new ArrayList<Syntagm>();
	public ArrayList<Syntagm> calllist = new ArrayList<Syntagm>();
	public ArrayList<Syntagm> findlist = new ArrayList<Syntagm>();
	
	public void run(){
		/* @ REQUIRES: none
		 * @ MODIFIES: none
		 * @ EFFECTS: modify the people's request,add them to the controller
		 */
		try{
			Random random = new Random();
			while(shut){
				sleep(10);
				String line = word.nextLine();
				
				String pattern = "\\[(CR),\\(([+-?]\\d*),([+-?]\\d*)\\),\\(([+-?]\\d*),([+-?]\\d*)\\)\\]|"
								+"\\[(FR),(Taxi|Status),([+-?]\\d*)\\]|"
								+"\\[(DR),(Open|Close),\\(([+-?]\\d*),([+-?]\\d*)\\),([+-?]\\d*)\\]";
				time=(int)((System.currentTimeMillis() - Main.time)%100);
				random.getsleeptime100();
				
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
									System.out.println("num1:"+num5+" num2:"+num6+" num3:"+num7+"num4:"+num8);
									System.err.println("Invalid input-"+m.group(0));
									break;
								}
								if(num5==num7 && num6==num8){
									System.err.println("Where are you going???");
									break;
								}
//								gui.RequestTaxi(new Point(num5-1,num6-1),new Point(num7-1,num8-1));
								time = ((int) (System.currentTimeMillis()-Main.time))/100;
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
							else{
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
					ctrl.addr_l(calllist.get(0));
					gui.RequestTaxi(new Point(calllist.get(0).line-1,calllist.get(0).list-1),new Point(calllist.get(0).orderline-1,calllist.get(0).orderlist-1));
					calllist.remove(0);
				}
				leng = findlist.size();
				for(int i=0;i<leng;i++){
					ctrl.addf_l(findlist.get(0));
					findlist.remove(0);
				}
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
				
				int sleeptime=random.getsleeptime100();
				if(sleeptime<0){
					System.err.println("What's wrong with the people? The taxi drive their bananas?");
				}
				
				
			}
			
			
		}
		catch(Exception e){
			System.err.println("Something wrong with the people in the city!");
		}
	}
	
	
	
	
	
	
	
	
	
	
	
	
}
