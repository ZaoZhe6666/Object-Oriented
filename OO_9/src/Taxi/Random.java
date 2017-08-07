package Taxi;

public class Random {
	private long b_time=0;
	private long c_time=0;
	public void settime(){
		/* @ REQUIRES: none
		 * @ MODIFIES: b_time
		 * @ EFFECTS: save the current time to the begin time
		 */
		b_time = System.currentTimeMillis();
	}
	public int get_random(int n){
		/* @ REQUIRES: int n
		 * @ MODIFIES: none
		 * @ EFFECTS: get a random number in [1,n]
		 */
		return (int) (((Math.random()*n)%n)+1);
	}
	public int getsleeptime100(){
		/* @ REQUIRES: none
		 * @ MODIFIES: none
		 * @ EFFECTS: get current time - older current time,to sleep 100ms
		 */
		long time = System.currentTimeMillis();
		long i =time;
		time-=b_time;
		b_time=i;
		return (100-(int)time%100);
	}
	public int getsleeptime200(){
		/* @ REQUIRES: none
		 * @ MODIFIES: none
		 * @ EFFECTS: get current time - older current time,to sleep 200ms
		 */
		long time = System.currentTimeMillis();
		long i=time;
		time-=c_time;
		c_time=i;
		return (200-(int)time%200);
//		return 200;
	}
	public int getsleeptime1000(){
		/* @ REQUIRES: none
		 * @ MODIFIES: none
		 * @ EFFECTS: get current time - older current time,to sleep 1s
		 */
		long time = System.currentTimeMillis();
		long i =time;
		time-=c_time;
		c_time=i;
		return (1000-(int)time%1000);
	}
}
