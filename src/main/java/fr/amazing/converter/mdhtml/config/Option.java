package fr.amazing.converter.mdhtml.config;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

@Getter
@Setter
public class Option {
    @Getter(AccessLevel.NONE)
    private String navcolor;
    private String inputDir;
    private String outputDir;
    private String tempDir;
    private String [] extensions;
    private String [] removePrefixes;
    @Getter(AccessLevel.NONE)
    private String projectName;
    @Getter(AccessLevel.NONE)
    private String subHeader;
    @Getter(AccessLevel.NONE)
    private String gitlabRepo;
    @Getter(AccessLevel.NONE)
    private String homePageMd;
    @Getter(AccessLevel.NONE)
    private String homePage;

    public String getProjectName() {
        return StringUtils.isBlank(this.projectName) ? DefaultOptions.PROJECT_NAME: this.projectName;
    }
    public String getnavColor() {
        return StringUtils.isBlank(this.navcolor) ? DefaultOptions.NAV_COLOR : this.navcolor;
    }
    public String getSubHeader() {
        return StringUtils.isBlank(this.subHeader) ? DefaultOptions.SUB_HEADER: this.subHeader;
    }
    public String getGitlabRepo() {
        return StringUtils.isBlank(this.gitlabRepo) ? DefaultOptions.GITLAB_REPO: this.gitlabRepo;
    }
    public String getHomePageMd() {
        return StringUtils.isBlank(this.homePageMd) ? DefaultOptions.HOME_PAGE_MD: this.homePageMd;
    }
    public String getHomePage() {
        return StringUtils.isBlank(this.homePage) ? DefaultOptions.HOME_PAGE: this.homePage;
    }
    private static class DefaultOptions {
        static final String NAV_COLOR = "#1e88e5";
        static final String PROJECT_NAME = "Generated by mdhtml";
        static final String SUB_HEADER = "Navigation menu";
        static final String GITLAB_REPO = "#NOT_DEFINED";
        static final String HOME_PAGE_MD = "README";
        static final String HOME_PAGE = "index";
    }
}
