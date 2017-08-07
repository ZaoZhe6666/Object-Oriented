package elevator;

import java.text.DecimalFormat;
import java.util.ArrayList;

/*
 * 		Overview
 * 
 * 	�������࣬�̳����ϵ�������
 * 	�ϵ����������ɣ��򵥵ļ������㣬��ָͬ���ɸ�� ��������ģ��(����+1s)
 * 	�µ�����(����)����ɣ��Ӵ�ָ����жϣ���ʱ��ģ��ĵ�������+ָ���ڿ���̨�����
 * 
 * */
public class scheduler extends scheduler_old{
	protected ArrayList<jiegou> alllist=new ArrayList<jiegou>();
	protected ArrayList<jiegou> mainlist=new ArrayList<jiegou>();
	protected Elevator ele;
	
	/*����ʽ
	 * 1	all listΪ��Ϊnull������ȫ��Ϊjiegou���͵�ArrayList����
	 * 2	main listΪ��Ϊnull������ȫ��Ϊjiegou���͵�ArrayList����
	 * 3	ele Ϊ��Ϊnull��Elevator���ͱ���,������Elevator���repOK
	 * */
	public boolean repOK(){
		/*@Effects: \result==invariant(this).*/
		if(alllist==null || mainlist==null || ele==null){
			return false;
		}
		if(!(alllist instanceof ArrayList)
		|| !(mainlist instanceof ArrayList)
		|| !(ele instanceof Elevator)){
			return false;
		}
		for(jiegou i:alllist){
			if(i==null){
				return false;
			}
			if(!(i instanceof jiegou)){
				return false;
			}
		}
		for(jiegou i:mainlist){
			if(i==null){
				return false;
			}
			if(!(i instanceof jiegou)){
				return false;
			}
		}
		if(!(ele.repOK())){
			return false;
		}
		return true;
	}
	
	scheduler(Elevator ele){
		/* @ REQUIRES: ele.repOK()
		 * @ MODIFIES: this
		 * @ EFFECTS: this.ele = ele
		 */
		this.ele=ele;
	}
	
	protected boolean btwjudge(int floororder){
		/* @ REQUIRES: 1<=floor order<=10 && repOK()
		 * @ MODIFIES: none
		 * @ EFFECTS: (ele.getway()== 1 && ele.getfloor()<floor order)
		 * 			||(ele.getway()==-1 && ele.getfloor()>floor order)==>/result == true
		 * 				else ==>/result==false
		 * 						
		 * 			�Ӵ��жϣ����((���ݵ�ǰ�˶�״̬ΪUP����ǰ¥��<Ŀ��¥��<=10)
		 * 					 || (���ݵ�ǰ�˶�״̬ΪDOWN��1<=Ŀ��¥��<��ǰ¥��))����true�����򷵻�false
		 */
		nft2=20;
		if(ele.getway()==1){
			nft2=21;
			if(ele.getfloor()<floororder){
				nft2=22;
				return true;
			}
		}
		else if(ele.getway()==-1){
			nft2=23;
			if(ele.getfloor()>floororder){
				nft2=24;
				return true;
			}
		}

		return false;
	}
	
	protected boolean btw(int floororder,String r,int flooradd,int wayadd){
		/* @ REQUIRES: 	1<=floor order<=10
		 * 			&&	(r.equals("ER") || r.equals("FR"))
		 * 			&&	1<=floor add<=10
		 * 			&&	-1<=way add<=1
		 * @ MODIFIES: none
		 * @ EFFECTS: ˳·�Ӵ��ж��õķ���(ժ��ָ������˵��ͼƬ)
		 * 				�������˳·�Ӵ�:
		 * 					1	(���ݵ�ǰ�˶�״̬ΪUP����ǰ¥��<Ŀ��¥��<=10)
		 * 					 || (���ݵ�ǰ�˶�״̬ΪDOWN��1<=Ŀ��¥��<��ǰ¥��)
		 * 					2	��������FR���������������1����
		 * 						(�ⲿ����¥��=��ǰ�˶�״̬) && ((��) || (��))
		 * 							�٣��ⲿ����¥����UP��
		 * 								(�ⲿ����¥��<=Ŀ��¥��) && (�ⲿ����¥��>��ǰ¥��)
		 * 							�ڣ��ⲿ����¥����UP��
		 * 								(�ⲿ����¥��>=Ŀ��¥��) && (�ⲿ����¥��<��ǰ¥��)
		 * 					3	��������ER���������������1,��
		 * 							(��ǰ״̬��UP������¥��>��ǰ¥��)
		 * 						||	(��ǰ״̬��DOWN������¥��<��ǰ¥��)
		 * 				==>\result==true
		 * 			else==>\result==false
		 */
		nft=50;
		if(btwjudge(floororder)){
			if(r.equals("ER")){
				if(ele.getway()==1){
					nft=52;
					if(flooradd>ele.getfloor()){
						nft=53;
						return true;
					}
				}
				else{
					nft=54;
					if(flooradd<ele.getfloor()){
						nft=55;
						return true;
					}
				}
			}
			else{
				nft=56;
				if(wayadd==ele.getway()){
					if(wayadd==1){
						nft=58;
						if(flooradd<=floororder){
							nft=59;
							if(flooradd>ele.getfloor()){
								nft=60;
								return true;
							}
						}
					}
					else{
						nft=61;
						if(flooradd>=floororder){
							nft=62;
							if(flooradd<ele.getfloor()){
								nft=63;
								return true;
							}
						}
					}
				}
			}
		}
		return false;
	}
	
	
	public void printf(jiegou j,String s){
		/* @ REQUIRES: (j.getr.equals("ER") || j.getr.equals("FR"))
		 * 			&& (s.equals("STILL") || s.equals("UP") || s.equals("DOWN"))
		 * @ MODIFIES: none
		 * @ EFFECTS: ���ָ��j�����Ϣ
		 */
		DecimalFormat df=new DecimalFormat("#####0.0");
		if(j.getr().equals("ER")){
			System.out.print("["+j.getr()+","+j.getfloor()+","+(int)j.gettime()+"]/(");
			nft=1;
		} 
		else{
			System.out.print("["+j.getr()+","+j.getfloor()+","+j.getway()+","+(int)j.gettime()+"]/(");
			nft=2;
		}
		System.out.println(j.getfloor()+","+s+","+df.format(finishtime)+")");
		return;
	}
	
	public void calculate(jiegou j){
		/* @ REQUIRES:	repOK() && j.repOK()
		 * @ MODIFIES: this
		 * @ EFFECTS: ��������󼰿�˳·�Ӵ�������
		 * 					==>�������valid��Ϊfalse
		 * 				����������(����ĳ���Ӵ�����δ���)==>��������Ϊ ��һ��δ����Ӵ�����
		 * 										else ==>�������Ϊ��һ��δ�������
		 * 				
		 */
		
		
//		System.out.println("the main is:");
//		printf(j,"???");
		
		if(!j.getvalid()){
			nft=3;
			return;
		}
		if(samejudge(j)){
			nft=4;
			return;
		}
		
		//����������״̬�����µ����˶�����
		j.setvalid();
		finishfloor=ele.getfloor();
		if(j.getfloor()>ele.getfloor()){
			nft=5;
			ele.setway(1);
		}
		else if(j.getfloor()==ele.getfloor()){
			nft=6;
			ele.setway(0);
		}
		else{
			nft=7;
			ele.setway(-1);
		}
		
		//�õ���������Ӵ�����
		mainlist.clear();
		for(jiegou ji:alllist){
			nft3=7;
			if(ji.getvalid()){
				nft3=8;
				int t=0;
				if(ji.getway().equals("UP")){
					nft3=9;
					t=1;
				}
				else if(ji.getway().equals("DOWN")){
					nft3=10;
					t=-1;
				}
				if(btw(j.getfloor(),ji.getr(),ji.getfloor(),t)){
					nft3=11;
					mainlist.add(ji);
				}
			}
		}
//		System.out.println("mlong is:"+mainlist.size());

		listadd(j.getr(),j.getfloor(),j.getway(),finishtime+0.5*sub(j.getfloor(),ele.getfloor())+1.0);
		for(int i=sub(finishfloor,j.getfloor());i>0;i--){
			if(ele.getway()==1){
				nft4=(nft4==0)?11:nft4;
				ele.setfloor(1);
			}
			else{
				nft4=(nft4==0)?12:nft4;
				ele.setfloor(-1);
			}
			finishtime+=0.5;
			boolean judge_shun=false;
			int fi=1;
			for(jiegou k:mainlist){
				if(k.gettime()<finishtime){
					//�ڵ���ǰ�����Ŀ��Ӵ�����ִ���Ӵ�
					nft4=(nft4>13)?nft4:13;
					if(k.getfloor()==ele.getfloor()){
						if(samejudge(k)){
							nft4=15;
							continue;
						}
						String s;
						if(ele.getway()==1){
							nft4=16;
							s="UP";
						}
						else{
							nft4=17;
							s="DOWN";
						}
						printf(k,s);
						judge_shun=true;
						listadd(k.getr(),k.getfloor(),k.getway(),finishtime+1.0);
						fi=k.getfloor();
						k.setvalid();
					}
				}
				else{
					nft4=(nft4>19)?nft4:19;
					//�����ִ�е��Ӵ�����
					if(k.getfloor()==ele.getfloor()){
						nft4=(nft4>20)?nft4:20;
						k.valid=false;
					}
				}
			}
			if(judge_shun && fi!=j.getfloor()){
				nft3=22;
				oac_door();
			}
		}

		print(j);
		warn=0;
		
		//δ����Ӵ������Ϊ������
		nft5=(nft5>0)?nft5:0;
		for(jiegou i:mainlist){
			nft5=(nft5>25)?nft5:25;
			if(i.getvalid()){
				nft5=(nft5>26)?nft5:26;
				if(finishtime-1>i.gettime()){
					nft5=(nft5>27)?nft5:27;
					calculate(i);
					break;
				}
			}
		}
		listadd(j.getr(),j.getfloor(),j.getway(),finishtime);
	}

	
	
	
	
	
	public void print(jiegou j){
		/* @ REQUIRES: (j.getr.equals("ER") || j.getr.equals("FR"))
		 * @ MODIFIES: finish time
		 * @ EFFECTS:���ָ��j����Ϣ�����ʱ��
		 * 				ģ�⿪����+1s ==>STILLָ���ȿ�����+1s�����
		 * 								����������ٿ�����+1s
		 */
		if(j.getr().equals("ER")){
			nft5=(nft5>0)?nft5:1;
			System.out.print("["+j.getr()+","+j.getfloor()+","+(int)j.gettime()+"]/");
		}
		else{
			nft5=(nft5>0)?nft5:2;
			System.out.print("["+j.getr()+","+j.getfloor()+","+j.getway()+","+(int)j.gettime()+"]/");
		}
		DecimalFormat df=new DecimalFormat("#####0.0");
		if(j.getfloor()==finishfloor){
			nft5+=(nft5>2)?0:10;
			oac_door();
			System.out.println("("+j.getfloor()+",STILL,"+df.format(finishtime)+")");
		}
		else{
			nft5+=(nft5>2)?0:20;
			String s = (j.getfloor()>finishfloor)?("UP"):("DOWN");
			System.out.println("("+j.getfloor()+","+s+","+df.format(finishtime)+")");
			oac_door();
		}
		return;
	}
	
	public void savelist(jiegou j){
		/* @ REQUIRES: j.repOK()
		 * @ MODIFIES: all list
		 * @ EFFECTS: �洢һ���µ���������ܵ��������
		 */
		alllist.add(j);
	}
	
	
	
}
