package Taxi;

/*
 * 		Overview
 *  
 * 	结构体类，专门用来存储结构体或者存储临时的多个变量(也是以结构体形式一起存储)
 * 
 * */

public class Syntagm {
	public int orderline=0;
	public int orderlist=0;
	public int line =-1;
	public int list =-1;
	public int name=-1;
	public int weight =0;
	public boolean can_work = true;
	public int ordertime=0;//time = pass time/100ms
	public int value=-1;
	public boolean firstcall=true;
	public String path="";
	public int ordernum=0;
	public String findword="";
	public int findtaxi =-1;
	public int mark=0;
	public int status =2;

	
	/*不变式：
	 * 1	0<=orderline<=80
	 * 2	0<=orderlist<=80
	 * 3	line=-1 || 1<=line<=80
	 * 4	list=-1 || 1<=list<=80
	 * 5	-1<=name<=99
	 * 6	0<=weight<=100
	 * 7	ordertime>=0
	 * 8	-1<=value<=6399
	 * 9	path为String型变量，且不为null
	 * 		path.equals("") || path.equals("Detail.txt") 
	 *   || path.equals("Request_"+ordernum+".txt") || path.equals("History.txt")
	 * 10	ordernum>=0
	 * 11	findword为String型变量，且不为null
	 * 		findword.queals("") || findword.equals("Taxi") 
	 *   || findword.equals("Status") ||findword.equals("History0")|| findword.equals("History1")
	 * 12	-1<=findtaxi<=99
	 * 13	mark>=0
	 * 14	0<=status<=3
	 * */
	public boolean repOK(){
		/*@Effects: \result==invariant(this).*/
		try{
			if(path==null || findword==null){
				return false;
			}
			if(orderline<0 || orderline>80
			|| orderlist<0 || orderlist>80
			|| line<-1 || line==0 || line>80
			|| list<-1 || list==0 || list>80
			|| name<-1 || name>99
			|| weight<0 || weight>100
			|| ordertime<0 || ordernum<0
			|| value<-1 || value>6399
			|| !(	path.equals("") 
				 || path.equals("Request_"+ordernum+".txt") 
				 || path.equals("Detail.txt")
				 || path.equals("History.txt"))
			|| !(	findword.equals("")
				 || findword.equals("Taxi")
				 || findword.equals("Status")
				 || findword.equals("History0")
				 || findword.equals("History1"))
			|| findtaxi<-1 || findtaxi>99
			|| mark<0 || status<0 || status>3){
				return false;
			}
			if(!(path instanceof String)
			|| !(findword instanceof String)){
				return false;
			}
				
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	Syntagm(int name,int line,int list,int mark,int status,boolean working){//存出租车信息
		/* @ REQUIRES: 1<=line<=80
		 * 			&& 1<=list<=80
		 * 			&& 0<=mark
		 * 			&& 0<=status<=3
		 * 			&&　repOK()
		 * @ MODIFIES: this
		 * @ EFFECTS: 创建一个结构体，将外部需要用到的所有出租车基本信息都作为变量存进去
		 */
		this.name=name;
		this.line=line;
		this.list=list;
		this.mark=mark;
		this.status=status;
		this.can_work=!working;
	}
	
	Syntagm(int line,int list,int orderline,int orderlist,int time){//存用户请求
		/* @ REQUIRES:	1<=line<=80
		 * 			&&	1<=list<=80
		 * 			&&	1<=orderline<=80
		 * 			&&	1<=orderlist<=80
		 * 			&&	0<=time
		 * 			&&　repOK()
		 * @ MODIFIES: this
		 * @ EFFECTS: 创建一个结构体，存储用户请求包含的信息
		 */
		this.orderline=orderline;
		this.orderlist=orderlist;
		this.line=line;
		this.list=list;
		this.ordertime=time;
	}
	Syntagm(int line,int list,int name){//存可抢单出租车
		/* @ REQUIRES:	1<=line<=80
		 * 			&&	1<=list<=80
		 * 			&&	0<=name<=99
		 * 			&&　repOK()
		 * @ MODIFIES: this
		 * @ EFFECTS: 创建一个结构体，存储可抢单的出租车的位置及编号信息
		 */
		this.line=line;
		this.list=list;
		this.name=name;
	}
	Syntagm(int line,int list,int value,int weight,int fatherline,int fatherlist){//存bfs已有结点
		/* @ REQUIRES:	1<=line<=80
		 * 			&&	1<=list<=80
		 * 			&&	0<=value<=6399
		 * 			&&	0<=weight<=100
		 * 			&&　repOK()
		 * @ MODIFIES: this
		 * @ EFFECTS: 创建一个结构体，存储广度优先遍历过程中的节点信息
		 */
		this.line=line;
		this.list=list;
		this.value=value;
		this.weight=weight;
	}
	Syntagm(int line,int list){//司机随便跑跑
		/* @ REQUIRES:	1<=line<=80
		 * 			&&	1<=list<=80
		 * 			&&　repOK()
		 * @ MODIFIES: this
		 * @ EFFECTS: 创建一个结构体，存储一个坐标值，用于司机随机移动使用
		 */
		this.line=line;
		this.list=list;
	}
	Syntagm(String findword,int findtaxi,String path){
		/* @ REQUIRES:	((findword.equals("")||findword.equals("Taxi")||findword.equals("Status"))
					&&	0<=find taxi<=99
					&&	path="Detail.txt"
					&&　repOK()
		 * @ MODIFIES: this
		 * @ EFFECTS: 创建一个结构体，存储查询指令信息
		 */
		this.findword=findword;
		this.findtaxi=findtaxi;
		this.path=path;
	}
	

	public void setpath(int num){
		/* @ REQUIRES: 1<=num
		 * 			&&　repOK()
		 * @ MODIFIES: this
		 * @ EFFECTS: 用于补充存用户请求时，请求的累计编号与最终结果输出的路径
		 */
		this.ordernum=num;
		this.path="Request_"+num+".txt";
	}
	
	
	
}
