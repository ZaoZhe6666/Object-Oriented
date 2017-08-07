package Taxi;

import java.awt.Point;

/*
 * 		Overview
 * 
 * 		��ͼ�࣬�����ж϶���ĵ�·��ͼ�Ƿ���Ϲ淶
 * 		ͬʱ��0-3��ɵĵ�ͼ�޸�Ϊ������Ƶ�1-15��ɵĵ�ͼ
 * */

public class Map {
	public Controller ctrl;
	public TaxiGUI gui;
	public int road[][];
	public int road_vip[][];
	public int map_gui[][];
	public int road_map[][];
	public int road_map_vip[][];
	public int weight_map_old[][];
	public int weight_map_new[][];
	/*
	 *  [1][1-80]  ~ [80][1-80] [i][j]�����ĺ� 
	 * 	[101][1-80] ~ [179][1-80] [i][j]��������
	 * */
	
	/*����ʽ��
	 * 1	ctrlΪController�ͱ�������Ϊnull
	 * 2	guiΪTaxiGUI�ͱ�������Ϊnull
	 * 3	road��Ϊnull������ÿһ������a������
	 * 			-1<=a<=3
	 * 4	map_gui��Ϊnull������ÿһ������a������
	 * 			0<=a<=3
	 * 5	road_map��Ϊnull������ÿһ������a������
	 * 			1<=a<=15
	 * 6	weight_map_old��Ϊnull������ÿһ������a������
	 * 			0<=a<=100
	 * 7	weight_map_new��Ϊnull������ÿһ������a������
	 * 			0<=a<=100
	 * 8	road_map_vip��Ϊnull������ÿһ������a������
	 * 			1<=a<=15
	 * 9	road_vip��Ϊnull������ÿһ������a������
	 * 			-1<=a<=3
	 * */
	public boolean repOK(){
		/*@Effects: \result==invariant(this).*/
		try{
			if(ctrl==null || !ctrl.repOK()
			|| road==null || road_vip==null
			|| map_gui==null || gui==null
			|| road_map==null || road_map_vip==null
			|| weight_map_old==null || weight_map_new==null){
				return false;
			}
			if(!(ctrl instanceof Controller)
			|| !(gui instanceof TaxiGUI)
			|| !(road instanceof int[][])
			|| !(road_vip instanceof int[][])
			|| !(map_gui instanceof int[][])
			|| !(road_map instanceof int[][])
			|| !(road_map_vip instanceof int[][])
			|| !(weight_map_old instanceof int[][])
			|| !(weight_map_new instanceof int[][])){
				return false;
			}
			if(Readin.MAX<0){
				return false;
			}
			for(int i=0;i<=Readin.MAX;i++){
				for(int j=0;j<=Readin.MAX;j++){
					if(road[i][j]<-1 || road[i][j]>3){
						return false;
					}
					if(road_vip[i][j]<-1 || road_vip[i][j]>3){
						return false;
					}
					if(road_map[i][j]<1 || road_map[i][j]>15){
						return false;
					}
					if(road_map_vip[i][j]<1 || road_map_vip[i][j]>15){
						return false;
					}
					if(map_gui[i][j]<0 || map_gui[i][j]>3){
						return false;
					}
				}
			}
			for(int i=0;i<=Readin.MAX*3+2;i++){
				for(int j=0;j<=Readin.MAX+2;j++){
					if(weight_map_old[i][j]<0 || weight_map_old[i][j]>100){
						return false;
					}
					if(weight_map_new[i][j]<0 || weight_map_new[i][j]>100){
						return false;
					}
				}
			}
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	
	Map(){
		/* @ REQUIRES:	Readin.MAX=80	this�и����Null repOK
		 * @ MODIFIES:	this
		 * @ EFFECTS: 	��ʼ������Ĵ�С�����Ұ�road[][]��ʹ�ò��ֳ�ʼ��Ϊ-1
		 */
		this.road = new int[Readin.MAX+5][Readin.MAX+5];
		this.road_vip = new int[Readin.MAX+5][Readin.MAX+5];
		this.road_map = new int[Readin.MAX+5][Readin.MAX+5];
		this.road_map_vip = new int[Readin.MAX+5][Readin.MAX+5];
		this.map_gui = new int[Readin.MAX+5][Readin.MAX+5];
		this.weight_map_old = new int [Readin.MAX*3+5][Readin.MAX+5];
		this.weight_map_new = new int [Readin.MAX*3+5][Readin.MAX+5];
		for(int i=0;i<Readin.MAX+2;i++){
			for(int j=0;j<Readin.MAX+2;j++){
				road[i][j]=-1;
			}
		}
	}
	public void addmap(int line,int list,int value){
		/* @ REQUIRES:	1<=line<=80
		 * 			&&	1<=list<=80
		 * 			&&	0<=value<=4
		 * 			&&	road!=null
		 * 			&&	repOK()
		 * @ MODIFIES:road[][] road_vip[][]
		 * @ EFFECTS: 
		 * 			����ʹ�ã���ʼ��road��road_vip������ͼ
		 * 			֮��ʹ�ã���ɾ��ͼ�������޸�(line,list)����λ�õ��Ҳ����ߺ��·����ߵ�����
		 * 				���Ҳ��������߶�Ҫ����Ҳ�����==>��ʾ���и�����
		 * 				���·��������߶�Ҫ����·�����==>��ʾ��������
		 * 				
		 * 				���Ҳ�û�����߶�Ҫɾ���Ҳ�����==>��ʾ�����ڸ�����
		 * 				���·�û�����߶�Ҫɾ���·�����==>��ʾ�����ڸ�����
		 * 
		 * 				����==>��ӻ�ɾ����Ӧ�����ߣ�ͬ����ʾ��gui�ϣ����Ӳ�����ͬ����road_vip�ϣ���ɾ����������
		 */
		if(road[line][list]==-1){
			this.road[line][list]=value;
			this.road_vip[line][list]=value;
		}
		else{
			switch(value){
			/*
			 * value:
			 * 1 open to right
			 * 2 open to down
			 * 3 close to right
			 * 4 close to down
			 * */
				case 1:{
					if(can_right(line,list,road_map)){
						//1->X   3->X
						System.out.println("The point ("+line+","+list+") could reach ("+line+","+(list+1)+") already!");
					}
					else{
						//0->1   2->3
						road[line][list]+=1;
						road_vip[line][list]+=1;
						gui.SetRoadStatus(new Point(line-1,list-1),new Point(line-1,list),1);
					}
					break;
				}
				case 2:{
					if(can_down(line,list,road_map)){
						//2->X   3->X
						System.out.println("The point ("+line+","+list+") could reach ("+(line+1)+","+list+") already!");
					}
					else{
						//0->2   1->3
						road[line][list]+=2;
						road_vip[line][list]+=2;
						gui.SetRoadStatus(new Point(line-1,list-1),new Point(line,list-1),1);
					}
					break;
				}
				case 3:{
					if(!can_right(line,list,road_map)){
						//0->X   2->X
						System.out.println("The point ("+line+","+list+") couldn't reach ("+line+","+(list+1)+") already!");
					}
					else{
						//1->0   3->2
						road[line][list]-=1;
						gui.SetRoadStatus(new Point(line-1,list-1),new Point(line-1,list),0);
					}
					break;
				}
				case 4:{
					if(!can_down(line,list,road_map)){
						//0->X   1->X
						System.out.println("The point ("+line+","+list+") couldn't reach ("+(line+1)+","+list+") already!");
					}
					else{
						//2->0   3->1
						road[line][list]-=2;
						gui.SetRoadStatus(new Point(line-1,list-1),new Point(line,list-1),0);
					}
					break;
				}
				default:{
					break;
				}
			}
		}
	}
	
	
	public void refreshmapweight(){
		/* @ REQUIRES:	Readin.MAX=80	this�и����Null repOK()
		 * @ MODIFIES:	weight_map_old[][] 	weight_map_new[][]
		 * @ EFFECTS: 	ˢ��������ͼ����֮ǰ200ms������ͼ����weight_map_old,��weight_map_new����
		 */
		for(int i=0;i<Readin.MAX*3+2;i++){
			for(int j=0;j<Readin.MAX+2;j++){
				this.weight_map_old[i][j]=this.weight_map_new[i][j];
				this.weight_map_new[i][j]=0;
			}
		}
	}
	
	
	public int getmapweight(int line,int list,int way){
		/* @ REQUIRES:	1<=line<=80
		 * 			&&	1<=list<=80
		 * 			&&	0<=way<=1
		 * 			&&	weight_map_old!=null
		 * 			&&	��ͼ��(line,list)��way���Ӧ�ĵ�֮���������
		 * 			(way=1ʱȡ�����������·�������
		 * 			 way=0ʱȡ�����������Ҳ������)
		 * 			&&	repOK()
		 * @ MODIFIES: none
		 * @ EFFECTS:  ͨ�����·�set map weight����ͬ�������ִ����㷨
		 * 				����(line,list)�������ıߵ�����
		 */
		int move = (way == 1)?100:0;
		int i=line+move;
		int j=list;
		return weight_map_old[i][j];
	}
	
	public void setmapweight(int line,int list,int way,int weight){
		/* @ REQUIRES:	1<=line<=80
		 * 			&&	1<=list<=80
		 * 			&&	0<=way<=1
		 * 			&&	weight_map_old!=null
		 * 			&&	1<=weight<=100
		 * 			&&	��ͼ��(line,list)��way���Ӧ�ĵ�֮���������
		 * 			(way=1ʱȡ�����������·�������
		 * 			 way=0ʱȡ�����������Ҳ������)
		 * 			&&	repOK()
		 * @ MODIFIES: weight_map_new[]
		 * @ EFFECTS:  ͨ�����Ϸ�get map weight����ͬ�������ִ����㷨
		 * 				��(line,list)�������ıߵ���������weight
		 */
		int move = (way == 1)?100:0;
		int i=line+move;
		int j=list;
		this.weight_map_new[i][j]+=weight;
	}
	public int setmap(){
		/* @ REQUIRES: road!=null && road_map!=null &&��main.reset=false && Readin.MAX=80&&	repOK()
		 * @ MODIFIES:road[][]	road_map[][],road_map_vip[][]
		 * @ EFFECTS: ��0-3��ʾ�ĵ�ͼroad��ת��������õ���1-15��ʾ�ĵ�ͼroad_map
		 * 				�ɹ�����0,ʧ����ǿ�м��������ļ������д����������Է���0
		 * 			ͬ���Ĳ�������0-3��ʾ�ĵ�ͼroad_vip��ת��������õ���1-15��ʾ�ĵ�ͼroad_map_vip
		 */
		//��ʼ����0��,��0��
		for(int i=0;i<=Readin.MAX;i++){
			road[0][i]=0;
			road[i][0]=0;
			road_map[0][i]=0;
			road_map[i][0]=0;
		}
		
		
		/*			2					10
		 * 1	��	��	��	3			�j			14 -
		 * 	
		 * 4	��	��	��	6	11 �d	�p	�g12		15 |
		 * 
		 * 7	��	��	��	9			�m	
		 * 			8					13
		 * 
		 * 0  .
		 * 1  -
		 * 2  ��
		 * 3  ��
		 * 
		 * 
		 * 
		 * */
		for(int i=1;i<=Readin.MAX;i++){
			for(int j=1;j<=Readin.MAX;j++){
				map_gui[i-1][j-1]=road[i][j];
				//�쳣�ж�
				if(j==Readin.MAX){//���Ҳ�Խ��
					if(road[i][j]==1 || road[i][j]==3){
						System.err.println("Line "+i+" List "+j+" ����Invalid Input!");
//						return -1;
					}
				}
				else if(i==Readin.MAX){//���·�Խ��
					if(road[i][j]==2 || road[i][j]==3){
						System.err.println("Line "+i+" List "+j+" ����Invalid Input!");
//						return -1;
					}
				}
				
				//�����ж�
				boolean up		=	(road[i-1][j]==2 || road[i-1][j]==3);
				boolean down	=	(road[i][j]==2 || road[i][j]==3);
				boolean left	=	(road[i][j-1]==1 || road[i][j-1]==3);
				boolean right	=	(road[i][j]==1 || road[i][j]==3);
				
				if(     !up && !down &&	!left && !right){
					System.err.println("Line "+i+" List "+j+" ����Invalid Input!(an isolated island)");
//					return -1;
				}
				else if(!up	&&  down && !left &&  right){
					road_map[i][j]=1;
				}
				else if(!up &&  down &&  left &&  right){
					road_map[i][j]=2;
				}
				else if(!up &&  down &&  left && !right){
					road_map[i][j]=3;
				}
				else if( up &&  down && !left &&  right){
					road_map[i][j]=4;
				}
				else if( up &&  down &&  left &&  right){
					road_map[i][j]=5;
				}
				else if( up &&  down &&  left && !right){
					road_map[i][j]=6;
				}
				else if( up && !down && !left &&  right){
					road_map[i][j]=7;
				}
				else if( up && !down &&  left &&  right){
					road_map[i][j]=8;
				}
				else if( up && !down &&  left && !right){
					road_map[i][j]=9;
				}
				else if(!up &&  down && !left && !right){
					road_map[i][j]=10;
				}
				else if(!up && !down && !left &&  right){
					road_map[i][j]=11;
				}
				else if(!up && !down &&  left && !right){
					road_map[i][j]=12;
				}
				else if( up && !down && !left && !right){
					road_map[i][j]=13;
				}
				else if(!up && !down &&  left &&  right){
					road_map[i][j]=14;
				}
				else if( up &&  down && !left && !right){
					road_map[i][j]=15;
				}
				else{
					System.out.println("How could you get there?");
				}
				
				
				
				
				
				
				if(j==Readin.MAX){//���Ҳ�Խ��
					if(road_vip[i][j]==1 || road_vip[i][j]==3){
						System.err.println("Line "+i+" List "+j+" ����Invalid Input!");
//						return -1;
					}
				}
				else if(i==Readin.MAX){//���·�Խ��
					if(road_vip[i][j]==2 || road_vip[i][j]==3){
						System.err.println("Line "+i+" List "+j+" ����Invalid Input!");
//						return -1;
					}
				}
				
				//�����ж�
				up		=	(road_vip[i-1][j]==2 || road_vip[i-1][j]==3);
				down	=	(road_vip[i][j]	 ==2 || road_vip[i][j]  ==3);
				left	=	(road_vip[i][j-1]==1 || road_vip[i][j-1]==3);
				right	=	(road_vip[i][j]  ==1 || road_vip[i][j]  ==3);
				
				if(     !up && !down &&	!left && !right){
					System.err.println("Line "+i+" List "+j+" ����Invalid Input!(an isolated island)");
//					return -1;
				}
				else if(!up	&&  down && !left &&  right){
					road_map_vip[i][j]=1;
				}
				else if(!up &&  down &&  left &&  right){
					road_map_vip[i][j]=2;
				}
				else if(!up &&  down &&  left && !right){
					road_map_vip[i][j]=3;
				}
				else if( up &&  down && !left &&  right){
					road_map_vip[i][j]=4;
				}
				else if( up &&  down &&  left &&  right){
					road_map_vip[i][j]=5;
				}
				else if( up &&  down &&  left && !right){
					road_map_vip[i][j]=6;
				}
				else if( up && !down && !left &&  right){
					road_map_vip[i][j]=7;
				}
				else if( up && !down &&  left &&  right){
					road_map_vip[i][j]=8;
				}
				else if( up && !down &&  left && !right){
					road_map_vip[i][j]=9;
				}
				else if(!up &&  down && !left && !right){
					road_map_vip[i][j]=10;
				}
				else if(!up && !down && !left &&  right){
					road_map_vip[i][j]=11;
				}
				else if(!up && !down &&  left && !right){
					road_map_vip[i][j]=12;
				}
				else if( up && !down && !left && !right){
					road_map_vip[i][j]=13;
				}
				else if(!up && !down &&  left &&  right){
					road_map_vip[i][j]=14;
				}
				else if( up &&  down && !left && !right){
					road_map_vip[i][j]=15;
				}
				else{
					System.out.println("How could you get there?");
				}
				
				
				
			}
		}
		Main.reset=true;
		Light.reset=true;
		return 0;
	}
	
	
	public boolean can_up(int line,int list,int[][] map){///////////////////////////////////////////////////////////////////////
		/* @ REQUIRES:	1<=line<=80
		 * 			&&	1<=list<=80
		 * 			&&	map[line][list]!=null
		 * 			&&	repOK()
		 * @ MODIFIES: none
		 * @ EFFECTS: �ж�ĳ��(line,list)�Ƿ�ֱ�ӿɴ��Ϸ��ĵ�
		 * 				���ɴ�򷵻�true
		 * 				���򷵻�false					
		 */
		int judge=map[line][list];
		return ((judge>=4 && judge<=9) || judge==13 || judge==15);
	}
	public boolean can_down(int line,int list,int[][] map){///////////////////////////////////////////////////////////////////////
		/* @ REQUIRES:	1<=line<=80
		 * 			&&	1<=list<=80
		 * 			&&	map[line][list]!=null
		 * 			&&	repOK()
		 * @ MODIFIES: none
		 * @ EFFECTS: �ж�ĳ��(line,list)�Ƿ�ֱ�ӿɴ��·��ĵ�
		 * 				���ɴ�򷵻�true
		 * 				���򷵻�false					
		 */
		int judge=map[line][list];
		return ((judge>=1 && judge<=6) || judge==10 || judge==15);
	}
	public boolean can_left(int line,int list,int[][]map){///////////////////////////////////////////////////////////////////////
		/* @ REQUIRES:	1<=line<=80
		 * 			&&	1<=list<=80
		 * 			&&	road_map[line][list]!=null
		 * 			&&	repOK()
		 * @ MODIFIES: none
		 * @ EFFECTS: �ж�ĳ��(line,list)�Ƿ�ֱ�ӿɴ��󷽵ĵ�
		 * 				���ɴ�򷵻�true
		 * 				���򷵻�false					
		 */
		int judge=map[line][list];
		return (judge==2 || judge==3 || judge==5 || judge==6 || judge==8 || judge==9 || judge==12 || judge==14);
	}
	public boolean can_right(int line,int list,int[][] map){///////////////////////////////////////////////////////////////////////
		/* @ REQUIRES:	1<=line<=80
		 * 			&&	1<=list<=80
		 * 			&&	road_map[line][list]!=null
		 * 			&&	repOK()
		 * @ MODIFIES: none
		 * @ EFFECTS: �ж�ĳ��(line,list)�Ƿ�ֱ�ӿɴ��ҷ��ĵ�
		 * 				���ɴ�򷵻�true
		 * 				���򷵻�false					
		 */
		int judge=map[line][list];
		return (judge==2 || judge==1 || judge==5 || judge==4 || judge==8 || judge==7 || judge==11 || judge==14);
	}
	public int[][] getmap_gui(){
		/* @ REQUIRES: map_gui!=null &&	repOK()
		 * @ MODIFIES: none
		 * @ EFFECTS: ����map_gui����ʼ��gui��ͼ
		 */
		return map_gui;
	}
	
	public void print_map(){
		/* @ REQUIRES: road_map!=null && Readin.MAX=80 &&	repOK()
		 * @ MODIFIES: none
		 * @ EFFECTS: �������Դ��ľ�̬��ͼ
		 */
		System.err.println("The map is:");
//		System.out.println("���Щ��רX�j�q�������V�W�u��");
		for(int i=1;i<=Readin.MAX;i++){
			for(int j=1;j<=Readin.MAX;j++){
				switch (road_map[i][j]){
				case 0:System.err.print("�w");
						break;
				case 1:System.err.print("��");
						break;
				case 2:System.err.print("��");
						break;
				case 3:System.err.print("��");
						break;
				case 4:System.err.print("��");
						break;
				case 5:System.err.print("��");
						break;
				case 6:System.err.print("��");
						break;
				case 7:System.err.print("��");
						break;
				case 8:System.err.print("��");
						break;
				case 9:System.err.print("��");
						break;
				case 10:System.err.print("�j");
						break;
				case 11:System.err.print("�d");
						break;
				case 12:System.err.print("�g");
						break;
				case 13:System.err.print("�m");
						break;
				case 14:System.err.print("��");
						break;
				case 15:System.err.print("��");
						break;
				default:System.err.print("How could you get there???");
						break;
				}
			}
			System.err.println("");
		}
	}
	
}
