package elevator;

/*		Overview
 * 
 * 	电梯类，模拟电梯升降运动，仅用于记录电梯当前运动方向和电梯所在楼层
 * 
 * 	AF(c) = (floor_now,way_now) .where floor_now = c.floor_now  way_now = c.way_now 
 * 
 * */

public class Elevator {
	private int floornow=1;
	private int waynow = 0;
	
	/*不变式
	 * 1	1<=floor_now<=10
	 * 2	-1<=way_now<=1
	 * */
	public boolean repOK(){
		if(floornow<1 || floornow>10){
			return false;
		}
		if(waynow<-1 || waynow>1){
			return false;
		}
		return true;
	}
	
	public void setfloor(int change){
		/* @ REQUIRES: repOK() && 
		 * 				(change == 1 || change==-1)
		 * 			&& 1<=floor_now+change<=10
		 * @ MODIFIES: floor now
		 * @ EFFECTS:floor now+=change
		 */
		this.floornow+=change;
	}
	public int getfloor(){
		/* @ REQUIRES: repOK()
		 * @ MODIFIES: none
		 * @ EFFECTS: \result == floor now
		 */
		return this.floornow;
	}
	
	public void setway(int way){
		/* @ REQUIRES: repOK()
		 * 			&& -1<=way<=1
		 * @ MODIFIES: way now
		 * @ EFFECTS:way now == way
		 */
		this.waynow=way;
	}
	public int getway(){
		/* @ REQUIRES: repOK()
		 * @ MODIFIES: none
		 * @ EFFECTS: \result == way now
		 */
		return this.waynow;
	}
	
}
