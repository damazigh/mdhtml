package fr.amazing.converter.mdhtml.generator;

import fr.amazing.converter.mdhtml.component.GenType;
import fr.amazing.converter.mdhtml.core.tree.TreeNode;
import freemarker.template.TemplateException;
import org.apache.commons.lang3.tuple.Pair;

import java.io.IOException;

public interface IGenerator<T, W> {
    Pair<String, W> doGenerate(TreeNode<T> node, Object...opts) throws IOException, TemplateException;

    String doGenerate(String template, String content, String key);

    String doGenerate(GenType type, String key, String content);
}
