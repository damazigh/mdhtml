package fr.amazing.converter.mdhtml.writer.impl;

import fr.amazing.converter.mdhtml.component.GenType;
import fr.amazing.converter.mdhtml.config.Context;
import fr.amazing.converter.mdhtml.generator.Extras;
import fr.amazing.converter.mdhtml.generator.IGenerator;
import fr.amazing.converter.mdhtml.generator.SideNavGenerator;
import fr.amazing.converter.mdhtml.writer.IWriter;
import org.apache.commons.lang3.tuple.Pair;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SideNavWriter implements IWriter<List<Pair<String, Extras>>> {
    private int max;
    private IGenerator generator = SideNavGenerator.getInstance();
    private Context context = Context.getInstance();
    private static SideNavWriter instance;
    @Override
    public void write(List<Pair<String, Extras>> input, Object... opts) {
        // first of all : regroup item hierarchically
        if (input == null) {
            throw new IllegalArgumentException("Cannot write from an empty list");
        }

        // calc deepest level
        this.calc(input);

        // loop on every level and set contento
        for (int i = max; i >= 0; i--) {
            // list of deepest node by level
            var groupedDeepestNode = this.findDeepestNode(input, i).stream()
                    .collect(Collectors.groupingBy(p -> p.getRight().getParent()));
            // set max level - 1 content
            this.setContent(input, this.joinSiblingNode(groupedDeepestNode));
        }

        // get top node
        String fullHtml = this.generator.doGenerate(GenType.SIDENAV, "content", this.findTopNode(input));
        this.writeContentToFile(fullHtml, "sideNav_temp.html", context.getOptions().getTempDir());
    }

    private String findTopNode(List<Pair<String, Extras>> htmlNode) {
        return htmlNode.stream().filter(x -> x.getRight().getLevel() == 0).map(x -> x.getLeft()).collect(Collectors.joining());
    }
    private void calc(List<Pair<String, Extras>> htmlNode) {
        this.max = htmlNode.stream()
                .max(Comparator.comparing(e -> e.getRight().getLevel()))
                .map(x -> x.getRight().getLevel()).orElse(1);
    }
    private List<Pair<String, Extras>> findDeepestNode(List<Pair<String, Extras>> htmlNode, int level) {
        return htmlNode.stream().filter(x -> x.getRight().getLevel() == (level == -1 ? this.max : level)).collect(Collectors.toList());
    }

    private List<Pair<String, String>> joinSiblingNode(Map<String, List<Pair<String, Extras>>> map) {
        List<Pair<String, String>> result = new ArrayList<>();
        for(var entry : map.entrySet()) {
            String html = entry.getValue().stream().map(x -> x.getLeft()).collect(Collectors.joining());
            result.add(Pair.of(entry.getKey(), html));
        }
        return result;
    }

    private void setContent(List<Pair<String, Extras>> initialInput, List<Pair<String, String>> childNodeHtml) {
        childNodeHtml.stream().forEach(p -> {
            // find node
            var node = this.findPair(initialInput, p.getKey());
            if (node != null) {
                // generator
                initialInput.add(Pair.of(this.generator.doGenerate(node.getLeft(), p.getRight(), "content"), node.getValue()));
                initialInput.remove(node);
            }

        });
    }

    private Pair<String, Extras> findPair(List<Pair<String, Extras>> initialInput, String name) {
        return initialInput.stream().filter(e -> e.getRight().getActual().equals(name)).findFirst().orElse(null);
    }
    public static SideNavWriter getInstance() {
        if (instance == null) {
            instance = new SideNavWriter();
        }
        return instance;
    }
}
