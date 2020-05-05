package fr.amazing.converter.mdhtml.config;

import lombok.Getter;
import lombok.Setter;
import org.commonmark.Extension;
import org.commonmark.ext.gfm.tables.TablesExtension;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class Context {
    private static Context instance;
    @Getter
    private Option options;
    @Getter
    @Setter
    private String workDir;
    @Getter
    private Parser parser;
    @Getter
    private HtmlRenderer renderer;

    private static final Logger LOG = LoggerFactory.getLogger(Context.class);
    private Context() {
        // private constructor
        this.configure();
    }

    public static Context getInstance() {
        if (instance == null) {
            instance = new Context();
        }
        return instance;
    }

    public void setOption(Option opt) {
        if (this.options == null) {
            this.options = opt;
        } else {
            throw new IllegalStateException("Options are already set !");
        }
    }

    private void configure() {
        LOG.info("Configuring parser and html renderer");
        List<Extension> extensions = Arrays.asList(TablesExtension.create());
        LOG.debug("Markdown table extension loaded");
        this.parser = Parser.builder().extensions(extensions).build();
        this.renderer = HtmlRenderer.builder().extensions(extensions).build();
        LOG.debug("html renderer and parser configured with markdown table extension");
    }
}
