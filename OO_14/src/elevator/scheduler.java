package elevator;

import java.text.DecimalFormat;
import java.util.ArrayList;

/*
 * 		Overview
 * 
 * 	调度器类，继承自老调度器类
 * 	老调度器类可完成：简单的减法运算，相同指令的筛除 ，开关门模拟(就是+1s)
 * 	新调度器(该类)可完成：捎带指令的判断，按时间模拟的电梯运行+指令在控制台的输出
 * 
 * 	AF(c) = (all_list,main_list,ele).where   all_list = c.all_list,main_list = c.main_list,ele = c.ele
 * 
 * */
public class scheduler extends scheduler_old{
	protected ArrayList<jiegou> alllist=new ArrayList<jiegou>();
	protected ArrayList<jiegou> mainlist=new ArrayList<jiegou>();
	protected Elevator ele;
	
	/*不变式
	 * 1	all list为不为null的子项全部为jiegou类型的ArrayList队列
	 * 2	main list为不为null的子项全部为jiegou类型的ArrayList队列
	 * 3	ele 为不为null的Elevator类型变量,且满足Elevator类的repOK
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
		 * 			捎带判断，如果((电梯当前运动状态为UP：当前楼层<目标楼层<=10)
		 * 					 || (电梯当前运动状态为DOWN：1<=目标楼层<当前楼层))返回true，否则返回false
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
		 * @ EFFECTS: 顺路捎带判断用的方法(摘自指导书中说明图片)
		 * 				当可完成顺路捎带:
		 * 					1	(电梯当前运动状态为UP：当前楼层<目标楼层<=10)
		 * 					 || (电梯当前运动状态为DOWN：1<=目标楼层<当前楼层)
		 * 					2	对于任意FR类型请求，如果满足1，则：
		 * 						(外部请求楼层=当前运动状态) && ((①) || (②))
		 * 							①：外部请求楼层是UP：
		 * 								(外部请求楼层<=目标楼层) && (外部请求楼层>当前楼层)
		 * 							②：外部请求楼层是UP：
		 * 								(外部请求楼层>=目标楼层) && (外部请求楼层<当前楼层)
		 * 					3	对于任意ER类型请求，如果满足1,则：
		 * 							(当前状态是UP：请求楼层>当前楼层)
		 * 						||	(当前状态是DOWN：请求楼层<当前楼层)
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
		 * @ EFFECTS: 输出指令j完成信息
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
		 * @ EFFECTS: 完成主请求及可顺路捎带的请求
		 * 					==>主请求的valid置为false
		 * 				更新主请求(若有某可捎带请求未完成)==>主请求变更为 第一个未完成捎带请求
		 * 										else ==>主请求变为第一个未完成请求
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
		
		//更新主请求状态，更新电梯运动方向
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
		
		//得到主请求可捎带队列
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
					//在到达前发出的可捎带请求，执行捎带
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
					//到达后执行的捎带请求
					if(k.getfloor()==ele.getfloor()){
						nft4=20;
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
		
		//未完成捎带请求成为主请求
		for(jiegou i:mainlist){
			nft5=(nft5>25)?nft5:25;
			if(i.getvalid()){
				nft5=(nft5>26)?nft5:26;
				if(finishtime-1>i.gettime()){
					nft5=27;
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
		 * @ EFFECTS:输出指令j的信息及完成时间
		 * 				模拟开关门+1s ==>STILL指令先开关门+1s再输出
		 * 								否则先输出再开关门+1s
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
		 * @ EFFECTS: 存储一个新的请求进入总的请求队列
		 */
		alllist.add(j);
	}
	
	
	
}
