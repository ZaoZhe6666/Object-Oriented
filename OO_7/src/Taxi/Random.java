package Taxi;

public class Random {
	private long b_time=0;
	private long c_time=0;
	public void settime(){
		b_time = System.currentTimeMillis();
	}
	public int get_random(int n){
		return (int) (Math.random()*n+1);
	}
	public int getsleeptime100(){
		long time = System.currentTimeMillis();
		long i =time;
		time-=b_time;
		b_time=i;
		return (100-(int)time%100);
	}
	public int getsleeptime200(){
		long time = System.currentTimeMillis();
		long i=time;
		time-=c_time;
		c_time=i;
		return (200-(int)time%200);
//		return 200;
	}
	public int getsleeptime1000(){
		long time = System.currentTimeMillis();
		long i =time;
		time-=c_time;
		c_time=i;
		return (1000-(int)time%1000);
	}
}
