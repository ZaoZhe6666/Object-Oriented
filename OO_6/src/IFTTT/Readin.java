package IFTTT;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;



public class Readin {
	private ArrayList<Syntagm> list=new ArrayList<Syntagm>(); 
	private ArrayList<String> pathlist=new ArrayList<String>();
	private Scanner word=new Scanner(System.in);
	private String readline(){
		String line = word.nextLine();
		return line;
	}
	
	
	public int read(){
		String pattern = "IF\\[(.*)\\] (renamed|Modified|path-changed|size-changed) THEN (record-summary|record-detail|recover)";

		
		while(true){
			String w = readline();//每次读一行
			if(readjudge(w)==0){//读到run判断是否结束读取
				if(pathlist.size()<1){
					System.out.println("Less than 5 Monitors!");
					continue;
				}
				break;
			}
			Pattern r = Pattern.compile(pattern);
			Matcher m = r.matcher(w);
	
			while(m.find()){
				String filePath = m.group(1);
				java.io.File myFilePath = new java.io.File(filePath);
				
				if(myFilePath.exists()){//若文件路径有效
					if(pathjudge(m.group(1))==0){//判断路径是否已有/已超过
						int style=(myFilePath.isDirectory())?1:0;
						if(m.group(3).equals("recover")){
							if(m.group(2).equals("Modified") || m.group(2).equals("size-change")){
								System.out.println("Invalid Input["+m.group(0)+"]");
								System.out.println("recover命令只可与重命名或路径改变触发器配合使用！");
								continue;
							}
						}
						if(samejudge(m.group(1),m.group(2),m.group(3),style)==0){//判断是否为重复指令
							list.add(new Syntagm(m.group(1),m.group(2),m.group(3),style));
						}
					}
				}
				else{
					System.out.println("["+m.group(1)+"]"+"--Invalid Path!");
					continue;
				}
				System.out.println("Add Succeed ["+m.group(0)+"]");
				w=w.replaceFirst(pattern,"");
			}
			if(!((w.replaceAll(" ","").replaceAll("\t","")).equals(""))){
				System.out.println("Invalid Input["+w+"]");
			}
		} 
		return 1;
	}
	private int readjudge(String line){
		if(line.equals("run"))return 0;
		return 1;
	}
	private int pathjudge(String path){
		int i;
		for(i=0;i<pathlist.size();i++){
			if(pathlist.get(i).equals(path)){
				break;
			}
		}
		if(i==pathlist.size()){
			if(pathlist.size()<=8){
				pathlist.add(path);
			}
			else{
				System.out.println("Invalid["+path+"]--More than 8 Monitors!");
				return 1;
			}
		}
		return 0;
	}
	private int samejudge(String path,String trigger,String task,int style){
		for(int i=0;i<list.size();i++){
			if(list.get(i).getpath().equals(path)){
				if(list.get(i).gettrigger().equals(trigger)){
					if(list.get(i).gettask().equals(task)){
						if(list.get(i).getstyle()==style){
							return 1;
						}
					}
				}
			}
		}
		return 0;
	}
	public ArrayList<Syntagm> getlist(){
		return list;
	}
	
}
