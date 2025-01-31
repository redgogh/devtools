package org.redgogh.devtools.test;

/* -------------------------------------------------------------------------------- *\
|*                                                                                  *|
|*    Copyright (C) 2019-2024 RedGogh All rights reserved.                          *|
|*                                                                                  *|
|*    Licensed under the Apache License, Version 2.0 (the "License");               *|
|*    you may not use this file except in compliance with the License.              *|
|*    You may obtain a copy of the License at                                       *|
|*                                                                                  *|
|*        http://www.apache.org/licenses/LICENSE-2.0                                *|
|*                                                                                  *|
|*    Unless required by applicable law or agreed to in writing, software           *|
|*    distributed under the License is distributed on an "AS IS" BASIS,             *|
|*    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.      *|
|*    See the License for the specific language governing permissions and           *|
|*    limitations under the License.                                                *|
|*                                                                                  *|
\* -------------------------------------------------------------------------------- */

import org.redgogh.devtools.collect.Lists;
import org.redgogh.devtools.generators.RandomGenerator;
import org.junit.Test;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * @author RedGogh
 */
@SuppressWarnings("ALL")
public class ThreadExecutorTest {

    @Test
    public void newExecutorFixedPoolTest() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(128);

        List<Integer> numbers = Lists.newSynchronizedList();

        for (int i = 0; i < (1024 * 1024); i++) {
            executor.execute(() -> {
                numbers.add(RandomGenerator.nextInt(6) + RandomGenerator.nextInt(6));
            });
        }

        executor.shutdown();
        executor.awaitTermination(30, TimeUnit.SECONDS);

        Integer sum = numbers.stream().reduce(0, Integer::sum);
        System.out.println("beg="+ Lists.beg(numbers) + ", sum=" + sum);
    }

}
