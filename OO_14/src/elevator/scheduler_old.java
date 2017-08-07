package elevator;

import java.util.ArrayList;

/*		Overview
 * 	老调度器类，可完成：简单的减法运算，相同指令的筛除 ，开关门模拟(就是+1s)
 * 
 * 	AF(c) = (finish_floor,finish_time,list,warn,number_for_test_i) .where
 * 			finish_floor = c.finish_floor,finish_time = c.finish_time,
 * 			list = c.list,warn = c.warn,number_for_test_i = c.number_for_test_i
 * 
 * */

public class scheduler_old {
	protected int finishfloor=1;
	protected double finishtime=0;
	protected ArrayList<jiegou> list=new ArrayList<jiegou>();
	protected int warn=1;
	protected int nft=0;
	protected int nft2=0;
	protected int nft3=0;
	protected int nft4=0;
	protected int nft5=0;
	
	/*不变式
	 * 1	1<=finish floor<=10
	 * 2	finish time>=0
	 * 3	list为不为NULL的子项全部为jiegou类型变量的ArrayList类型变量
	 * 4	0<=warn<=1
	 * 5	0<=number for test 空/2/3/4<=100
	 * */
	public boolean repOK_old(){
		/*@Effects: \result==invariant(this).*/
		if(finishfloor<1 || finishfloor>10){
			return false;
		}
		if(finishtime<0){
			return false;
		}
		if(warn<0 || warn>1){
			return false;
		}
		if(list==null){
			return false;
		}
		if(!(list instanceof ArrayList)){
			return false;
		}
		for(jiegou j:list){
			if(j==null){
				return false;
			}
			if(!(j instanceof jiegou)){
				return false;
			}
		}
		if(nft<0 || nft>100){
			return false;
		}
		if(nft2<0 || nft2>100){
			return false;
		}
		if(nft3<0 || nft3>100){
			return false;
		}
		if(nft4<0 || nft4>100){
			return false;
		}
		if(nft5<0 || nft5>100){
			return false;
		}
		return true;
	}
	
	public int getwarn(){
		/* @ REQUIRES: repOK()
		 * @ MODIFIES: none
		 * @ EFFECTS: \result == warn
		 */
		return warn;
	}

	public int sub(int a,int b){
		/* @ REQUIRES: none
		 * @ MODIFIES: none
		 * @ EFFECTS: \result == |a-b| (||表示绝对值，即ABS)
		 */
		if(a>b){
			return a-b;
		}
		else{
			return b-a;
		}
	}

	public void listadd(String a,int b,String c, double d){
		/* @ REQUIRES: repOK() && 
		 * 			j = new jiegou(a,b,c,d)==>j.repOK()
		 * @ MODIFIES: list
		 * @ EFFECTS: 向list中添加一项新的结构体子项jiegou(a,b,c,d)
		 */
		list.add(new jiegou(a,b,c,d));
		return;
	}
	
	public boolean samejudge(jiegou j){
		/* @ REQUIRES: repOK() j.repOK()
		 * @ MODIFIES: list,j,finishtime
		 * @ EFFECTS: 若当前时刻前发出结构体j中存储指令==>
		 * 					判断结构体j中存储的指令是否与已完成指令存在SAME情况
		 * 					若有(即同质请求，在某条指令执行完毕前，存在同层反复按的情况)==>\result==true
		 * 					否则==>\result==false
		 * 			  否则==>更新时间到结构体j中存储指令发出时间
		 */
		if(j.gettime()<=finishtime){
			nft=60;
			for(jiegou i:list){
				nft=61;
				if(i.getr()==j.getr()){
					nft=62;
					if(i.getfloor()==j.getfloor()){
						nft=63;
						if(i.getway().equals(j.getway())){
							nft=64;
							if(i.gettime()>=j.gettime()){
								nft=65;
								if(j.getvalid()){
									nft=66;
									String s=j_s(j);
									System.out.println("SAME"+s);
									j.setvalid();
									return true;
								}
							}
						}
					}
				}
			}
		}
		else{
			nft=67;
			finishtime=j.gettime();
		}
		return false;
	}
	
	public String j_s(jiegou j){
		/* @ REQUIRES: repOK() && j.repOK()
		 * @ MODIFIES: none
		 * @ EFFECTS: \result == j中所存储指令从外部输入时的格式
		 * 			if j中存储的是FR指令==>
		 * 					\result == [FR,X,Y,Z] 
		 * 						其中：(	1<=X<=10 && 
		 * 								Y==UP || Y==DOWN &&
		 * 								0<=Z)
		 * 			else j 中存储的是ER指令==>
		 * 					\result == [ER,X,Y] 
		 * 						其中：(	1<=X<=10 && 
		 * 								0<=Y)
		 */
		String s;
		if(j.getr().equals("ER")){
			s="["+j.getr()+","+j.getfloor()+","+(int)j.gettime()+"]";
			nft2=41;
		}
		else{
			s="["+j.getr()+","+j.getfloor()+","+j.getway()+","+(int)j.gettime()+"]";
			nft2=42;
		}
		return s;
	}
	

	public void oac_door(){//open and close the door +1s!
		/* @ REQUIRES: none
		 * @ MODIFIES: finish time
		 * @ EFFECTS: finish time +1s!!!
		 * 			模拟开关门时候的+1s,再展开写可能存在暴力膜,所以不写了
		 */
		finishtime++;
		return;
	}
}
