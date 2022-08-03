package edu.uchicago.gerber._08final.mvc.model

import lombok.AllArgsConstructor
import lombok.Data
import javax.swing.Action

/**
 * Created by ag on 6/17/2015.
 */

data class GameOp(
    val movable: Movable,
    val action: Action
){
    //this could also be a boolean, but we want to be explicit about what we're doing
    enum class Action {
        ADD, REMOVE
    }
}
