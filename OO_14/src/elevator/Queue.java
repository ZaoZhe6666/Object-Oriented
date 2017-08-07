package elevator;

import java.util.ArrayList;

/*
 * 		Overview
 * 
 * 	请求队列类，用于存储所有的有效请求(无效请求经由Request外部读入类已经删去)
 * 
 * 	AF(c) = (list,number_for_test,order) .where list = c.list,number_for_test = c.number_for_test,order=c.order
 * 
 * */

class Queue {
	protected ArrayList<jiegou> list=new ArrayList<jiegou>();
	protected int nft=0;
	
	protected int order=0;
	
	/*不变式
	 * 1	list为不为NULL的子项全部为jiegou类型变量的ArrayList类型变量
	 * 2	order>=0
	 * 3	0<=number for test<=
	 * */
	public boolean repOK(){
		/*@Effects: \result==invariant(this).*/
		if(list==null || !(list instanceof ArrayList)){
			return false;
		}
		for(jiegou j:list){
			if(j==null || !(j instanceof jiegou)){
				return false;
			}
			if(!j.repOK()){
				return false;
			}
		}
		if(order<0){
			return false;
		}
		if(nft<0 || nft>10){
			return false;
		}
		return true;
	}
	
	
	public Queue(){
		/* @ REQUIRES: none
		 * @ MODIFIES: none
		 * @ EFFECTS: none
		 */
	}
	
	public void saveone(String s){
		/* @ REQUIRES: repOK() && s为合法请求
		 * @ MODIFIES: list
		 * @ EFFECTS: 从外部输入类request中获得一个新的合法请求，并存入list末尾
		 * 
		 * 			合法请求定义：
		 * 				请求为String类型的变量
		 * 				s为形如(FR,X,Y,Z)的字符串
		 * 						其中：(	1<=X<=10 && 
		 * 								Y==UP || Y==DOWN &&
		 * 								0<=Z,X和Z均为正整数)
		 * 				或形如(ER,X,Y)的字符串
		 * 						其中：(	1<=X<=10 && 
		 * 								0<=Y,X和Y均为正整数)
		 */
		s=s.replaceAll("\\(", "").replaceAll("\\)", "");
		String[] sarray = s.split(",");
		
		
		int a=0;
		double b=0;
	//	System.out.println("the 0 is:"+sarray[0]);
		if(sarray[0].equals("FR")){
			nft=1;
			a = Integer.parseInt(sarray[1]);
			b = Double.parseDouble(sarray[3]);
			list.add(new jiegou("FR",a,sarray[2],b));
		}
		else{
			nft=2;
			a = Integer.parseInt(sarray[1]);
			b = Double.parseDouble(sarray[2]);
			list.add(new jiegou("ER",a,b));
		}
		return;
	}
	
	public int getlistlong(){
		/* @ REQUIRES: repOK()
		 * @ MODIFIES: none
		 * @ EFFECTS: \result == list.size()
		 */
		return list.size();
	}
	
	public jiegou getone(){
		/* @ REQUIRES: repOK() && 0<=order<list.size()
		 * @ MODIFIES: order
		 * @ EFFECTS: \result == list.get(order)
		 * 			&& order++
		 * 			按顺序不重复的取出从0到list.size()中的第order项
		 */
		return list.get(order++);
	}
	public void setorder(){
		/* @ REQUIRES: repOK()
		 * @ MODIFIES: order
		 * @ EFFECTS: 初始化将order置零
		 */
		order=0;
		return;
	}
}