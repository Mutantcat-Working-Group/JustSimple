/*
 * Copyright 2017-2025 noear.org and authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.mutantcat.justsimple.sessionstate.jwt;

import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.annotation.BindProps;

/**
 * Jwt 会话状态配置属性
 *
 * 注解用于生成配置元信息
 * */
@BindProps(prefix = "server.session.state.jwt")
public class JwtSessionProps {
    private static JwtSessionProps instance;

    public static JwtSessionProps getInstance() {
        if (instance == null) {
            instance = new JwtSessionProps();
        }

        return instance;
    }

    /**
     * 变量名
     */
    public final String name;
    /**
     * 密钥
     */
    public final String secret;
    /**
     * 前缀
     */
    public final String prefix;
    /**
     * 充许超时
     */
    public final boolean allowExpire;
    /**
     * 充许自动发布（即输出到header或cookie）
     */
    public final boolean allowAutoIssue;
    /**
     * 充许使用header承载
     */
    public final boolean allowUseHeader;


    private JwtSessionProps() {
        name = JustSimple.cfg().get("server.session.state.jwt.name", "TOKEN");
        secret = JustSimple.cfg().get("server.session.state.jwt.secret");
        prefix = JustSimple.cfg().get("server.session.state.jwt.prefix", "").trim();

        allowExpire = JustSimple.cfg().getBool("server.session.state.jwt.allowExpire", true);

        //兼容旧版本（以后会被弃用）
        boolean issueOld = JustSimple.cfg().getBool("server.session.state.jwt.allowIssue", true);
        boolean issueNew = JustSimple.cfg().getBool("server.session.state.jwt.allowAutoIssue", true);
        allowAutoIssue = (issueOld && issueNew);


        boolean allowUseHeaderTmp = JustSimple.cfg().getBool("server.session.state.jwt.allowUseHeader", false);
        //兼容旧版本（以后会被弃用）
        boolean requestUseHeader = JustSimple.cfg().getBool("server.session.state.jwt.requestUseHeader", false);
        //兼容旧版本（以后会被弃用）
        boolean responseUseHeader = JustSimple.cfg().getBool("server.session.state.jwt.responseUseHeader", false);

        if (requestUseHeader || responseUseHeader) {
            allowUseHeaderTmp = true;
        }

        allowUseHeader = allowUseHeaderTmp;
    }
}
