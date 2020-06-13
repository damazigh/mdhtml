package fr.amazing.converter.mdhtml.parser.md;

import fr.amazing.converter.mdhtml.core.tree.TreeNode;
import fr.amazing.converter.mdhtml.generator.Extras;
import fr.amazing.converter.mdhtml.generator.IGenerator;
import fr.amazing.converter.mdhtml.generator.SideNavGenerator;
import fr.amazing.converter.mdhtml.parser.IParser;
import fr.amazing.converter.mdhtml.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.PriorityQueue;
import java.util.stream.Collectors;

public class NavigationParser implements IParser<TreeNode<File>, List<Pair<String, Extras>>> {
    private final static Logger LOG = LoggerFactory.getLogger(NavigationParser.class);
    private IGenerator generator = SideNavGenerator.getInstance();

    public List<Pair<String, Extras>> parse(TreeNode<File> arg) {
        if (LOG.isDebugEnabled()) {
            LOG.debug("Navigation : generate side navigation menu from directory: {}", arg.getContent().getAbsolutePath());
        }
        PriorityQueue<TreeNode<File>> queue = new PriorityQueue<>(Utils.tnOfFileComparator());
        queue.add(arg);
        boolean first = true;
        int level = 0;
        int levelSize = 0;
        int nextLevelSize = 0;
        int index = 0;
        List<Pair<String, Extras>> htmlNode = new ArrayList<>();
        while (!queue.isEmpty()) {
            TreeNode<File> front = queue.poll();
            LOG.debug("{} node {} added to priority queue - index {} - levelSize: {} - actual level : {}", front.getChildren().size(), front.getChildren().size() > 1 ? "were" : "was", index, levelSize, level);
            queue.addAll(front.getChildren());

            if (!first) {
                LOG.debug("Processing node : {}", front.getContent().getAbsolutePath());
                index++;
                boolean collapsible = front.getContent().isDirectory();
                int childrenSize = front.getChildren().size();
                try {
                    Pair<String, Extras> pair = generator.doGenerate(front, new Object[]{collapsible, level, childrenSize});
                    htmlNode.add(pair);
                    LOG.debug("Generated html : {}",pair.getLeft());
                } catch (Exception e) {
                    LOG.error("an expected error occured when parsing and generating navigation element", e);
                }
                if (index == levelSize) {
                    levelSize = levelSize + nextLevelSize;
                    nextLevelSize = 0;
                    level++;
                } else {
                    nextLevelSize = nextLevelSize + front.getChildren().size();
                }

            } else {
            LOG.debug("Processing the root element (parent dir)");
            first = false;
            levelSize = front.getChildren().size();
        }
        }
        return htmlNode;
    }

}
