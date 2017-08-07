package elevator;

class jiegou {
	private boolean valid=true;
	private String r="";
	private int floor=0;
	private String way="";
	private double time=0;
	public jiegou(String a,int b,String c,double finishtime){
		r=a;
		floor=b;
		way=c;
		time=finishtime;
	}
	public jiegou(String a,int b,double d){
		r=a;
		floor=b;
		time=d;
	}
	public jiegou(boolean d,String a,int b,String c,double finishtime){
		r=a;
		floor=b;
		way=c;
		time=finishtime;
		valid=d;
	}
	public String getr(){
		return r;
	}
	public int getfloor(){
		return floor;
	}
	public String getway(){
		return way;
	}
	public double gettime(){
		return time;
	}
	public boolean getvalid(){
		return valid;
	}
	public void setvalid(){
		valid=false;
	}
}
