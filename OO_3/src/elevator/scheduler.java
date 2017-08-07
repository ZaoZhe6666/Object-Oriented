package elevator;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class scheduler extends Elevator{
	private int waynow=0;
	private int floornow=1;
	private int finishfloor=1;
	private double finishtime=0;
	private ArrayList<jiegou> alllist=new ArrayList<jiegou>();
	private ArrayList<jiegou> mainlist=new ArrayList<jiegou>();
	private int alllistlong=0;
	private int mainlistlong=0;
	private int warn=1;
	
	private boolean btwjudge(int floororder){
		if(waynow==1){
			if(floornow<floororder){
				return true;
			}
		}
		else if(waynow==-1){
			if(floornow>floororder){
				return true;
			}
		}
		else{
			return false;
		}
		return false;
	}
	
	private boolean btw(int floororder,String r,int flooradd,int wayadd){
		if(btwjudge(floororder)){
			if(r.equals("ER")){
				if(waynow==1){
					if(flooradd>floornow){
						return true;
					}
				}
				else if(waynow==-1){
					if(flooradd<floornow){
						return true;
					}
				}
			}
			else{
				if(wayadd==waynow){
					if(wayadd==1){
						if(flooradd<=floororder){
							if(flooradd>floornow){
								return true;
							}
						}
					}
					else if(wayadd==-1){
						if(flooradd>=floororder){
							if(flooradd<floornow){
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	public void savelist(jiegou j){
		alllist.add(j);
		alllistlong++;
		return;
	}
	
	public int getwarn(){
		return warn;
	}
	
	public void printf(jiegou j,String s){
		DecimalFormat df=new DecimalFormat("#####0.0");
		if(j.getr().equals("ER")){
			System.out.print("["+j.getr()+","+j.getfloor()+","+(int)j.gettime()+"]/(");
		}
		else{
			System.out.print("["+j.getr()+","+j.getfloor()+","+j.getway()+","+(int)j.gettime()+"]/(");
		}
		System.out.println(j.getfloor()+","+s+","+df.format(finishtime)+")");
		return;
	}
	
	public void calculate(jiegou j){
		System.out.println("the main is:");
		printf(j,"???");
		setfinishtime(finishtime);
		
		if(!j.getvalid()){
			return;
		}
		if(samejudge(j)){
			return;
		}
		
		finishtime=getfinishtime();
		j.setvalid();
		finishfloor=floornow;
		if(j.getfloor()>floornow){
			waynow=1;
		}
		else if(j.getfloor()==floornow){
			waynow=0;
		}
		else{
			waynow=-1;
		}
		mainlist.clear();
		mainlistlong=0;
		for(int i=0;i<alllistlong;i++){
			jiegou ji=alllist.get(i);
			if(ji.getvalid()){
				int t=0;
				if(ji.getway().equals("UP")){
					t=1;
				}
				else{
					t=-1;
				}
				if(btw(j.getfloor(),ji.getr(),ji.getfloor(),t)){
					mainlist.add(ji);
					mainlistlong++;
				}
			}
		}
		System.out.println("mlong is:"+mainlistlong);

		listadd(j.getr(),j.getfloor(),j.getway(),finishtime+0.5*sub(j.getfloor(),floornow)+1.0);
		for(int i=sub(finishfloor,j.getfloor());i>0;i--){
//			System.out.println("begin at:"+finishfloor+"\nend with:"+j.getfloor());
			if(waynow==1){
				floornow+=1;
			}
			else if(waynow==-1){
				floornow-=1;
			}
			finishtime+=0.5;
			setfinishtime(finishtime);
//			System.out.println("floornow is:"+floornow);
//			System.out.println("timenow is:"+finishtime);
			boolean judge_shun=false;
			int fi=1;
			for(int k=0;k<mainlistlong;k++){
				if(mainlist.get(k).gettime()<finishtime){
					if(mainlist.get(k).getfloor()==floornow){
						if(samejudge(mainlist.get(k))){
							continue;
						}
						String s;
						if(waynow==1){
							s="UP";
						}
						else{
							s="DOWN";
						}
						finishtime=getfinishtime();
						printf(mainlist.get(k),s);
						judge_shun=true;
						listadd(mainlist.get(k).getr(),mainlist.get(k).getfloor(),mainlist.get(k).getway(),finishtime+1.0);
						fi=mainlist.get(k).getfloor();
						mainlist.get(k).setvalid();
					}
				}
				else if(mainlist.get(k).gettime()>=finishtime){
					System.out.println(finishtime+" floornow:"+floornow);
					if(mainlist.get(k).getfloor()==floornow){
						if(waynow!=0){
							mainlist.remove(k);
							mainlistlong--;
						}
					}
				}
			}
			if(judge_shun && fi!=j.getfloor()){
				finishtime=getfinishtime();
				oac_door();
				finishtime=getfinishtime();
			}
		}

		setfloor(finishfloor);
		print(j);
		warn=0;
		
		for(int i=0;i<mainlistlong;i++){
			if(mainlist.get(i).getvalid()){
				if(finishtime>mainlist.get(i).gettime()){
					finishtime=getfinishtime();
					calculate(mainlist.get(i));
					break;
				}
			}
		}
		finishtime=getfinishtime();
		listadd(j.getr(),j.getfloor(),j.getway(),finishtime);
	}

	
	public String toString(){
		String s="the state is:"+waynow;
		return s;
	}
	public String toString(int a){
		String s="the floor is:"+floornow;
		return s;
	}
	
}
