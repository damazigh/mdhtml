package fr.amazing.converter.mdhtml.core.tree;

public interface ITreeBuilder<T> {
    TreeNode<T> build(T content, TreeNode<T> t);
}
