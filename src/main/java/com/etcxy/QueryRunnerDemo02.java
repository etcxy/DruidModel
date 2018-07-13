package com.etcxy;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
 
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayHandler;
import org.apache.commons.dbutils.handlers.ArrayListHandler;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.ColumnListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;
import org.apache.commons.dbutils.handlers.ScalarHandler;
 

/**
 * QueryRunner数据查询操作
 * 	调用QueryRunner类方法query(Connection con,String sql,ResultSetHandler r,Object..params)
 * 		ResultSetHandler r 结果集的处理方式，传递ResultSetHandler接口实现类
 * 		Object...params sql语句的?占位符
 *  
 * 注意：query方法返回值，返回的是T 泛型，具体返回值类型，跟随结果集处理方式变化
 * */
public class QueryRunnerDemo02 {
	private static Connection con = DruidUtils.getConnection();
	public static void main(String[] args) throws SQLException {
		//ArrayHandler();
		//arrayListHandler();
		//beanHandler();
		//beanListHandler();
		//columnListHandler();
		//scalarHandler();
		//mapHandler();
		mapListHandler();
	}
	
	/**	
	 * 结果集第一种处理方法，ArrayHandler
	 * 将结果第一行存储到对象数组中Object[]
	 * @throws SQLException 
	 * */
	public static void ArrayHandler() throws SQLException {
		QueryRunner qr = new QueryRunner();
		String sql = "SELECT * FROM Bean";
		//调用query执行查询，传递连接对象，SQL语句，结果集处理方式的实现类 
		//返回对象数组
		Object[] result = qr.query(con, sql,new ArrayHandler());
		for (Object obj : result) {
			System.out.println(obj);
		}
	}
	
	/**
	 * 结果集的第二种处理方法，ArrayListHandler
	 * 将结果集的每一行，封装到对象数组中，出现很多对象数组
	 * 对象数组存储到List集合
	 * @throws SQLException 
	 * */
	public static void arrayListHandler() throws SQLException{
		QueryRunner qr = new QueryRunner();
		String sql  = "SELECT * FROM Bean";
		//调用query方法，结果集处理的参数上，传递实现类ArrayListHandler
		//方法返回值 每行是一个数组
		List<Object[]> result = qr.query(con, sql, new ArrayListHandler());
		//集合的遍历
		for (Object[] objs : result) {
			for (Object obj : objs) {
				System.out.print(obj+"\t");
			}
			System.out.println();
		}
	}
	/**
	 * 结果集的第三种处理方法BeanHandler
	 * 将结果集的第一行数据，封装为JavaBean对象
	 * 注意：被封装成数据到JavaBean对象，Bean类必须有空参构造
	 * @throws SQLException 
	 * */
	public static void beanHandler() throws SQLException{
		QueryRunner qr = new QueryRunner();
		String sql = "SELECT * FROM Bean";
		//调用方法，传递结果集实现BeanHandler
		//BeanHandler(Class<T> type)
        Bean s = qr.query(con, sql,new BeanHandler<Bean>(Bean.class));
		System.out.println(s);
	} 
	
	/**
	 * 结果集第四种处理方法，BeanListHandler
	 * 将数据结果集的每一行数据，封装为JavaBean对象
	 * 多个JavaBean对象封装到List集合中
	 * @throws SQLException 
	 * */
	public static void beanListHandler() throws SQLException{
		QueryRunner qr = new QueryRunner();
		String sql = "SELECT * FROM Bean";
		//调用方法传递结果集的实现类BeanListHandler
		//BeanListHandler(Class<T> type)
		List<Bean> list = qr.query(con, sql, new BeanListHandler<Bean>(Bean.class));
		for (Bean s : list) {
			System.out.println(s);
		}
	}
	
	/**
	 * 结果集第五种处理方法，ColumnListHandler
	 * 结果集，指定列的数据，存储到List集合中
	 * List<Object> 每个列数据类型不同
	 * @throws SQLException 
	 * */
	public static void columnListHandler() throws SQLException{
		QueryRunner qr = new QueryRunner();
		String sql = "SELECT * FROM Bean";
		//调用方法query，传递结果集实现类ColumnListHandler
		//实现类构造方法，使用字符串的列名
		List<Object> list = qr.query(con, sql, new ColumnListHandler<Object>("sname"));
		for (Object obj : list) {
			System.out.println(obj);
		}
	}
	/**
	 * 结果集第六种处理方法、
	 * 对于查询后，只有一个结果
	 * @throws SQLException 
	 * */
	public static void scalarHandler() throws SQLException{
		QueryRunner qr = new QueryRunner();
		String sql = "SELECT COUNT(*) FROM Bean";
		//调用方法query,传递结果集处理类ScalarHandler
		long count = qr.query(con,sql,new ScalarHandler<Long>());
		System.out.println(count);
	}
	
	/**
	 * 结果集的第七种处理方式，MapHandler
	 * 将结果集第一行数据，封装到Map集合中
	 * Map<键，值> 键：列名 值：这列数据
	 * @throws SQLException 
	 * */
	public static void mapHandler() throws SQLException{
		QueryRunner qr = new QueryRunner();
		String sql = "SELECT * FROM Bean";
		//调用方法query，传递结果集实现类MapHandler
		//返回值：Map集合，Map接口实现类 泛型
		Map<String,Object> map = qr.query(con, sql,new MapHandler());
		for (String key : map.keySet()) {
			System.out.println(key+"..."+map.get(key));
		}
	}
	/**
	 * 结果集第八种处理方法，MapListHandler
	 * 将结果集每一行存储到Map集合，键：列名 值：数据
	 * Map集合过多，存储到List集合
	 * @throws SQLException 
	 * */
	public static void mapListHandler() throws SQLException{
		QueryRunner qr = new QueryRunner();
		String sql = "SELECT * FROM Bean";
		//调用方法query，传递结果集实现类MapListHander
		//返回值List集合，存储的是Map集合
		List<Map<String, Object>> list = qr.query(con, sql,new MapListHandler());
		for (Map<String, Object> map : list) {
			for (String key : map.keySet()) {
				System.out.print(key+"..."+map.get(key));
			}
			System.out.println();
		}
	}
}