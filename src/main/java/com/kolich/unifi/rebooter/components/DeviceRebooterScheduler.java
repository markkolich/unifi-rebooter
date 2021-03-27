/*
 * Copyright (c) 2021 Mark S. Kolich
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

import com.kolich.unifi.rebooter.entities.Device;
import curacao.annotations.Component;
import curacao.annotations.Injectable;
import curacao.components.CuracaoComponent;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;

import static com.kolich.unifi.rebooter.components.DeviceRebooterJob.*;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

@Component
public final class DeviceRebooterScheduler implements CuracaoComponent {

    private final DeviceRebooterConfig deviceRebooterConfig_;

    private final Scheduler quartzScheduler_;

    @Injectable
    public DeviceRebooterScheduler(
            final DeviceRebooterConfig deviceRebooterConfig,
            final DeviceRebooterSchedulerFactory deviceRebooterSchedulerFactory) throws Exception {
        deviceRebooterConfig_ = deviceRebooterConfig;
        quartzScheduler_ = deviceRebooterSchedulerFactory.getNewScheduler();

        for (final Device deviceToReboot : deviceRebooterConfig.getDevicesToReboot()) {
            final JobDataMap jobDataMap = new JobDataMap();
            jobDataMap.put(ID_JOB_DATA_MAP_KEY, deviceToReboot.getId());
            jobDataMap.put(HOSTNAME_JOB_DATA_MAP_KEY, deviceToReboot.getHostname());
            jobDataMap.put(USERNAME_JOB_DATA_MAP_KEY, deviceToReboot.getUsername());
            jobDataMap.put(PASSWORD_JOB_DATA_MAP_KEY, deviceToReboot.getPassword());

            final JobDetail job = newJob(DeviceRebooterJob.class)
                    .setJobData(jobDataMap)
                    .build();
            final Trigger trigger = newTrigger()
                    .withSchedule(cronSchedule(deviceToReboot.getCronExpression()))
                    .build();

            quartzScheduler_.scheduleJob(job, trigger);
        }
    }

    @Override
    public void initialize() throws Exception {
        // Starts the scheduler.
        quartzScheduler_.start();
    }

    @Override
    public void destroy() throws Exception {
        // Clears any pending jobs in prep for shutdown.
        quartzScheduler_.clear();
        quartzScheduler_.shutdown();
    }

}
