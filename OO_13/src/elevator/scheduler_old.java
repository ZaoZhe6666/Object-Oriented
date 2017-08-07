package elevator;

import java.util.ArrayList;

/*		Overview
 * 	�ϵ������࣬����ɣ��򵥵ļ������㣬��ָͬ���ɸ�� ��������ģ��(����+1s)
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
	
	/*����ʽ
	 * 1	1<=finish floor<=10
	 * 2	finish time>=0
	 * 3	listΪ��ΪNULL������ȫ��Ϊjiegou���ͱ�����ArrayList���ͱ���
	 * 4	0<=warn<=1
	 * 5	0<=number for test ��/2/3/4<=100
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
		/* @ REQUIRES: repOK()
		 * @ MODIFIES: none
		 * @ EFFECTS: \result == |a-b| (||��ʾ����ֵ����ABS)
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
		 * @ EFFECTS: ��list�����һ���µĽṹ������jiegou(a,b,c,d)
		 */
		list.add(new jiegou(a,b,c,d));
		return;
	}
	
	public boolean samejudge(jiegou j){
		/* @ REQUIRES: repOK() j.repOK()
		 * @ MODIFIES: list
		 * @ EFFECTS: ����ǰʱ��ǰ�����ṹ��j�д洢ָ��==>
		 * 					�жϽṹ��j�д洢��ָ���Ƿ��������ָ�����SAME���
		 * 					����(��ͬ��������ĳ��ָ��ִ�����ǰ������ͬ�㷴���������)==>\result==true
		 * 					����==>\result==false
		 * 			  ����==>����ʱ�䵽�ṹ��j�д洢ָ���ʱ��
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
		 * @ EFFECTS: \result == j�����洢ָ����ⲿ����ʱ�ĸ�ʽ
		 * 			if j�д洢����FRָ��==>
		 * 					\result == [FR,X,Y,Z] 
		 * 						���У�(	1<=X<=10 && 
		 * 								Y==UP || Y==DOWN &&
		 * 								0<=Z)
		 * 			else j �д洢����ERָ��==>
		 * 					\result == [ER,X,Y] 
		 * 						���У�(	1<=X<=10 && 
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
		/* @ REQUIRES: repOK() && j.repOK()
		 * @ MODIFIES: finish time
		 * @ EFFECTS: finish time +1s!!!
		 * 			ģ�⿪����ʱ���+1s,��չ��д���ܴ��ڱ���Ĥ,���Բ�д��
		 */
		finishtime++;
		return;
	}
}
