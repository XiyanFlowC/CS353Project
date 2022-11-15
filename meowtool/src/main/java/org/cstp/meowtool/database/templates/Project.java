package org.cstp.meowtool.database.templates;

import lombok.Data;

@Data
public class Project {
    private Integer id;
    private Integer type;
    private String name;
    private String tags;
    private String ori_lang;
    private String tar_lang;
}
