package fr.amazing.converter.mdhtml.component;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
public class Collapsible implements Serializable {
    private String collapsibleHeaderImg;
    private String collapsibleHeader;
    private String collapsibleBody;
    private String container;
}
