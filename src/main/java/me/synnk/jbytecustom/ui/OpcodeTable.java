package me.synnk.jbytecustom.ui;

import me.synnk.jbytecustom.utils.ErrorDisplay;
import org.apache.commons.io.IOUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

public class OpcodeTable extends JEditorPane {
    public OpcodeTable() {
        this.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        this.setEditable(false);
        this.setContentType("text/html");
        this.setText(loadTable());
    }

    private String loadTable() {
        try {
            return IOUtils.toString(Objects.requireNonNull(this.getClass().getResourceAsStream("/resources/html/optable.html")));
        } catch (Exception e) {
            new ErrorDisplay(e);
            return "";
        }
    }
}
