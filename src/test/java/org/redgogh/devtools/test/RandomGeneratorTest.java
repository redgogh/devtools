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

import org.redgogh.devtools.generators.RandomGenerator;
import org.junit.Test;

@SuppressWarnings("ALL")
public class RandomGeneratorTest {

    @Test
    public void nextCodeTest() {
        for (int i = 0; i < 165; i++) {
            System.out.println(RandomGenerator.nextCode(8) + "75b0-11ef-a8c9-005056935d62");
        }
    }

    @Test
    public void nextLetterTest() {
        for (int i = 0; i < 100; i++) {
            System.out.println(RandomGenerator.nextLetterCode(8));
        }
    }

    @Test
    public void nextNumberTest() {
        for (int i = 0; i < 100; i++) {
            System.out.println(RandomGenerator.nextNumberCode(6));
        }
    }

    @Test
    public void nextCodeRangeTest() {
        for (int i = 0; i < 100; i++) {
            System.out.println(RandomGenerator.nextCode(0, 8));
        }
    }

    @Test
    public void nextLetterRangeTest() {
        for (int i = 0; i < 100; i++) {
            System.out.println(RandomGenerator.nextLetterCode(0, 6));
        }
    }

    @Test
    public void nextNumberRangeTest() {
        for (int i = 0; i < 100; i++) {
            System.out.println(RandomGenerator.nextNumberCode(0, 6));
        }
    }

}
