package elevator;


import java.text.DecimalFormat;
import java.util.ArrayList;
public class scheduler_old {

	private int waynow=0;
//	private int floornow=1;
	private double finishtime=0;
	private ArrayList<jiegou> alllist=new ArrayList<jiegou>();
	private ArrayList<jiegou> mainlist=new ArrayList<jiegou>();
	private int alllistlong=0;
	private int mainlistlong=0;
	private int warn=1;
		
	private boolean btwjudge(int floororder,int floornow){
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
		
		private boolean btw(int floororder,String r,int flooradd,int wayadd,int floornow){
			if(btwjudge(floororder,floornow)){
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
		
		public void calculate(jiegou j,int floornow){
//			System.out.println("the main is:");
			
			if(!j.getvalid()){
				return;
			}
			j.setvalid();
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
					if(btw(j.getfloor(),ji.getr(),ji.getfloor(),t,floornow)){
						mainlist.add(ji);
						mainlistlong++;
					}
				}

			for(int t=0;t>0;t--){
//				System.out.println("begin at:"+finishfloor+"\nend with:"+j.getfloor());
				if(waynow==1){
					floornow+=1;
				}
				else if(waynow==-1){
					floornow-=1;
				}
				finishtime+=0.5;
//				System.out.println("floornow is:"+floornow);
//				System.out.println("timenow is:"+finishtime);
				boolean judge_shun=false;
				int fi=1;
				for(int k=0;k<mainlistlong;k++){
					if(mainlist.get(k).gettime()<finishtime){
						if(mainlist.get(k).getfloor()==floornow){
							
							String s;
							if(waynow==1){
								s="UP";
							}
							else{
								s="DOWN";
							}
							printf(mainlist.get(k),s);
							judge_shun=true;
							fi=mainlist.get(k).getfloor();
							mainlist.get(k).setvalid();
						}
					}
					else if(mainlist.get(k).gettime()>=finishtime){
						if(mainlist.get(k).getfloor()==floornow){
							if(waynow!=0){
								mainlist.remove(k);
								mainlistlong--;
							}
						}
					}
				}
				if(judge_shun && fi!=j.getfloor()){
				}
			}

			
			for(int x=0;x<mainlistlong;x++){
				if(mainlist.get(i).getvalid()){
					if(finishtime>mainlist.get(i).gettime()){
						calculate(mainlist.get(i),floornow);
						break;
					}
				}
			}
		}

		
		
	}

}
