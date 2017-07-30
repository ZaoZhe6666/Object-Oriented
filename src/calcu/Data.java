package calcu;

public class Data {
	int [][] coffee=new int[20][50];
	int [][] degree=new int[20][50];
	int [] c_long=new int[20];
	int [] finalcoffee=new int[1000];
	int [] finaldegree=new int[1000];
	int i=0;
	int j=0;
	int k=0;
	public int save(int x,int y){
		try{
			coffee[i][j]=x;
			degree[i][j]=y;
			j++;
		}catch(Exception e){
			return 1;
		}
		return 0;
	}
	public int addi(){
		c_long[i]=j;
		i++;
		j=0;
		if(i>20){
			return 1;
		}
		return 0;
	}
	public void add_coffee(){
		for(int t1=0;t1<i;t1++){
			for(int t2=0;t2<c_long[t1];t2++){
				int t3;
				for(t3=0;t3<k;t3++){
					if(finaldegree[t3]==degree[t1][t2]){
						finalcoffee[t3]+=coffee[t1][t2];
						break;
					}
				}
				if(t3==k){
					finaldegree[k]=degree[t1][t2];
					finalcoffee[k]=coffee[t1][t2];
					k++;
				}
			}
		}
		return;
	}
	public void sort(){
		for(int t1=0;t1<k;t1++){
			for(int t2=t1;t2<k;t2++){
				if(finaldegree[t2]<finaldegree[t1]){
					int t3;
					t3=finaldegree[t2];
					finaldegree[t2]=finaldegree[t1];
					finaldegree[t1]=t3;
					
					t3=finalcoffee[t2];
					finalcoffee[t2]=finalcoffee[t1];
					finalcoffee[t1]=t3;
				}
			}
		}
		return;
	}
	
	public void print(){
		int judge=0;
		for(int t1=0;t1<k;t1++){
			if(finalcoffee[t1]==0){
				continue;
			}
			else{
				if(judge==0){
					System.out.print("最终结果为：{("+finalcoffee[t1]+","+finaldegree[t1]+")");
					judge=1;
				}
				else{
					System.out.print(",("+finalcoffee[t1]+","+finaldegree[t1]+")");
				}
			}
		}
		if(judge==0){
			System.out.println("最终结果为0");
		}
		else{
			System.out.print("}");
		}
		return;
	}
}
