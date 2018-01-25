/*
 * Copyright 1999-2015 dangdang.com.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * </p>
 */

package org.jon.lv.config;

import com.dangdang.ddframe.job.api.simple.SimpleJob;
import com.dangdang.ddframe.job.config.JobCoreConfiguration;
import com.dangdang.ddframe.job.config.simple.SimpleJobConfiguration;
import com.dangdang.ddframe.job.lite.api.JobScheduler;
import com.dangdang.ddframe.job.lite.config.LiteJobConfiguration;
import com.dangdang.ddframe.job.lite.spring.api.SpringJobScheduler;
import com.dangdang.ddframe.job.reg.zookeeper.ZookeeperRegistryCenter;
import org.jon.lv.job.SpringSimpleJob;
import org.jon.lv.job.TestSimpleJob;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * @author jon
 */
@Configuration
public class SimpleJobConfig {
    
    @Resource
    private ZookeeperRegistryCenter regCenter;

    @Resource
    public SpringSimpleJob springSimpleJob;

    @Resource
    public TestSimpleJob testSimpleJob;

//    @Bean
//    public SimpleJob simpleJob() {
//        return new SpringSimpleJob();
//    }
//
//    @Bean
//    public TestSimpleJob testSimpleJob(){
//        return new TestSimpleJob();
//    }

    @Bean(initMethod = "init", name = "testJobScheduler")
    public JobScheduler testJobScheduler(final TestSimpleJob testSimpleJob,
                                           @Value("${testJob.cron}") final String cron,
                                           @Value("${testJob.shardingTotalCount}") final int shardingTotalCount,
                                           @Value("${testJob.shardingItemParameters}") final String shardingItemParameters) {

        return new SpringJobScheduler(testSimpleJob, regCenter, getLiteJobConfiguration(testSimpleJob.getClass(), cron,
                shardingTotalCount, shardingItemParameters));
    }

    @Bean(initMethod = "init", name = "simpleJobScheduler")
    public JobScheduler simpleJobScheduler(final SpringSimpleJob springSimpleJob,
                                           @Value("${simpleJob.cron}") final String cron,
                                           @Value("${simpleJob.shardingTotalCount}") final int shardingTotalCount,
                                           @Value("${simpleJob.shardingItemParameters}") final String shardingItemParameters) {

        return new SpringJobScheduler(springSimpleJob, regCenter, getLiteJobConfiguration(springSimpleJob.getClass(), cron,
                shardingTotalCount, shardingItemParameters));
    }
    
    private LiteJobConfiguration getLiteJobConfiguration(final Class<? extends SimpleJob> jobClass,
                                                         final String cron,
                                                         final int shardingTotalCount,
                                                         final String shardingItemParameters) {
        return LiteJobConfiguration.newBuilder(new SimpleJobConfiguration(JobCoreConfiguration.newBuilder(
                jobClass.getName(), cron, shardingTotalCount).shardingItemParameters(shardingItemParameters).build(),
                jobClass.getCanonicalName())).overwrite(true).build();
    }
}
