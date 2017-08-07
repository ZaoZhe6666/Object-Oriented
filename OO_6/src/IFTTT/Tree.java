package IFTTT;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;


public class Tree extends Thread{
	private SafeFile sf;
	private String Treename="";
	private String task="";
	private String trigger="";
	private Syntagm syntagm;
	private String path="";
	private String fatherpath="";
	private String name="";
	private int style=0;
	private long big=0;
	private boolean shut=true;
	private static File f;
	private ArrayList<Syntagm> oldtree = new ArrayList<Syntagm>();
	private ArrayList<Syntagm> newtree = new ArrayList<Syntagm>();
	private ArrayList<Syntagm> judgetree=new ArrayList<Syntagm>();
	Tree(String Treename,Syntagm syntagm){
		this.Treename=Treename;
		this.syntagm=syntagm;
		this.f=new File(syntagm.getpath());
		this.style=syntagm.getstyle();
		this.trigger=syntagm.gettrigger();
		this.task=syntagm.gettask();
		this.path=syntagm.getpath();
		this.name=f.getName();
		if(f.isDirectory()){
			routetree_firsttime(1,f);
		}
		else{
			routetree(1,f.getParentFile());
		}
	}
	
	public void run(){
		while(shut){
			try {
				sleep(10);
			} catch (InterruptedException e) {
			}
			newtree = new ArrayList<Syntagm>();
			routetree(1,f);
			judge();
			oldtree=newtree;
		}
		return;
	}
	
	
	public void routetree_firsttime(int level,File f){
		newtree.add(new Syntagm(level,f.getAbsolutePath(),f.isDirectory(),f.length(),f.lastModified(),f.getName()));
		File[] childs = f.listFiles();
		for(int i=0;i<childs.length;i++){
			newtree.add(new Syntagm(level,childs[i].getAbsolutePath(),childs[i].isDirectory(),childs[i].length(),childs[i].lastModified(),childs[i].getName()));
			if(childs[i].isDirectory()){
				routetree_firsttime(level+1,childs[i]);
			}
		}
		oldtree=newtree;
	}
	
	
	public void routetree(int level,File f){
		newtree.add(new Syntagm(level,f.getAbsolutePath(),f.isDirectory(),f.length(),f.lastModified(),f.getName()));
		File[] childs = f.listFiles();
		for(int i=0;i<childs.length;i++){
			newtree.add(new Syntagm(level,childs[i].getAbsolutePath(),childs[i].isDirectory(),childs[i].length(),childs[i].lastModified(),childs[i].getName()));
			if(childs[i].isDirectory()){
				routetree(level+1,childs[i]);
			}
		}
	}
	
	public void setshut(){
		shut=false;
		return;
	}
	
	public void judge(){
		if(trigger.equals("renamed")){
			renamed();
		}
		else if(trigger.equals("Modified")){
			modified();
		}
		else if(trigger.equals("path-changed")){
			pc();
		}
		else if(trigger.equals("size-changed")){
			sc();
		}
		return;
	}
	
	
	private void renamed(){
		judgetree.clear();
		for(int j=0;j<newtree.size();j++){//new 有 old 无(path
			boolean flag=false;
			for(int i=0;i<oldtree.size();i++){
				if(newtree.get(j).getpath().equals(oldtree.get(i).getpath())){
					flag=true;
					break;
				}
			}
			if(flag){
				continue;
			}
			judgetree.add(newtree.get(j));
		}
		if(judgetree.size()==0){
			return;
		}
//		System.out.println("changed!  "+judgetree.get(0).getpath());
		for(int i=0;i<oldtree.size();i++){
			boolean flag=false;
			for(int j=0;j<newtree.size();j++){
				if(newtree.get(j).getpath().equals(oldtree.get(i).getpath())){
					flag=true;
					break;
				}
			}
			if(flag){
				continue;
			}
			for(int j=0;j<judgetree.size();j++){
				File judgepath = new File(judgetree.get(j).getpath());
				if(oldtree.get(i).getstyle()==0 && judgepath.isFile()){
					//判断文件大小
					if(oldtree.get(i).getbig()!=judgepath.length()){
						continue;
					}
					
					//判断最后处理时间
					if(oldtree.get(i).gettime()!=judgepath.lastModified()){
						System.out.println(oldtree.get(i).gettime()+" "+judgepath.lastModified());
						continue;
					}
					String s1="文件规模前后变化：\t"+oldtree.get(i).getbig()+"B=>"+judgepath.length()+"B\n"
							 +"文件名前后变化：\t"+oldtree.get(i).getname()+"=>"+judgepath.getName()+"\n"
							 +"文件路径前后变化：\t"+oldtree.get(i).getpath()+"\n\t\t\t\t=>\t"+judgepath.getAbsolutePath();
					taskjudge(s1,oldtree.get(i),judgetree.get(j));
					judgetree.remove(j);
					break;
				}
				else{
					System.out.println(oldtree.get(i).getpath() +" "+judgetree.get(j).getpath());
					System.out.println(oldtree.get(i).getstyle() +" "+ judgepath.isDirectory());
				}
			}
			if(judgetree.size()==0){
				return;
			}
		}
		return;
	}
	
	
	private void modified(){
		judgetree.clear();
		for(int i=0;i<oldtree.size();i++){
			for(int j=0;j<newtree.size();j++){
				if(oldtree.get(i).getpath().equals(newtree.get(j).getpath())){
					if(oldtree.get(i).gettime()!=newtree.get(j).gettime()){
						judgetree.add(newtree.get(j));
					}
				}
			}
		}
		if(judgetree.size()==0){
			return;
		}
//		System.out.println("changed!  "+judgetree.get(0).getpath());
		for(int i=0;i<oldtree.size();i++){
			for(int j=0;j<judgetree.size();j++){
				if(oldtree.get(i).getpath().equals(judgetree.get(j).getpath())){
					String s1="文件规模前后变化：\t"+oldtree.get(i).getbig()+"B=>"+judgetree.get(j).getbig()+"B\n"
							 +"文件名前后变化：\t"+oldtree.get(i).getname()+"=>"+judgetree.get(j).getname()+"\n"
							 +"文件路径前后变化：\t"+oldtree.get(i).getpath()+"\n\t\t\t\t=>\t"+judgetree.get(j).getpath();
					taskjudge(s1,oldtree.get(i),judgetree.get(j));
				}
			}
		}
		return;
	}
	
	private void pc(){
		judgetree.clear();
		for(int i=0;i<oldtree.size();i++){
			boolean flag=false;
			for(int j=0;j<newtree.size();j++){
				if(oldtree.get(i).getpath().equals(newtree.get(j).getpath())){
					flag=true;
					break;
				}
			}
			if(flag){
				continue;
			}
			judgetree.add(oldtree.get(i));
		}
		for(int i=0;i<judgetree.size();i++){
			for(int j=0;j<newtree.size();j++){
				if(judgetree.get(i).getname().equals(newtree.get(j).getname())
				&& judgetree.get(i).getbig()==newtree.get(j).getbig()
				&& judgetree.get(i).gettime()==newtree.get(j).gettime()
				&& judgetree.get(i).getlevel()<newtree.get(j).getlevel()){
					String s1="文件规模前后变化：\t"+judgetree.get(i).getbig()+"B=>"+newtree.get(j).getbig()+"B\n"
							 +"文件名前后变化：\t"+judgetree.get(i).getname()+"=>"+newtree.get(j).getname()+"\n"
							 +"文件路径前后变化：\t"+judgetree.get(i).getpath()+"\n\t\t\t\t=>\t"+newtree.get(j).getpath();
					taskjudge(s1,judgetree.get(i),newtree.get(j));
				}
			}
		}
		return;
	}
	
	private void sc(){
		judgetree.clear();
		if(style==1){//若为文件夹
			for(int i=0;i<oldtree.size();i++){
				boolean flag=false;
				for(int j=0;j<newtree.size();j++){
					if(oldtree.get(i).getpath().equals(newtree.get(j).getpath())){
						flag=true;
						break;
					}
				}
				if(flag){
					continue;
				}
				String s1="文件规模前后变化：\t"+oldtree.get(i).getbig()+"B=>0B\n"
						 +"文件名前后变化：\t"+oldtree.get(i).getname()+"=>"+oldtree.get(i).getname()+"\n"
						 +"文件路径前后变化：\t"+oldtree.get(i).getpath()+"\n\t\t\t\t=>\t"+oldtree.get(i).getpath();
				taskjudge(s1,oldtree.get(i),oldtree.get(i));
			}
			for(int j=0;j<newtree.size();j++){
				boolean flag=false;
				for(int i=0;i<oldtree.size();i++){
					if(oldtree.get(i).getpath().equals(newtree.get(j).getpath())){
						flag=true;
						break;
					}
				}
				if(flag){
					continue;
				}
				String s1="文件规模前后变化：\t0B=>"+newtree.get(j).getbig()+"B\n"
						 +"文件名前后变化：\t"+newtree.get(j).getname()+"=>"+newtree.get(j).getname()+"\n"
						 +"文件路径前后变化：\t"+newtree.get(j).getpath()+"\n\t\t\t\t=>\t"+newtree.get(j).getpath();
				taskjudge(s1,newtree.get(j),newtree.get(j));
			}
			for(int i=0;i<oldtree.size();i++){
				for(int j=0;j<newtree.size();j++){
					if(oldtree.get(i).getpath().equals(newtree.get(j).getpath())){
						if(oldtree.get(i).getbig()!=newtree.get(j).getbig()){
							String s1="文件规模前后变化：\t"+oldtree.get(i).getbig()+"B=>"+newtree.get(j).getbig()+"B\n"
									 +"文件名前后变化：\t"+oldtree.get(i).getname()+"=>"+newtree.get(j).getname()+"\n"
									 +"文件路径前后变化：\t"+oldtree.get(i).getpath()+"\n\t\t\t\t=>\t"+newtree.get(j).getpath();
							taskjudge(s1,oldtree.get(i),newtree.get(j));
							break;
						}
					}
				}
			}
		}
		else{//若为文件
			File filepath = new File(path);
			boolean flag=false;
			for(int i=0;i<oldtree.size();i++){
				if(oldtree.get(i).getpath().equals(path)){
					flag=true;
					break;
				}
			}
			if(!flag){//之前就没了
				if(filepath.exists()){//无中生有
					big=filepath.length();
					String s1="文件规模前后变化：\t0B=>"+big+"B\n"
							 +"文件名前后变化：\t"+name+"=>"+name+"\n"
							 +"文件路径前后变化：\t"+path+"\n\t\t\t\t=>\t"+path;
					taskjudge(s1,syntagm,syntagm);
					return;
				}
				return;
			}
			if(!filepath.exists()){//有中变无
				String s1="文件规模前后变化：\t"+big+"B=>0B\n"
						 +"文件名前后变化：\t"+name+"=>"+name+"\n"
						 +"文件路径前后变化：\t"+path+"\n\t\t\t\t=>\t"+path;
				big=0;
				taskjudge(s1,syntagm,syntagm);
				return;
			}
		}
	}
	
	//任务触发！
	
	public void taskjudge(String s,Syntagm oldpath,Syntagm judgepath){
		File jf = new File(judgepath.getpath());
		if(task.equals("record-summary")){
			Main.summary++;
		}
		else if(task.equals("record-detail")){
			Main.detail.add(s);
		}
		else if(task.equals("recover")){
			if(trigger.equals("renamed")){
				try{
					sf.RenameFile(jf.getParentFile().getAbsolutePath(), jf.getName(),oldpath.getname());
					judgepath.setname(oldpath.getname());
	//				sleep(10);
				}catch(Exception e){
				}
			}
			else if(trigger.equals("path-changed")){
				try{
					sf.MoveFile(jf.getAbsolutePath(),oldpath.getpath());
					judgepath.setpath(oldpath.getpath());
				}catch(Exception e){
				}
			}
		}
		return;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public void setSafeFile(SafeFile sf){
		this.sf=sf;
		return;
	}
	
	
}
