#include<stdio.h>

int main()
{
	FILE *in,*out;
	//r ��
	//w	д 
	in	=fopen("in.txt","r");
	out =fopen("out.txt","w");
	int i,j;
	char c;
	for(i=0;i<79;i++){
		for(j=0;j<79;j++){
			//��һ��char�ͱ��� 
			fscanf(in,"%c",&c);
			if(c=='\n'){
				for(;j<79;j++){
					//����������һ��1 
					fprintf(out,"1");	
				}
				break;
			}	
			if(c=='2'){
				//����������һ��i 
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
	//�رն�д�ӿ� 
	fclose(in);
	fclose(out); 
	
	return 0;
}
