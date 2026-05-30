/*
 * Copyright (c) 2026 Mark S. Kolich
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

import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
import com.kolich.unifi.rebooter.exceptions.UnifiRebooterException;
import org.apache.commons.codec.binary.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;

public final class DeviceCommandExecutor {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceCommandExecutor.class);

    private static final String EXEC_CHANNEL = "exec";

    @SuppressWarnings("try")
    public static void execute(
            final String hostname,
            final String username,
            final String password,
            final String command) {
        try {
            final ByteArrayOutputStream os = new ByteArrayOutputStream();

            final Session session = new JSch().getSession(username, hostname, 22);
            try (AutoCloseable ignored = session::disconnect) {
                session.setPassword(StringUtils.getBytesUtf8(password));
                session.setConfig("StrictHostKeyChecking", "no"); // ignore key issues
                session.connect();

                final ChannelExec channel = (ChannelExec) session.openChannel(EXEC_CHANNEL);
                try (AutoCloseable ignored1 = channel::disconnect) {
                    channel.setOutputStream(os);
                    channel.setCommand(command);
                    channel.connect();

                    // Wait until connected - sigh.
                    while (channel.isConnected()) {
                        Thread.sleep(100L);
                    }
                }
            }

            LOG.info("Successfully executed command on device: {}: {} -> {}",
                    hostname, command, StringUtils.newStringUtf8(os.toByteArray()).trim());
        } catch (final Exception e) {
            throw new UnifiRebooterException("Failed to execute command on device: "
                    + hostname, e);
        }
    }

}
