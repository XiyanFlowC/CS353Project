package org.cstp.meowtool.database;

import java.util.Date;

import lombok.Data;

@Data
public class Text {
    private Integer id;
    private Integer fileId;
    private String oriText;
    private String comment;
    private String translation;

    private Boolean marked;

    private Integer stage;
    private Integer commiter;
    private Date time;
}
