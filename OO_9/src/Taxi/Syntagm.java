package Taxi;

public class Syntagm {
	public int orderline=0;
	public int orderlist=0;
	public int line =0;
	public int list =0;
	public int name=0;
	public int weight =0;
	public boolean can_work = true;
	public int ordertime=0;//time = pass time/100ms
	public int value=-1;
	public boolean firstcall=true;
	public int bfsjudge=-1;
	public String path="";
	public int ordernum=0;
	public String findword="";
	public int findtaxi =0;
	public int fatherline=0;
	public int fatherlist=0;
	
	Syntagm(int line,int list,int orderline,int orderlist,int time){//存用户请求
		/* @ REQUIRES: Point(x,y) Point(x',y') int time
		 * @ MODIFIES: none
		 * @ EFFECTS: initial the syntagm to save a request
		 */
		this.orderline=orderline;
		this.orderlist=orderlist;
		this.line=line;
		this.list=list;
		this.ordertime=time;
	}
	Syntagm(int line,int list,int name){//存可抢单出租车
		/* @ REQUIRES: Point(x,y)  int name
		 * @ MODIFIES: none
		 * @ EFFECTS: initial the syntagm to save a hear-the-calling taxi
		 */
		this.line=line;
		this.list=list;
		this.name=name;
	}
	Syntagm(int line,int list,int value,int weight,int fatherline,int fatherlist){//存bfs已有结点
		/* @ REQUIRES: Point(x,y) int value int weight Point(x',y')
		 * @ MODIFIES: none
		 * @ EFFECTS: initial the syntagm to save a simple point during bfs
		 */
		this.line=line;
		this.list=list;
		this.value=value;
		this.weight=weight;
	}
	Syntagm(int line,int list){//司机随便跑跑
		/* @ REQUIRES: Point(x,y)
		 * @ MODIFIES: none
		 * @ EFFECTS: initial the syntagm to save a simple point to let the driver move
		 */
		this.line=line;
		this.list=list;
	}
	Syntagm(String findword,int findtaxi,String path){
		/* @ REQUIRES: String find word int find taxi String path
		 * @ MODIFIES: none
		 * @ EFFECTS: initial the syntagm to save a searching request
		 */
		this.findword=findword;
		this.findtaxi=findtaxi;
		this.path=path;
	}
	

	public void setpath(int num){
		/* @ REQUIRES: int num
		 * @ MODIFIES: none
		 * @ EFFECTS: initial the syntagm to save the path of the calling request
		 */
		this.ordernum=num;
		this.path="Request_"+num+".txt";
	}
	
	
	
}
