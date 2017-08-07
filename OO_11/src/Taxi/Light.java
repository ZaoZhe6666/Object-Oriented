package Taxi;

import java.util.ArrayList;

/*
 * 			Overview 
 * 
 * 		红绿灯类，模拟红绿灯亮灭，存储有红绿灯的点
 * 		每一个红绿灯变换周期，改变一次所有的红绿灯的方向
 * 
 * */
public class Light extends Thread{
	final public Random random = new Random();
	public static boolean reset=false;
	public TaxiGUI gui;
	public Map map;
	final public int light_map[][]=new int[Readin.MAX+5][Readin.MAX+5];
	final public ArrayList<Integer>light_location = new ArrayList<Integer>();
	public static int light_change_time=0;
	public int last_time=0;
	public	boolean clock=true;
	public boolean shut =true;
	/*
	 * 		clock value
	 * 		false		south-north close
	 * 		true		east-west	close
	 * */
	
	/*不变式：
	 * 1	random为Random型变量,不为Null
	 * 2	gui为TaxiGUI型变量,不为Null
	 * 3	map为Map型变量,不为Null
	 * 4	light_map不为Null，其中每一项a均满足
	 * 			0<=a<=1
	 * 5	light_location不为Null，为其中每一个子项均不为Null,且为Integer型变量的ArrayList型数组
	 * 		light_location的每个子项a均满足
	 * 			1<=a/100<=80
	 * 			1<=a%100<=80
	 * 6	50<=light_change_time<=100
	 * 7	last_time>=0
	 * */
	public boolean repOK(){
		/*@Effects: \result==invariant(this).*/
		try{
			if(random==null || !random.repOK()
			|| gui==null 
			|| light_map==null || light_location==null 
			|| map==null || !map.repOK()){
				return false;
			}
			for(int i=0;i<light_location.size();i++){
				if(light_location.get(i)==null){
					return 	false;
				}
			}
			if(!(map instanceof Map)
			|| !(gui instanceof TaxiGUI)
			|| !(random instanceof Random)
			|| !(light_location instanceof ArrayList)){
				return false;
			}
			for(int i=0;i<Readin.MAX+2;i++){
				for(int j=0;j<Readin.MAX;j++){
					if(light_map[i][j]<0 || light_map[i][j]>1){
						return false;
					}
				}
			}
			for(int i=0;i<light_location.size();i++){
				if(!(light_location.get(i) instanceof Integer)){
					return false;
				}
			}
			for(int i=0;i<light_location.size();i++){
				int t=light_location.get(i);
				int line = t/100;
				int list = t%100;
				if(line<1 || line>80
				|| list<1 || list>80){
					return false;
				}
			}
			if(light_change_time>500 || light_change_time<200){
				return false;
			}
			if(last_time<0){
				return false;
			}
			
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	
	Light(){
		/* @ REQUIRES:	clock!=null && random!=null && readin.max!=null
		 * @ MODIFIES:	clock,light_map
		 * @ EFFECTS: 	初始化红绿灯变灯周期，初始化红绿灯(最开始是南北红还是东西红)
		 */
		this.clock=(random.get_random(2)==2);
		
		light_change_time=random.get_random(301);
		light_change_time+=199;
	}
	
	
	public void run(){
		/* @ REQUIRES:	Driver.time!=null && guigv!=null && light_location!=null && map!=null
		 * @ MODIFIES:	clock,light_map
		 * @ EFFECTS: 	每一个红绿灯变灯周期过去后，都改变红绿灯的方向，并同步在gui上
		 * 				每次增删边后，都会重新判断有红绿灯的点是否还为丁字路口或十字路口，若不是，则提示并删除该红绿灯
		 */
		try{
			while(shut){
				sleep(5);
				if(last_time==Controller.time){
					continue;
				}
				if(reset){//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
					for(int i=0;i<light_location.size();i++){
						int line = this.light_location.get(i)/100;
						int list = this.light_location.get(i)%100;
						int way = (map.can_up(line, list,map.road_map)?1:0)+
								  (map.can_down(line, list,map.road_map)?1:0)+
								  (map.can_left(line, list,map.road_map)?1:0)+
								  (map.can_right(line, list,map.road_map)?1:0);
						if(way<=2){
							light_map[line][list]=0;
							guigv.lightmap[line-1][list-1]=0;
							System.out.println("Do not need a traffic light in ("+line+","+list+") !");
							light_location.remove(i);
							continue;
						}
					}
					reset=false;
				}
				if(Controller.time%light_change_time==0){
					clock=!clock;
					for(int i=0;i<light_location.size();i++){
						int line = this.light_location.get(i)/100;
						int list = this.light_location.get(i)%100;
						guigv.lightmap[line-1][list-1]=clock?1:2;
					}
				}
				last_time=Controller.time;
			}
		}catch(Exception e){
			System.out.println("Something wrong with the signal light!");
		}
	}
}
