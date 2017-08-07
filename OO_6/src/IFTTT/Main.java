package IFTTT;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class Main {
	static Writer w;
	static BufferedWriter buffWriter;
	public static SafeFile sf =new SafeFile();
	public static boolean shut=true;
	public static int summary=0;
	public static ArrayList<String> detail = new ArrayList<String>();
	public static void main(String[] args) {
		System.out.println("-------------------------Begin Work-------------------------");
	
		try{
			w=new FileWriter("result.txt");
			buffWriter=new BufferedWriter(w);
			Readin readin = new Readin();
			ArrayList<Syntagm> list;
			Out out = new Out();
			
			out.getsf(sf);
			readin.read();
			list=readin.getlist();
			
			
			ArrayList<Tree> treelist = new ArrayList<Tree>();
			for(int i=0;i<list.size();i++){
				treelist.add(new Tree(""+(i+1),list.get(i)));
				treelist.get(i).setSafeFile(sf);
				treelist.get(i).start();
			}
			out.start();
			
			System.out.println("thread opened");
			while(shut){
				Thread.sleep(3000);

				Main.buffWriter.write("summary="+Main.summary+"\n");
				Main.buffWriter.flush();
				for(int i=0;i<Main.detail.size();i++){
					Main.buffWriter.write(Main.detail.get(i)+"\n");
				}
				Main.buffWriter.flush();
				Main.detail.clear();
			}
			for(int i=0;i<list.size();i++){
				treelist.get(i).setshut();
			}
			System.out.println("--------------------------End Work--------------------------");
		}
		catch(Exception e){
			System.out.println("Something was wrong!");
		}
		return;
	}
	public void setsum(){
		summary++;
		return;
	}
	public int getsum(){
		return summary;
	}
	public void setshut(){
		shut=false;
		return;
	}

}
