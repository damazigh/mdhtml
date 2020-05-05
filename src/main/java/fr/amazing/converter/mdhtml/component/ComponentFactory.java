package fr.amazing.converter.mdhtml.component;

import fr.amazing.converter.mdhtml.config.ILoader;
import fr.amazing.converter.mdhtml.config.LoaderImpl;
import org.apache.commons.lang3.SerializationUtils;
import org.yaml.snakeyaml.Yaml;

public class ComponentFactory {
    private ComponentWrapper cw;
    private ILoader loader = new LoaderImpl();
    private static ComponentFactory cf;
    private ComponentFactory() {
        this.cw = this.loader.loadYmlFromClasspath("components.yml", ComponentWrapper.class);
    }

    public static ComponentFactory getInstance() {
        if (cf == null) {
            cf = new ComponentFactory();
        }
        return cf;
    }

    public SideNav ofSideNav() {
        return SerializationUtils.clone(this.cw.getComponents().getSidenav());
    }

    public Collapsible ofCollapsible() {
        return SerializationUtils.clone(this.cw.getComponents().getCollapsible());
    }
}
