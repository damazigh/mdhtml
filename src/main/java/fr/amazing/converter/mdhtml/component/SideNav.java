package fr.amazing.converter.mdhtml.component;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class SideNav implements Serializable {
    private String wavyLink;
    private String divider;
    private String subheader;
    private String link;
    private String container;
    private String linkAsDiv;
    private String staticPart;
}
