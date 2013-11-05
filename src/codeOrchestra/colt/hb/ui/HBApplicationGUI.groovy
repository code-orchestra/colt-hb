package codeOrchestra.colt.hb.ui

import codeOrchestra.colt.core.ui.components.StatusButton
import codeOrchestra.colt.core.ui.groovy.GroovyDynamicMethods
import codeOrchestra.colt.hb.cookie.PersistentCookieStore
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.TextField
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.web.WebView

/**
 * @author Dima Kruk
 */
class HBApplicationGUI extends BorderPane {

    TextField utr_tf
    WebView wv
    HBox top_hb
    HBox bottom_hb

    protected StatusButton statusButton

    HBApplicationGUI() {
        GroovyDynamicMethods.init()

        setTop(top_hb = new HBox(newChildren: [
                utr_tf = new TextField(text: "http://habrahabr.ru", prefHeight:30)
        ]))
        HBox.setHgrow(utr_tf, Priority.ALWAYS)

        PersistentCookieStore store = new PersistentCookieStore()
        CookieHandler.setDefault(new CookieManager(store, CookiePolicy.ACCEPT_ALL))
        store.initCookieHandler(CookieHandler.getDefault())

        setCenter(wv = new WebView())
        wv.engine.load(utr_tf.text)

        setBottom(bottom_hb = new HBox(alignment: Pos.CENTER_RIGHT, newChildren: [
                statusButton = new StatusButton()
        ]))

        statusButton.onAction = {
            store.removeAll()
        } as EventHandler
    }
}
