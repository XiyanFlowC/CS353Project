package org.cstp.meowtool.database;

import java.util.Date;

import lombok.Data;

@Data
public class Translation {
    private Integer id;
    private Integer oriId;
    private Boolean marked;
    private String comment;
    private String trans;
    private Integer commiter;
    private Date updateTime;
}
