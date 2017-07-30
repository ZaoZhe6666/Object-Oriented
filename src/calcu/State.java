package calcu;

public class State {
	public int state_change(int last,int check){
		switch(last){
			case -2:{
				switch(check){
					case 6:return 0;
					case 2:return 1;
					case 7:return 0;
					default:return -3;
				}
			}
			case -1:{
				switch(check){
					case 6:return 0;
					case 7:return 0;
					default:return -3;
				}
			}
			case 0:{
				switch(check){
					case 2:return 1;
					default:return -3;
				}
			}
			case 1:{
				switch(check){
					case 4:return 2;
					default:return -3;
				}
			}
			case 2:{
				switch(check){
					case 6:return 4;
					case 7:return 3;
					case 1:return 4; 
					default:return -3;
				}
			}
			case 3:{
				switch(check){
					case 1:return 4;
					default:return -3;
				}
			}
			case 4:{
				switch(check){
					case 1:return 4;
					case 8:return 5;
					default:return -3;
				}
			}
			case 5:{
				switch(check){
					case 7:return -2;
					case 1:return 6;
					case 6:return 6;
					default:return -3;
				}
			}
			case 6:{
				switch(check){
					case 1:return 6;
					case 5:return 7;
					default:return -3;
				}
			}
			case 7:{
				switch(check){
					case 3:return 8;
					case 8:return 1;
					default:return -3;
				}
			}
			case 8:{
				switch(check){
					case 6:return 0;
					case 7:return 0;
					default:return -3;
				}
			}
		}
		
		return 0;
	}
}
