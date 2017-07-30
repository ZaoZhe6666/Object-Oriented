package calcu;

public class word_check {
	public int w_check(char x){
		if(x<='9' && x>='0'){
			return 1;
		}
		else if(x=='{'){
			return 2;
		}
		else if(x=='}'){
			return 3;
		}
		else if(x=='('){
			return 4;
		}
		else if(x==')'){
			return 5;
		}
		else if(x=='+'){
			return 6;
		}
		else if(x=='-'){
			return 7;
		}
		else if(x==','){
			return 8;
		}
		else if(x==' '){
			return 0;
		}
		return 9;
	}
}
