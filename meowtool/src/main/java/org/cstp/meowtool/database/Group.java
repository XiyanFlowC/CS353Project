package org.cstp.meowtool.database;

import lombok.Data;

@Data
public class Group {
    private Integer id;
    private Integer proj_id;
    private Integer user_id;
    private String role;
}
