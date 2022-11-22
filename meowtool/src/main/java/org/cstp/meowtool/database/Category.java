package org.cstp.meowtool.database;

import lombok.Data;

@Data
public class Category {
    private Integer id;
    private Integer proj_id;
    private String name;
    private String comment;
}
