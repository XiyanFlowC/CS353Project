package org.cstp.meowtool.database;

import lombok.Data;

@Data
public class Terminology {
    private Integer id;
    private Integer projId;
    private String oriWord;
    private String tarWord;
    private String comment;
    private Integer commiter;
}
