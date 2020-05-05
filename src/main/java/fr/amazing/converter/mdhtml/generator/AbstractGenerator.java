package fr.amazing.converter.mdhtml.generator;

import fr.amazing.converter.mdhtml.Start;
import fr.amazing.converter.mdhtml.component.GenType;
import fr.amazing.converter.mdhtml.config.Context;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

public abstract class AbstractGenerator<T, W> implements IGenerator<T, W> {
    protected Configuration cfg;
    protected Context context = Context.getInstance();
    private static final Logger LOG = LoggerFactory.getLogger(AbstractGenerator.class);
    private void init() {
        cfg = new Configuration(Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        cfg.setClassForTemplateLoading(Start.class, "/templates");

        // Some other recommended settings:
        cfg.setDefaultEncoding("UTF-8");
        cfg.setLocale(Locale.FRANCE);
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
    }

    protected String process(String templateAsStr, String templateName, Map<String, String> props) throws IOException, TemplateException {
        Template template = new Template(templateName, new StringReader(templateAsStr), this.cfg);
        return this.process(template, props);
    }
    protected String process(Template template, Map<String, String> props) throws IOException, TemplateException {
        Writer out = new StringWriter();
        template.process(props, out);
        return out.toString();
    }

    protected Map<String, String> ofMap(String[] keys, String[] values) {
        Map<String, String> map = new HashMap<>();
        for(int i = 0; i < keys.length; i++) map.put(keys[i], values[i]);
        return map;
    }
    protected Map<String, String> ofUniqueEntryMap(String k, String v) {
        Map<String, String> map = new HashMap<>();
        map.put(k, v);
        return map;
    }

    @Override
    public String doGenerate(GenType type, String key, String content) {
        throw new IllegalStateException("Not implemented");
    }
    public AbstractGenerator() {
        init();
    }

    @Override
    public String doGenerate(String template, String content, String key) {
        try {
            LOG.debug("generating - template : {} - body {}", template, content);
            return this.process(template, "unknown-template", this.ofUniqueEntryMap(key, content));
        } catch(Exception e) {
            LOG.error("unexpected error when generating body", e);
        }
        return "<h3>An error occured when generating</h3>";
    }
    protected String readSideNavFromTemp() throws FileNotFoundException, IOException {
        FileReader fr = new FileReader(new File(new StringBuilder(this.context.getOptions().getTempDir()).append(File.separator).append("sideNav_temp.html").toString()));
        String res = IOUtils.readLines(fr).stream().collect(Collectors.joining());
        fr.close();
        return res;
    }
}
