package org.jon.lv.mapper;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

/**
 * Package: com.shanlinjinrong.data.mapper.TestMapper
 * Description: 描述
 * @author lv bin
 * Version: V1.0.0
 */
@Component
public class TestMapper {

    public void testMapper(){

        System.out.println("*****************************" + LocalDateTime.now());
    }
}
