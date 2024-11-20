package francesco.santaniello.gui.component;

import francesco.santaniello.gui.MainWindow;
import francesco.santaniello.service.AutoCompleteService;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;

public class TextFieldAutoComplete extends JTextField {

    private final JPopupMenu popupMenu = new JPopupMenu();
    private final JList<String> list = new JList<>();

    public TextFieldAutoComplete(int columns){
        super(columns);
        setFont(MainWindow.DEFAULT_FONT);
        list.setFont(MainWindow.DEFAULT_FONT);

        popupMenu.add(new JScrollPane(list));

        if (!AutoCompleteService.getInstance().getAutoCompleteSource().isEmpty())
            initDocumentListener();
    }

    private void initDocumentListener(){
        getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                showAutoComplete();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                showAutoComplete();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                showAutoComplete();
            }
        });

        list.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseClicked(java.awt.event.MouseEvent e) {
                if (e.getClickCount() == 2) {
                    setText(list.getSelectedValue());
                    popupMenu.setVisible(false);
                }
            }
        });

        addFocusListener(new FocusAdapter() {
            @Override
            public void focusLost(FocusEvent e) {
                popupMenu.setVisible(false);
            }
        });
    }

    private void showAutoComplete(){
        final String input = getText() != null ? getText().toLowerCase() : "";
        if (input.isBlank()){
            popupMenu.setVisible(false);
            return;
        }

        final String[] comuniFiltrati = AutoCompleteService.getInstance().getAutoCompleteSource().parallelStream()
                .filter(e -> e != null && !e.isBlank() && e.toLowerCase().startsWith(input))
                .toArray(String[]::new);
        if (comuniFiltrati.length > 0){
            list.setListData(comuniFiltrati);
            list.setSelectedIndex(0);
            list.setVisibleRowCount(Math.min(comuniFiltrati.length, 10));
            Point location = getLocationOnScreen();
            location.y += getHeight();
            popupMenu.setLocation(location);
            popupMenu.setVisible(true);
        }
        else{
            popupMenu.setVisible(false);
        }
    }
}
