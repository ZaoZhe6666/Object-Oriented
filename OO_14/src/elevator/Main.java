package elevator;
/*
 * 		Overview
 * 
 * 	Main类， 控制输出欢迎语和结束语
 * 	实例化各类，总体控制外部读入、指令处理、结果输出
 * 
 * */
class Main {
	public static void main(String[] args) {
		try{
			System.out.println("----------- Start Work -----------");
			
			Request request = new Request();
			Queue queue = new Queue();
			Floor floor = new Floor();
			Elevator ele = new Elevator();
			scheduler sche = new scheduler(ele);
			
			request.read();
			
			for(int i=0;i<request.getlistlong();i++){
				queue.saveone(request.getList());
			}
			
			for(int i=0;i<queue.getlistlong();i++){
				sche.savelist(queue.getone());
			}
			queue.setorder();
			for(int i=0;i<queue.getlistlong();i++){
				sche.calculate(queue.getone());
			}
			
			if(sche.getwarn()==1){
				System.out.println("No valid input at all!");
			}
			floor.floor();
		}catch(Exception e){
			System.out.println("Something was wrong!");
		}
		return;
	}
}
