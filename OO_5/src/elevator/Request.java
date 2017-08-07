package elevator;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.IOException;
import java.util.ArrayList;

class Request {
	private double begin_time;
	private ArrayList<String> list=new ArrayList<String>();
	private Scanner word=new Scanner(System.in);
	private double time=0;
	private int getnum=0;
	private int judge=0;
	public String getList(){
		return list.get(getnum++);
	}
	public int getlistlong(){
		return list.size();
	}
	public void settime(double settime){
		begin_time=settime;
	}
	private String read1(){
		String line = word.nextLine();
		time = (System.currentTimeMillis()-begin_time)/1000;
		line=line.replaceAll(" ","");
		line=line.replaceAll("\t", "");
		
		return line;
	}
	private int read2(String line){
		
		if(line.equals("END"))return 0;
		
		return 1;
	}
	public int read(){
		String pattern = "\\((FR),([+-?]\\d*),(UP|DOWN)\\)|\\((ER),\\#([+-?]\\d*),([+-?]\\d*)\\)";
		//Thanks the . taught the .;And the . taught me this Expression
		//String pattern = "\\(.*?\\)";
		//String pattern2= "\\(\\)";
//		while(true){
			String w=read1();
			if(read2(w)==1){
				Pattern r = Pattern.compile(pattern);
				Matcher m = r.matcher(w);
				int ALLString=w.length();
				judge=0;
				while(m.find()){
					ALLString-=m.group(0).length();
					w=w.replaceFirst("\\("+m.group(0)+"\\)","");
					String word=m.group(0).replaceAll("\\(", "").replaceAll("\\)", "");
					String[] sarray = word.split(",");
					
					int a=0;
					double c=0;
					int d=0;
					if(sarray[0].equals("FR")){
						try{
							c = Double.parseDouble(sarray[1]);
							a = (int)c;
						}
						catch(Exception e){
							long sysTime=System.currentTimeMillis();
							String str ="("+sysTime+")";
							try {
								controller.buffWriter.write(str+":"+m.group(0)+"	--Illegal Input!(Are you trying to crash my int variables?)");
								controller.buffWriter.flush();
						
							} catch (IOException p) {
								p.printStackTrace();
							}
							System.out.println(str+":"+m.group(0)+"	--Illegal Input!(Are you trying to crash my int variables?)");
							continue;
						}
						if(a>20 || a<1){
							long sysTime=System.currentTimeMillis();
							String str ="("+sysTime+")";
							try {
								controller.buffWriter.write(str+":"+m.group(0)+"	--Illegal Input!(Where are you going?)");
								controller.buffWriter.flush();
							} catch (IOException p) {
								p.printStackTrace();
							}
							System.out.println(str+":"+m.group(0)+"	--Illegal Input!(Where are you going?)");
							continue;
						}
						if(sarray[2].equals("UP") && a==20){
							long sysTime=System.currentTimeMillis();
							String str ="("+sysTime+")";
							try {
								controller.buffWriter.write(str+":"+m.group(0)+"	--Illegal Input!(The house has a roof!)");
								controller.buffWriter.flush();
							} catch (IOException p) {
								p.printStackTrace();
							}
							System.out.println(str+":"+m.group(0)+"	--Illegal Input!(The house has a roof!)");
							continue;
						}
						if(sarray[2].equals("DOWN") && a==1){
							long sysTime=System.currentTimeMillis();
							String str ="("+sysTime+")";
							try {
								controller.buffWriter.write(str+":"+m.group(0)+"	--Illegal Input!(There's no basement!)\n");
								controller.buffWriter.flush();
							} catch (IOException p) {
								p.printStackTrace();
							}
							System.out.println(str+":"+m.group(0)+"	--Illegal Input!(There's no basement!)");
							continue;
						}
						
					}
					else{
						try{
							c = Double.parseDouble(sarray[2]);
							a = (int)c;
							sarray[1]=sarray[1].replace("#", "");
							c = Double.parseDouble(sarray[1]);
							d = (int)c;
						}
						catch(Exception e){
							long sysTime=System.currentTimeMillis();
							String str ="("+sysTime+")";
							try {
								controller.buffWriter.write(str+":"+m.group(0)+"	--Illegal Input!(Are you trying to crash my int variables?)\n");
								controller.buffWriter.flush();
							} catch (IOException p) {
								p.printStackTrace();
							}
							System.out.println(str+":"+m.group(0)+"	--Illegal Input!(Are you trying to crash my int variables?)");
							continue;
						}
						
						if(a>20 || a<1){
							long sysTime=System.currentTimeMillis();
							String str ="("+sysTime+")";
							try {
								controller.buffWriter.write(str+":"+m.group(0)+"	--Illegal Input!(Where are you going?)\n");
								controller.buffWriter.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
							System.out.println(str+":"+m.group(0)+"	--Illegal Input!(Where are you going?)");
							continue;
						}
						if(d>3  || d<1){
							long sysTime=System.currentTimeMillis();
							String str ="("+sysTime+")";
							try {
								controller.buffWriter.write(str+":"+m.group(0)+"	--Illegal Input!(Invalid Elevator Number!)\n");
								controller.buffWriter.flush();
							} catch (IOException e) {
								e.printStackTrace();
							}
							System.out.println(str+":"+m.group(0)+"	--Illegal Input!(Invalid Elevator Number!)");
							continue;
						}
					}
					judge++;
					if(judge>10){
						long sysTime=System.currentTimeMillis();
						String str ="("+sysTime+")";
						try {
							controller.buffWriter.write(str+":"+m.group(0)+"	--Illegal Input!(More than 10 right requests in same line!)\n");
							controller.buffWriter.flush();
						} catch (IOException e) {
							e.printStackTrace();
						}
						System.out.println(str+":"+m.group(0)+"	--Illegal Input!(More than 10 right requests in same line!)");
						continue;
					}
					list.add(m.group(0)+","+time);
					
				}
				if(ALLString!=0){
					long sysTime=System.currentTimeMillis();
					String str ="("+sysTime+")";
					try {
						controller.buffWriter.write(str+":Invalid["+w+"]\n");
						controller.buffWriter.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
					System.out.println(str+":Invalid["+w+"]");
				}
			}
			else{
				return 1;
			}
//		}
		return 0;
	}
}
