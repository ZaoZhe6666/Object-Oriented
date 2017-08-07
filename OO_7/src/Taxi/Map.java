package Taxi;

public class Map {
	private int road[][];
	private int map_gui[][];
	private int road_map[][];
	Map(){
		this.road = new int[Readin.MAX+5][Readin.MAX+5];
		this.road_map = new int[Readin.MAX+5][Readin.MAX+5];
		this.map_gui = new int[Readin.MAX+5][Readin.MAX+5];
	}
	public void addmap(int line,int list,int value){
		road[line][list]=value;
	}
	public int getmappoint(int line,int list){
		return road[line][list];
	}
	public int setmap(){
		//初始化第0行,第0列
		for(int i=0;i<=Readin.MAX;i++){
			road[0][i]=0;
			road[i][0]=0;
			road_map[0][i]=0;
			road_map[i][0]=0;
		}
		
		
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
	
	public int getmap(int line,int list){
		return road_map[line][list];
	}
	public boolean can_up(int line,int list){
		int judge=road_map[line][list];
		return ((judge>=4 && judge<=9) || judge==13 || judge==15);
	}
	public boolean can_down(int line,int list){
		int judge=road_map[line][list];
		return ((judge>=1 && judge<=6) || judge==10 || judge==15);
	}
	public boolean can_left(int line,int list){
		int judge=road_map[line][list];
		return (judge==2 || judge==3 || judge==5 || judge==6 || judge==8 || judge==9 || judge==12 || judge==14);
	}
	public boolean can_right(int line,int list){
		int judge=road_map[line][list];
		return (judge==2 || judge==1 || judge==5 || judge==4 || judge==8 || judge==7 || judge==11 || judge==14);
	}
	public int[][] getmap_gui(){
		return map_gui;
	}
	
	public void print_map(){
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
