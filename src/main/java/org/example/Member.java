package org.example;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author: HAPPY
 * @Project_name: SpEL
 * @Date: 2023/12/22 11:29
 * @Description:
 */
@Data
@Accessors(chain = true)
public class Member {
    private Inventor[] members;
}
