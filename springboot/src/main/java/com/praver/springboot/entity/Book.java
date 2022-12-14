package com.praver.springboot.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Data
public class Book extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    private String avatar;
    private String name;
    private String description;
    private String publishDate;
    private String author;
    private String publisher;
    private String category;
    private String bookNo;

    private List<String> categories;
    private Integer score;
    private Integer nums;
}
