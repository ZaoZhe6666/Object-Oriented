package calcu;
public class read {
	public static void main(String[] args){
		Warning warning =new Warning();
		System.out.println("请输入要计算的多项式：");
		try{
			int input;
			int check=0;
			int i=0;
			char[] word=new char[30000];
			input=System.in.read();
			while(input !='\n' && input!='\r'){
				word_check w_c=new word_check();
				check=w_c.w_check((char)input);
				if(check>0){
					if(check==9){
						warning.word((char)input);
						return;
					}
					word[i++]=(char)input;
				}
				if(i>29998){
					warning.out_wide();
					return;
				} 
				input = System.in.read();
			};
	//		System.out.println(word);
			
			int state=-2;
			int num1=0;
			int num2=0;
			int f1=1;
			int f2=1;
			State s=new State();
			
			Data data=new Data();
			
			
			for(int t=0;t<i;t++){
				word_check w_c=new word_check();
				check=w_c.w_check(word[t]);
				state=s.state_change(state,check);
				switch(state){
					case -3:{
						warning.wmatch(((t>1)?word[t-2]:' '),((t>0)?word[t-1]:' '), word[t], (t<(i-1)?word[t+1]:' '),(t<(i-2)?word[t+2]:' '));
						return;
					}
					case -2:{
						warning.out_fdegree();
						return;
					}
					case 0:{
						if(check==6){
							f1=1;
						}
						else{
							f1=-1;
						}
						break;
					}
					case 3:{
						f2=-1;
						break;
					}
					case 4:{
						num1*=10;
						num1+=word[t]-'0';
						if(num1>1000000){
							warning.out_num();
							return;
						}
						break;
					}
					case 5:{
						num1*=f1*f2;
						f2=1;
						break;
					}
					case 6:{
						num2*=10;
						num2+=word[t]-'0';
						if(num2>1000000){
							warning.out_num();
							return;
						}
						break;
					}
					case 7:{
						int p;
						p=data.save(num1, num2);
						if(p==1){
							warning.out_num_of_nums();
							return;
						}
						num1=0;
						num2=0;
						break;
					}
					case 8:{
						int p;
						p=data.addi();
						if(p==1){
							warning.out_num_of_nums();
							return;
						}
						break;
					}
				}
			}
			switch(state){
				case 8:break;
				case 0:{
					warning.avoidf();
					break;
				}
				default:{
					warning.needmore();
					return;
				}
			}
			data.add_coffee();
			data.sort();	
			data.print();
			
		}catch(Exception e){
			System.out.println("hhhhhh GG!");
		}
		
		return;
	}
}
