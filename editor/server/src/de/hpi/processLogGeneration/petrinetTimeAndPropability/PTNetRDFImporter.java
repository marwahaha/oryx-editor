package de.hpi.processLogGeneration.petrinetTimeAndPropability;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

public class PTNetRDFImporter extends de.hpi.PTnet.serialization.PTNetRDFImporter {

	public PTNetRDFImporter(Document doc) {
		super(doc);
	}
	
	@Override
	protected void addTransition(Node node, ImportContext c) {
		LabeledTransition t = new LabeledTransition();
		c.net.getTransitions().add(t);
		c.objects.put(getResourceId(node), t);
		
		for (Node n=node.getFirstChild(); n != null; n=n.getNextSibling()) {
			if (n instanceof Text) continue;
			String attribute = n.getNodeName().substring(n.getNodeName().indexOf(':')+1);

			if (attribute.equals("title")) {
				t.setLabel(getContent(n));
			} else if (attribute.equals("outgoing")) {
				c.connections.put(getResourceId(getAttributeValue(n, "rdf:resource")), t);
			} else if (attribute.equals("propability")) {
				String content = getContent(n);
				if (content != null) t.setPropability(Integer.parseInt(content));
			} else if (attribute.equals("time")) {
				String content = getContent(n);
				if (content != null) t.setTime(Integer.parseInt(content));
			}
		}
		if (t.getId() == null)
			t.setId(getResourceId(node));
	}

	protected void addSilentTransition(Node node, ImportContext c) {
		SilentTransition t = new SilentTransition();
		c.net.getTransitions().add(t);
		c.objects.put(getResourceId(node), t);
		
		for (Node n=node.getFirstChild(); n != null; n=n.getNextSibling()) {
			if (n instanceof Text) continue;
			String attribute = n.getNodeName().substring(n.getNodeName().indexOf(':')+1);
			
			if (attribute.equals("outgoing")) {
				c.connections.put(getResourceId(getAttributeValue(n, "rdf:resource")), t);
			} else if (attribute.equals("propability")) {
				String content = getContent(n);
				if (content != null) t.setPropability(Integer.parseInt(content));
			}
		}
		if (t.getId() == null)
			t.setId(getResourceId(node));
	}
	
	private String getAttributeValue(Node node, String attribute) {
		Node item = node.getAttributes().getNamedItem(attribute);
		if (item != null)
			return item.getNodeValue();
		else
			return null;
	}
}
