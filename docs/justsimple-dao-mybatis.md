<div align=center>
<img src="https://s2.loli.net/2025/03/19/Gf9yaFAJmnVWIBH.png" style="width:100px;" width="100"/>
   <img src="https://s2.loli.net/2025/04/08/RKAvIHYPZM8b5aL.png" style="width:100px;" width="100"/>
<h2>仅见+Mybatis</h2>
</div>

### 一、插件功能

- 快速引入MyBatis工具作为ORM框架
- 快速获取Mapper（同时支持注解与xml定义）
- 自动生成Mybatis实例的形式操作，无学习成本
- 以下的操作以连接MySQL为例，其他数据库修改配置即可连接

### 二、插件引入

- 引入pom依赖

```xml
 <dependencies>

        <!-- JustSimple -->
        <dependency>
            <groupId>org.mutantcat.justsimple</groupId>
            <artifactId>justsimple-core</artifactId>
            <version>1.0.20250408</version>
        </dependency>
     
        <!-- JustSimple-MyBatis -->
        <dependency>
            <groupId>org.mutantcat.justsimple</groupId>
            <artifactId>justsimple-dao-mybatis</artifactId>
            <version>1.0.20250408</version>
        </dependency>

        <!-- MySQL Connector -->
        <dependency>
            <groupId>mysql</groupId>
            <artifactId>mysql-connector-java</artifactId>
            <version>8.0.32</version>
        </dependency>

</dependencies>
```

- 创建resources下的mybatis-config.xml（修改为自己想要配置，若不配置则引入无效）

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE configuration
        PUBLIC "-//mybatis.org//DTD Config 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-config.dtd">
<configuration>
    <environments default="development">
        <environment id="development">
            <transactionManager type="JDBC"/>
            <dataSource type="POOLED">
                <property name="driver" value="com.mysql.cj.jdbc.Driver"/>
                <property name="url" value="jdbc:mysql://localhost:3306/local_test?useSSL=false&amp;serverTimezone=UTC&amp;characterEncoding=utf8"/>
                <property name="username" value="root"/>
                <property name="password" value="root"/>
            </dataSource>
        </environment>
    </environments>
    <mappers>
        <mapper resource="mapper/UserMapper.xml"/>
    </mappers>
</configuration>
```

### 三、Mapper使用XML定义

- 创建实体类

```java
package com.tyza66.demo4.entity;

public class User {
    private int id;
    private String name;

    public User() {
    }

    public User(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
```

- 先在mybatis-config.xml添加Mapprt

```xml
<mappers>
    <mapper resource="mapper/UserMapper.xml"/>
</mappers>
```

- 之后在resources下的mapper/UserMapper.xml编辑mapper信息

```xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper
        PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN"
        "https://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="com.tyza66.demo4.mapper.UserMapper">
    <select id="getAllUsers" resultType="com.tyza66.demo4.entity.User">
        select * from `user`
    </select>
</mapper>
```

- 创建Mapper接口

```Java
package com.tyza66.demo4.mapper;

import com.tyza66.demo4.entity.User;

import java.util.List;

public interface UserMapper {

    List<User> getAllUsers();
}
```

- 在Controller层或Service层引入

```java
package com.tyza66.demo4.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.tyza66.demo4.config.MybatisSqlSessionGetter;
import com.tyza66.demo4.entity.User;
import com.tyza66.demo4.mapper.UserMapper;
import org.apache.ibatis.session.SqlSession;
import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Handler;
import org.mutantcat.justsimple.dao.mybatis.Mybatis;
import org.mutantcat.justsimple.instance.InstanceHandler;

import java.util.List;

@Controller
public class UserController {

    UserMapper userMapper;

    public UserController() {
        Mybatis mybatis = InstanceHandler.getInstance("mybatis");
        SqlSession session = mybatis.getSession();
        userMapper = mybatis.getMapper(session, UserMapper.class);
    }

    @Handler(path = "/user/all")
    public JSON getAllUsers() {
        List<User> allUsers = userMapper.getAllUsers();
        return JSONUtil.parse(allUsers);
    }

}
```

### 四、Mapper使用注解定义

- 创建实体类

```java
package com.tyza66.demo4.entity;

public class Cat {
    private String name;
    private int age;

    public Cat() {
    }

    public Cat(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }
}
```

- 创建Mapper接口

```java
package com.tyza66.demo4.mapper;

import com.tyza66.demo4.entity.Cat;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CatMapper {

    @Select("SELECT * FROM cat")
    List<Cat> getAllCats();
}
```

- 在Controller层或Service层引入

```java
package com.tyza66.demo4.controller;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.tyza66.demo4.entity.Cat;
import com.tyza66.demo4.mapper.CatMapper;
import org.apache.ibatis.session.SqlSession;
import org.mutantcat.justsimple.annotation.Controller;
import org.mutantcat.justsimple.annotation.Handler;
import org.mutantcat.justsimple.dao.mybatis.Mybatis;
import org.mutantcat.justsimple.instance.InstanceHandler;

import java.util.List;

@Controller
public class CatController {
    // 注入 CatMapper
    CatMapper catMapper;

    public CatController() {
        // 获取 CatMapper 实例
        Mybatis mybatis = InstanceHandler.getInstance("mybatis");
        SqlSession session = mybatis.getSession();
        catMapper = mybatis.getMapper(session, CatMapper.class);
    }

    @Handler(path = "/cat/all")
    public JSON getAllCats() {
        // 获取所有猫
        List<Cat> allCats = catMapper.getAllCats();
        // 返回 JSON 格式的猫列表
        return JSONUtil.parse(allCats);
    }
}
```

### 五、Mybatis内置对象

- 当插件正确配置时，程序将会自动注册一个名称为`mybatis`的实例
- 此实例与正常存入的实例无异，直接取用即可

- 使用此实例可以实现与Mybatis对接的一些列功能，方法列表如下

| 方法                                                      | 说明                                               | 示例                                        |
| --------------------------------------------------------- | -------------------------------------------------- | ------------------------------------------- |
| void close(Closeable any)                                 | 关闭实现/继承Closeable接口的类对象                 | mybatis.close(session)                      |
| SqlSessionFactory getSqlSessionFactory()                  | 获得SqlSession工厂                                 | mybatis.getSqlSessionFactory()              |
| SqlSession getSession()                                   | 获得一个新的SqlSession                             | mybatis.getSession()                        |
| <T> T getMapper(Class<T> mapperClass)                     | 获得一个指定的Mapper实现（使用公共临时SqlSession） | mybatis.getMapper(UserMapper.class)         |
| <T> T getMapper(SqlSession session, Class<T> mapperClass) | 获得一个指定的Mapper实现（使用指定SqlSession）     | mybatis.getMapper(session, CatMapper.class) |

