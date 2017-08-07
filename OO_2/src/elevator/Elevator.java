package elevator;

import java.text.DecimalFormat;
import java.util.ArrayList;

class Elevator {
	private int finishfloor=1;
	private double finishtime=0;
	private ArrayList<jiegou> list=new ArrayList<jiegou>();
	private int listlong=0;
	private int warn=1;
	
	private double sub(double a,double b){
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
	
	public void calculate(jiegou j){
		
		/*
		if(j.getr().equals("ER")){
		//		finishtime=max(finishtime,j.gettime());
			if(j.gettime()<=finishtime){
				for(int i=0;i<listlong;i++){
					if(list.get(i).getr()==j.getr()){		
						if(list.get(i).getfloor()==j.getfloor()){
							if(list.get(i).getway().equals(j.getway())){
								if(list.get(i).gettime()>=j.gettime()){
									return;
								}
							}
						}
					}
				}
				if(j.getfloor()==finishfloor){
					finishtime+=(sub(finishfloor,j.getfloor()))*0.5;
					oac_door();
					System.out.println("("+j.getfloor()+",STILL,"+finishtime+")");
					warn=0;
				}
				else{
					finishtime+=(sub(finishfloor,j.getfloor()))*0.5;
					String s= (j.getfloor()>finishfloor)?("UP"):("DOWN");
					System.out.println("("+j.getfloor()+","+s+","+finishtime+")");
					warn=0;
					oac_door();
				}
				finishfloor=j.getfloor();
				list.add(new jiegou(j.getr(),j.getfloor(),j.getway(),finishtime));
				listlong++;
			}
			else{
				finishtime=j.gettime();
				if(j.getfloor()==finishfloor){
					finishtime+=(sub(finishfloor,j.getfloor()))*0.5;
					oac_door();
					System.out.println("("+j.getfloor()+",STILL,"+finishtime+")");
					warn=0;
				}
				else{
					finishtime+=(sub(finishfloor,j.getfloor()))*0.5;
					String s= (j.getfloor()>finishfloor)?("UP"):("DOWN");
					System.out.println("("+j.getfloor()+","+s+","+finishtime+")");
					warn=0;
					oac_door();
				}
				finishfloor=j.getfloor();
				list.add(new jiegou(j.getr(),j.getfloor(),j.getway(),finishtime));
				listlong++;
			}
				
			
		}
		else{*/
			if(j.gettime()<=finishtime){
				for(int i=0;i<listlong;i++){
					if(list.get(i).getr()==j.getr()){		
						if(list.get(i).getfloor()==j.getfloor()){
							if(list.get(i).getway().equals(j.getway())){
								if(list.get(i).gettime()>=j.gettime()){
									return;
								}
							}
						}
					}
				}
			}
			else{
				finishtime=j.gettime();
			}
			finishtime+=(sub(finishfloor,j.getfloor()))*0.5;

			DecimalFormat df=new DecimalFormat("#####0.0");
			if(j.getfloor()==finishfloor){
				oac_door();
				System.out.println("("+j.getfloor()+",STILL,"+df.format(finishtime)+")");
				warn=0;
			}
			else{
				String s = (j.getfloor()>finishfloor)?("UP"):("DOWN");
				System.out.println("("+j.getfloor()+","+s+","+df.format(finishtime)+")");
				warn=0;
				oac_door();
			}
			finishfloor=j.getfloor();
			list.add(new jiegou(j.getr(),j.getfloor(),j.getway(),finishtime));
			listlong++;
	}
	
	private void oac_door(){//open and close the door +1s!
		finishtime++;
	}
}
