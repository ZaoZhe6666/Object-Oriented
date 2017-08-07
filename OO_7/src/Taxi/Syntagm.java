package Taxi;

public class Syntagm {
	private int orderline=0;
	private int orderlist=0;
	private int line =0;
	private int list =0;
	private int name=0;
	private boolean can_work = true;
	private boolean work_driver=false;
	private boolean work_people=false;
	private int ordertime=0;//time = pass time/100ms
	private int value=-1;
	private boolean firstcall=true;
	private int bfsjudge=-1;
	private String path="";
	private int ordernum=0;
	private String findword="";
	private int findtaxi =0;
	
	Syntagm(int line,int list,int orderline,int orderlist,int time){//存用户请求
		this.orderline=orderline;
		this.orderlist=orderlist;
		this.line=line;
		this.list=list;
		this.ordertime=time;
	}
	Syntagm(int line,int list,int name){//存可抢单出租车
		this.line=line;
		this.list=list;
		this.name=name;
	}
	Syntagm(int line,int list,int value,int a){//存bfs已有结点
		this.line=line;
		this.list=list;
		this.value=value;
	}
	Syntagm(int line,int list){//司机随便跑跑
		this.line=line;
		this.list=list;
	}
	Syntagm(String findword,int findtaxi,String path){
		this.findword=findword;
		this.findtaxi=findtaxi;
		this.path=path;
	}
	
	
	public int getfindtaxi(){
		return this.findtaxi;
	}
	public String getfindword(){
		return this.findword;
	}
	public boolean getfirstcall(){
		return firstcall;
	}
	public void setfirstcall(){
		this.firstcall=false;
	}
	public int getbfsjudge(){
		return bfsjudge;
	}
	public boolean getcan_work(){
		return can_work;
	}
	public int getline(){
		return line;
	}
	public int getlist(){
		return list;
	}
	public int get_orderline(){
		return orderline;
	}
	public int get_orderlist(){
		return orderlist;
	}
	public boolean get_work_driver(){
		return work_driver;
	}
	public boolean get_work_people(){
		return work_people;
	}
	public int gettime(){
		return ordertime;
	}
	public int getname(){
		return name;
	}
	
	public void setbfsjudge(int n){
		this.bfsjudge=n;
	}
	public void setcan_work(boolean b){
		this.can_work=b;
	}
	public void set_orderline(int orderline){
		this.orderline=orderline;
	}
	public void set_orderlist(int orderlist){
		this.orderlist=orderlist;
	}
	public void set_work_driver(){
		this.work_driver=true;
	}
	public void set_work_people(){
		this.work_people=true;
	}
	public String getpath(){
		return path;
	}
	public void setpath(int num){
		this.ordernum=num;
		this.path="Request_"+num+".txt";
	}
	public int get_order(){
		return ordernum;
	}
	
	
	
}
