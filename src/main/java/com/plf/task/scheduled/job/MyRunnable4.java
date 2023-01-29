package com.plf.task.scheduled.job;

import com.plf.task.scheduled.dto.RunnableDto;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author panlf
 * @date 2023/1/29
 */

@Slf4j
public class MyRunnable4 extends JobTemplate<RunnableDto>{

    public MyRunnable4(){}

    public MyRunnable4(RunnableDto runnableDto){super(runnableDto);}

    @Override
    public void preHandle() {
      log.info("前置处理");
    }

    @Override
    public void handle(RunnableDto runnableDto) {
        log.info("获取的参数为: {}",runnableDto);
    }

    @Override
    public void postHandle() {
        log.info("后置处理");
    }
}
