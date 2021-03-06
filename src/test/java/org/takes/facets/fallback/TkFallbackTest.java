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
package org.takes.facets.fallback;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.takes.Request;
import org.takes.Response;
import org.takes.Take;
import org.takes.misc.Opt;
import org.takes.rq.RqFake;
import org.takes.rs.ResponseOf;
import org.takes.rs.RsPrint;
import org.takes.rs.RsText;
import org.takes.tk.TkFailure;

/**
 * Test case for {@link TkFallback}.
 * @since 0.9.6
 * @checkstyle ClassDataAbstractionCouplingCheck (500 lines)
 */
public final class TkFallbackTest {

    /**
     * TkFallback can fall back.
     * @throws Exception If some problem inside
     */
    @Test
    public void fallsBack() throws Exception {
        final String err = "message";
        MatcherAssert.assertThat(
            new RsPrint(
                new TkFallback(
                    new TkFailure(err),
                    new Fallback() {
                        @Override
                        public Opt<Response> route(final RqFallback req) {
                            return new Opt.Single<Response>(
                                new RsText(req.throwable().getMessage())
                            );
                        }
                    }
                ).act(new RqFake())
            ).printBody(),
            Matchers.endsWith(err)
        );
    }

    /**
     * TkFallback can fall back.
     * @throws Exception If some problem inside
     */
    @Test
    public void fallsBackInsideResponse() throws Exception {
        MatcherAssert.assertThat(
            new RsPrint(
                new TkFallback(
                    new Take() {
                        @Override
                        public Response act(final Request req) {
                            return new ResponseOf(
                                () -> {
                                    throw new UnsupportedOperationException("");
                                },
                                () -> {
                                    throw new IllegalArgumentException(
                                        "here we fail"
                                    );
                                }
                            );
                        }
                    },
                    new FbFixed(new RsText("caught here!"))
                ).act(new RqFake())
            ).printBody(),
            Matchers.startsWith("caught")
        );
    }

    /**
     * TkFallback can throw an Exception when no fallback is available.
     */
    @Test
    @SuppressWarnings("PMD.AvoidCatchingGenericException")
    public void fallsBackWithProperMessage() {
        try {
            new TkFallback(
                new TkFailure(),
                new FbChain()
            ).act(new RqFake());
            MatcherAssert.assertThat("Must throw exception", false);
            //@checkstyle IllegalCatch (1 line)
        } catch (final Exception exception) {
            MatcherAssert.assertThat(
                exception.getMessage(),
                Matchers.containsString("fallback ")
            );
        }
    }
}
