package fr.amazing.converter.mdhtml;

import fr.amazing.converter.mdhtml.assertions.FileAssertion;
import fr.amazing.converter.mdhtml.config.*;
import fr.amazing.converter.mdhtml.core.tree.FileTreeBuilder;
import fr.amazing.converter.mdhtml.core.tree.ITreeBuilder;
import fr.amazing.converter.mdhtml.core.tree.TreeNode;
import fr.amazing.converter.mdhtml.generator.IGenerator;
import fr.amazing.converter.mdhtml.generator.PageGenerator;
import fr.amazing.converter.mdhtml.parser.IParser;
import fr.amazing.converter.mdhtml.parser.md.NavigationParser;
import fr.amazing.converter.mdhtml.utils.AsciiArt;
import fr.amazing.converter.mdhtml.writer.IWriter;
import fr.amazing.converter.mdhtml.writer.impl.PageWriter;
import fr.amazing.converter.mdhtml.writer.impl.SideNavWriter;
import org.apache.commons.io.DirectoryWalker;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class Facade {
    private ILoader loader = new LoaderImpl();
    private ITreeBuilder<File> ftb = new FileTreeBuilder();
    private IParser parser = new NavigationParser();
    private IWriter sideNavWriter = SideNavWriter.getInstance();
    private Context context = Context.getInstance();
    private IGenerator pageGenerator = PageGenerator.getInstance();
    private IWriter pageWriter = new PageWriter();
    private static final Logger LOG = LoggerFactory.getLogger(Facade.class);
    private AsciiArt asciiArt = AsciiArt.getInstance();
    private static Facade instance;

    /**
     * Startup method for generation
     * @param dir
     * @throws Exception
     */
    public void doGenerate(String dir) throws Exception{
        LOG.info("Beginning generation of html static site ");
        if (FileAssertion.assertExists(dir + "config.yml")) {
            LOG.warn("Cannot generate static site without config.yml file. use mdhtml init to generate config file.");
            return;
        }
        LOG.info("Loading configuration form : {}", dir + File.separator + "config.yml");
        this.loader.load(dir + File.separator + "config.yml");
        LOG.info("Prepare output file dir");
        this.doPrepare(dir);
        LOG.info("Construction of file tree representation");
        TreeNode<File> tree = ftb.build(new File(dir), null);
        LOG.info("Parsing tree for navigation sidenav");
        var htmlNode = parser.parse(tree);
        LOG.info("Regrouping html node hierarchically and writing to temp directory");
        this.sideNavWriter.write(htmlNode);
        var pair = this.pageGenerator.doGenerate(tree, new Object[] {});
        LOG.info("Page generation begins...");
        this.pageWriter.write(pair.getValue());
        LOG.info("Cleaning temp and work directory");
        this.cleanup();
        String output = new StringBuilder(context.getWorkDir()).append(context.getOptions().getOutputDir()).toString();
        LOG.info("Conversion process terminated, Generated file are under {}", output);
        LOG.info(asciiArt.defaultDraw("Good bye !", false));
    }

    public static Facade getInstance() {
        if (instance == null) {
            instance = new Facade();
        }
        return instance;
    }

    private void doPrepare(String dir) throws IOException {
        File tempDir = new File(new StringBuilder(dir).append(File.separator).append(context.getOptions().getTempDir()).toString());
        this.context.getOptions().setTempDir(tempDir.getAbsolutePath());
        this.context.setWorkDir(dir);
        File generateddir =  new File(new StringBuilder(dir).append(File.separator).append(context.getOptions().getOutputDir()).toString());
        if (generateddir.exists()) {
            generateddir.delete();
        }
        if (tempDir.exists()) {
            FileUtils.cleanDirectory(tempDir);
        } else {
            tempDir.mkdir();
            LOG.info("temp dire created under {}", tempDir.getAbsolutePath());
        }

        final String outputdir = new StringBuilder(dir).append(File.separator)
                .append(this.context.getOptions().getOutputDir()).toString();
        File outputFile = new File(outputdir);
        if (outputFile.exists()) {
            LOG.info("Output directory already exists - {}", outputdir);
            FileUtils.cleanDirectory(outputFile);
        } else {
            LOG.info("{} created", outputdir);
            outputFile.mkdir();
        }
        LOG.info("Copy static resources (css - javascript)");
        copyResFromClasspath(outputdir, "templates/style.css", "css", "style.css");
        copyResFromClasspath(outputdir, "templates/js/init.js", "js", "init.js");
        copyResFromClasspath(outputdir, "templates/js/chance.js", "js", "chance.js");
    }

    private void copyResFromClasspath(String dest,  String src, String parentDir, String fileName) throws IOException{
        File parent = new File(new StringBuilder(dest).append(File.separator).append(parentDir).toString());
        parent.mkdir();
        File destFile = new File(new StringBuilder(dest).append(File.separator).append(parentDir).append(File.separator).append(fileName).toString());
        destFile.createNewFile();
        URL url = this.getClass().getClassLoader().getResource(src);
        FileUtils.copyURLToFile(url, destFile);
        LOG.debug("File created under {}", destFile.getAbsolutePath());
    }

    public void doInit(String dir, String[] args) {
        try {
            FileAssertion.assertDirExist(new File(dir));
            Map<OptionKey, String> props = this.parseArgs(args);
            Option options = this.loader.loadYmlFromClasspath("default.yml", Option.class);
            if (props != null) {
                props.entrySet().forEach(entry -> {
                    switch (entry.getKey()) {
                        case REPOSITORY:
                            LOG.info("Setting gitlab repository url to {}", entry.getValue());
                            options.setGitlabRepo(entry.getValue());
                            break;
                        case PROJECT_NAME:
                            LOG.info("Overriding default project name with {}", entry.getValue());
                            options.setProjectName(entry.getValue());
                            break;
                        case COLOR:
                            LOG.info("Overriding default color name with {}", entry.getValue());
                            options.setNavcolor(entry.getValue());
                            break;
                        case SUBHEADER:
                            LOG.info("Overriding default sub-header phrase name with {}", entry.getValue());
                            options.setSubHeader(entry.getValue());
                            break;
                    }
                });
                this.pageWriter.writeYmlFileFromObject(options, "config.yml", dir);
                LOG.info("config file generated at : {}", dir + File.separator + "config.yml");
            }
        }catch (Exception e) {
            LOG.error("Fatal error occured :(", e);
        }
    }


    private Map<OptionKey, String> parseArgs(String... args) {
        char delimiter = ':';
        Map<OptionKey, String> props = new HashMap<>();
        if (args != null && args.length > 0) {
            for (String arg : args) {
                String [] temp = StringUtils.split(arg, delimiter);
                if (temp.length == 2 || temp.length == 3) {
                    OptionKey key = OptionKey.reverse(temp[0]);
                    if (key != null) {
                        props.put(key, temp.length ==2 ? temp[1] : new StringBuilder(temp[1]).append(":").append(temp[2]).toString());
                    } else {
                        LOG.warn("Unknown property {}", temp[0]);
                    }
                }
                else {
                    LOG.warn("Malformed argument detected at {} - expected format key:value", arg);
                }
            }
        }
        return props;
    }

    private void cleanup() {
        this.deleteDir(this.context.getOptions().getTempDir());
    }

    private void deleteDir(String path) {
        File tempDir = new File(path);
        LOG.info(path);
        if (tempDir.exists() && tempDir.isDirectory()) {
            LOG.debug("delete dir {}", tempDir.getAbsolutePath());
            try {
                FileUtils.cleanDirectory(tempDir);
                FileUtils.deleteDirectory(tempDir);
            } catch(IOException e) {
                LOG.warn("An exception occured while trying to delete this directory {}", tempDir.getAbsolutePath());
            }
        }
    }
}
