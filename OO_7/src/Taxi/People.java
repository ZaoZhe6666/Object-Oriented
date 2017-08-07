package Taxi;

import java.awt.Point;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class People extends Thread{
	private TaxiGUI gui;
	private Controller ctrl;
	private boolean shut=true;
	private int time=0;
	private Scanner word=new Scanner(System.in);
	private ArrayList<Syntagm> calllist = new ArrayList<Syntagm>();
	private ArrayList<Syntagm> findlist = new ArrayList<Syntagm>();
	
	public void setshut(){
		this.shut=false;
	}
	public void setgui(TaxiGUI gui){
		this.gui=gui;
	}
	public void setctrl(Controller ctrl){
		this.ctrl=ctrl;
	}
	public void run(){
		try{
			Random random = new Random();
			while(shut){
				sleep(10);
				String line = word.nextLine();
				
				String pattern = "\\[(CR),\\(([+-?]\\d*),([+-?]\\d*)\\),\\(([+-?]\\d*),([+-?]\\d*)\\)\\]|\\[(FR),(Taxi|Status),([+-?]\\d*)\\]";
				time=(int)((System.currentTimeMillis() - Main.time)%100);
				random.getsleeptime100();
				
				Pattern r = Pattern.compile(pattern);
				Matcher m = r.matcher(line);
				
				
				
				
				
				while(m.find()){
					do{
						try{
							if(m.group(1).equals("CR")){
								try{
									double num1 = Double.parseDouble(m.group(2));
									double num2 = Double.parseDouble(m.group(3));
									double num3 = Double.parseDouble(m.group(4));
									double num4 = Double.parseDouble(m.group(5));
									
									int num5 = (int) num1;
									int num6 = (int) num2;
									int num7 = (int) num3;
									int num8 = (int) num4;
									
									System.out.println(" "+num5+" "+num6+" "+num7+" "+num8);
									if(num5<=0 || num5>80 || num6<=0 || num6>80 || num7<=0 ||num7>80 ||num8 <=0 ||num8>80){
										System.out.println("num1:"+num5+" num2:"+num6+" num3:"+num7+"num4:"+num8);
										System.err.println("Invalid input-"+m.group(0));
										break;
									}
									if(num5==num7 && num6==num8){
										System.err.println("Where are you going???");
										break;
									}
									gui.RequestTaxi(new Point(num5-1,num6-1),new Point(num7-1,num8-1));
									time = ((int) (System.currentTimeMillis()-Main.time))/100;
									Syntagm snew = new Syntagm(num5,num6,num7,num8,time);
									for(int i=0;i<calllist.size();i++){
										if(calllist.get(i).equals(snew)){
											calllist.remove(i);
											break;
										}
									}
									calllist.add(snew);
								}
								catch(Exception e){
									System.out.println("The catch wrong");
									System.err.println("Invalid input-"+m.group(0));
									break;
								}				
							}
						}catch(Exception e){
							if(m.group(6).equals("FR")){
							if(m.group(7).equals("Taxi")){
								try{
									double num1 = Double.parseDouble(m.group(8));
									
									int num2 = (int) num1;
									if(num2>100 || num2<1){
										System.out.println("Invalid Taxi Number! [Taxi "+num2+"]");
										break;
									}
									findlist.add(new Syntagm("Taxi",(num2-1),"Detail.txt"));
								}
								catch(Exception f){
									System.err.println("Invalid Input ["+m.group(0)+"]");
									break;
								}
							}
							else{
								try{
									double num1 = Double.parseDouble(m.group(8));
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
					calllist.remove(0);
				}
				leng = findlist.size();
				for(int i=0;i<leng;i++){
					ctrl.addf_l(findlist.get(0));
					findlist.remove(0);
				}
				
				
				
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
