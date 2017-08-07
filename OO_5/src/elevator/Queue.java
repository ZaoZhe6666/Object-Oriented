package elevator;

import java.util.ArrayList;

class Queue {
	private ArrayList<jiegou> list=new ArrayList<jiegou>();
	
	private boolean flag=true;
	public int getlistlong(){
		return list.size();
	}
	public boolean getflag(){
		return flag;
	}
	public ArrayList<jiegou> getlist(){
		return list;
	}
	public void setlist(){
		list.clear();
		return;
	}
	public void setflag(boolean a){
		flag=a;
	}
	public Queue(){
	}
	
	public void saveone(Request request){
		String s = request.getList();
		s=s.replaceAll("\\(", "").replaceAll("\\)", "");
		String[] sarray = s.split(",");
		
		
		int a=0;
		double b=0;
		int elevator=0;
		if(sarray[0].equals("FR")){
			a = Integer.parseInt(sarray[1]);
			b = Double.parseDouble(sarray[3]);
			list.add(new jiegou("FR",a,sarray[2],b));
		}
		else{
			a = Integer.parseInt(sarray[2]);
			sarray[1]=sarray[1].replaceAll("#", "");
			elevator=Integer.parseInt(sarray[1]);
			b = Double.parseDouble(sarray[3]);
			list.add(new jiegou("ER",a,elevator,b));
		}
		return;
	}
	
}