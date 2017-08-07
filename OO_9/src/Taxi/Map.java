package Taxi;

import java.awt.Point;

public class Map {
	public Controller ctrl;
	public TaxiGUI gui;
	public int road[][];
	public int map_gui[][];
	public int road_map[][];
	public int weight_map[][];
	/*
	 *  [1][1-80]  ~ [80][1-80] [i][j]相连的横 
	 * 	[101][1-80] ~ [179][1-80] [i][j]相连的竖
	 * */
	
	
	Map(){
		/* @ REQUIRES:	NONE
		 * @ MODIFIES:	road[][]	road_map[][]	map_gui[][]	weight_map[][]
		 * @ EFFECTS: 	initial these maps
		 */
		this.road = new int[Readin.MAX+5][Readin.MAX+5];
		this.road_map = new int[Readin.MAX+5][Readin.MAX+5];
		this.map_gui = new int[Readin.MAX+5][Readin.MAX+5];
		this.weight_map = new int [Readin.MAX*3+5][Readin.MAX+5];
		for(int i=0;i<Readin.MAX+2;i++){
			for(int j=0;j<Readin.MAX+2;j++){
				road[i][j]=-1;
			}
		}
	}
	public void addmap(int line,int list,int value){
		/* @ REQUIRES:Point(x,y),value
		 * @ MODIFIES:road[][]
		 * @ EFFECTS: read the file into the map //change the map
		 */
		if(road[line][list]==-1){
			this.road[line][list]=value;
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
					if(can_right(line,list)){
						//1->X   3->X
						System.out.println("The point ("+line+","+list+") could reach ("+line+","+(list+1)+") already!");
					}
					else{
						//0->1   2->3
						road[line][list]+=1;
						gui.SetRoadStatus(new Point(line-1,list-1),new Point(line-1,list),1);
					}
					break;
				}
				case 2:{
					if(can_down(line,list)){
						//2->X   3->X
						System.out.println("The point ("+line+","+list+") could reach ("+(line+1)+","+list+") already!");
					}
					else{
						//0->2   1->3
						road[line][list]+=2;
						gui.SetRoadStatus(new Point(line-1,list-1),new Point(line,list-1),1);
					}
					break;
				}
				case 3:{
					if(!can_right(line,list)){
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
					if(!can_down(line,list)){
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
	public int getmapweight(int line,int list,int way){
		/* @ REQUIRES: Point(x,y) int way
		 * @ MODIFIES: none
		 * @ EFFECTS:  get weight of the map of the weightmap
		 */
		int move = (way == 1)?100:0;
		int i=line+move;
		int j=list;
		return weight_map[i][j];
	}
	public void setmapweight(int line,int list,int way,int weight){
		/* @ REQUIRES: Point(x,y) int weight int way
		 * @ MODIFIES: weightmap[][]
		 * @ EFFECTS: set weight of the map to the weightmap
		 */
		int move = (way == 1)?100:0;
		int i=line+move;
		int j=list;
		this.weight_map[i][j]+=weight;
	}
	public int setmap(){
		/* @ REQUIRES: none
		 * @ MODIFIES:road[][]	road_map[][]
		 * @ EFFECTS: change the file number to my interesting number
		 */
		//初始化第0行,第0列
		for(int i=0;i<=Readin.MAX;i++){
			road[0][i]=0;
			road[i][0]=0;
			road_map[0][i]=0;
			road_map[i][0]=0;
		}
		
		Main.reset=true;
		
		/*			2					10
		 * 1	┌	┬	┐	3			╦			14 -
		 * 	
		 * 4	├	┼	┤	6	11 ╠	╬	╣12		15 |
		 * 
		 * 7	└	┴	┘	9			╩	
		 * 			8					13
		 * 
		 * 0  .
		 * 1  -
		 * 2  │
		 * 3  ┌
		 * 
		 * 
		 * 
		 * */
		for(int i=1;i<=Readin.MAX;i++){
			for(int j=1;j<=Readin.MAX;j++){
				map_gui[i-1][j-1]=road[i][j];
				//异常判断
				if(j==Readin.MAX){//最右侧越界
					if(road[i][j]==1 || road[i][j]==3){
						System.err.println("Line "+i+" List "+j+" ——Invalid Input!");
//						return -1;
					}
				}
				else if(i==Readin.MAX){//最下方越界
					if(road[i][j]==2 || road[i][j]==3){
						System.err.println("Line "+i+" List "+j+" ——Invalid Input!");
//						return -1;
					}
				}
				
				//正常判断
				boolean up		=	(road[i-1][j]==2 || road[i-1][j]==3);
				boolean down	=	(road[i][j]==2 || road[i][j]==3);
				boolean left	=	(road[i][j-1]==1 || road[i][j-1]==3);
				boolean right	=	(road[i][j]==1 || road[i][j]==3);
				
				if(     !up && !down &&	!left && !right){
					System.err.println("Line "+i+" List "+j+" ——Invalid Input!(an isolated island)");
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
			}
		}
		return 0;
	}
	public boolean can_up(int line,int list){
		/* @ REQUIRES: Point(x,y) 
		 * @ MODIFIES: none
		 * @ EFFECTS: return if the point could reach the up point
		 */
		int judge=road_map[line][list];
		return ((judge>=4 && judge<=9) || judge==13 || judge==15);
	}
	public boolean can_down(int line,int list){
		/* @ REQUIRES: Point(x,y) 
		 * @ MODIFIES: none
		 * @ EFFECTS: return if the point could reach the down point
		 */
		int judge=road_map[line][list];
		return ((judge>=1 && judge<=6) || judge==10 || judge==15);
	}
	public boolean can_left(int line,int list){
		/* @ REQUIRES: Point(x,y) 
		 * @ MODIFIES: none
		 * @ EFFECTS: return if the point could reach the left point
		 */
		int judge=road_map[line][list];
		return (judge==2 || judge==3 || judge==5 || judge==6 || judge==8 || judge==9 || judge==12 || judge==14);
	}
	public boolean can_right(int line,int list){
		/* @ REQUIRES: Point(x,y) 
		 * @ MODIFIES: none
		 * @ EFFECTS: return if the point could reach the right point
		 */
		int judge=road_map[line][list];
		return (judge==2 || judge==1 || judge==5 || judge==4 || judge==8 || judge==7 || judge==11 || judge==14);
	}
	public int[][] getmap_gui(){
		/* @ REQUIRES: none
		 * @ MODIFIES: none
		 * @ EFFECTS: return the map to initial the gui's map
		 */
		return map_gui;
	}
	
	public void print_map(){
		/* @ REQUIRES: none
		 * @ MODIFIES: none
		 * @ EFFECTS: print my interesting static map
		 */
		System.err.println("The map is:");
//		System.out.println("┌┬┏┳╔╦╭─┍┎╒╓╱┄");
		for(int i=1;i<=Readin.MAX;i++){
			for(int j=1;j<=Readin.MAX;j++){
				switch (road_map[i][j]){
				case 0:System.err.print("╳");
						break;
				case 1:System.err.print("┌");
						break;
				case 2:System.err.print("┬");
						break;
				case 3:System.err.print("┐");
						break;
				case 4:System.err.print("├");
						break;
				case 5:System.err.print("┼");
						break;
				case 6:System.err.print("┤");
						break;
				case 7:System.err.print("└");
						break;
				case 8:System.err.print("┴");
						break;
				case 9:System.err.print("┘");
						break;
				case 10:System.err.print("╦");
						break;
				case 11:System.err.print("╠");
						break;
				case 12:System.err.print("╣");
						break;
				case 13:System.err.print("╩");
						break;
				case 14:System.err.print("─");
						break;
				case 15:System.err.print("│");
						break;
				default:System.err.print("How could you get there???");
						break;
				}
			}
			System.err.println("");
		}
	}
	
}
