#include<stdio.h>
#include<string.h>
#include<stdlib.h>

typedef struct num{
	int degree;
	int coffe;
	struct num *next;
}numi,*list;

void wrong_k(int n){
	if(n==1){
		printf("大括号无法匹配");
	}
	else if(n==2){
		printf("小括号无法匹配"); 
	}
	return;
}


int main()
{
	int stack=0;
	int i=0,j=0,k=0;
	int judge=0;
	int judge_num=0;
	char c;
	list p,top,t;
	
	top=(list)malloc(sizeof(numi));
	top->coffe =0;
	top->degree=0;
	top->next=NULL;
	
	c=getchar();
	while(c!='\n'){
		if(c!=' '){
			if(c<='9' && c>='0'){
				judge=1;
				i*=10;
				i+=c-'0';
				c=getchar();
				continue;
			}
			else{
				if(judge){
					judge=0;
					if(!judge_num){
						judge_num=1;
						j=i*(k?-1:1);
						i=0;
					}
					else{
						judge_num=0;		
						p=(list)malloc(sizeof(numi));
						p->coffe=j;
						p->degree=i;
						for(t=top;t->next!=NULL;t=t->next){
							if(t->next->degree<p->degree){
								continue;
							}
							else if(t->next->degree==p->degree){
								t->next->coffe+=p->coffe;
								free(p);
								break;
							}
							else{
								p->next=t->next;
								t->next=p;
								break;
							}
						}
						if(t->next==NULL){
							t->next=p;
							p->next=NULL;
						}
						i=0;
						j=0;
					}
				}
			}
			if(c=='{'){
				if(stack){
					wrong_k(stack);
					break;
				}
				stack=1;
			}
			else if(c=='('){
				if(stack==2){
					wrong_k(2);
					break;
				}
				stack=2;
			}
			else if(c=='}'){
				if(!stack){
					wrong_k(1);
					break;
				}
				else if(stack==2){
					wrong_k(2);
					break;
				}
				stack=0;
				k=0;
			}
			else if(c==')'){
				if(!stack){
					wrong_k(2);
					break;
				}
				else if(stack==1){
					wrong_k(1);
					break;
				}
				stack=1;
			}
			else if(c=='-'){
				k=1-k;
			}
			else if(c=='+'){
				
			}
			else if(c==','){
				
			}
			else{
				printf("错误字符");
				break;
			}
		}
		c=getchar();
	}
	
	if(judge || judge_num || stack){
		printf("格式错误");
		return 0;
	}
	judge=0;
	printf("结果为:");
	for(t=top->next;t!=NULL;t=t->next){
		if(t->coffe!=0){
			if(!judge){
				judge=1;
				printf("{(%d,%d)",t->coffe,t->degree);
			}
			else{
				printf(",(%d,%d)",t->coffe,t->degree);
			}
		}
	}
	if(!judge){
		printf("0");
	}
	else{
		printf("}");
	}
	
	return 0;
}
