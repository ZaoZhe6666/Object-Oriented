package Taxi;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;


class mapInfo{
	int[][] map=new int[80][80];
	public void readmap(String path){//读入地图信息
		//Requires:String类型的地图路径,System.in
		//Modifies:System.out,map[][]
		//Effects:从文件中读入地图信息，储存在map[][]中
		Scanner scan=null;
		File file=new File(path);
		if(file.exists()==false){
			System.out.println("地图文件不存在,程序退出");
			System.exit(1);
			return;
		}
		try {
			scan = new Scanner(new File(path));
		} catch (FileNotFoundException e) {
			
		}
		for(int i=0;i<80;i++){
			String[] strArray = null;
			try{
				strArray=scan.nextLine().split("");
			}catch(Exception e){
				System.out.println("地图文件信息有误，程序退出");
				System.exit(1);
			}
			for(int j=0;j<80;j++){
				try{
					this.map[i][j]=Integer.parseInt(strArray[j]);
				}catch(Exception e){
					System.out.println("地图文件信息有误，程序退出");
					System.exit(1);
				}
			}
		}
		scan.close();
	}
}

public class Main {
	public static long time=System.currentTimeMillis();

	public static void main(String[] args) {
		System.out.println("Old_drivers are coming!");
		try{
			TaxiGUI gui=new TaxiGUI();
			

			ArrayList<Driver> driver_list = new ArrayList<Driver>();
			Map map = new Map();
			Readin readin = new Readin();
			Controller ctrl = new Controller();
			People people = new People();
			Random random = new Random();
			SafeFile sf = new SafeFile();
			
			readin.getmap(map);
			people.setctrl(ctrl);
			
			if(!readin.read()){
				return;
			}
			else{
				System.out.println("File-read successfully!");
			}

			map.setmap();
			map.print_map();
			
			ctrl.setmap(map);
			gui.LoadMap(map.getmap_gui(), Readin.MAX);
			
			for(int i=0;i<100;i++){
				
				int line = (int) (Math.random()*80)+1;
				int list = (int) (Math.random()*80)+1;;
//				int line = 1;
//				int list = 1;
				driver_list.add(new Driver((i+1),line,list,map));
				ctrl.getd_l(driver_list.get(i));
				gui.SetTaxiStatus(i,new Point(line,list),2);
				driver_list.get(i).setgui(gui);
			}
			for(int i=0;i<100;i++){
				driver_list.get(i).start();
			}
			System.out.println("The drivers are all here now!");
			
			
			people.setgui(gui);
			ctrl.setsf(sf);
			
			
			
			
			people.start();
			ctrl.start();
			
			System.out.println("The people can call the taxi now!");
			
			
		}
		catch(Exception e){
			System.err.println("Something was wrong!");
		}
	}

}
