/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2014-2019 Yegor Bugayenko
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.takes.tk;

import java.io.IOException;
import org.junit.Test;
import org.takes.rq.RqFake;

/**
 * Test case for {@link TkSlf4j}.
 * @since 0.11.2
 */
public final class TkSlf4jTest {

    /**
     * TkSlf4j can log message.
     * @throws Exception If some problem inside
     */
    @Test
    public void logsMessage() throws Exception {
        new TkSlf4j(new TkText("test")).act(new RqFake());
    }

    /**
     * TkSlf4j can log exception.
     * @throws Exception If some problem inside
     */
    @Test(expected = IOException.class)
    public void logsException() throws Exception {
        new TkSlf4j(new TkFailure(new IOException(""))).act(new RqFake());
    }

    /**
     * TkSlf4j can log runtime exception.
     * @throws Exception If some problem inside
     */
    @Test(expected = RuntimeException.class)
    public void logsRuntimeException() throws Exception {
        new TkSlf4j(new TkFailure(new RuntimeException(""))).act(new RqFake());
    }

    /**
     * TkSlf4j can work with {@link TkEmpty}.
     * @throws Exception If some problem inside
     */
    @Test
    public void logsEmptyMessage() throws Exception {
        new TkSlf4j(new TkEmpty()).act(new RqFake());
    }
}
