package fr.amazing.converter.mdhtml.core.tree;

import fr.amazing.converter.mdhtml.assertions.FileAssertion;
import fr.amazing.converter.mdhtml.config.Context;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Arrays;

public class FileTreeBuilder implements ITreeBuilder<File> {
    private Context context = Context.getInstance();

    public TreeNode<File> build(File content, TreeNode<File> t) {
        FileAssertion.assertExists(content);
        TreeNode<File> tree = t == null ? new TreeNode<>() : t;
        tree.setContent(content);
        Arrays.stream(content.listFiles()).filter(f -> f.exists() && this.matchCriteria(f)).forEach(file -> {
            TreeNode<File> actual = TreeNode.of(file);
            actual.setParent(tree);
            tree.getChildren().add(actual);
            if (file.isDirectory()) {
                build(file, actual);
            }
        });
        return tree;
    }

    private boolean matchCriteria(File file) {
        String name = file.getName();
        return (file.isDirectory() && this.matchDirCriteria(name)) || name.contains(".") &&
                Arrays.stream(this.context.getOptions().getExtensions())
                        .anyMatch(ext -> StringUtils.equalsIgnoreCase(name.substring(name.lastIndexOf('.') + 1), ext));
    }

    private boolean matchDirCriteria(String dirname) {
        return !StringUtils.equals(dirname, context.getOptions().getTempDir()) && ! StringUtils.equals(dirname, context.getOptions().getOutputDir());
    }
}