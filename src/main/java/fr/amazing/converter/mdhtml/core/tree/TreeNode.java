package fr.amazing.converter.mdhtml.core.tree;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TreeNode<T> {
    TreeNode<T> parent;
    @Getter(AccessLevel.NONE)
    List<TreeNode<T>> children;
    T content;

    public static <T> TreeNode<T> of(T content) {
        TreeNode<T> t = new TreeNode<>();
        t.setContent(content);
        return t;
    }
    public List<TreeNode<T>> getChildren() {
        if (this.children == null) {
            children = new ArrayList<>();
        }
        return children;
    }

}
