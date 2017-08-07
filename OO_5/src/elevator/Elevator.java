package elevator;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;

class Elevator extends Thread{
	private boolean shut=true;
	private int mainflag=0;
	private int flag=0;
	private int waynow=0;
	private jiegou mainj=null;
	private int ctrl=0;
	private Thread t;
	private String ElevatorName;
	private double time=0;
	private int had=0;
	private double begin_time=0;
	private int floornow=1;
	private int floororder=0;
	private ArrayList<jiegou> hadlist=new ArrayList<jiegou>();
	private ArrayList<jiegou> list=new ArrayList<jiegou>();
	private ArrayList<jiegou> mainlist=new ArrayList<jiegou>();
	Elevator( String name){
		ElevatorName="Elevator"+name;
		ctrl=Integer.parseInt(name);
	}
	public boolean first_r_samejudge(jiegou j){
		if(list.size()==0){
			return false;
		}
		for(int i=0;i<list.size();i++){
			if(j.getfloor()==list.get(i).getfloor()){
				if(j.getr()==list.get(i).getr()){
					if(j.getway().equals(list.get(i).getway())){
						return true;
					}
				}
			}
		}
		return false;
	}
	public void settime(double time){
		begin_time=time;
		return;
	}
	public int getctrl(){
		return ctrl;
	}
	public void setflag(){
		flag=1;
		return;
	}
	public int getflag(){
		return flag;
	}
	public void gettime(){
		time=(System.currentTimeMillis()-begin_time)/1000;
		return;
	}
	public int gethad(){
		return had;
	}
	public void addmain(jiegou mainj){
		for(int i=0;i<list.size();i++){//add mainlist
			jiegou ji=list.get(i);
//			System.out.println("             CHECK:"+ji.getfloor()+ji.getvalid());
			if(ji.getvalid()){
				int t=0;
				if(ji.getway().equals("UP")){
					t=1;
				}
				else{
					t=-1;
				}
				if(btw(ji.getr(),ji.getfloor(),t)){
					mainlist.add(ji);
				}
			}
		}
		return;
	}
	public void addlist(jiegou j){
		if(j==null)return;
		list.add(j);
		samejudge(j);
		return;
	}
	public void setshut(){
		shut=false;
		return;
	}
	public void run(){
		while(shut || list.size()!=0){
			try{
				sleep(10);
			}catch(InterruptedException e){				
			}
			if(list.size()!=0){//if there are some request to deal
				flag=1;
				if(mainflag==0 || mainflag==2) {//if do not have a main_request
					if(mainflag==0){//if do not have a main_request at all
						mainj=list.get(0);
					}
					if(samejudge(mainj)){
						list.remove(mainj);
						continue;
					}
					floororder=mainj.getfloor();
					hadlist.add(mainj);
					if(floororder>floornow){//judge the way up/down/still
						waynow=1;
						flag=ctrl;
					}
					else if(floororder==floornow){
						waynow=0;
						flag=ctrl;
					}
					else{
						waynow=-1;
						flag=ctrl;
					}
					mainflag=1;
				}
				if(mainj.gettime()>time){//if the request time>old_time=>refresh the old time
					time=mainj.gettime();
				}
				else{
					mainj.setf_time(time);
				}
						
				floororder=mainj.getfloor();
					
				if(floornow-floororder!=0){//if do not get the main_request order floor
						
					//refresh the main list
					mainlist.clear();
					mainj.setvalid();
					flag=ctrl;

					//move a floor
					System.out.println("Elevator"+ctrl+" "+floornow+"->"+floororder);
					try{
						//Modify the moving +3s
						System.out.println("Elevator"+ctrl+" MOVING(3s)");
						sleep(3000);
						System.out.println("Elevator"+ctrl+" FINISH MOVING");
					}catch(InterruptedException e){
						
					}
					time+=3.0;
					mainj.setf_time(time);
					addmain(mainj);
					if(waynow==1){
						floornow+=1;
						had++;
					}
					else if(waynow==-1){
						floornow-=1;
						had++;
					}				
		
					//judge if could by the way
					boolean judge_shun=false;
					int fi=1;
					for(int i=0;i<mainlist.size();i++){
						if(mainlist.get(i).getfloor()==floornow){
							if(mainlist.get(i).gettime()<time){
								if(samejudge(mainlist.get(i))){
									continue;
								}
								String s="";
								if(waynow==1){
									s="UP";							
								}
								else if(waynow==-1){
									s="DOWN";
								}
								printf(mainlist.get(i),s);//print do not need the +6s(UP/DOWN)
								mainlist.get(i).setf_time(time+6.0);//save the btw request to the had_list
								hadlist.add(mainlist.get(i));
								judge_shun=true;
								fi=mainlist.get(i).getfloor();
								mainlist.get(i).setvalid();
							}
							else if(mainlist.get(i).gettime()>=time){
								if(mainlist.get(i).getfloor()==floornow){
									if(waynow!=0){
										mainlist.remove(i);
										i--;
									}
								}
							} 
						}
					}
					
					if(judge_shun && fi!=floororder){//if by the way in the way
						try{//modify open the door
							System.out.println("Elevator"+ctrl+" OPEN THE DOOR(6s)");
							sleep(6000);
							System.out.println("Elevator"+ctrl+" FINISH OPEN THE DOOR");
							time+=6.0;
						}catch(InterruptedException e){
							
						}
					}
				}
				else{//if the elevator get the main_request order floor
					mainflag=0;
					String s = (waynow==1)?("UP"):((waynow==0)?("STILL"):("DOWN"));
					printf(mainj,s);
					mainj.setf_time(time);
					mainj.setvalid();
					floororder=0;
					waynow=0;
					mainj=null;
					if(!s.equals("STILL")){
						try{//modify open the door //for the main_request
							System.out.println("Elevator"+ctrl+" OPEN THE DOOR(6s)");
							sleep(6000);
							System.out.println("Elevator"+ctrl+" FINISH OPEN THE DOOR");
							time+=6.0;
						}catch(InterruptedException e){
						}
					}					
					//invalid the main_request and the btw_request
					for(int i=0;i<list.size();i++){
						if(!(list.get(i).getvalid())){
							list.remove(i);
							i--;
						}
					}
					
					//if main_list do not empty(valid not empty)
					for(int i=0;i<mainlist.size();i++){
						if(mainlist.get(i).getvalid()){
							mainj=mainlist.get(i);
							mainflag=2;
							break;
						}
					}
					
				}
				
				
			}
			else{//if the list is empty
				flag=0;
			}
		}
		flag=0;
	}
	
	public void start(){
		if( t == null ){
			t=new Thread(this,ElevatorName);
			t.start();
		}
	}
	
	public int getfloor(){
		return floornow;
	}
	
	public boolean getshut(){
		return shut;
	}
	
	public int aaa(){
		return list.size();
	}
	
	//btw
	public boolean btw(String r,int flooradd,int wayadd){
		if(list.size()==0){
			return false;
		}
		if(floororder==0){
			floororder=list.get(0).getfloor();
			if(floororder>floornow){
				waynow=1;
			}
			else if(floororder==floornow){
				waynow=0;
			}
			else{
				waynow=-1;
			}
		}
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
	public boolean samejudge(jiegou j){
		for(int i=0;i<hadlist.size();i++){
			if(hadlist.get(i).getr()==j.getr()){
				if(hadlist.get(i).getfloor()==j.getfloor()){
					if(hadlist.get(i).getway().equals(j.getway())){
						if(hadlist.get(i).getftime()>=j.gettime())
							if(j.getvalid()){
								String s=j_s(j);
								long sysTime=System.currentTimeMillis();
								String str ="("+sysTime+")";
								try {
									controller.buffWriter.write(str+":SAME"+s+"\n");
									controller.buffWriter.flush();
								} catch (IOException e) {
									e.printStackTrace();
								}
								System.out.println(str+"SAME"+s);
								j.setvalid();
								return true;
							}
						}
					}
				}
			}	
		return false;
	}
	
	public String j_s(jiegou j){
		String s;
		DecimalFormat df=new DecimalFormat("#####0.0");
		if(j.getr().equals("ER")){
			s="["+j.getr()+",#"+j.getelevator()+","+j.getfloor()+","+df.format(j.gettime())+"]";
		}
		else{
			s="["+j.getr()+","+j.getfloor()+","+j.getway()+","+df.format(j.gettime())+"]";
		}
		return s;
	}
	public void printf(jiegou j,String s){
		DecimalFormat df=new DecimalFormat("#####0.0");
		
		if(s.equals("STILL")){//if print still
			//1.deal the door 2.print
			try{
				System.out.println("Elevator"+ctrl+" OPEN THE DOOR(6s)");
				sleep(6000);
				System.out.println("Elevator"+ctrl+" CLOSE THE DOOR");
			}catch(InterruptedException e){
				
			}
			long sysTime=System.currentTimeMillis();
			String str ="("+sysTime+")";
			String s_half="";
			if(j.getr().equals("ER")){
				s_half=str+":["+j.getr()+",#"+j.getelevator()+","+j.getfloor()+","+df.format(j.gettime())+"]/(";
			}
			else{
				s_half=str+":["+j.getr()+","+j.getfloor()+","+j.getway()+","+df.format(j.gettime())+"]/(";
			}
			time+=6.0;
			try {
				controller.buffWriter.write(s_half+"#"+ctrl+","+j.getfloor()+","+s+","+had+","+df.format(time)+")\n");
				controller.buffWriter.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
			System.out.println(s_half+"#"+ctrl+","+j.getfloor()+","+s+","+had+","+df.format(time)+")");
			return;
		}

		long sysTime=System.currentTimeMillis();
		String str ="("+sysTime+")";
		String s_half="";
		if(j.getr().equals("ER")){
			s_half=str+":["+j.getr()+",#"+j.getelevator()+","+j.getfloor()+","+df.format(j.gettime())+"]/(";
		}
		else{
			s_half=str+":["+j.getr()+","+j.getfloor()+","+j.getway()+","+df.format(j.gettime())+"]/(";
		}
		try {
			controller.buffWriter.write(s_half+"#"+ctrl+","+j.getfloor()+","+s+","+had+","+df.format(time)+")\n");
			controller.buffWriter.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(s_half+"#"+ctrl+","+j.getfloor()+","+s+","+had+","+df.format(time)+")");
		return;
	}
}
