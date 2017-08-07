package elevator;

import java.util.ArrayList;

//public class scheduler extends Thread{
public class scheduler extends scheduler_old implements Runnable{
	
	Elevator E1,E2,E3;
	private boolean shut=true;
	private Tray tray;
	private Queue queue;
	private ArrayList<jiegou> list=new ArrayList<jiegou>();
	scheduler(){
	}
	public void setshut(boolean shut){
		this.shut=shut;
		return;
	}
	public void setelevator(Elevator E1,Elevator E2,Elevator E3){
		this.E1=E1;
		this.E2=E2;
		this.E3=E3;
		return;
	}
	public void settray(Tray tray){
		this.tray=tray;
		return;
	}
	public void setqueue(Queue queue){
		this.queue=queue;
	}
	public Elevator min(){
		int a=E1.gethad();
		int b=E2.gethad();
		int c=E3.gethad();
		if(a<=b && a<=c){
			return E1;
		}
		else if(b<=a && b<=c){
			return E2;
		}
		else{
			return E3;
		}
	}
	
	public Elevator max(){
		int a=E1.gethad();
		int b=E2.gethad();
		int c=E3.gethad();
		if(c>=b && c>=a){
			return E3;
		}
		else if(b>=a && b>=c){
			return E2;
		}
		else{
			return E1;
		}
	}
	
	public Elevator middle(){
		if(E1.getctrl()==min().getctrl() && E2.getctrl()==max().getctrl()){
			return E3;//1<3<2
		}
		else if(E1.getctrl()==min().getctrl() && E3.getctrl()==max().getctrl()){
			return E2;//1<2<3
		}
		else if(E2.getctrl()==min().getctrl() && E1.getctrl()==max().getctrl()){
			return E3;//2<3<1
		}
		else if(E2.getctrl()==min().getctrl() && E3.getctrl()==max().getctrl()){
			return E1;//2<1<3
		}
		else if(E3.getctrl()==min().getctrl() && E1.getctrl()==max().getctrl()){
			return E2;//3<2<1
		}
		else{
			return E1;//3<1<2
		}
	}
	
	
	public void run(){
		while(shut || list.size()!=0){
			try{
				list=queue.getlist();
				Thread.sleep(5);
				if(list.size()!=0){
					jiegou j;
					for(int i=0;i<list.size();i++){
						j=list.get(i);
						if(j.getvalid()){
							if(j.getr().equals("FR")){
								int x=0;
								

								//judge same
								if(E1.getflag()!=0){
									if(E1.first_r_samejudge(j)){
										E1.addlist(j);
										list.remove(i);
										i--;
										continue;
									}
								}
								else if(E2.getflag()!=0){
									if(E2.first_r_samejudge(j)){
										E2.addlist(j);
										list.remove(i);
										i--;
										continue;
									}
								}
								else if(E3.getflag()!=0){
									if(E3.first_r_samejudge(j)){
										E3.addlist(j);
										list.remove(i);
										i--;
										continue;
									}
								}
								
								
								//judge if by the way
								if(j.getway().equals("UP")){
									x=1;
								}
								else{
									x=-1;
								}
								if(min().btw("FR",j.getfloor(),x)){
									min().addlist(j);
									list.remove(i);
									i--;
//									System.out.println("min "+min().getctrl());
									continue;
								}
								else if(middle().btw("FR",j.getfloor(),x)){
									middle().addlist(j);
									list.remove(i);
									i--;
//									System.out.println("middle "+middle().getctrl());
									continue;
								}
								else if(max().btw("FR",j.getfloor(),x)){
									max().addlist(j);
									list.remove(i);
									i--;

//									System.out.println("max "+max().getctrl());
									continue;
								}
								
								
								//some elevator stop
								if(min().getflag()==0){
									min().addlist(j);
									list.remove(i);
									i--;
									min().setflag();
//									System.out.println("min2 "+min().getctrl());
									continue;
								}
								else if(middle().getflag()==0){
									middle().addlist(j);
									list.remove(i);
									i--;
									middle().setflag();
//									System.out.println("middle2 "+middle().getctrl());
									continue;
								}
								else if(max().getflag()==0){
									max().addlist(j);
									list.remove(i);
									i--;
									max().setflag();
//									System.out.println("max2 "+max().getctrl());
									continue;
								}
							}
							else{//if ER request
								if(j.getelevator()==1){
									E1.addlist(j);
									E1.setflag();
								}
								else if(j.getelevator()==2){
									E2.addlist(j);
									E2.setflag();
								}
								else if(j.getelevator()==3){
									E3.addlist(j);
									E3.setflag();
								}
								list.remove(i);
								i--;
							}
						}
						else{//if j is invalid
							list.remove(i);
							i--;
						}
					}
				}
				else{
					queue.setlist();
				}
			}catch(InterruptedException e){
				
			}
			
			
		}
	}
	
	
}
