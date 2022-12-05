package org.cstp.meowtool.database;

import lombok.Data;

@Data
public class File {
    private Integer id;
    private Integer categoryId;
    private String name;
    private String converter;
    private String comment;
}
