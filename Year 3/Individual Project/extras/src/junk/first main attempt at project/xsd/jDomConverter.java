package xsd;
import org.jdom.Element;
import org.jdom.input.DOMBuilder;

/*
 * Created< n 16-Feb-2004
 *
 */

/**
 * @author ss401
 *
 */

public class jDomConverter {
		
	public static Element toJDomElement(org.w3c.dom.Element domElem){
		DOMBuilder builder = new DOMBuilder();
		return builder.build(domElem);
	}
}
