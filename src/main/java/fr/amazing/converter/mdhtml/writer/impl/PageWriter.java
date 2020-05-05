package fr.amazing.converter.mdhtml.writer.impl;

import fr.amazing.converter.mdhtml.component.GenType;
import fr.amazing.converter.mdhtml.config.Context;
import fr.amazing.converter.mdhtml.generator.IGenerator;
import fr.amazing.converter.mdhtml.generator.PageGenerator;
import fr.amazing.converter.mdhtml.utils.Utils;
import fr.amazing.converter.mdhtml.writer.IWriter;
import org.apache.commons.io.FileUtils;
import org.commonmark.node.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.List;

public class PageWriter implements IWriter<List<File>> {
    private Context ctx = Context.getInstance();
    private IGenerator generator = PageGenerator.getInstance();
    private static final Logger LOG = LoggerFactory.getLogger(PageWriter.class);
    @Override
    public void write(List<File> input, Object... opts) throws IOException {
        String pages = new StringBuilder(ctx.getWorkDir()).append(File.separator).append(File.separator).append(this.ctx.getOptions().getOutputDir()).append(File.separator).append("pages").toString();
        File pageDir = new File(pages);
        if (!pageDir.exists() || !pageDir.isDirectory()) {
            pageDir.mkdir();
        } else {
            FileUtils.cleanDirectory(pageDir);
        }
        if(input != null && input.size() > 0) {
            for(var f : input) {
                Reader in = null;
                Writer w = null;
                try {
                    in = new InputStreamReader(new FileInputStream(f), "UTF-8");
                    Node node = ctx.getParser().parseReader(in);
                    String converted = ctx.getRenderer().render(node);
                    final String fullhtml = this.generator.doGenerate(GenType.PAGE, "skeleton_content", converted);
                    final String name = Utils.buildFileName(f, "html");
                    this.writeContentToFile(fullhtml, name, pages);
                    LOG.info("Generated page {} in {}", name, f.getParent());
                } catch (FileNotFoundException e) {
                } finally {
                    if (in != null) {
                        in.close();
                    }
                    if (w != null) {
                        w.close();
                    }
                }
            };
        }
    }
}
