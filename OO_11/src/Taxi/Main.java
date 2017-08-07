package Taxi;

/*
 * 			Overview
 * 
 * 			主函数，起到将各实例化对象传递给各类的作用
 * */
public class Main {
	public static long time=System.currentTimeMillis();
	public static boolean reset = false;

	/*不变式：
	 * 1	没什么需要限制的
	 * */
	public boolean repOK(){
		/*@Effects: \result==invariant(this).*/
		try{
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	public static void main(String[] args) {
		/* @ REQUIRES: none
		 * @ MODIFIES: none
		 * @ EFFECTS: 调度器，实例化各类，并将实例化对象传给需要共享实例化对象的相应类
		 */
		System.out.println("More Old_drivers are coming!");
		try{
			

			Light light = new Light();
			TaxiGUI gui=new TaxiGUI();
			Map map = new Map();
			Readin readin = new Readin();
			Controller ctrl = new Controller();
			People people = new People();
			readin.map=map;
			readin.light=light;
			people.ctrl=ctrl;
			
			if(!readin.read_map("in.txt")){
				return;
			}
			else{
				System.out.println("Map File read successfully!");
			}
			

			map.gui=gui;
			map.setmap();
			map.print_map();
			light.gui=gui;
			ctrl.map=map;
			people.map=map;
			gui.LoadMap(map.getmap_gui(), Readin.MAX);
			light.map=map;
			

			if(!readin.read_light("light.txt")){
				return;
			}
			else{
				System.out.println("Light File read successfully!");
			}
			
			people.gui=gui;
			
			Driver [] driver_list = init_taxi(gui,map,light);
			
			ctrl.driver_list = driver_list;
			
			people.start();
			light.start();
			ctrl.start();
			System.out.println("The drivers are all here now!");
			
			System.out.println("The people can call the taxi now!");
			
			
		}
		catch(Exception e){
			System.err.println("Something was wrong!");
		}
	}
	
	public static Driver[] init_taxi(TaxiGUI gui,Map map,Light light){
		/* @ REQUIRES: map!=null && map.repOK() 
		 * 			&& light!=null &&　light.repOK()
		 * 			&& gui !=null
		 * @ MODIFIES: none
		 * @ EFFECTS: 实例化30辆超级出租车和70辆普通出租车
		 * 			返回一个存放了上述100个实例化对象的Driver类型数组
		 */
		Driver [] driver_list = new Driver[102];
		
		//超级出租车
		for(int i=0;i<30;i++){
			driver_list[i] = new Driver_VIP(gui,i,map,light);
		}
		
		//普通出租车
		for(int i=30;i<100;i++){
			driver_list[i] = new Driver(gui,i,map,light);
		}
		
		return driver_list;
	}

}
