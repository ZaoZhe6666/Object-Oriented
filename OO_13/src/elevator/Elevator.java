package elevator;

/*		Overview
 * 
 * 	电梯类，模拟电梯升降运动，仅用于记录电梯当前运动方向和电梯所在楼层
 * 
 * */

public class Elevator {
	private int floornow=1;
	private int waynow = 0;
	
	/*不变式
	 * 1	1<=floor now<=10
	 * 2	-1<=way now<=1
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
		 * @ MODIFIES: floor now
		 * @ EFFECTS: \result ==> floor now+=change
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
		 * @ EFFECTS: \result ==> way now == way
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
