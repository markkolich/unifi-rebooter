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

package com.kolich.unifi.rebooter.components.config;

import com.typesafe.config.Config;
import com.typesafe.config.ConfigFactory;
import curacao.annotations.Component;

/**
 * An {@link UnifiRebooterConfig} implementation backed by the Typesafe (a.k.a., Lightbend)
 * configuration library.
 */
@Component
public final class UnifiRebooterTypesafeConfig implements UnifiRebooterConfig {

    private static final String UNIFI_REBOOTER_CONFIG_PATH = "unifi-rebooter";

    private final Config config_;

    public UnifiRebooterTypesafeConfig() {
        config_ = ConfigFactory.load().getConfig(UNIFI_REBOOTER_CONFIG_PATH);
    }

    @Override
    public Config getUnifiRebooterConfig() {
        return config_;
    }

    // Application config

    @Override
    public String getContextPath() {
        return config_.getString(CONTEXT_PATH_PROP);
    }

    @Override
    public String getBaseUri() {
        return config_.getString(BASE_URI_PROP);
    }

    @Override
    public String getFullUri() {
        return config_.getString(FULL_URI_PROP);
    }

}
