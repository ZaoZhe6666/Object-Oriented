package elevator;

import java.text.DecimalFormat;
import java.util.ArrayList;

class Elevator implements Ele_interface {
	private int finishfloor=1;
	private double finishtime=0;
	private ArrayList<jiegou> list=new ArrayList<jiegou>();
	private int listlong=0;
	private int warn=1;
	
	
	public int sub(int a,int b){
		if(a>b){
			return a-b;
		}
		else{
			return b-a;
		}
	}
	
	public int getwarn(){
		return warn;
	}
	
	public void listadd(String a,int b,String c, double d){
		list.add(new jiegou(a,b,c,d));
		listlong++;
		return;
	}
	
	public String j_s(jiegou j){
		String s;
		if(j.getr().equals("ER")){
			s="["+j.getr()+","+j.getfloor()+","+(int)j.gettime()+"]";
		}
		else{
			s="["+j.getr()+","+j.getfloor()+","+j.getway()+","+(int)j.gettime()+"]";
		}
		return s;
	}
	
	public boolean samejudge(jiegou j){
//		System.out.println("finish:"+finishtime);
//		System.out.println("gettime:"+j.gettime());
		if(j.gettime()<=finishtime){
			for(int i=0;i<listlong;i++){
				if(list.get(i).getr()==j.getr()){
					if(list.get(i).getfloor()==j.getfloor()){
						if(list.get(i).getway().equals(j.getway())){
							if(list.get(i).gettime()>=j.gettime()){
								if(j.getvalid()){
									String s=j_s(j);
									System.out.println("SAME"+s);
									j.setvalid();
									return true;
								}
							}
						}
					}
				}
			}
		}
		else{
			finishtime=j.gettime();
		}
		return false;
	}
	
	public double getfinishtime(){
		return finishtime;
	}
	
	public void setfinishtime(double a){
		finishtime=a;
		return;
	}
	
	public void print(jiegou j){
		if(j.getr().equals("ER")){
			System.out.print("["+j.getr()+","+j.getfloor()+","+(int)j.gettime()+"]/");
		}
		else{
			System.out.print("["+j.getr()+","+j.getfloor()+","+j.getway()+","+(int)j.gettime()+"]/");
		}
		DecimalFormat df=new DecimalFormat("#####0.0");
		if(j.getfloor()==finishfloor){
			oac_door();
			System.out.println("("+j.getfloor()+",STILL,"+df.format(finishtime)+")");
		}
		else{
			String s = (j.getfloor()>finishfloor)?("UP"):("DOWN");
			System.out.println("("+j.getfloor()+","+s+","+df.format(finishtime)+")");
			oac_door();
		}
		return;
	}
	
	public void calculate(jiegou j){
			if(samejudge(j)){
				return;
			}
			
			finishtime+=(sub(finishfloor,j.getfloor()))*0.5;
			warn=0;
			print(j);
			
			finishfloor=j.getfloor();
			
			listadd(j.getr(),j.getfloor(),j.getway(),finishtime);
	}
	
	public void setfloor(int a){
		finishfloor=a;
		return;
	}
	
	public void oac_door(){//open and close the door +1s!
		finishtime++;
		return;
	}
	
}
