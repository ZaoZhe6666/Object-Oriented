package elevator;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Test;

public class QueueTest {

	@Test
	public void testRepOK() {
		Queue queue = new Queue();
		
		//list为null测试
		queue.list=null;
		assertEquals(queue.repOK(),false);
		
		queue.list = new ArrayList<jiegou>();
		queue.list.add(null);
		assertEquals(queue.repOK(),false);
		
		//list初值为正常
		queue.list.clear();
		assertEquals(queue.repOK(),true);
		
		//list子项名(FR/ER)不合法
		jiegou j = new jiegou("不合法的请求",1,"UP",10);
		queue.list.add(j);
		assertEquals(queue.repOK(),false);
		
		//list子项楼层不合法
		queue.list.clear();
		j = new jiegou("FR",0,"UP",10);
		queue.list.add(j);
		assertEquals(queue.repOK(),false);
		queue.list.clear();
		j = new jiegou("FR",11,"UP",10);
		queue.list.add(j);
		assertEquals(queue.repOK(),false);
		
		//list子项方向不合法
		queue.list.clear();
		j = new jiegou("FR",1,"不合法的方向",10);
		queue.list.add(j);
		assertEquals(queue.repOK(),false);
		
		//list子项时间不合法
		queue.list.clear();
		j = new jiegou("FR",1,"UP",-5);
		queue.list.add(j);
		assertEquals(queue.repOK(),false);
		
		queue.list.clear();
		
		//合法测试
		queue.list.clear();
		j = new jiegou("FR",1,"UP",5);
		queue.list.add(j);
		j = new jiegou("ER",2,10);
		queue.list.add(j);
		assertEquals(queue.repOK(),true);
		
		//order<0
		queue.order=-1;
		assertEquals(queue.repOK(),false);
		
		//order=0
		queue.order=0;
		assertEquals(queue.repOK(),true);
		
		//number for test越界测试
		queue.nft=-1;
		assertEquals(queue.repOK(),false);
		queue.nft=11;
		assertEquals(queue.repOK(),false);
		
	}

	@Test
	public void testQueue() {
		Queue queue = new Queue();
		assertEquals(queue.repOK(),true);
	}

	@Test
	public void testSaveone() {
		Queue queue = new Queue();

		assertEquals(queue.nft,0);
		queue.saveone("(FR,1,UP,0)");
		assertEquals(queue.nft,1);
		
		queue.saveone("(ER,1,0)");
		assertEquals(queue.nft,2);
		
	}

	@Test
	public void testGetlistlong() {
		Queue queue = new Queue();
		assertEquals(queue.getlistlong(),0);
		queue.list.add(null);
		queue.list.add(null);
		assertEquals(queue.getlistlong(),2);

	}

	@Test
	public void testGetone() {
		Queue queue = new Queue();
		queue.setorder();
		
		queue.list.add(null);
		jiegou j = new jiegou("FR",1,"UP",0);
		queue.list.add(j);
		assertEquals(queue.order,0);
		assertEquals(queue.getone(),null);
		assertEquals(queue.order,1);
		assertEquals(queue.getone(),j);
		assertEquals(queue.order,2);
		
	}

	@Test
	public void testSetorder(){
		Queue queue = new Queue();
		queue.order=1;
		queue.setorder();
		assertEquals(queue.order,0);
	}
	


}
