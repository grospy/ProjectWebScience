package application;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import application.Graph.Edge;


public class EdgeToXML {
	
	private Graph graph;
	
	public EdgeToXML(Graph g) {
		this.graph = g;
	}
	
	public void loadWebsiteDataFromFile() {
		File file = new File("edgesXML.xml");
		try {
			JAXBContext context = JAXBContext.newInstance(EdgeWrapper.class);
			Unmarshaller um = context.createUnmarshaller();
			EdgeWrapper wrapper = (EdgeWrapper) um.unmarshal(file);
	
			graph.clearStorage();
	
			ArrayList<Edge> tempList = new ArrayList<Edge>();
			tempList.addAll(wrapper.getEdgeList());
			for (Edge e : tempList) {
				// TO DO 
				System.out.println(e);
			}
		} catch (Exception e) {
			System.out.println("Error Loading");
		}
	}

	public void saveWebsiteDataToFile() {
//		File file = new File("edgesXML.xml");
//		try {
//			JAXBContext context = JAXBContext.newInstance(EdgeWrapper.class);
//			Marshaller m = context.createMarshaller();
//			m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//			EdgeWrapper wrapper = new EdgeWrapper();
//			wrapper.setEdgeList(graph.getEdges());
////			m.marshal(wrapper, file);
//			m.marshal(wrapper, System.out);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//			System.out.println("Error saving file");
//		}
		
//		XStream xStream = new XStream();
				
//		String xml = xStream.toXML(graph.getEdges());
				
//		System.out.println("Serialized XML: /n"+xml);
	}
	
	@XmlRootElement(name = "Edges")
	static class EdgeWrapper {
	
		private List<Edge> edgeList;
		
		@XmlElement(name = "Edge")
		public List<Edge> getEdgeList() {
			return edgeList;
		}
		
		public void setEdgeList(List<Edge> edgeList) {
			this.edgeList = edgeList;
		}
	}
}
