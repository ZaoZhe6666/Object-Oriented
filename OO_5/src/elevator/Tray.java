package elevator;

public class Tray {
	private jiegou j1,j2,j3;
	private boolean e1=false;
	private boolean e2=false;
	private boolean e3=false;
	public synchronized jiegou get1(){
		while(e1==false){
			try{
				wait();
			}catch(InterruptedException e){
				
			}
		}
		e1=false;
		notifyAll();
		return j1;
	}
	public synchronized jiegou get2(){
		while(e2==false){
			try{
				wait();
			}catch(InterruptedException e){
				
			}
		}
		e2=false;
		notifyAll();
		return j2;
	}
	public synchronized jiegou get3(){
		while(e3==false){
			try{
				wait();
			}catch(InterruptedException e){
				
			}
		}
		e3=false;
		notifyAll();
		return j3;
	}
	public boolean gete1(){
		return e1;
	}
	public boolean gete2(){
		return e2;
	}
	public boolean gete3(){
		return e3;
	}
	public synchronized void put(jiegou j){
		while(e1==true || e2==true || e3==true){
			try{
				wait();
			}catch(InterruptedException e){
				
			}
		}
		e1=true;
		e2=true;
		e3=true;
		j1=j;
		j2=j;
		j3=j;
		notifyAll();
	}
	public synchronized void put1(jiegou j){
		while(e1==true){
			try{
				wait();
			}catch(InterruptedException e){
				
			}
		}
		e1=true;
		j1=j;
		notifyAll();
	}
	public synchronized void put2(jiegou j){
		while(e2==true){
			try{
				wait();
			}catch(InterruptedException e){
				
			}
		}
		e2=true;
		j2=j;
		notifyAll();
	}
	public synchronized void put3(jiegou j){
		while(e3==true){
			try{
				wait();
			}catch(InterruptedException e){
				
			}
		}
		e3=true;
		j3=j;
		notifyAll();
	}
}
