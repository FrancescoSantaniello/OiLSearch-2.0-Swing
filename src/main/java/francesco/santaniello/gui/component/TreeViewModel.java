package francesco.santaniello.gui.component;

import francesco.santaniello.gui.MainWindow;
import francesco.santaniello.model.Benzinaio;
import francesco.santaniello.model.Carburante;

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

public class TreeViewModel extends DefaultTreeModel {

    private final Set<Benzinaio> benzinai = new HashSet<>();

    public TreeViewModel(DefaultMutableTreeNode rootNode){
        super(rootNode);
    }

    public void addBenzinai(Set<Benzinaio> benzinai, boolean sortAsc){
        if (benzinai != null){
            this.benzinai.clear();
            this.benzinai.addAll(benzinai);
            benzinai.stream()
                    .sorted((a,b) -> Double.compare((sortAsc ? b : a).getFuels().stream().mapToDouble(Carburante::getPrice).max().getAsDouble(), (sortAsc ? a : b).getFuels().stream().mapToDouble(Carburante::getPrice).max().getAsDouble()))
                    .forEach((e) -> this.addBenzinaio(e, sortAsc));
        }
    }

    public Set<Benzinaio> getBenzinai(){
        return benzinai;
    }

    private void addBenzinaio(Benzinaio bezinaio, boolean sortAsc) {
        if (bezinaio != null){
            DefaultMutableTreeNode benzinaioNode = new DefaultMutableTreeNode(bezinaio.getName(), true);
            DefaultMutableTreeNode positionNode = new DefaultMutableTreeNode("Posizione", true);

            if (bezinaio.getLocation() != null){
                positionNode.add(new DefaultMutableTreeNode("Latitudine: " + bezinaio.getLocation().getLat()));
                positionNode.add(new DefaultMutableTreeNode("Longitudine: " + bezinaio.getLocation().getLng()));
            }
            positionNode.add(new DefaultMutableTreeNode("Indirizzo: " + bezinaio.getAddress()));
            benzinaioNode.add(positionNode);

            benzinaioNode.add(new DefaultMutableTreeNode("Brand: " + bezinaio.getBrand()));
            if (bezinaio.getInsertDate() != null){
                benzinaioNode.add(new DefaultMutableTreeNode("Data d' inserimento: " + OffsetDateTime.parse(bezinaio.getInsertDate(), DateTimeFormatter.ISO_OFFSET_DATE_TIME).format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))));
            }

            DefaultMutableTreeNode carburantiNode = new DefaultMutableTreeNode("Carburanti", true);
            bezinaio.getFuels()
                    .stream()
                    .sorted((a,b) -> sortAsc ? Float.compare(a.getPrice(), b.getPrice()) : Float.compare(b.getPrice(), a.getPrice()))
                    .forEach(e -> {
                        DefaultMutableTreeNode subNode = new DefaultMutableTreeNode(e.getName(), true);
                        subNode.add(new DefaultMutableTreeNode("Prezzo: " + e.getPrice() + " euro"));
                        subNode.add(new DefaultMutableTreeNode("Numero pompa: " + e.getFuelId()));
                        subNode.add(new DefaultMutableTreeNode(!e.isSelf() ? "Servito" : "Non servito"));
                        carburantiNode.add(subNode);
                        MainWindow.getInstance().addCarburante(e.getName());
                    });

            benzinaioNode.add(carburantiNode);
            insertNodeInto(benzinaioNode,(DefaultMutableTreeNode) getRoot(), 0);
        }
    }
}
