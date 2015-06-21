package neeedo.imimaprx.htw.de.neeedo.entities.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import org.simpleframework.xml.Root;

import java.io.Serializable;


@Root(name = "attributes")
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Attributes implements Serializable {



}
