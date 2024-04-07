package com.klaus.demo.vt;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.IntStream;

/**
 * @author Klaus
 */
@Slf4j
@SpringBootTest
public class VirtualThreadTest {

    @Test
    void testThreadName() throws InterruptedException {
        // 平台线程
        Thread.ofPlatform().start(() -> {
            System.out.println(Thread.currentThread());
        });
        // 虚拟线程
        Thread vt = Thread.ofVirtual().start(() -> {
            System.out.println(Thread.currentThread());
        });
        // 等待虚拟线程打印完毕再退出
        vt.join();
    }


    @Test
    void testVirtual() {
        Set<String> threadNames = ConcurrentHashMap.newKeySet();

        // 使用Executors创建虚拟线程
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            // 创建100万个虚拟线程
            IntStream.of(0, 1_000_000).forEach(i -> {
                executor.submit(() -> {
                    try {
                        Thread.sleep(Duration.ofSeconds(1));
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    String threadInfo = Thread.currentThread().toString();
                    String workerName = threadInfo.split("@")[1];
                    threadNames.add(workerName);

                    return i;
                });
            });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // 最终会发现使用的平台线程数才个位数
        log.info("Platform threads count: {}", threadNames.size());
    }
}
