package elevator;

import java.util.ArrayList;

/*
 * 		Overview
 * 
 * 	��������࣬���ڴ洢���е���Ч����(��Ч������Request�ⲿ�������Ѿ�ɾȥ)
 * 
 * */

class Queue {
	protected ArrayList<jiegou> list=new ArrayList<jiegou>();
	protected int nft=0;
	
	protected int order=0;
	
	/*����ʽ
	 * 1	listΪ��ΪNULL������ȫ��Ϊjiegou���ͱ�����ArrayList���ͱ���
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
		/* @ REQUIRES: repOK()
		 * @ MODIFIES: none
		 * @ EFFECTS: none
		 */
	}
	
	public void saveone(String s){
		/* @ REQUIRES: repOK() && sΪ�Ϸ�����
		 * @ MODIFIES: list
		 * @ EFFECTS: ���ⲿ������request�л��һ���µĺϷ����󣬲�����listĩβ
		 * 
		 * 			�Ϸ������壺
		 * 				����ΪString���͵ı���
		 * 				sΪ����(FR,X,Y,Z)���ַ���
		 * 						���У�(	1<=X<=10 && 
		 * 								Y==UP || Y==DOWN &&
		 * 								0<=Z,X��Z��Ϊ������)
		 * 				������(ER,X,Y)���ַ���
		 * 						���У�(	1<=X<=10 && 
		 * 								0<=Y,X��Y��Ϊ������)
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
		 * 			��˳���ظ���ȡ����0��list.size()�еĵ�order��
		 */
		return list.get(order++);
	}
	public void setorder(){
		/* @ REQUIRES: repOK()
		 * @ MODIFIES: order
		 * @ EFFECTS: ��ʼ����order����
		 */
		order=0;
		return;
	}
}