package Taxi;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Readin {
	public static int MAX=80;
	private Map map;
	public int read_from_file(){
		/*
		 * return value
		 * 
		 * 0	correct
		 * -1	detail error
		 * -2	file error
		 * */
		try{
			File file = new File("in.txt");
			if(file.exists()){
				if(file.isDirectory()){
					System.err.println("in.txt is not a file!");
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
//					System.out.println(tempString);
//					System.out.println(tempString.length());
					while(m.find()){
//						System.out.println(m.group(0));
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
			System.err.println("Something was wrong with the file!");
			return -2;
		}
		return 0;
	}
	
	public boolean read(){
		try{
			return (read_from_file()==0);
		}catch(Exception e){
			System.err.println("Something wrong during Reading the file!");
			return false;
		}
		
	}
	
	
	
	
	
	
	
	
	public void getmap(Map map){
		this.map=map;
		return;
	}
	
	
	
	
	
	
	
	
}
