/**
 * 
 */
package org.sanat.app;

import java.util.List;

import org.sanat.dao.JDBCMDaoImp;
import org.sanat.model.Circle;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author DELL
 *
 */
public class JDBCdemo {
	
	public static void main(String[] args) {
		ApplicationContext ctx = new ClassPathXmlApplicationContext("spring.xml");
		JDBCMDaoImp dao = ctx.getBean("JDBCMDaoImp", JDBCMDaoImp.class);
//		Circle circle = dao.getCircle(1);
//		if (circle != null) { 
//			System.out.println(circle.getName());
//		}
		//System.out.println(dao.getCircleCount());
		//System.out.println(dao.getCircleName(1));
		//System.out.println(dao.getCircle(1).getName());
		Circle circle = new Circle(5, "sanat circle");
		dao.insertCircle(circle);
		List<Circle> listofCircle = dao.getAllCircle();
		for (Circle circle1 : listofCircle) {
			System.out.println(circle1.getId());
			System.out.println(circle1.getName());
		}
	//	dao.createTriangleTable();
		
	}
}
