package com.fanglin.utils;

import com.fanglin.core.others.ValidateException;
import lombok.extern.slf4j.Slf4j;

import java.sql.*;

/**
 * jdbc连接
 * @author 彭方林
 * @date 2019/4/3 16:33
 * @version 1.0
 **/
@Slf4j
public class JdbcUtils {
    private static Connection con = null;

    static {
        String url = "jdbc:mysql://127.0.0.1:3306/test?useSSL=false&serverTimezone=UTC&characterEncoding=UTF-8";
        String username = "root";
        String password = "123456";
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(url, username, password);
        } catch (SQLException | ClassNotFoundException e) {
            log.warn(e.getMessage());
            throw new ValidateException(e.getMessage());
        }
    }

    /**
     * 获取数据库连接
     */
    public static Connection getConnection() {
        return con;
    }

    /**
     * 关闭数据库资源
     */
    public static void close(Connection connection, PreparedStatement preparedStatement, ResultSet resultSet) {
        try {
            if (resultSet != null) {
                resultSet.close();
            }
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            log.warn(e.getMessage());
            throw new ValidateException(e.getMessage());
        }
    }
}
