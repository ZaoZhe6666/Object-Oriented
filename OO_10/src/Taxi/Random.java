package Taxi;

/*
 * 		Overview
 * 
 * 		随机类，专门用来统一生成随机数和睡眠时间的一个类
 * 
 * */
public class Random {
	private long b_time=0;
	private long c_time=0;
	/*不变式：
	 * 1	没什么需要限定的
	 * */
	public boolean repOK(){
		/*@Effects: \result==invariant(this).*/
		try{
			return true;
		}
		catch(Exception e){
			return false;
		}
	}
	public void settime(){
		/* @ REQUIRES: none
		 * @ MODIFIES: b_time
		 * @ EFFECTS: save the current time to the begin time
		 */
		b_time = System.currentTimeMillis();
	}
	public int get_random(int n){
		/* @ REQUIRES: 1<n
		 * @ MODIFIES: none
		 * @ EFFECTS: return a random number in [1,n]
		 */
		return (int) (((Math.random()*n)%n)+1);
	}
	public int getsleeptime100(){
		/* @ REQUIRES: none
		 * @ MODIFIES: b_time
		 * @ EFFECTS: return current time - older current time,to sleep 100ms
		 * 				save the current time to the begin time
		 */
		long time = System.currentTimeMillis();
		long i =time;
		time-=b_time;
		b_time=i;
		return (100-(int)time%100);
	}
	public int getsleeptime10(){
		/* @ REQUIRES: none
		 * @ MODIFIES: c_time
		 * @ EFFECTS: return current time - older current time,to sleep 10ms
		 * 				save the current time to the c_begin time
		 */
		long time = System.currentTimeMillis();
		long i=time;
		time-=c_time;
		c_time=i;
		return (10-(int)time%10);
	}
	public int getsleeptime1000(){
		/* @ REQUIRES: none
		 * @ MODIFIES: c_time
		 * @ EFFECTS: return current time - older current time,to sleep 1s
		 * 				save the current time to the c_begin time
		 */
		long time = System.currentTimeMillis();
		long i =time;
		time-=c_time;
		c_time=i;
		return (1000-(int)time%1000);
	}
}
