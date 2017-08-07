package IFTTT;

import java.util.Scanner;

public class Out extends Thread{
	private boolean shut=true;
	private SafeFile sf;
	private Scanner word=new Scanner(System.in);
	public void run(){
		try {
			sleep(10);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		while(shut){
			welcome();
			String line = word.nextLine();
			try{
				int num = Integer.parseInt(line);
				if(num<=9 && num>=1){
					controller(num);
				}
				else{
					System.out.println("Invalid Input ["+line+"]");
				}
			}
			catch(Exception e){
				System.out.println("Invalid Input ["+line+"]");
			}
		}
		return;
	}
	
	
	
	
	
	private void welcome(){
		System.out.println("--------------------可---执---行---操---作--------------------");
//		System.out.println("\t\t可执行操作");
		System.out.println("1\t新建文件\t\t\t2\t删除文件");
		System.out.println("3\t新建文件夹\t\t4\t删除文件夹");
		System.out.println("5\t查看文件/文件夹属性\t6\t文件重命名");
		System.out.println("7\t文件移动\t\t\t8\t写入内容到文件");
		System.out.println("9\t退出命令输入，停止所有监控");
		System.out.println("------------------------------------------------------------");
		System.out.println("请输入您想执行的操作：");
	}
	
	private void controller(int num){
		if(num==1){
			addfile();
		}
		else if(num==2){
			deletefile();
		}
		else if(num==3){
			addfilebox();
		}
		else if(num==4){
			deletefilebox();
		}
		else if(num==5){
			seefile();
		}
		else if(num==6){
			renamefile();
		}
		else if(num==7){
			movefile();
		}
		else if(num==8){
			writefile();
		}
		else{
			setshut();
		}
		return;
	}
	
	//新建文件
	private void addfile(){
		System.out.println("请输入新建文件的路径及文件名：");
		String line=word.nextLine();
		if(sf.AddFile(line)==1){
			System.out.println("创建成功");
		}
		else{
			System.out.println("创建失败");
		}
		return;
	}
	
	//新建文件夹
	private void addfilebox(){
		System.out.println("请输入新建文件夹的路径及文件夹名：");
		String line=word.nextLine();
		if(sf.AddFilebox(line)==1){
			System.out.println("创建成功");
		}
		else{
			System.out.println("创建失败");
		}
		return;
	}
	
	//删除文件
	private void deletefile(){
		System.out.println("请输入删除文件的路径及文件名：");
		String line=word.nextLine();
		if(sf.DeleteFile(line)==1){
			System.out.println("删除成功");
		}
		else{
			System.out.println("删除失败");
		}
		return;
	}
	
	//删除文件夹
	private void deletefilebox(){
		System.out.println("请输入删除文件夹的路径及文件名：");
		String line=word.nextLine();
		if(sf.DeleteFilebox(line)==1){
			System.out.println("删除成功");
		}
		else{
			System.out.println("删除失败");
		}
		return;
	}
	
	//查看文件\文件夹属性
	private void seefile(){
		System.out.println("请输入要查询文件/文件夹的路径：");
		System.out.println();
		String line=word.nextLine();
		if(sf.SeeFile(line)==1){
			System.out.println("查询成功");
		}
		else{
			System.out.println("查询失败");
		}
		return;
	}
	
	
	//文件重命名
	private void renamefile(){
		System.out.println("请输入要重命名文件的路径(至父目录即可)：");
		String line1=word.nextLine();
		System.out.println("请输入原文件名：");
		String line2=word.nextLine();
		System.out.println("请输入新文件名：");
		String line3=word.nextLine();
		if(sf.RenameFile(line1,line2,line3)==1){
			System.out.println("文件名更改成功");
		}
		else{
			System.out.println("文件名更改失败");
		}
		return;
	}
	
	
	//文件移动
	private void movefile(){
		System.out.println("请输入要移动文件路径(至父目录即可)：");
		String line1=word.nextLine();
		System.out.println("请输入要移动文件名：");
		String line3=word.nextLine();
		System.out.println("请输入要移动至的路径(至父目录即可)：");
		String line2=word.nextLine();
		if(sf.MoveFile(line1,line2,line3)==1){
			System.out.println("文件移动成功");
		}
		else{
			System.out.println("文件移动失败");
		}
		return;
	}
	
	
	//写入内容到文件
	private void writefile(){
		System.out.println("请输入待写入文件的路径：");
		String line=word.nextLine();
		if(sf.WriteFile(line)==1){
			System.out.println("文件写入成功");
		}
		else{
			System.out.println("文件写入失败");
		}
		return;
	}
	
	
	
	//退出所有进程
	private void setshut(){
		shut=false;
		System.out.println("外部输入已终止！");
		Main.shut=false;
		return;
	}
	

	public void getsf(SafeFile sf){
		this.sf=sf;
	}

	
}
