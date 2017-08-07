package elevator;

import java.util.ArrayList;

class Queue {
	private ArrayList<jiegou> list=new ArrayList<jiegou>();
	
	private int listlong=0;
	private int order=0;
	public Queue(){
	}
	
	public void saveone(Request request){
		String s = request.getList();
		s=s.replaceAll("\\(", "").replaceAll("\\)", "");
		String[] sarray = s.split(",");
		
		
		int a=0;
		double b=0;
	//	System.out.println("the 0 is:"+sarray[0]);
		if(sarray[0].equals("FR")){
			a = Integer.parseInt(sarray[1]);
			b = Double.parseDouble(sarray[3]);
			list.add(new jiegou("FR",a,sarray[2],b));
			listlong++;
		}
		else{
			a = Integer.parseInt(sarray[1]);
			b = Double.parseDouble(sarray[2]);
			list.add(new jiegou("ER",a,b));
			listlong++;
		}
		return;
	}
	
	public int getlistlong(){
		return listlong;
	}
	
	public jiegou getone(){
		return list.get(order++);
	}
	public void setorder(){
		order=0;
		return;
	}
}