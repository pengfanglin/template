package com.fanglin.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 线程池配置
 *
 * @author 彭方林
 * @version 1.0
 * @date 2019/5/5 19:22
 **/
@Configuration
@Slf4j
public class ThreadConfig {

    @Bean
    ExecutorService executorService() {
        //固定一个线程存活
        int corePoolSize = 1;
        //最大十个线程
        int maxPoolSize = 10;
        //60s无活动线程自动销毁
        long keepAliveTime = 60L;
        TimeUnit unit = TimeUnit.SECONDS;
        //采用链表队列，任务无限堆积
        BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<>();
        ThreadFactory threadFactory = new NameTreadFactory();
        ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maxPoolSize, keepAliveTime, unit, workQueue, threadFactory);
        // 预启动所有核心线程
        executor.prestartAllCoreThreads();
        return executor;
    }

    /**
     * 自定义创建线程
     */
    static class NameTreadFactory implements ThreadFactory {

        private final AtomicInteger mThreadNum = new AtomicInteger(1);

        @Override
        public Thread newThread(Runnable r) {
            Thread thread = new Thread(r, "my-thread-" + mThreadNum.getAndIncrement());
            log.warn("线程{}创建成功", thread.getName());
            return thread;
        }
    }
}
