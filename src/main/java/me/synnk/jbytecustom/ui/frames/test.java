package me.synnk.jbytecustom.ui.frames;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class test {
    public static void main(String[] args) {
        String content = "This is the content to write into file\n";

        // If the file doesn't exists, create and write to it
        // If the file exists, truncate (remove all content) and write to it
        try (FileWriter writer = new FileWriter("test.txt");
             BufferedWriter bw = new BufferedWriter(writer)) {

            bw.write(content);

        } catch (IOException e) {
            System.err.format("IOException: %s%n", e);
        }
    }
}
