package org.cstp.meowtool.database.templates;

import lombok.Data;

@Data
public class File {
    private Integer id;
    private Integer category_id;
    private String name;
    private String converter;
    private String comment;
}
