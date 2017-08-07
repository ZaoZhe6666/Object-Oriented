package elevator;

import static org.junit.Assert.*;

import org.junit.Test;

public class ElevatorTest {

	@Test
	public void testRepOK() {
		Elevator ele = new Elevator();
		
		//初值正常
        assertEquals(ele.repOK(),true);
        
        //改变为不正确
        //1	0层
        ele.setfloor(-1);
        assertEquals(ele.repOK(),false);
        
        //2	11层	
        ele.setfloor(12);
        assertEquals(ele.repOK(),false);

        //3	方向错误
        ele.setfloor(-2);
        ele.setway(-2);
        assertEquals(ele.repOK(),false);
        ele.setway(2);
        assertEquals(ele.repOK(),false);
        
        //回归正确
        ele.setway(0);
        assertEquals(ele.repOK(),true);
	}

	@Test
	public void testSetfloor() {
		Elevator ele = new Elevator();
		//从1层往上运动到2层
		ele.setfloor(1);
		assertEquals(ele.getfloor(),2);
		
		//从2层往下运动到1层
		ele.setfloor(-1);
		assertEquals(ele.getfloor(),1);
	}

	@Test
	public void testGetfloor() {
		Elevator ele = new Elevator();
		//从1层往上运动到2层
		ele.setfloor(1);
		assertEquals(ele.getfloor(),2);
		
		//从2层往下运动到1层
		ele.setfloor(-1);
		assertEquals(ele.getfloor(),1);
	}

	@Test
	public void testSetway() {
		Elevator ele = new Elevator();
		
		//设置方向为向上
		ele.setway(1);
		assertEquals(ele.getway(),1);
		
		//设置方向为静止
		ele.setway(0);
		assertEquals(ele.getway(),0);
		
		//设置方向为向下
		ele.setway(-1);
		assertEquals(ele.getway(),-1);
	}

	@Test
	public void testGetway() {
		Elevator ele = new Elevator();
		
		//设置方向为向上
		ele.setway(1);
		assertEquals(ele.getway(),1);
		
		//设置方向为静止
		ele.setway(0);
		assertEquals(ele.getway(),0);
		
		//设置方向为向下
		ele.setway(-1);
		assertEquals(ele.getway(),-1);
	}

}
