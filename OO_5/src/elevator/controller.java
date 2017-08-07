package elevator;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;

class controller {
	static Writer w;
	static BufferedWriter buffWriter;
	public static void main(String[] args) {
		try{
		w=new FileWriter("result.txt");
		buffWriter=new BufferedWriter(w);
		System.out.println("----------- Start Work -----------");
		double begin_time=System.currentTimeMillis();
		Request request = new Request();
		Queue queue = new Queue();
		Floor floor = new Floor();
		scheduler sche1 = new scheduler();
		Thread sche = new Thread(sche1);
		Tray tray = new Tray();
		
		
		request.settime(begin_time);
		sche1.setqueue(queue);

		Elevator E1 = new Elevator("1");
		Elevator E2 = new Elevator("2");
		Elevator E3 = new Elevator("3");
		E1.settime(begin_time);
		E2.settime(begin_time);
		E3.settime(begin_time);
		sche1.settray(tray);
		sche1.setelevator(E1, E2, E3);
		E1.start();
		E2.start();
		E3.start();
		sche.start();
		int i=0;
		while(true){		
			if(request.read()==1){
				break;
			}
			for(;i<request.getlistlong();i++){
				queue.saveone(request);
			}
		}
		sche1.setshut(false);
		while(E1.getshut() || E2.getshut() || E3.getshut()){
			try{
				Thread.sleep(10);
			}catch(InterruptedException e){
				
			}
			if(E1.getflag()==0 && E1.getshut()){
				E1.setshut();
			}
			if(E2.getflag()==0 && E2.getshut()){
				E2.setshut();
			}
			if(E3.getflag()==0 && E3.getshut()){
				E3.setshut();
			}
		}
		floor.floor();
		
		}catch(Exception e){
			try {
				controller.buffWriter.write("Something was wrong!");
				controller.buffWriter.flush();
			} catch (IOException p) {
				p.printStackTrace();
			}
			System.out.println("Something was wrong!");
		}
		return;
	}
}
