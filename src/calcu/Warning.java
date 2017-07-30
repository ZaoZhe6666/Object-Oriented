package calcu;

public class Warning {
	public void word(char x){
		System.out.println("输入中存在不符合输入规范的字符“"+x+"”！");
		return;
	}
	public void out_wide(){
		System.out.println("输入内容过长，已截取为前30000个字符！");
		return;
	}
	public void out_num_of_nums(){
		System.out.println("输入的多项式个数或项数超过规定值！");
		return;
	}
	public void out_num(){
		System.out.println("输入的多项式中存在某项系数或指数过大！");
		return;
	}
	public void out_fdegree(){
		System.out.println("输入的多项式中存在某项次数非正数！");
		return;
	}
	public void wmatch(char x1,char x2,char x3,char x4,char x5){
		System.out.println("输入中存在不正确匹配字符段“"+x1+x2+x3+x4+x5+"”！");
		return;
	}
	public void needmore(){
		System.out.println("多项式不完整，无法计算，请检查是否缺少末尾‘}’！");
		return;
	}
	public void avoidf(){
		System.out.println("请不要以加减符号结尾哦！");
		return;
	}
}
