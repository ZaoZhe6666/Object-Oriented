package elevator;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

class Request {
	private ArrayList<String> list=new ArrayList<String>();
	private Scanner word=new Scanner(System.in);
	private double time=0;
	private int listlong=0;
	private int getnum=0;
	private int judge=0;
	public String getList(){
		return list.get(getnum++);
	}
	public int getlistlong(){
		return listlong;
	}
	private String read1(){
		
		String line = word.nextLine();
		line=line.replaceAll(" ","");
		line=line.replaceAll("\t", "");
		
		return line;
	}
	private int read2(String line){
		
		if(line.equals("run"))return 0;
		
		return 1;
	}
	public void read(){
		String pattern = "\\((FR),(\\d{1,2}),(UP|DOWN),(\\d*)\\)|\\((ER),(\\d{1,2}),(\\d*)\\)";
		//Thanks the . taught the .;And the . taught me this Expression
		//String pattern = "\\(.*?\\)";
		//String pattern2= "\\(\\)";
		while(true){
			String w=read1();
			if(read2(w)==1){
				Pattern r = Pattern.compile(pattern);
				Matcher m = r.matcher(w);
				int ALLString=w.length();
				while(m.find()){
					ALLString-=m.group(0).length();
				//	System.out.println(m.group(0));
					String word=m.group(0).replaceAll("\\(", "").replaceAll("\\)", "");
					String[] sarray = word.split(",");
					
					int a=0;
					double b=0;
					if(sarray[0].equals("FR")){
						a = Integer.parseInt(sarray[1]);
						if(a>10 || a<1){
							System.out.println("Illegal Input!(Where are you going?)");
							continue;
						}
						if(sarray[2].equals("UP") && a==10){
							System.out.println("Illegal Input!(The house has a roof!)");
							continue;
						}
						if(sarray[2].equals("DOWN") && a==1){
							System.out.println("Illegal Input!(There's no basement!)");
							continue;
						}
						
						try{
							b = Double.parseDouble(sarray[3]);
							if(b>4294967295.0){
								System.out.println("Illegal Input!(Are you trying to crash my int variables?)");
								continue;
							}
						}
						catch(Exception e){
							System.out.println("Illegal Input!(Are you trying to crash my int variables?)");
							continue;
						}
						
						if(time==0 && b>0 && judge==0){
							System.out.println("Illegal Input!(The time of the first instruction should be zero!)");
							continue;
						}
						if(b<time){
							System.out.println("Illegal Input!(Do not satisfy the time order!)");
							continue;
						}
						else{
							time=b;
						}
					}
					else{
						a = Integer.parseInt(sarray[1]);
						
						try{
							b = Double.parseDouble(sarray[2]);
							if(b>4294967295.0){
								System.out.println("Illegal Input!(Are you trying to crash my int variables?)");
								continue;
							}
						}
						catch(Exception e){
							System.out.println("Illegal Input!(Are you trying to crash my int variables?)");
							continue;
						}
						
						if(a>10 || a<1){
							System.out.println("Illegal Input!(Where are you going?)");
							continue;
						}
						if(time==0 && b>0 && judge==0){
							System.out.println("Illegal Input!(The time of the first instruction should be zero!)");
							continue;
						}
						if(b<time){
							System.out.println("Illegal Input!(Do not satisfy the time order!)");
							continue;
						}
						else{
							time=b;
						}
					}
					
					list.add(m.group(0));
					judge=1;
					//	System.out.println(list);
					listlong++;
				}
				if(ALLString!=0){
					System.out.println("Illegal Input!");
				}
			}
			else{
				System.out.println("The result is:");
				break;
			}
		}
		return;
	}
}
