package com.example.demo.sheduler;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {

    private final Scheduler scheduler;

    @Value("${spring.quartz.job-interval}")
    private long interval;

    @PostConstruct
    public void schedule() {
        var jobName = UsersBalanceUpdateJob.class.getSimpleName();
        var scheduleTimer = getSimpleScheduleBuilder();
        var job = getJobDetail(jobName);
        var trigger = getTrigger(jobName, scheduleTimer);

        try {
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException e) {
            log.error(e.getMessage());
        }
    }

    private Trigger getTrigger(String jobName, SimpleScheduleBuilder scheduleTimer) {
        return TriggerBuilder
                .newTrigger()
                .forJob(jobName)
                .startNow()
                .withSchedule(scheduleTimer)
                .build();
    }

    private JobDetail getJobDetail(String jobName) {
        return JobBuilder
                .newJob(UsersBalanceUpdateJob.class)
                .withIdentity(jobName)
                .build();
    }

    private SimpleScheduleBuilder getSimpleScheduleBuilder() {
        return SimpleScheduleBuilder
                .simpleSchedule()
                .withIntervalInMilliseconds(interval)
                .repeatForever();
    }
}
