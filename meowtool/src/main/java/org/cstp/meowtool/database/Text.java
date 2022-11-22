package org.cstp.meowtool.database;

import lombok.Data;

@Data
public class Text {
    private Integer id;
    private Integer file_id;
    private String ori_text;
    private String trans;
}
