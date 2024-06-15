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

package com.kolich.unifi.rebooter.components;

import com.google.common.collect.ImmutableList;
import com.kolich.unifi.rebooter.components.config.UnifiRebooterConfig;
import com.kolich.unifi.rebooter.entities.Device;
import com.kolich.unifi.rebooter.exceptions.UnifiRebooterException;
import com.typesafe.config.ConfigList;
import com.typesafe.config.ConfigValue;
import com.typesafe.config.ConfigValueType;
import curacao.annotations.Component;
import curacao.annotations.Injectable;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Component
public final class DeviceRebooterConfig {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceRebooterConfig.class);

    private static final String DEVICES_CONFIG_PATH = "devices";

    private static final String HOSTNAME_PROP = "hostname";
    private static final String USERNAME_PROP = "username";
    private static final String PASSWORD_PROP = "password";
    private static final String CRON_EXPRESSION_PROP = "cronExpression";

    private final ConfigList devicesToRebootConfigList_;

    private final List<Device> devicesToReboot_;

    @Injectable
    public DeviceRebooterConfig(
            final UnifiRebooterConfig unifiRebooterConfig) {
        devicesToRebootConfigList_ = unifiRebooterConfig.getUnifiRebooterConfig()
                .getList(DEVICES_CONFIG_PATH);

        devicesToReboot_ = buildDevicesToRebootFromConfig();
    }

    private List<Device> buildDevicesToRebootFromConfig() {
        final ImmutableList.Builder<Device> devicesToRebootBuilder =
                ImmutableList.builder();

        for (final ConfigValue configValue : devicesToRebootConfigList_) {
            if (!ConfigValueType.OBJECT.equals(configValue.valueType())) {
                continue;
            }

            @SuppressWarnings("unchecked") // intentional, and safe
            final Map<String, String> configDevice = (Map<String, String>) configValue.unwrapped();

            final String hostname = configDevice.get(HOSTNAME_PROP);
            if (StringUtils.isBlank(hostname)) {
                throw new UnifiRebooterException("Blank or null device config key: "
                        + HOSTNAME_PROP);
            }

            final String username = configDevice.get(USERNAME_PROP);
            if (StringUtils.isBlank(username)) {
                throw new UnifiRebooterException("Blank or null device config key: "
                        + USERNAME_PROP);
            }

            final String password = configDevice.get(PASSWORD_PROP);
            if (StringUtils.isBlank(password)) {
                throw new UnifiRebooterException("Blank or null device config key: "
                        + PASSWORD_PROP);
            }

            final String cronExpression = configDevice.get(CRON_EXPRESSION_PROP);
            if (StringUtils.isBlank(cronExpression)) {
                throw new UnifiRebooterException("Blank or null device config key: "
                        + CRON_EXPRESSION_PROP);
            }

            devicesToRebootBuilder.add(new Device.Builder()
                    .setId(UUID.randomUUID().toString())
                    .setHostname(hostname)
                    .setUsername(username)
                    .setPassword(password)
                    .setCronExpression(cronExpression)
                    .build());
        }

        return devicesToRebootBuilder.build();
    }

    /**
     * Returns an immutable list representing the list of devices to reboot.
     */
    public List<Device> getDevicesToReboot() {
        return devicesToReboot_;
    }

}
