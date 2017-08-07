package elevator;

class jiegou {
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
}
