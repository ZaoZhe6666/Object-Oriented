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
			Driver driver = new Driver(gui);
			driver.map=map;
			driver.light=light;
			light.map=map;
			

			if(!readin.read_light("light.txt")){
				return;
			}
			else{
				System.out.println("Light File read successfully!");
			}
			
			people.gui=gui;
			ctrl.driver=driver;
			driver.ctrl=ctrl;
			
			
			people.start();
			driver.start();
			light.start();
			System.out.println("The drivers are all here now!");
			
			System.out.println("The people can call the taxi now!");
			
			
		}
		catch(Exception e){
			System.err.println("Something was wrong!");
		}
	}

}
