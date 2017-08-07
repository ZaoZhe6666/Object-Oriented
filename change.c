#include<stdio.h>

int main()
{
	FILE *in,*out;
	//r 读
	//w	写 
	in	=fopen("in.txt","r");
	out =fopen("out.txt","w");
	int i,j;
	char c;
	for(i=0;i<79;i++){
		for(j=0;j<79;j++){
			//读一个char型变量 
			fscanf(in,"%c",&c);
			if(c=='\n'){
				for(;j<79;j++){
					//往输出里输出一个1 
					fprintf(out,"1");	
				}
				break;
			}	
			if(c=='2'){
				//往输出里输出一个i 
				fprintf(out,"2");
			}
			else if(c=='1'){
				fprintf(out,"1");
			}
			else if(c=='3'){
				fprintf(out,"3");
			}
			else{
				fprintf(out,"0");
			}
		}
		fprintf(out,"2\n");
		while(c!='\n'){
			fscanf(in,"%c",&c);
		}
	}
	for(i=0;i<79;i++){
		fprintf(out,"1");
	}
	fprintf(out,"0");
	printf("Finished!");
	//关闭读写接口 
	fclose(in);
	fclose(out); 
	
	return 0;
}
