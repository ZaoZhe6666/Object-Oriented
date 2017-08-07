package elevator;

class controller {

	public static void main(String[] args) {
		try{
		System.out.println("----------- Start Work -----------");
		
		Request request = new Request();
		Queue queue = new Queue();
		QueueRequest QR = new QueueRequest();
		Elevator elevator = new Elevator();
		Floor floor = new Floor();
		
		request.read();
		
		for(int i=0;i<request.getlistlong();i++){
			queue.saveone(request);
		}
		
		for(int i=0;i<queue.getlistlong();i++){
			elevator.calculate(QR.getone(queue));
		}
		
		if(elevator.getwarn()==1){
			System.out.println("No valid input at all!");
		}
		
		floor.floor();
		}catch(Exception e){
			System.out.println("Something was wrong!");
		}
		return;
	}
}
