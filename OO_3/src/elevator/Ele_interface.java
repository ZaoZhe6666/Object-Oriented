package elevator;

public interface Ele_interface {
	public void calculate(jiegou j);
	public double getfinishtime();
	public int getwarn();
	public String j_s(jiegou j);
	public void listadd(String a,int b,String c,double d);
	public boolean samejudge(jiegou j);
	public void print(jiegou j);
	public void oac_door();
	public int sub(int a,int b);
	public void setfinishtime(double t);
	public void setfloor(int a);
}
