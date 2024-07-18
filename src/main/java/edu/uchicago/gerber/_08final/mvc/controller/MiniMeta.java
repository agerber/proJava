package edu.uchicago.gerber._08final.mvc.controller;


import lombok.AllArgsConstructor;
import lombok.Data;

/**

 */

//the lombok @Data gives us automatic getters and setters
@Data
//the lombok @AllArgsConstructor gives us an All-Args-Constructor :)
@AllArgsConstructor
public class MiniMeta {

    //members
    private int scaleX;
    private int scaleY;


}
