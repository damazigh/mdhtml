package fr.amazing.converter.mdhtml.generator;

import fr.amazing.converter.mdhtml.component.Collapsible;
import fr.amazing.converter.mdhtml.component.ComponentFactory;
import fr.amazing.converter.mdhtml.component.GenType;
import fr.amazing.converter.mdhtml.component.SideNav;
import fr.amazing.converter.mdhtml.core.tree.TreeNode;
import fr.amazing.converter.mdhtml.utils.Utils;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Map;

public class SideNavGenerator extends AbstractGenerator<File, Extras> {
    private ComponentFactory cf = ComponentFactory.getInstance();
    private static SideNavGenerator sng;
    private static final Logger LOG = LoggerFactory.getLogger(SideNavGenerator.class);


    /**
     * @param node
     * @param opts: 1: Collapsible
     * @return
     */
    @Override
    public Pair<String, Extras> doGenerate(TreeNode<File> node, Object... opts) throws IOException, TemplateException {
        SideNav sn = this.cf.ofSideNav();
        Collapsible c = this.cf.ofCollapsible();
        boolean collapsible = false;
        if (opts != null && opts.length > 0) {
            collapsible = (boolean) opts[0];
        }
        int level = (Integer) opts[1];
        int childrenSize = (Integer) opts[2];
        if (collapsible) {
            String container = c.getContainer();
            String body = childrenSize > 0 ? c.getCollapsibleBody() : "<div class=\"collapsible-body\"><a href=\"#\">Missing content</a></div>";
            String header = this.process(c.getCollapsibleHeader(), "collapsible-header", this.ofUniqueEntryMap("text", FilenameUtils.removeExtension(node.getContent().getName())));
            container = this.process(container, "collapsible-container", this.ofMap(new String[]{"header", "body"}, new String[]{header, body}));
            return Pair.of(container, Extras.of(this.buildName(node, true), this.buildName(node, false), (Integer) opts[1]));
        } else {
            String container = level > 0 ? sn.getLinkAsDiv() : sn.getLink();
            String href = Utils.buildFileName(node.getContent(), "html");
            String link = this.process(container, "sidenav-link", this.ofMap(new String [] {"content", "link"}, new String [] {FilenameUtils.removeExtension(node.getContent().getName()), href}));
            return Pair.of(link, Extras.of(this.buildName(node, true), this.buildName(node, false), level));
        }
    }

    @Override
    public String doGenerate(GenType type, String key, String content) {
        if(!type.equals(GenType.SIDENAV)) {
            throw new IllegalArgumentException("Cannot generate anything else than sidenav from SideNavGenerator !");
        }
        try {
            String staticPart = this.process(this.cf.ofSideNav().getStaticPart(), "sideNav_staticPart", this.ofMap(new String [] {"subheader", "gitlabrepo"}, new String[] {this.context.getOptions().getSubHeader(), this.context.getOptions().getGitlabRepo()}));
            return  this.process(this.cf.ofSideNav().getContainer(), "sideNav_full", this.ofMap(new String[] {key, "static" }, new String[] {content, staticPart}));
        } catch (IOException | TemplateException e) {
            LOG.error("Error when generating", e);
        }
        return "<h3>Error occured</h3>";
    }

    public static SideNavGenerator getInstance() {
        if (sng == null) {
            sng = new SideNavGenerator();
        }
        return sng;
    }

    public SideNavGenerator() {
        super();
    }

    private String buildName(TreeNode<File> tn, boolean isParent) {
        return isParent ?
                (tn.getParent() != null && tn.getParent().getContent() != null ?
                        StringUtils.remove(tn.getParent().getContent().getAbsolutePath(), File.separator) : "root")
                : StringUtils.remove(tn.getContent().getAbsolutePath(), File.separator);
    }
}
