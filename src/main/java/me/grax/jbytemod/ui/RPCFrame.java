package me.grax.jbytemod.ui;

import me.grax.jbytemod.JByteMod;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class RPCFrame extends JDialog {

    public static JLabel labelUsername = new JLabel("State: ");
    public static JLabel labelPassword = new JLabel("Details: ");
    public static JTextField fieldUsername = new JTextField(20);
    public static JCheckBox checker = new JCheckBox("Editable");
    public static JCheckBox checkerTwo = new JCheckBox("Editable");
    public static JButton buttonLogin = new JButton("Apply");


    public RPCFrame(JByteMod jbm) {

        this.setTitle("RPC Changer");
        // create a new panel with GridBagLayout manager
        JPanel newPanel = new JPanel(new GridBagLayout());

        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        // add components to the panel
        constraints.gridx = 0;
        constraints.gridy = 0;
        newPanel.add(labelUsername, constraints);

        constraints.gridx = 1;
        newPanel.add(fieldUsername, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 2;
        constraints.anchor = GridBagConstraints.CENTER;
        newPanel.add(buttonLogin, constraints);

        // set border for the panel
        newPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Discord RPC"));

        // add the panel to this frame
        add(newPanel);

        pack();
        setLocationRelativeTo(null);
    }
}

