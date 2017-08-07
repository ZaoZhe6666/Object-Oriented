package elevator;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class schedulerTest {
	private Elevator ele = new Elevator();

	@Test
	public void testRepOK_old() {
		scheduler sche = new scheduler(ele);
		
		//初值正确
		assertEquals(sche.repOK_old(),true);
		
		//finish floor越界
		sche.finishfloor=0;
		assertEquals(sche.repOK_old(),false);
		
		sche.finishfloor=11;
		assertEquals(sche.repOK_old(),false);
		
		sche.finishfloor=5;
		assertEquals(sche.repOK_old(),true);
		
		//finish time<0
		sche.finishtime=-1;
		assertEquals(sche.repOK_old(),false);
		
		sche.finishtime=5;
		assertEquals(sche.repOK_old(),true);
		
		//warn越界
		sche.warn=-1;
		assertEquals(sche.repOK_old(),false);
		
		sche.warn=2;
		assertEquals(sche.repOK_old(),false);
		
		sche.warn=0;
		assertEquals(sche.repOK_old(),true);
		
		//nft越界
		sche.nft=-1;
		assertEquals(sche.repOK_old(),false);
		
		sche.nft=101;
		assertEquals(sche.repOK_old(),false);
		
		sche.nft=0;
		assertEquals(sche.repOK_old(),true);
		
		sche.nft2=-1;
		assertEquals(sche.repOK_old(),false);
		
		sche.nft2=101;
		assertEquals(sche.repOK_old(),false);
		
		sche.nft2=0;
		assertEquals(sche.repOK_old(),true);
		
		sche.nft3=-1;
		assertEquals(sche.repOK_old(),false);
		
		sche.nft3=101;
		assertEquals(sche.repOK_old(),false);
		
		sche.nft3=0;
		assertEquals(sche.repOK_old(),true);
		
		sche.nft4=-1;
		assertEquals(sche.repOK_old(),false);
		
		sche.nft4=101;
		assertEquals(sche.repOK_old(),false);
		
		sche.nft4=0;
		assertEquals(sche.repOK_old(),true);
		
		sche.nft5=-1;
		assertEquals(sche.repOK_old(),false);
		
		sche.nft5=101;
		assertEquals(sche.repOK_old(),false);
		
		sche.nft5=0;
		assertEquals(sche.repOK_old(),true);
		
		//list为null
		sche.list=null;
		assertEquals(sche.repOK_old(),false);
		assertEquals(sche.list instanceof ArrayList,false);
		
		
		sche.list=new ArrayList<>();
		
		assertEquals(sche.list instanceof ArrayList,true);
		
		//list子项为空
		sche.list.add(null);
		assertEquals(sche.repOK_old(),false);
		
		sche.list.clear();
		assertEquals(sche.repOK_old(),true);
		
	}
	
	@Test
	public void testRepOK() {
		scheduler sche = new scheduler(ele);
		
		//初值正确
		assertEquals(sche.repOK(),true);
		
		//all list为null
		sche.alllist=null;
		assertEquals(sche.repOK(),false);
		
		sche.alllist = new ArrayList<>();
		assertEquals(sche.repOK(),true);
		
		//all list子项为null
		sche.alllist.add(null);
		assertEquals(sche.repOK(),false);
		
		sche.alllist.clear();
		assertEquals(sche.repOK(),true);
		
		//main list为null
		sche.mainlist=null;
		assertEquals(sche.repOK(),false);
		
		sche.mainlist = new ArrayList<>();
		
		//main list子项为null
		sche.mainlist.add(null);
		assertEquals(sche.repOK(),false);
		
		sche.mainlist.clear();
		assertEquals(sche.repOK(),true);
		
		//ele为null
		sche.ele=null;
		assertEquals(sche.repOK(),false);
		
		sche.ele=this.ele;
		assertEquals(sche.repOK(),true);
		assertEquals(sche.ele.repOK(),true);
		
		sche.ele.setway(-2);
		assertEquals(sche.repOK(),false);
	}

	@Test
	public void testScheduler() {
		scheduler sche1 = new scheduler(null);
		assertEquals(sche1.ele,null);
		
		scheduler sche2 = new scheduler(this.ele);
		assertEquals(sche2.ele,this.ele);
	}

	@Test
	public void testPrintf() {
		scheduler sche = new scheduler(this.ele);

		assertEquals(sche.nft,0);
		
		//FR指令分支
		jiegou j = new jiegou("FR",1,"UP",0);
		sche.printf(j, "scheduler test-printf-1");
		assertEquals(sche.nft,2);
		
		//ER指令分支
		j = new jiegou("ER",1,5);
		sche.printf(j, "scheduler test-printf-2");
		assertEquals(sche.nft,1);
	}

	@Test
	public void testCalculate() {
		scheduler sche = new scheduler(this.ele);
		assertEquals(sche.nft,0);
		assertEquals(sche.nft2,0);
		assertEquals(sche.nft3,0);
		assertEquals(sche.nft4,0);
		
		sche.finishtime=10;
		jiegou j = new jiegou("",5,"",5);
		j.valid=false;
		
		sche.calculate(j);
		assertEquals(sche.nft,3);
		
		j.valid=true;
		sche.list.add(j);
		sche.calculate(j);
		assertEquals(sche.nft,4);
		
		j.valid=true;
		sche.nft=0;
		sche.list.clear();
		sche.ele.setfloor(0);
		sche.calculate(j);
		assertEquals(sche.nft,5);
		
		j.valid=true;
		sche.list.clear();
		sche.ele.setfloor(0);
		sche.calculate(j);
		assertEquals(sche.nft,6);
		
		j.valid=true;
		sche.list.clear();
		sche.ele.setfloor(1);
		sche.calculate(j);
		assertEquals(sche.nft,7);
		
		j.valid=true;
		sche.list.clear();
		jiegou t = new jiegou("",1,"",5);
		t.valid=false;
		sche.alllist.add(t);
		sche.calculate(j);
		assertEquals(sche.nft3,7);
		
		j.valid=true;
		sche.list.clear();
		t.valid=true;
		sche.alllist.clear();
		sche.alllist.add(t);
		sche.calculate(j);
		assertEquals(sche.nft3,8);
		
		j.valid=true;
		sche.list.clear();
		t.way="UP";
		sche.alllist.clear();
		sche.alllist.add(t);
		sche.calculate(j);
		assertEquals(sche.nft3,9);
		
		j.valid=true;
		sche.list.clear();
		t.way="DOWN";
		sche.alllist.clear();
		sche.alllist.add(t);
		sche.calculate(j);
		assertEquals(sche.nft3,10);
		
		j.valid=true;
		sche.list.clear();
		t.way="UP";
		t.floor=10;
		j.floor=10;
		sche.alllist.clear();
		sche.alllist.add(t);
		sche.ele.setway(1);
		sche.ele.setfloor(-4);
		sche.calculate(j);
		assertEquals(sche.nft3,11);
		
		
		sche.nft4=0;
		j.valid=true;
		sche.list.clear();
		sche.alllist.clear();
		sche.mainlist.clear();
		sche.ele.setway(0);
		sche.finishfloor=1;
		sche.ele.setfloor(-9);
		j.floor=2;
		sche.calculate(j);
		assertEquals(sche.nft4,11);
		
		sche.nft4=0;
		j.valid=true;
		sche.list.clear();
		sche.alllist.clear();
		sche.mainlist.clear();
		sche.finishfloor=1;
		j.floor=0;
		sche.calculate(j);
		assertEquals(sche.nft4,12);
		
		sche.nft4=0;
		j.valid=true;
		sche.list.clear();
		sche.alllist.clear();
		sche.mainlist.clear();
		sche.finishfloor=1;
		sche.finishtime=100;
		j.floor=10;
		sche.alllist.add(new jiegou("ER",11,"",0));
		sche.calculate(j);
		assertEquals(sche.nft4,13);
		
		sche.nft4=0;
		j.valid=true;
		sche.list.clear();
		sche.alllist.clear();
		sche.mainlist.clear();
		sche.finishfloor=1;
		sche.finishtime=100;
		j.floor=7;
		sche.alllist.add(new jiegou("ER",7,"",0));
		sche.list.add(new jiegou("ER",7,"",0));
		sche.calculate(j);
		assertEquals(sche.nft4,15);
		
		sche.nft4=0;
		j.valid=true;
		sche.list.clear();
		sche.alllist.clear();
		sche.mainlist.clear();
		sche.finishfloor=1;
		sche.finishtime=100;
		sche.ele.setfloor(-6);
		j.floor=8;
		sche.alllist.add(new jiegou("ER",7,"",0));
		sche.alllist.add(new jiegou("ER",3,"",0));
		sche.calculate(j);
		assertEquals(sche.nft4,16);
		
		sche.nft4=0;
		j.valid=true;
		sche.list.clear();
		sche.alllist.clear();
		sche.mainlist.clear();
		sche.finishfloor=1;
		sche.finishtime=100;
		j.floor=1;
		sche.alllist.add(new jiegou("ER",1,"",0));
		sche.alllist.add(new jiegou("ER",2,"",0));
		sche.calculate(j);
		assertEquals(sche.nft4,17);
		assertEquals(sche.nft3,22);
		
		sche.nft4=0;
		j.valid=true;
		sche.list.clear();
		sche.alllist.clear();
		sche.mainlist.clear();
		sche.finishfloor=1;
		sche.finishtime=0;
		j.floor=10;
		sche.alllist.add(new jiegou("ER",11,"",20));
		sche.calculate(j);
		assertEquals(sche.nft4,19);
		
		sche.nft4=0;
		sche.nft5=0;
		j.valid=true;
		sche.list.clear();
		sche.alllist.clear();
		sche.mainlist.clear();
		sche.finishfloor=1;
		sche.finishtime=0;
		sche.ele.setfloor(-9);
		j.floor=10;
		sche.alllist.add(new jiegou("ER",8,"",20));
		sche.calculate(j);
		assertEquals(sche.nft4,20);
		assertEquals(sche.nft5,25);
		
		sche.nft5=0;
		j.valid=true;
		sche.list.clear();
		sche.alllist.clear();
		sche.mainlist.clear();
		sche.ele.setfloor(-9);
		sche.finishtime=0;
		sche.finishfloor=1;
		j.floor=10;
		sche.alllist.add(new jiegou("ER",11,"",200));
		sche.calculate(j);
		assertEquals(sche.nft5,26);
		
		sche.nft5=0;
		j.valid=true;
		sche.list.clear();
		sche.alllist.clear();
		sche.mainlist.clear();
		sche.ele.setfloor(-9);
		sche.finishtime=0;
		sche.finishfloor=1;
		j.floor=5;
		sche.alllist.add(new jiegou("ER",7,"",0));
		sche.calculate(j);
		assertEquals(sche.nft5,27);
	}

	@Test
	public void testPrint() {
		scheduler sche = new scheduler(this.ele);

		sche.nft5=0;
		
		//FR指令分支
		sche.finishfloor=5;
		jiegou j = new jiegou("FR",5,"UP",0);
		sche.print(j);
		assertEquals(sche.nft5,12);
		
		sche.nft5=0;
		j = new jiegou("FR",4,"UP",0);
		sche.print(j);
		assertEquals(sche.nft5,22);
		
		//ER指令分支
		sche.nft5=0;
		j = new jiegou("ER",5,0);
		sche.print(j);
		assertEquals(sche.nft5,11);
		
		sche.nft5=0;
		j = new jiegou("ER",4,0);
		sche.print(j);
		assertEquals(sche.nft5,21);
	}

	@Test
	public void testSavelist() {
		scheduler sche = new scheduler(this.ele);
		
		assertEquals(sche.alllist.size(),0);
		jiegou j = new jiegou("ER",1,5);
		sche.savelist(j);
		assertEquals(sche.alllist.size(),1);
		sche.savelist(null);
		assertEquals(sche.alllist.size(),2);
		
		assertEquals(sche.alllist.get(0),j);
		assertEquals(sche.alllist.get(1),null);
		
	}

	@Test
	public void testGetwarn() {
		scheduler sche = new scheduler(ele);
		sche.warn=-1;
		assertEquals(sche.getwarn(),-1);
		sche.warn=1;
		assertEquals(sche.getwarn(),1);
	}

	@Test
	public void testSub() {
		scheduler sche = new scheduler(ele);
		assertEquals(sche.sub(1,2),1);
		assertEquals(sche.sub(2,1),1);
		assertEquals(sche.sub(1,1),0);
		
	}

	@Test
	public void testListadd() {
		scheduler sche = new scheduler(this.ele);
		
		assertEquals(sche.list.size(),0);
		jiegou j = new jiegou("ER",1,5);
		sche.listadd("ER",1,"",5);
		assertEquals(sche.list.size(),1);
		
		jiegou t = sche.list.get(0);
		assertEquals(t.valid,j.valid);
		assertEquals(t.floor,j.floor);
		assertEquals(t.way,j.way);
		assertEquals(t.r,j.r);
		assertEquals(t.time,j.time,0.001);
		
	}

	@Test
	public void testSamejudge() {
		scheduler sche = new scheduler(this.ele);
		
		//初值正确
		assertEquals(sche.nft,0);
		
		jiegou j = new jiegou("",5,"",20);
		sche.finishtime=20;
		sche.samejudge(j);
		assertEquals(sche.nft,60);
		
		sche.list.add(new jiegou("FR",1,"UP",10));
		sche.samejudge(j);
		assertEquals(sche.nft,61);
		
		j.r="FR";
		sche.samejudge(j);
		assertEquals(sche.nft,62);
		
		j.floor=1;
		sche.samejudge(j);
		assertEquals(sche.nft,63);
		
		j.way="UP";
		sche.samejudge(j);
		assertEquals(sche.nft,64);
		
		j.time=5;
		j.valid=false;
		sche.samejudge(j);
		assertEquals(sche.nft,65);
		
		j.valid=true;
		sche.samejudge(j);
		assertEquals(sche.nft,66);
		
		sche.finishtime=0;
		sche.samejudge(j);
		assertEquals(sche.nft,67);
	}

	@Test
	public void testJ_s() {
		scheduler sche = new scheduler(this.ele);
		assertEquals(sche.nft2,0);
		
		jiegou j = new jiegou("ER",1,5);
		sche.j_s(j);
		assertEquals(sche.nft2,41);
		
		j = new jiegou("FR",1,"UP",0);
		sche.j_s(j);
		assertEquals(sche.nft2,42);
	}

	@Test
	public void testOac_door() {
		scheduler sche = new scheduler(this.ele);
		
		sche.finishtime=0.5;
		sche.oac_door();
		assertEquals(sche.finishtime,1.5,0.001);
		
		sche.finishtime=0;
		sche.oac_door();
		assertEquals(sche.finishtime,1,0.001);
	}
	
	@Test
	public void testBtwjudge() {
		scheduler sche = new scheduler(this.ele);
		
		//设置电梯在5层，检测初值正确
		sche.ele.setfloor(4);
		assertEquals(sche.nft2,0);
		
		//静止状态
		sche.ele.setway(0);
		sche.btwjudge(1);
		assertEquals(sche.nft2,20);
		
		//上行
		sche.ele.setway(1);
		sche.btwjudge(1);
		assertEquals(sche.nft2,21);
		
		sche.btwjudge(10);
		assertEquals(sche.nft2,22);
		
		//下行
		sche.ele.setway(-1);
		sche.btwjudge(10);
		assertEquals(sche.nft2,23);
		
		sche.btwjudge(1);
		assertEquals(sche.nft2,24);
		
	}
	
	@Test
	public void testBtw() {
		scheduler sche = new scheduler(this.ele);
		
		//设置为5层上行
		sche.ele.setway(1);
		sche.ele.setfloor(4);
		
		//不可捎带指令
		assertEquals(sche.nft,0);
		sche.btw(3,"FR",4, -1);
		assertEquals(sche.nft,50);


		//可捎带的ER指令
		//电梯上行
		sche.ele.setway(1);
		sche.btw(9,"ER",3, 1);
		assertEquals(sche.nft,52);
		
		sche.btw(9,"ER",10, 1);
		assertEquals(sche.nft,53);
		
		//电梯下行
		sche.ele.setway(-1);
		sche.btw(3,"ER",6, -1);
		assertEquals(sche.nft,54);
		
		sche.btw(3,"ER",2, -1);
		assertEquals(sche.nft,55);
		
		//可捎带的FR指令
		//方向不可捎带
		sche.ele.setway(1);
		sche.btw(6, "FR", 5, -1);
		assertEquals(sche.nft,56);
		
		//电梯上行
		sche.btw(9,"FR",10, 1);
		assertEquals(sche.nft,58);
		
		sche.btw(9,"FR",4, 1);
		assertEquals(sche.nft,59);
		
		sche.btw(9,"FR",6, 1);
		assertEquals(sche.nft,60);
		
		//电梯下行
		sche.ele.setway(-1);
		sche.btw(3,"FR",2, -1);
		assertEquals(sche.nft,61);
		
		sche.btw(3,"FR",10, -1);
		assertEquals(sche.nft,62);
		
		sche.btw(3,"FR",4, -1);
		assertEquals(sche.nft,63);
		
	}

}
