package com.prhythm.app.remotetail.data;

import com.prhythm.app.remotetail.models.HighLight;
import javafx.scene.control.ListCell;

/**
 * 顯著標示呈現
 * Created by nanashi07 on 16/1/5.
 */
public class HighLightListCell extends ListCell<HighLight> {

    @Override
    protected void updateItem(HighLight item, boolean empty) {
        if (empty) {
            setGraphic(null);
        } else {

        }
    }
}
