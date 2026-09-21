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

    private SqlSessionFactory sqlSessionFactory;

    private volatile SqlSession sessionInstance;

    public Mybatis() {
        String resource = "mybatis-config.xml";
        // 判断资源是否存在
        if (getClass().getClassLoader().getResource(resource) != null) {
            try (InputStream inputStream = Resources.getResourceAsStream(resource)) {
                sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
            } catch (Exception e) {
                System.err.println("加载 mybatis-config.xml 失败: " + e.getMessage());
            }
        } else {
            System.out.println("若想使用Mybatis作为ORM框架需要定义mybatis-config.xml");
        }
    }

    public void close(Closeable any) {
        if (any == null) {
            return;
        }
        try {
            any.close();
        } catch (Exception e) {
            System.err.println("关闭失败: " + e.getMessage());
        }
    }

    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    public SqlSession getSession() {
        if (sqlSessionFactory == null) {
            throw new IllegalStateException("SqlSessionFactory 未初始化,请先配置 mybatis-config.xml");
        }
        return sqlSessionFactory.openSession();
    }

    public <T> T getMapper(Class<T> mapperClass) {
        SqlSession session = getOrCreateSharedSession();
        return getMapper(session, mapperClass);
    }

    public <T> T getMapper(SqlSession session, Class<T> mapperClass) {
        if (sqlSessionFactory == null) {
            throw new IllegalStateException("SqlSessionFactory 未初始化,请先配置 mybatis-config.xml");
        }
        Configuration configuration = sqlSessionFactory.getConfiguration();
        // 如果没有注册 ClassMapper,则注册它
        if (!configuration.hasMapper(mapperClass)) {
            configuration.addMapper(mapperClass);
        }
        return session.getMapper(mapperClass);
    }

    private SqlSession getOrCreateSharedSession() {
        SqlSession session = sessionInstance;
        if (session == null) {
            synchronized (this) {
                session = sessionInstance;
                if (session == null) {
                    session = sqlSessionFactory.openSession();
                    sessionInstance = session;
                }
            }
        }
        return session;
    }
}