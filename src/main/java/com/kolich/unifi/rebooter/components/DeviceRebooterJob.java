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

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class DeviceRebooterJob implements Job {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceRebooterJob.class);

    public static final String ID_JOB_DATA_MAP_KEY = "id";
    public static final String HOSTNAME_JOB_DATA_MAP_KEY = "hostname";
    public static final String USERNAME_JOB_DATA_MAP_KEY = "username";
    public static final String PASSWORD_JOB_DATA_MAP_KEY = "password";

    private static final String REBOOT_CMD = "reboot";

    @Override
    public void execute(
            final JobExecutionContext context) throws JobExecutionException {
        final JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();

        final String hostname = jobDataMap.getString(HOSTNAME_JOB_DATA_MAP_KEY);
        final String username = jobDataMap.getString(USERNAME_JOB_DATA_MAP_KEY);
        final String password = jobDataMap.getString(PASSWORD_JOB_DATA_MAP_KEY);

        try {
            DeviceCommandExecutor.execute(hostname, username, password, REBOOT_CMD);
        } catch (final Exception e) {
            LOG.error("Failed to run device rebooter job: {}", hostname, e);
        }
    }

}
