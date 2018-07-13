package com.etcxy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ArrayListHandler;

public class QueryRunnerDemo {

    private static Connection connection;
    private static QueryRunner qr = new QueryRunner();

    public static void main(String[] args) {
        connection = DruidUtils.getConnection();
        select(connection);
        System.out.println(connection);
    }

    public static void insert()throws SQLException{
        //创建QueryRunner类对象
        QueryRunner qr = new QueryRunner();
        String sql = "INSERT INTO sort (sname,sprice,sdesc)VALUES(?,?,?)";
        //将三个?占位符的实际参数,写在数组中
        Object[] params = {"体育用品",289.32,"购买体育用品"};
        //调用QueryRunner类的方法update执行SQL语句
        int row = qr.update(connection, sql, params);
        System.out.println(row);
    }

    //数据表查询
    public static void select(Connection connection) {
        String sql = "SELECT * FROM list";
        try {
            List<Object[]> list = qr.query(connection, sql, new ArrayListHandler());
//			List studentList = qr.query(connection, sql, new BeanListHandler<>(student.class));
            for (Object[] objs : list) {
                for (Object obj : objs) {
                    System.out.print(obj + "\t");
                }
                System.out.println();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("数据查询失败");
        }
    }

    // 对事务的测试之有会回滚
    public void test3() {
        try {
            //开启事务
            DruidUtils.beginTransaction();
            //得到QueryRunner
            QueryRunner qr = new QueryRunner();
            String sql1 = "update user set salary = salary + ? where id = 1;";
            String sql2 = "update user set salary = salary - ? where id = 2;";
            //执行第一条sql
            qr.update(sql1, 100);
            //人为制造异常
            int b = 1 / 0;
            qr.update(sql2, 100);
            //提交事务
            DruidUtils.commitTransaction();
        } catch (Exception e) {
            try {
                DruidUtils.rollbackTransaction();
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }
    }

    /*
     *  定义方法,使用QueryRunner类的方法delete将数据表的数据删除
     */
    public static void delete() throws SQLException {
        //创建QueryRunner类对象
        QueryRunner qr = new QueryRunner();
        //写删除的SQL语句
        String sql = "DELETE FROM sort WHERE sid=?";
        //调用QueryRunner方法update
        int row = qr.update(connection, sql, 8);
        System.out.println(row);
        /*
         *  判断insert,update,delete执行是否成功
         *  对返回值row判断
         *  if(row>0) 执行成功
         */
    }

    /*
     *  定义方法,使用QueryRunner类的方法update将数据表的数据修改
     */
    public static void update() throws SQLException {
        //创建QueryRunner类对象
        QueryRunner qr = new QueryRunner();
        //写修改数据的SQL语句
        String sql = "UPDATE sort SET sname=?,sprice=?,sdesc=? WHERE sid=?";
        //定义Object数组,存储?中的参数
        Object[] params = {"花卉", 100.88, "情人节玫瑰花", 4};
        //调用QueryRunner方法update
        int row = qr.update(connection, sql, params);
        System.out.println(row);
    }


    /*
     * 定义方法,使用QueryRunner类的方法update向数据表中,添加数据
     */
}
