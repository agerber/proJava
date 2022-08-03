package edu.uchicago.gerber._08final.mvc.model

import lombok.AllArgsConstructor
import lombok.Data
import javax.swing.Action

/**
 * Created by ag on 6/17/2015.
 */
//the lombok @Data gives us automatic getters and setters
@Data //the lombok @AllArgsConstructor gives us an All-Args-Constructor :)
@AllArgsConstructor
data class GameOp(

    //members
    val movable: Movable? = null,
    val action: Action? = null
){
    //this could also be a boolean, but we want to be explicit about what we're doing
    enum class Action {
        ADD, REMOVE
    }
}