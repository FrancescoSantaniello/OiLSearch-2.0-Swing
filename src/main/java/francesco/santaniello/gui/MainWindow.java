package francesco.santaniello.gui;

import com.formdev.flatlaf.themes.FlatMacDarkLaf;
import francesco.santaniello.gui.component.Label;
import francesco.santaniello.gui.component.TextFieldAutoComplete;
import francesco.santaniello.gui.component.TreeViewModel;
import francesco.santaniello.model.Benzinaio;
import francesco.santaniello.model.Carburante;
import francesco.santaniello.model.RequestMessage;
import francesco.santaniello.model.ResponseMessage;
import francesco.santaniello.service.BenzinaiService;
import francesco.santaniello.service.AutoCompleteService;
import francesco.santaniello.service.GeolocalizzazioneService;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

public class MainWindow extends JFrame {

    public static final Font DEFAULT_FONT = new Font("Segoe UI Emoji", Font.PLAIN,18);
    private final TreeViewModel treeViewModel = new TreeViewModel(null);
    private final JTree treeView = new JTree(treeViewModel);
    private final JProgressBar progressBar = new JProgressBar();
    private final TextFieldAutoComplete cittaTextField = new TextFieldAutoComplete(20);
    private final JSlider radiusSlider = new JSlider(1,10);
    private final JButton buttonSearch = new JButton("Cerca");
    private final JPanel panelCheckBoxCarburanti = new JPanel(new GridLayout(12,1));
    private final JScrollPane scrollPanePanelCheckBox = new JScrollPane(panelCheckBoxCarburanti);
    private final JRadioButton radioButtonAsc = new JRadioButton("Prezzo crescente");
    private final JRadioButton radioButtonDesc = new JRadioButton("Prezzo decrescente");

    private final Set<String> carburanti = new HashSet<>();
    private final Set<String> carburantiSelected = new HashSet<>();

    private static class InnerClass{
        private static final MainWindow instance = new MainWindow();
    }

    static{
        try{
            UIManager.setLookAndFeel(new FlatMacDarkLaf());
        }
        catch (Exception ex){}
    }

    private MainWindow(){
        super("OiLSearch");
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        setSize((int)(dimension.getWidth() / 1.5),(int)(dimension.getHeight() / 1.3));
        setMinimumSize(getSize());
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        try{
            setIconImage(ImageIO.read(new File("./image/icon.png")));
        }
        catch (Exception ex){}
        initComponent();
        setVisible(true);
    }

    public static MainWindow getInstance(){
        return InnerClass.instance;
    }

    public void addCarburante(String carburante){
        if (carburante != null && !carburante.isBlank()){
            carburanti.add(carburante);
        }
    }

    private void search(boolean filtrer, boolean sortAsc) {
        SwingWorker<Void, Void> worker = new SwingWorker<>() {
            @Override
            protected Void doInBackground() {
                SwingUtilities.invokeLater(() -> {
                    buttonSearch.setEnabled(false);
                    setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    treeView.setCursor(new Cursor(Cursor.WAIT_CURSOR));
                    progressBar.setVisible(true);
                });

                try{
                    final String citta = cittaTextField.getText().trim();
                    RequestMessage requestMessage = new RequestMessage();
                    requestMessage.setRadius((short)radiusSlider.getValue());
                    requestMessage.setPoints(GeolocalizzazioneService.getInstance().getCoordianteFromCitta(citta));

                    final ResponseMessage responseMessage = BenzinaiService.getInstance().getBenzinai(requestMessage);
                    if (responseMessage.isSuccess()){
                        treeView.removeAll();

                        final Set<Benzinaio> results = responseMessage.getResults();
                        if (filtrer && !carburantiSelected.isEmpty()){
                            results.removeIf(e -> {
                                Set<Carburante> carburanti = e.getFuels();
                                carburanti.removeIf(el -> !carburantiSelected.contains(el.getName()));
                                return carburanti.isEmpty();
                            });
                            treeViewModel.setRoot(new DefaultMutableTreeNode("Benzinai (" + results.size() + ")", true));
                            treeViewModel.addBenzinai(results, sortAsc);
                            treeView.expandPath(new TreePath(treeViewModel.getRoot()));
                        }
                        else{
                            panelCheckBoxCarburanti.removeAll();
                            carburanti.clear();
                            carburantiSelected.clear();
                            treeViewModel.setRoot(new DefaultMutableTreeNode("Benzinai (" + results.size() + ")", true));
                            treeViewModel.addBenzinai(results, sortAsc);
                            treeView.expandPath(new TreePath(treeViewModel.getRoot()));
                            addCheckBoxCarburanti();
                        }

                        AutoCompleteService.getInstance().add(citta);
                        if (!scrollPanePanelCheckBox.isVisible())
                            scrollPanePanelCheckBox.setVisible(true);
                    }
                    else{
                        throw new IllegalArgumentException("Errore durante la ricerca dei distributori");
                    }
                }
                catch (IllegalArgumentException ex){
                    JOptionPane.showMessageDialog(null, ex.getMessage(), "Errore",JOptionPane.ERROR_MESSAGE);
                }
                catch (Exception ex){
                    JOptionPane.showMessageDialog(null, "Errore durante la ricerca dei distrubutori", "Errore",JOptionPane.ERROR_MESSAGE);
                }
                return null;
            }

            @Override
            protected void done() {
                SwingUtilities.invokeLater(() -> {
                    buttonSearch.setEnabled(true);
                    setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                    treeView.setCursor(new Cursor(Cursor.HAND_CURSOR));
                    progressBar.setVisible(false);
                });
            }
        };
        worker.execute();
    }

    private void addCheckBoxCarburanti(){
        Cursor cursor = new Cursor(Cursor.HAND_CURSOR);
        carburanti.forEach(e -> {
            JCheckBox checkBox = new JCheckBox(e);
            checkBox.setFont(DEFAULT_FONT);
            checkBox.setCursor(cursor);
            checkBox.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if (checkBox.isSelected())
                        carburantiSelected.add(checkBox.getText());
                    else
                        carburantiSelected.remove(checkBox.getText());
                }
            });
            panelCheckBoxCarburanti.add(checkBox);
        });
        carburanti.clear();
    }

    private void initComponent(){
        Cursor handCursor = new Cursor(Cursor.HAND_CURSOR);
        JPanel panel = new JPanel(new GridBagLayout());
        JPanel searchInputPanel = new JPanel(new GridBagLayout());
        JPanel radioButtoPanel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        Label labelSlider = new Label("5 km");
        ButtonGroup radioButtonGroup = new ButtonGroup();
        JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, new JScrollPane(panel), new JScrollPane(treeView));

        scrollPanePanelCheckBox.setVisible(false);

        radioButtonAsc.setFont(DEFAULT_FONT);
        radioButtonAsc.setCursor(handCursor);
        radioButtonAsc.setSelected(true);

        radioButtonDesc.setFont(DEFAULT_FONT);
        radioButtonDesc.setCursor(handCursor);

        radioButtonGroup.add(radioButtonAsc);
        radioButtonGroup.add(radioButtonDesc);

        progressBar.setString("Ricerca distributori in corso. . .");
        progressBar.setIndeterminate(true);
        progressBar.setStringPainted(true);
        progressBar.setVisible(false);
        progressBar.setFont(DEFAULT_FONT);

        buttonSearch.setCursor(handCursor);
        buttonSearch.setFont(DEFAULT_FONT);

        radiusSlider.setCursor(handCursor);

        treeView.setFont(DEFAULT_FONT);
        treeView.setCursor(handCursor);

        buttonSearch.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                search(!carburantiSelected.isEmpty(), radioButtonAsc.isSelected());
            }
        });

        radiusSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                labelSlider.setText(radiusSlider.getValue() + " km");
            }
        });

        treeView.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    Object obj = treeView.getPathForLocation(e.getX(), e.getY());
                    if (obj instanceof TreePath treePath){
                        Object value = treePath.getLastPathComponent();
                        if (value != null){
                            String v = value.toString();
                            if (v.startsWith("Indirizzo") && JOptionPane.showConfirmDialog(null, "Hai cliccato sull' indirizzo di questo distributore, sarai reindirizzato verso una pagina di Google Maps sul brawser, continuare?", "Sei sicuro?", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION){
                                try{
                                    Desktop.getDesktop().browse(new URI("https://www.google.com/maps?q=" + v.replace(" ", "%20")));
                                }
                                catch (Exception ex){
                                    JOptionPane.showMessageDialog(null, "Errore durante il reindirizzamento", "Errore", JOptionPane.ERROR_MESSAGE);
                                }
                            }
                        }
                    }
                }
            }
        });

        constraints.insets = new Insets(7,15,7,15);
        constraints.anchor = GridBagConstraints.NORTHWEST;

        searchInputPanel.add(new Label("Citta o indirizzo"), constraints);

        constraints.gridy = 1;
        searchInputPanel.add(cittaTextField, constraints);

        constraints.gridy = 2;
        constraints.fill = GridBagConstraints.NONE;
        searchInputPanel.add(new Label("Raggio di ricerca"),constraints);

        constraints.gridy = 3;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        searchInputPanel.add(radiusSlider, constraints);

        constraints.fill = GridBagConstraints.NONE;
        searchInputPanel.add(labelSlider, constraints);

        constraints.gridy = 4;
        searchInputPanel.add(new Label("Ordinamento"), constraints);

        constraints.gridy = 5;
        radioButtoPanel.add(radioButtonAsc, constraints);

        constraints.gridy = 6;
        radioButtoPanel.add(radioButtonDesc, constraints);

        constraints.gridy = 7;
        searchInputPanel.add(radioButtoPanel, constraints);

        constraints.gridx = 8;
        panel.add(searchInputPanel, constraints);

        constraints.gridy = 9;
        panel.add(new Label("Filtra carburanti"), constraints);

        constraints.gridy = 10;
        constraints.fill = GridBagConstraints.HORIZONTAL;
        panel.add(scrollPanePanelCheckBox, constraints);

        constraints.gridy = 11;
        panel.add(progressBar, constraints);

        constraints.gridy = 12;
        panel.add(buttonSearch, constraints);

        add(splitPane, BorderLayout.CENTER);
    }
}
