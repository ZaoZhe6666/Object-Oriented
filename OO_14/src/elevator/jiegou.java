package elevator;

class jiegou {
	protected boolean valid=true;
	protected String r="";
	protected int floor=0;
	protected String way="";
	protected double time=0;
	
	/*不变式
	 * 1	r.equals("") || r.equals("ER") || r.equals("FR") 
	 * 2	r是个不为空的String型变量
	 * 3	1<=floor<=10
	 * 4	way.equals("") || way.equals("UP") || way.equals("STILL") || way.equals("DOWN")
	 * 5	way是个不为空的String型变量
	 * 6	time>=0
	 * */
	public boolean repOK(){
		if(r==null || way==null){
			return false;
		}
		if(!(r instanceof String) || !(way instanceof String)){
			return false;
		}
		if(!(	r.equals("")
			||	r.equals("ER")
			||	r.equals("FR"))){
			return false;
		}
		if(!(		way.equals("")
				||	way.equals("UP")
				||	way.equals("STILL")
				||	way.equals("DOWN"))){
				return false;
		}
		if(floor<1 || floor>10){
			return false;
		}
		if(time<0){
			return false;
		}

		return true;
	}
	
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
