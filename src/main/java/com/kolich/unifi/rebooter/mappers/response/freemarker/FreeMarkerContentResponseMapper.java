/*
 * Copyright (c) 2024 Mark S. Kolich
 * https://mark.koli.ch
 *
 * Permission is hereby granted, free of charge, to any person
 * obtaining a copy of this software and associated documentation
 * files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use,
 * copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following
 * conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES
 * OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT
 * HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
 * WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR
 * OTHER DEALINGS IN THE SOFTWARE.
 */

package com.kolich.unifi.rebooter.mappers.response.freemarker;

import com.kolich.unifi.rebooter.components.config.UnifiRebooterConfig;
import com.kolich.unifi.rebooter.components.freemarker.FreeMarkerContentToString;
import com.kolich.unifi.rebooter.entities.freemarker.FreeMarkerContent;
import com.kolich.unifi.rebooter.mappers.response.AbstractFreeMarkerContentAwareResponseMapper;
import curacao.annotations.Injectable;
import curacao.annotations.Mapper;
import curacao.core.servlet.AsyncContext;
import curacao.core.servlet.HttpResponse;

import javax.annotation.Nonnull;

@Mapper
public final class FreeMarkerContentResponseMapper
        extends AbstractFreeMarkerContentAwareResponseMapper<FreeMarkerContent> {

    private final UnifiRebooterConfig unifiRebooterConfig_;

    @Injectable
    public FreeMarkerContentResponseMapper(
            final FreeMarkerContentToString fmContentToString,
            final UnifiRebooterConfig unifiRebooterConfig) {
        super(fmContentToString);
        unifiRebooterConfig_ = unifiRebooterConfig;
    }

    @Override
    public void render(
            final AsyncContext context,
            final HttpResponse response,
            @Nonnull final FreeMarkerContent content) throws Exception {
        renderFreeMarkerContent(response, content);
    }

}
