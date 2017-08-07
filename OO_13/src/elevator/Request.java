package elevator;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.ArrayList;

class Request {
	private ArrayList<String> list=new ArrayList<String>();
	private Scanner word=new Scanner(System.in);
	private double time=0;
	private int getnum=0;
	private int judge=0;
	
	/*不变式
	 * 1	list为不为NULL的子项全部为String类型变量的ArrayList类型变量
	 * 2	word为不为NULL的Scanner类型变量
	 * 3	time>=0
	 * 4	getnum>=0
	 * 5	0<=judge<=1
	 * */
	public boolean repOK(){
		/*@Effects: \result==invariant(this).*/
		if(list==null || !(list instanceof ArrayList)){
			return false;
		}
		if(word==null || !(word instanceof Scanner)){
			return false;
		}
		for(String j:list){
			if(j==null || !(j instanceof String)){
				return false;
			}
		}
		if(time<0 || getnum<0 || judge<0 || judge>1){
			return false;
		}
		return true;
	}
	
	public String getList(){
		return list.get(getnum++);
	}
	public int getlistlong(){
		return list.size();
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
		String pattern = "\\((FR),([+-?]\\d*),(UP|DOWN),([+-?]\\d*)\\)|\\((ER),([+-?]\\d*),([+-?]\\d*)\\)";
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
					double c=0;
					if(sarray[0].equals("FR")){
						try{
							c = Double.parseDouble(sarray[1]);
							a = (int)c;
						}
						catch(Exception e){
							System.out.println(m.group(0)+"	--Illegal Input!(Are you trying to crash my int variables?)");
							continue;
						}
						if(a>10 || a<1){
							System.out.println(m.group(0)+"	--Illegal Input!(Where are you going?)");
							continue;
						}
						if(sarray[2].equals("UP") && a==10){
							System.out.println(m.group(0)+"	--Illegal Input!(The house has a roof!)");
							continue;
						}
						if(sarray[2].equals("DOWN") && a==1){
							System.out.println(m.group(0)+"	--Illegal Input!(There's no basement!)");
							continue;
						}
						
						try{
							b = Double.parseDouble(sarray[3]);
							if(b>2147483647.0){
								System.out.println(m.group(0)+"	--Illegal Input!(Are you trying to crash my int variables?)");
								continue;
							}
						}
						catch(Exception e){
							System.out.println(m.group(0)+"	--Illegal Input!(Are you trying to crash my int variables?)");
							continue;
						}
						
						if(((a!=1) || (sarray[2].equals("DOWN")) || (b!=0))&& judge==0){
							System.out.println(m.group(0)+"	--Illegal Input!(The first instruction should be (FR,1,UP,0)!)");
							continue;
						}
						if(b<time){
							System.out.println(m.group(0)+"	--Illegal Input!(Do not satisfy the time order!)");
							continue;
						}
						else{
							time=b;
						}
					}
					else{
						try{
							b = Double.parseDouble(sarray[2]);
							c = Double.parseDouble(sarray[1]);
							a = (int)c;
							if(b>4294967295.0){
								System.out.println(m.group(0)+"	--Illegal Input!(Are you trying to crash my int variables?)");
								continue;
							}
						}
						catch(Exception e){
							System.out.println(m.group(0)+"	--Illegal Input!(Are you trying to crash my int variables?)");
							continue;
						}
						
						if(a>10 || a<1){
							System.out.println(m.group(0)+"	--Illegal Input!(Where are you going?)");
							continue;
						}
						if(judge==0){
							System.out.println(m.group(0)+"	--Illegal Input!(The first instruction should be (FR,1,UP,0)!)");
							continue;
						}
						if(b<time){
							System.out.println(m.group(0)+"	--Illegal Input!(Do not satisfy the time order!)");
							continue;
						}
						else{
							time=b;
						}
					}
					
					list.add(m.group(0));
					judge=1;
					//	System.out.println(list);
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
