package francesco.santaniello.gui.component;

import francesco.santaniello.gui.MainWindow;

import javax.swing.JLabel;

public class Label extends JLabel {
    public Label(String text){
        super(text);
        setFont(MainWindow.DEFAULT_FONT);
    }
}