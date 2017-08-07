package Taxi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
 * 		Overview
 * 
 * 		文件读入类，从文件读入通路地图和红绿灯地图
 * */
public class Readin {
	public static int MAX=80;
	public Map map;
	public TaxiGUI gui;
	public Light light;
	
	/*不变式：
	 * 1	MAX=80
	 * 2	gui为TaxiGUI型变量,不为Null
	 * 3	map为Map型变量,不为Null
	 * 4	light为Light型变量,不为Null
	 * */
	public boolean repOK(){
		/*@Effects: \result==invariant(this).*/
		try{
			if(map==null || !map.repOK()
			|| gui==null 
			|| light==null || !light.repOK()){
				return false;
			}
			if(!(map instanceof Map)
			|| !(gui instanceof TaxiGUI)
			|| !(light instanceof Light)){
				return false;
			}
			if(MAX!=80){
				return false;
			}
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	public int read_light_from_file(String path){
		/* @ REQUIRES: String path 
		 * 			&& map!=null &&	map.repOK()
		 * 			&& light!=null &&	light.repOK()
		 * 			&&	repOK()
		 * @ MODIFIES: light
		 * @ EFFECTS: read from the file and save it to the light map
		 * return value
		 * 
		 * 0	correct
		 * -1	detail error
		 * -2	file error
		 * */
		try{
			File file = new File(path);
			if(file.exists()){
				if(file.isDirectory()){
					System.err.println(path+" is not a file!");
					return -2;
				}
				BufferedReader reader = null;
				reader = new BufferedReader(new FileReader(file));
				String tempString =null;
				String pattern = "[0-9]";
				int line=0;
				while((tempString = reader.readLine())!=null){
					line++;
					if(line>MAX){
						System.err.println("Out of "+MAX+" Lines!");
						reader.close();
						return -1;
					}
					Pattern r = Pattern.compile(pattern); 
					Matcher m = r.matcher(tempString);
					int list=0;
					while(m.find()){
						list++;
						if(list>MAX){
							System.err.println("In line "+line+" Out of "+MAX+" Numbers!");
							reader.close();
							return -1;
						}
						if(m.group(0).equals("0")){
							light.light_map[line][list]=0;
							guigv.lightmap[line-1][list-1]=0;
						}
						else if(m.group(0).equals("1")){
							int way = (map.can_up(line, list,map.road_map)?1:0)+
									  (map.can_down(line, list,map.road_map)?1:0)+
									  (map.can_left(line, list,map.road_map)?1:0)+
									  (map.can_right(line, list,map.road_map)?1:0);
							if(way<=2){
								light.light_map[line][list]=0;
								guigv.lightmap[line-1][list-1]=0;
								System.out.println("Do not need a traffic light in ("+line+","+list+") !");
								continue;
							}
							light.light_map[line][list]=1;
							guigv.lightmap[line-1][list-1]=light.clock?1:2;
							light.light_location.add(line*100+list);
						}
						else{
							System.err.println("In line "+line+" list "+list+
									" Wrong Number--"+ m.group(0));
							reader.close();
							return -1;
						}
					}
					if(list!=MAX){
						System.err.println("In line "+line+" Less than "+MAX+" Numbers!");
						reader.close();
						return -1;
					}
					tempString = tempString.replaceAll("0","");
					tempString = tempString.replaceAll("1","");
					tempString = tempString.replaceAll(" ","");
					tempString = tempString.replaceAll("\t","");
					if(tempString.length()!=0){
						System.err.println("In line "+line+" Invalid word["+tempString+"]");
						reader.close();
						return -1;
					}
				}
				if(line!=MAX){
					System.err.println("In line "+line+" Less than "+MAX+" Numbers!");
					reader.close();
					return -1;
				}
				
				reader.close();
			}
			else{
				System.err.println("The file do not exists!");
				return -2;
			}
		}
		catch(Exception e){
			System.err.println("Something was wrong with the file "+path+" !"+e);
			return -2;
		}
		return 0;
	}
	
	public int read_map_from_file(String path){
		/* @ REQUIRES: String path && map!=null 
		 * 			&&	repOK()
		 * 			&&	map.repOK()
		 * @ MODIFIES: map
		 * @ EFFECTS: read from the file and save it to the map
		 * return value
		 * 
		 * 0	correct
		 * -1	detail error
		 * -2	file error
		 * */
		try{
			File file = new File(path);
			if(file.exists()){
				if(file.isDirectory()){
					System.err.println(path+" is not a file!");
					return -2;
				}
				BufferedReader reader = null;
				reader = new BufferedReader(new FileReader(file));
				String tempString =null;
				String pattern = "[0-9]";
				int line=0;
				while((tempString = reader.readLine())!=null){
					line++;
					if(line>MAX){
						System.err.println("Out of "+MAX+" Lines!");
						reader.close();
						return -1;
					}
					Pattern r = Pattern.compile(pattern); 
					Matcher m = r.matcher(tempString);
					int list=0;
					while(m.find()){
						list++;
						if(list>MAX){
							System.err.println("In line "+line+" Out of "+MAX+" Numbers!");
							reader.close();
							return -1;
						}
						if(m.group(0).equals("0")){
							map.addmap(line,list,0);
						}
						else if(m.group(0).equals("1")){
							map.addmap(line,list,1);
						}
						else if(m.group(0).equals("2")){
							map.addmap(line,list,2);
						}
						else if(m.group(0).equals("3")){
							map.addmap(line,list,3);
						}
						else{
							System.err.println("In line "+line+" list "+list+
									" Wrong Number--"+ m.group(0));
							reader.close();
							return -1;
						}
					}
					if(list!=MAX){
						System.err.println("In line "+line+" Less than "+MAX+" Numbers!");
						reader.close();
						return -1;
					}
					tempString = tempString.replaceAll("0","");
					tempString = tempString.replaceAll("1","");
					tempString = tempString.replaceAll("2","");
					tempString = tempString.replaceAll("3","");
					tempString = tempString.replaceAll(" ","");
					tempString = tempString.replaceAll("\t","");
					if(tempString.length()!=0){
						System.err.println("In line "+line+" Invalid word["+tempString+"]");
						reader.close();
						return -1;
					}
				}
				if(line!=MAX){
					System.err.println("In line "+line+" Less than "+MAX+" Numbers!");
					reader.close();
					return -1;
				}
				
				reader.close();
			}
			else{
				System.err.println("The file do not exists!");
				return -2;
			}
		}
		catch(Exception e){
			System.err.println("Something was wrong with the file "+path+" !");
			return -2;
		}
		return 0;
	}
	
	public boolean read_map(String path){
		/* @ REQUIRES: String path &&　repOK()
		 * @ MODIFIES: none
		 * @ EFFECTS: tell the main if the map file read succeed
		 * 					succeed==>return true
		 * 					failed ==>return false
		 */
		try{
			return (read_map_from_file(path)==0);
		}catch(Exception e){
			System.err.println("Something wrong during Reading the map file!");
			return false;
		}
		
	}
	
	public boolean read_light(String path){
		/* @ REQUIRES: String path &&　repOK()
		 * @ MODIFIES: none
		 * @ EFFECTS: tell the main if the light file read succeed
		 * 					succeed==>return true
		 * 					failed ==>return false
		 */
		try{
			return (read_light_from_file(path)==0);
		}
		catch(Exception e){
			System.out.println("Something wrong during Reading the light file");
			return false;
		}
	}
}
