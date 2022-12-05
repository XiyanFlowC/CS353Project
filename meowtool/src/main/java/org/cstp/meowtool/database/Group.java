package org.cstp.meowtool.database;

import lombok.Data;

@Data
public class Group {
    private Integer id;
    private Integer projId;
    private Integer userId;
    private String role;
}
