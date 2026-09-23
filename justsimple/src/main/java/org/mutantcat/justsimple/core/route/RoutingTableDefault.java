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
package org.mutantcat.justsimple.core.route;

import org.mutantcat.justsimple.core.handle.Action;
import org.mutantcat.justsimple.core.handle.MethodType;
import org.mutantcat.justsimple.core.handle.Result;
import org.mutantcat.justsimple.core.util.Assert;
import org.mutantcat.justsimple.core.util.RankEntity;
import org.mutantcat.justsimple.lang.Nullable;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 路由表默认实现
 *
 * @author noear
 * @since 1.0
 * @since 3.4
 * */
public class RoutingTableDefault<T> implements RoutingTable<T> {
    private final LinkedList<RankEntity<Routing<T>>> table = new LinkedList<>();
    private final Map<String, RoutingDefault<T>> routingCache = new ConcurrentHashMap<>();
    private final Map<String, Version> versionCache = new ConcurrentHashMap<>();
    // 修复：版本缓存容量上限，防止攻击者伪造海量版本串导致缓存无限增长而 OOM
    private static final int VERSION_CACHE_LIMIT = 128;


    /**
     * 获取版本对象（带缓存）
     */
    private Version versionOf(@Nullable String versionStr) {
        if (Assert.isEmpty(versionStr)) {
            return null;
        }

        // 修复：缓存超限时直接新建不缓存
        Version cached = versionCache.get(versionStr);
        if (cached != null) {
            return cached;
        }

        Version version = new Version(versionStr);
        if (versionCache.size() < VERSION_CACHE_LIMIT) {
            versionCache.putIfAbsent(versionStr, version);
        }

        return version;
    }

    /**
     * 添加路由记录
     *
     * @param path       路径
     * @param method     方式
     * @param index      序位
     * @param versionStr 版本字符串（x.y.z）
     * @param target     目标
     */
    @Override
    public void add(String path, MethodType method, int index, @Nullable String versionStr, T target) {
        String key = path + ":" + method.name;

        routingCache.computeIfAbsent(key, k -> {
            RoutingDefault<T> routing = new RoutingDefault<>(path, method, index);
            doAdd(routing);
            return routing;
        }).addVersionTarget(versionOf(versionStr), target);
    }

    private void doAdd(Routing<T> routing) {
        int precedence = precedenceOf(routing);

        RankEntity<Routing<T>> entity = new RankEntity<>(routing, precedence, routing.index(), false);

        if (precedence != 0 || routing.index() != 0) {
            //有 * 号的 或有 index 的；排序下
            table.addLast(entity);
            Collections.sort(table);
        } else {
            //纯静态路径，且无自定义序位；直接排在最前
            table.addFirst(entity);
        }
    }

    /**
     * 段规则等级（越小越精确）
     */
    private static final int SEGMENT_CONST = 0; //常量
    private static final int SEGMENT_VAR = 1; //变量 {x}
    private static final int SEGMENT_STAR = 2; //单段通配 *
    private static final int SEGMENT_GLOBSTAR = 3; //跨段通配 **

    /**
     * 参与精确排序的段数（每段 2 bit，共 28 bit，不会溢出）
     */
    private static final int SEGMENT_LIMIT = 14;

    /**
     * 大层级占位（用于保证段位键不会越过层级）
     */
    private static final int LEVEL_UNIT = 1 << (2 * SEGMENT_LIMIT);

    /**
     * 计算排序键（越小越前）
     * <p>
     * 1. 大层级：静态 &gt; 变量 &gt; 单段通配 &gt; 跨段通配 &gt; 全匹配
     * 2. 同层级：按路径段从左到右的规则等级比较（先出现的段更优先）
     */
    private static int precedenceOf(Routing<?> routing) {
        int level = 0;

        if (routing.globstar() == 0) { // "/**"
            level = 4;
        } else if (routing.globstar() > 0) {  // "/a/**"
            level = 3;
        } else if (routing.path().indexOf('*') >= 0) { // "/a/*"
            level = 2;
        } else if (routing.path().indexOf('{') >= 0) { // "/a/{x}"
            level = 1;
        }

        return level * LEVEL_UNIT + segmentKeyOf(routing.path());
    }

    /**
     * 计算路径段排序键（从左到右逐段对比，越靠前的段越精确）
     * <p>
     * 超过 {@link #SEGMENT_LIMIT} 的深段不再参与对比（由注册顺序决定）
     */
    private static int segmentKeyOf(String path) {
        String[] segments = path.split("/");

        //首段是前导空串（必然为常量），不参与计算
        int count = Math.min(segments.length - 1, SEGMENT_LIMIT);
        int key = 0;

        for (int i = 0; i < count; i++) {
            key |= segmentRankOf(segments[i + 1]) << (2 * (SEGMENT_LIMIT - 1 - i));
        }

        return key;
    }

    private static int segmentRankOf(String segment) {
        if (segment.contains("**")) {
            return SEGMENT_GLOBSTAR;
        } else if (segment.contains("*")) {
            return SEGMENT_STAR;
        } else if (segment.contains("{")) {
            return SEGMENT_VAR;
        } else {
            return SEGMENT_CONST;
        }
    }

    /**
     * 移除路由记录
     *
     * @param pathPrefix 路径前缀
     */
    @Override
    public void remove(String pathPrefix) {
        table.removeIf(l -> {
            if (l.target.path().startsWith(pathPrefix)) {
                routingCache.remove(l.target.path() + ":" + l.target.method().name());
                return true;
            } else {
                return false;
            }
        });
    }

    /**
     * 移除路由记录
     *
     * @param controllerClz 控制器类
     */
    @Override
    public void remove(Class<?> controllerClz) {
        table.removeIf(l -> {
            l.target.targets().removeIf(vt -> {
                if (vt.getTarget() instanceof Action) {
                    Action a = (Action) vt.getTarget();
                    if (a.controller().clz().equals(controllerClz)) {
                        return true;
                    }
                }

                return false;
            });

            return l.target.targets().size() == 0;
        });
    }

    @Override
    public int count() {
        return table.size();
    }

    @Override
    public Collection<Routing<T>> getAll() {
        return table.stream().map(l -> l.target).collect(Collectors.toList());
    }

    @Override
    public Collection<Routing<T>> getBy(String pathPrefix) {
        return table.stream()
                .filter(l -> l.target.path().startsWith(pathPrefix))
                .map(l -> l.target)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<Routing<T>> getBy(Class<?> controllerClz) {
        return table.stream()
                .filter(l -> l.target.targets().stream().anyMatch(vt -> {
                    if (vt.getTarget() instanceof Action) {
                        Action a = (Action) vt.getTarget();
                        if (a.controller().clz().equals(controllerClz)) {
                            return true;
                        }
                    }
                    return false;
                }))
                .map(l -> l.target)
                .collect(Collectors.toList());
    }

    /**
     * 区配一个目标
     *
     * @param path   路径
     * @param method 方法
     * @return 一个区配的目标
     */
    public T matchOne(String path, @Nullable String versionStr, MethodType method) {
        Version version2 = versionOf(versionStr);

        for (RankEntity<Routing<T>> l : table) {
            if (l.target.matches(method, path)) {
                return l.target.target(version2);
            }
        }

        return null;
    }

    /**
     * 区配一个目标并给出状态
     *
     * @param path   路径
     * @param method 方法
     * @return 一个区配的目标
     */
    @Override
    public Result<T> matchOneAndStatus(String path, @Nullable String versionStr, MethodType method) {
        Version version2 = versionOf(versionStr);

        int degrees = 0;
        for (RankEntity<Routing<T>> l : table) {
            int tmp = l.target.degrees(method, path);
            if (tmp == 2) {
                return Result.succeed(l.target.target(version2));
            } else {
                if (tmp > degrees) {
                    degrees = tmp;
                }
            }
        }

        if (degrees == 1) {
            return Result.failure(405);
        } else {
            return Result.failure(404);
        }
    }

    @Override
    public void clear() {
        table.clear();
    }
}