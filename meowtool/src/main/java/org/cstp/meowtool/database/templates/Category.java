package org.cstp.meowtool.database.templates;

import lombok.Data;

@Data
public class Category {
    private Integer id;
    private Integer proj_id;
    private String name;
    private String desc;
}
