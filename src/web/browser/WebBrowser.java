package web.browser;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;

public class WebBrowser {

    public static void main(String[] args) {
        JFrame jframe = new EditorPaneFrame();
        jframe.show();
    }
}

class EditorPaneFrame extends JFrame {

    private JTextField url;
    private JCheckBox editable;
    private JButton load_button;
    private JButton back_button;
    private JEditorPane editor_pane;
    private Stack url_stack = new Stack();

    public EditorPaneFrame() {
        setTitle("Java Web Browser");
        setSize(600, 400);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });

        // Set up TextField and load button for typing in URL.
        url = new JTextField(30);

        load_button = new JButton("Load");
        load_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                try {
                    //Push URL onto stack for back button.
                    url_stack.push(url.getText());
                    editor_pane.setPage(url.getText());
                } catch (Exception e) {
                    editor_pane.setText("Error: " + e);
                }
            }
        });

        back_button = new JButton("Back");
        back_button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                if (url_stack.size() <= 1) {
                    return;
                }
                try {
                    url_stack.pop();
                    String urlString = (String) url_stack.peek();
                    url.setText(urlString);
                    editor_pane.setPage(urlString);
                } catch (IOException e) {
                    editor_pane.setText("Error : " + e);
                }
            }
        });

        editor_pane = new JEditorPane();
        editor_pane.setEditable(false);
        editor_pane.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent event) {
                if (event.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                    try {
                        url_stack.push(event.getURL().toString());
                        url.setText(event.getURL().toString());

                        editor_pane.setPage(event.getURL());
                    } catch (IOException e) {
                        editor_pane.setText("Error: " + e);
                    }
                }
            }
        });

        editable = new JCheckBox();
        editable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent event) {
                editor_pane.setEditable(editable.isSelected());
            }
        });

        Container contentPane = getContentPane();
        contentPane.add(new JScrollPane(editor_pane), "Center");

        JPanel panel = new JPanel();
        panel.add(new JLabel("URL"));
        panel.add(url);
        panel.add(load_button);
        panel.add(back_button);
        panel.add(new JLabel("Editable"));
        panel.add(editable);

        contentPane.add(panel, "South");
    }

}
