package elevator;

class jiegou {
	private boolean valid=true;
	private String r="";
	private int floor=0;
	private String way="";
	private int Elevator=0;
	private double s_time=0;
	private double f_time=0;
	public jiegou(String a,int b,String c,double starttime){
		r=a;
		floor=b;
		way=c;
		s_time=starttime;
		f_time=starttime;
	}
	public jiegou(String a,int b,int c,double d){
		r=a;
		floor=b;
		Elevator=c;
		s_time=d;
		f_time=d;
	}
	public jiegou(boolean d,String a,int b,String c,double starttime,double finishtime){
		r=a;
		floor=b;
		way=c;
		s_time=starttime;
		valid=d;
	}
	public void setf_time(double f_time){
		this.f_time=f_time;
		return;
	}
	public int getelevator(){
		return Elevator;
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
		return s_time;
	}
	public double getftime(){
		return f_time;
	}
	public boolean getvalid(){
		return valid;
	}
	public void setvalid(){
		valid=false;
	}
}
