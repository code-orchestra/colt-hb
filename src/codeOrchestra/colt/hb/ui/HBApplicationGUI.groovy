package codeOrchestra.colt.hb.ui

import codeOrchestra.colt.core.ui.components.StatusButton
import codeOrchestra.colt.core.ui.groovy.GroovyDynamicMethods
import codeOrchestra.colt.hb.cookie.PersistentCookieStore
import javafx.beans.value.ChangeListener
import javafx.beans.value.ObservableValue
import javafx.event.EventHandler
import javafx.geometry.Pos
import javafx.scene.control.Button
import javafx.scene.control.TextField
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.scene.layout.BorderPane
import javafx.scene.layout.HBox
import javafx.scene.layout.Priority
import javafx.scene.web.WebHistory
import javafx.scene.web.WebView

/**
 * @author Dima Kruk
 */
class HBApplicationGUI extends BorderPane {

    TextField utr_tf
    WebView wv
    HBox top_hb
    HBox bottom_hb
    Button back
    Button forward

    protected StatusButton statusButton

    HBApplicationGUI() {
        GroovyDynamicMethods.init()

        initView()

        initCookies()

        initHistory()

        initBrowserLine()

        wv.engine.load(utr_tf.text)
    }

    private void initView() {
        setTop(top_hb = new HBox(newChildren: [
                back = new Button(text: "<", focusTraversable: false, disable: true),
                forward = new Button(text: ">", focusTraversable: false, disable: true),
                utr_tf = new TextField(text: "http://habrahabr.ru", prefHeight: 30)
        ]))
        HBox.setHgrow(utr_tf, Priority.ALWAYS)

        setCenter(wv = new WebView())

        setBottom(bottom_hb = new HBox(alignment: Pos.CENTER_RIGHT, newChildren: [
                statusButton = new StatusButton()
        ]))
    }

    private void initBrowserLine() {
        utr_tf.onKeyPressed = { KeyEvent it ->
            if (it.code == KeyCode.ENTER) {
                if (utr_tf.text.contains("http://")) {
                    wv.engine.load(utr_tf.text)
                } else {
                    wv.engine.load("http://" + utr_tf.text)
                }
            }
        } as EventHandler

        wv.engine.locationProperty().addListener({ ObservableValue<? extends String> observableValue, String t, String t1 ->
            utr_tf.text = t1
        } as ChangeListener)
    }

    private void initHistory() {
        final WebHistory history = wv.engine.history
        history.currentIndexProperty().addListener({ ObservableValue<? extends Number> observableValue, Number t, Number t1 ->
            back.disable = !(t1 > 0)
            forward.disable = t1 == history.entries.size() - 1
        } as ChangeListener)
        back.onAction = {
            history.go(-1)
        } as EventHandler
        forward.onAction = {
            history.go(1)
        } as EventHandler
    }

    private static void initCookies() {
        PersistentCookieStore store = new PersistentCookieStore()
        CookieHandler.setDefault(new CookieManager(store, CookiePolicy.ACCEPT_ALL))
        store.initCookieHandler(CookieHandler.getDefault())
    }
}
