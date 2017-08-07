package elevator;

import static org.junit.Assert.*;

import org.junit.Test;

public class ElevatorTest {

	@Test
	public void testRepOK() {
		Elevator ele = new Elevator();
		
		//��ֵ����
        assertEquals(ele.repOK(),true);
        
        //�ı�Ϊ����ȷ
        //1	0��
        ele.setfloor(-1);
        assertEquals(ele.repOK(),false);
        
        //2	11��	
        ele.setfloor(12);
        assertEquals(ele.repOK(),false);

        //3	�������
        ele.setfloor(-2);
        ele.setway(-2);
        assertEquals(ele.repOK(),false);
        ele.setway(2);
        assertEquals(ele.repOK(),false);
        
        //�ع���ȷ
        ele.setway(0);
        assertEquals(ele.repOK(),true);
	}

	@Test
	public void testSetfloor() {
		Elevator ele = new Elevator();
		//��1�������˶���2��
		ele.setfloor(1);
		assertEquals(ele.getfloor(),2);
		
		//��2�������˶���1��
		ele.setfloor(-1);
		assertEquals(ele.getfloor(),1);
	}

	@Test
	public void testGetfloor() {
		Elevator ele = new Elevator();
		//��1�������˶���2��
		ele.setfloor(1);
		assertEquals(ele.getfloor(),2);
		
		//��2�������˶���1��
		ele.setfloor(-1);
		assertEquals(ele.getfloor(),1);
	}

	@Test
	public void testSetway() {
		Elevator ele = new Elevator();
		
		//���÷���Ϊ����
		ele.setway(1);
		assertEquals(ele.getway(),1);
		
		//���÷���Ϊ��ֹ
		ele.setway(0);
		assertEquals(ele.getway(),0);
		
		//���÷���Ϊ����
		ele.setway(-1);
		assertEquals(ele.getway(),-1);
	}

	@Test
	public void testGetway() {
		Elevator ele = new Elevator();
		
		//���÷���Ϊ����
		ele.setway(1);
		assertEquals(ele.getway(),1);
		
		//���÷���Ϊ��ֹ
		ele.setway(0);
		assertEquals(ele.getway(),0);
		
		//���÷���Ϊ����
		ele.setway(-1);
		assertEquals(ele.getway(),-1);
	}

}
