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
package ch.qos.logback.justsimple.integration;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusUtil;
import ch.qos.logback.core.util.StatusPrinter;
import ch.qos.logback.justsimple.JustSimpleConfigurator;
import org.fusesource.jansi.AnsiConsole;
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.Utils;
import org.mutantcat.justsimple.core.runtime.NativeDetector;
import org.mutantcat.justsimple.core.util.*;
import org.mutantcat.justsimple.logging.LogIncubator;
import org.mutantcat.justsimple.logging.LogOptions;
import org.mutantcat.justsimple.logging.model.LoggerLevelEntity;
import org.slf4j.LoggerFactory;

import java.net.URL;
import java.util.List;

/**
 * 日志孵化器
 *
 * @author noear
 * @since 2.4
 */
public class LogIncubatorImpl implements LogIncubator {
    @Override
    public void incubate() throws Throwable {
        if (JavaUtil.IS_WINDOWS && JustSimple.cfg().isFilesMode() == false) {
            //只在 window 用 jar 模式下才启用
            if (ClassUtil.hasClass(() -> AnsiConsole.class)) {
                try {
                    AnsiConsole.systemInstall();
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }

        //加载pid
        Utils.pid();

        //尝试从配置里获取
        URL url = getUrlOfConfig();

        //加载配置文件
        doLoadUrl(url);
        doInit();
    }

    protected void doLoadUrl(URL url) throws Exception {
        //尝试包内定制加载
        if (url == null) {
            //检查是否有原生配置文件
            if (ResourceUtil.hasResource("logback.xml")) {
                //如果有直接返回（不支持对它进行 JustSimple 扩展）
                return;
            }

            //1::尝试应用环境加载
            if (url == null) {
                if (Utils.isNotEmpty(JustSimple.cfg().env())) {
                    url = ResourceUtil.getResource("logback-justsimple-" + JustSimple.cfg().env() + ".xml");
                }
            }

            //2::尝试应用加载
            if (url == null) {
                url = ResourceUtil.getResource("logback-justsimple.xml");
            }
        }

        /// ///////////

        LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

        loggerContext.reset();

        JustSimpleConfigurator configurator = new JustSimpleConfigurator();
        configurator.setContext(loggerContext);

        if (url == null) {
            //::尝试默认加载
            DefaultLogbackConfiguration configuration = new DefaultLogbackConfiguration();
            configuration.apply(new LogbackConfigurator(loggerContext));
        } else {
            //::加载 xml url
            configurator.doConfigure(url);
        }
    }

    protected void doInit() {
        try {
            LoggerContext loggerContext = (LoggerContext) LoggerFactory.getILoggerFactory();

            //同步 logger level 配置
            if (LogOptions.getLoggerLevels().size() > 0) {
                for (LoggerLevelEntity lle : LogOptions.getLoggerLevels()) {
                    Logger logger = loggerContext.getLogger(lle.getLoggerExpr());
                    logger.setLevel(Level.valueOf(lle.getLevel().name()));
                }
            }

            if (NativeDetector.inNativeImage()) {
                reportConfigurationErrorsIfNecessary(loggerContext);
            }
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }


    /**
     * 报告配置错误（原生运行时）
     */
    private void reportConfigurationErrorsIfNecessary(LoggerContext loggerContext) {
        List<Status> statuses = loggerContext.getStatusManager().getCopyOfStatusList();

        StringBuilder errors = new StringBuilder();
        for (Status status : statuses) {
            if (status.getLevel() == Status.ERROR) {
                errors.append((errors.length() > 0) ? String.format("%n") : "");
                errors.append(status);
            }
        }

        if (errors.length() > 0) {
            throw new IllegalStateException(String.format("Logback configuration error detected: %n%s", errors));
        }

        if (!StatusUtil.contextHasStatusListener(loggerContext)) {
            StatusPrinter.printInCaseOfErrorsOrWarnings(loggerContext);
        }
    }

    /**
     * 基于配置，获取日志配置文件
     */
    private URL getUrlOfConfig() {
        String logConfig = JustSimple.cfg().get("justsimple.logging.config");

        if (Utils.isNotEmpty(logConfig)) {
            URL logConfigUrl = ResourceUtil.findResource(logConfig);
            if (logConfigUrl != null) {
                return logConfigUrl;
            } else {
                System.err.println("Props: No logging config file exists: " + logConfig);
            }
        }

        return null;
    }
}
