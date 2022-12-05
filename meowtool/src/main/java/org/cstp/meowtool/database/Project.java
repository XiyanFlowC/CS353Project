package org.cstp.meowtool.database;

import lombok.Data;

@Data
public class Project {
    private Integer id;
    private Integer owner;
    private Integer type;
    private String name;
    private String tags;
    private String oriLang;
    private String tarLang;
}
