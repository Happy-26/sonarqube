package org.example;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Map;

/**
 * @Author: HAPPY
 * @Project_name: SpEL
 * @Date: 2023/12/25 10:31
 * @Description:
 */
@Data
@Accessors(chain = true)
public class InventorMap {
    private Map<String, Object> officers;
}
