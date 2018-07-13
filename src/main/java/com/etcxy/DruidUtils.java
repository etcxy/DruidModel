package com.etcxy;
import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;


/**
 * 使用Druid实现数据库连接
 */
public class DruidUtils {
    private static DruidDataSource dds = null;
    private static ThreadLocal<Connection> conn = new ThreadLocal<Connection>();

    static {
        try {
            //读取配置文件
            Properties properties = new Properties();
            InputStream is = DruidUtils.class.getClassLoader()
                    .getResourceAsStream("druid.properties");
            properties.load(is);

            dds = (DruidDataSource) DruidDataSourceFactory.createDataSource(properties);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static DruidDataSource getDataSource() {
        return dds;
    }

    //通过DataSource得到Connection
    public static Connection getConnection() {
        //得到ThreadLocal中的connection
        Connection con = conn.get();
        //如果开启了事务，则con不为空，应该直接返回con
        if (con != null) {
            return con;
        }
        try {
            return dds.getConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void beginTransaction() throws SQLException {
        //得到ThreadLocal中的connection
        Connection con = conn.get();
        //判断con是否为空，如果不为空，则说明事务已经开启
        if (con != null) {
            throw new SQLException("事务已经开启了,不能重复开启事务");
        }
        //如果不为空，则开启事务
        con = getConnection();
        //设置事务提交为手动
        con.setAutoCommit(false);
        //把当前开启的事务放入ThreadLocal中
        conn.set(con);
    }

    // 提交事务
    public static void commitTransaction() throws SQLException {
        //得到ThreadLocal中的connection
        Connection con = conn.get();
        //判断con是否为空，如果为空，则说明没有开启事务
        if (con == null) {
            throw new SQLException("没有开启事务,不能提交事务");
        }
        //如果con不为空,提交事务
        con.commit();
        //事务提交后，关闭连接
        con.close();
        //将连接移出ThreadLocal
        conn.remove();
    }

    // 回滚事务
    public static void rollbackTransaction() throws SQLException {
        //得到ThreadLocal中的connection
        Connection con = conn.get();
        //判断con是否为空，如果为空，则说明没有开启事务，也就不能回滚事务
        if (con == null) {
            throw new SQLException("没有开启事务,不能回滚事务");
        }
        //事务回滚
        con.rollback();
        //事务回滚后，关闭连接
        con.close();
        //将连接移出ThreadLocal
        conn.remove();
    }

    // 关闭事务
    public static void releaseConnection(Connection connection) throws SQLException {
        //得到ThreadLocal中的connection
        Connection con = conn.get();
        //如果参数连接与当前事务连接不相等，则说明参数连接不是事务连接，可以关闭，否则交由事务关闭
        if (connection != null && con != connection) {
            //如果连接没有被关闭，关闭之
            if (!connection.isClosed()) {
                connection.close();
            }
        }
    }

    public static void releaseAll(Connection conn, Statement ps, ResultSet rs) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            conn = null;
        }

        if(ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            ps = null;
        }

        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            rs = null;
        }
    }
}

