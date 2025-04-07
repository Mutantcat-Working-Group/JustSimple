package org.mutantcat.justsimple.dao.mybatis;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.mutantcat.justsimple.annotation.Instance;

import java.io.Closeable;
import java.io.InputStream;

@Instance(name = "mybatis")
public class Mybatis {

    private static SqlSessionFactory sqlSessionFactory;

    private static SqlSession sessionInstance;

    public Mybatis() {
        try {
            String resource = "mybatis-config.xml";
            // 判断资源是否存在
            if (getClass().getClassLoader().getResource(resource) != null) {
                InputStream inputStream = Resources.getResourceAsStream(resource);
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } else {
                System.out.println("若想使用Mybatis作为ORM框架需要定义mybatis-config.xml");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close(Closeable any) {
        try {
            if (any != null) {
                any.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public SqlSession getSession() {
        return sqlSessionFactory.openSession();
    }

    public <T> T getMapper(Class<T> mapperClass) {
        try {
            if (sessionInstance == null) {
                sessionInstance = sqlSessionFactory.openSession();
            }
            Configuration configuration = sqlSessionFactory.getConfiguration();
            // 如果没有注册 ClassMapper，则注册它
            if (!configuration.hasMapper(mapperClass)) {
                configuration.addMapper(mapperClass);
            }
            return sessionInstance.getMapper(mapperClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public <T> T getMapper(SqlSession session, Class<T> mapperClass) {
        Configuration configuration = sqlSessionFactory.getConfiguration();
        // 如果没有注册 ClassMapper，则注册它
        if (!configuration.hasMapper(mapperClass)) {
            configuration.addMapper(mapperClass);
        }
        return session.getMapper(mapperClass);
    }
}
