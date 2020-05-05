package fr.amazing.converter.mdhtml.generator;

import fr.amazing.converter.mdhtml.component.GenType;
import fr.amazing.converter.mdhtml.core.tree.TreeNode;
import fr.amazing.converter.mdhtml.utils.Utils;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;

public class PageGenerator extends AbstractGenerator<File, List<File>> {
private static final Logger LOG = LoggerFactory.getLogger(PageGenerator.class);
private static IGenerator instance;

    @Override
    public Pair<String, List<File>> doGenerate(TreeNode<File> node, Object... opts) throws IOException, TemplateException {
        PriorityQueue<TreeNode<File>> queue = new PriorityQueue<>(Utils.tnOfFileComparator());
        List<File> toProcess = new ArrayList<>();
        queue.add(node);
        while (!queue.isEmpty()) {
            TreeNode<File> front = queue.poll();
            if (front != null && front.getContent() != null && front.getContent().isFile() && front.getContent().exists()) {
                toProcess.add(front.getContent());
            }
            if (front.getChildren().size() > 0)
                queue.addAll(front.getChildren());
        }
        return Pair.of("pageToProcess", toProcess);
    }

    public static IGenerator getInstance() {
        if (instance == null) {
            instance = new PageGenerator();
        }
        return instance;
    }

    @Override
    public String doGenerate(GenType type, String key, String content) {
        try {
            String[] keys = new String[] {"skeleton_sideNav", key, "navcolor", "projectName"};
            String[] values = new String [] {this.readSideNavFromTemp(), content, this.context.getOptions().getnavColor(), this.context.getOptions().getProjectName()};
            Template t = this.cfg.getTemplate("skeleton.html");
            return this.process(t, this.ofMap(keys, values));
        } catch (IOException e) {
            LOG.error("error", e);
        } catch (TemplateException e) {
            LOG.error("error", e);
        }
        return "<h3>error occured at generation</h3>";
    }
}
