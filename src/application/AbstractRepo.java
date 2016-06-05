package application;

import javax.xml.stream.*;
import javax.xml.stream.events.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

// Adapted version of the original AbstractRepo.java
// https://github.com/danvatamaniuc/NewsSite/blob/master/src/data/repository/AbstractRepo.java

public abstract class AbstractRepo<E> {

    private List<E> entities = new ArrayList<E>();
    private String storageFile;
    private final static Logger LOGGER = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME);
    private HashMap<String, String> objectProperties;

    public AbstractRepo(){
    	storageFile = "storage/" + getNameForGenericE() + "Storage.xml";
        objectProperties = new HashMap<>();
    }

    public void setXMLFilename(String filename){
        storageFile = "storage/" + filename;
    }
    
    public void save(E e){
        entities.add(e);
    }

    public List<E> getAll(){
        return entities;
    }

    public void saveAllToXml() throws FileNotFoundException{

        try {
            // create an XMLOutputFactory
            XMLOutputFactory outputFactory = XMLOutputFactory.newInstance();

            // create XMLEventWriter
            final FileOutputStream outputStream = new FileOutputStream(storageFile);
            XMLEventWriter eventWriter = outputFactory.createXMLEventWriter(outputStream, "UTF-8");

            // create an EventFactory
            XMLEventFactory eventFactory = XMLEventFactory.newInstance();

            //newline
            XMLEvent end = eventFactory.createDTD("\n");
            //tab
            XMLEvent tab = eventFactory.createDTD("\t");

            // create and write Start Tag
            StartDocument startDocument = eventFactory.createStartDocument();
            eventWriter.add(startDocument);

            // create file open tag
            StartElement configStartElement = eventFactory.createStartElement("",
                    "", "vertices");
            eventWriter.add(configStartElement);
            eventWriter.add(end);

            //get class name
            final String className = getNameForGenericE();

            //iterator for the entities in the repository
            final Iterator<E> it = entities.iterator();

            //avoid reflecting every single object in the entities arraylist
            //and just get the fields and methods of the first item
            //since all the rest got the same fields and methods

            Field[] fields = null;
            Method[] methods = null;

            if (!entities.isEmpty()){
                E item = entities.get(0);

                //get the fields and methods of the object E
                fields = item.getClass().getDeclaredFields();
                methods = item.getClass().getMethods();
            }

            //insert each element in entities into the xml file
            while(it.hasNext()) {
                //get the next item
                E item = it.next();

                eventWriter.add(tab);
                eventWriter.add(eventFactory.createStartElement("", "", className));
                eventWriter.add(end);

                //for each field
                //get the value of the field using get methods
                //and insert them into the file
                for (Field field : fields) {

                    String fieldName = field.getName();
                    String fieldValue = null; //to be updated with the value of the field

                    for (Method method : methods) {

                        //find the getter of the current field
                        if (method.getName().startsWith("get") &&
                                method.getName().toLowerCase().contains(field.getName().toLowerCase())) {

                            method.setAccessible(true);

                            //in case we find the value, store it and write into file
                            Object o = method.invoke(item);
                            if (o != null) {
                                fieldValue = o.toString();

                                createNode(eventWriter, fieldName, fieldValue);

                                break;
                            }

                        }
                    }
                }

                eventWriter.add(tab);
                eventWriter.add(eventFactory.createEndElement("", "", className));
                eventWriter.add(end);

            }

            eventWriter.add(eventFactory.createEndElement("", "", "objects"));
            eventWriter.add(end);
            eventWriter.add(eventFactory.createEndDocument());

            eventWriter.flush();
            eventWriter.close();
            outputStream.close();


        } catch (Exception e){
//            e.printStackTrace();

            //logs the exception message to the console
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

	public void loadAllFromXML(){

        //CAUTION! empties all elements in the entities hasharray
        //and replaces them with objects from the file

        entities.clear();

        try{
            //set up classes needed to read from an xml file

            //create an input factory for the stream reader
            XMLInputFactory inputFactory = XMLInputFactory.newFactory();

            //create an input stream, where we specify the name of the source file
            final InputStream fileInputStream = new FileInputStream(storageFile);

            //create the stream reader, that will read from the file
            //uses the input factory created above
            XMLStreamReader reader = inputFactory.createXMLStreamReader(fileInputStream);

            //get the name of the class via reflection
            String className = getNameForGenericE();

            //variable to store the name of the tags read
            String elementName = null;

            //variable to store field name
            String fieldName;
            String fieldValue;

            //get all the fields of the class via reflection
            Field[] fields = (Class.forName(getTypeForGenericE().getTypeName())).getDeclaredFields();
            //Method[] methods = (Class.forName(getTypeForGenericE().getTypeName())).getMethods();

            //read the file while you can
            while (reader.hasNext()){
                //get next element
                reader.next();

                //get the event type of the thing read
                int eventType = reader.getEventType();

                switch (eventType){
                    case XMLStreamReader.START_ELEMENT:

                        elementName = reader.getLocalName();

                        for (Field field : fields){
                            fieldName = field.getName();

                            //if we identify what field we are reading, we need to
                            //insert the useful info in the element
                            if (elementName.equals(fieldName)) {

                                //actually tried here invoking the method on an object
                                //created earlier, but can't really keep track
                                //of the type of the argument that needs to be sent

                                //for example, ids are numbers, names are strings,
                                //for ids there is need to convert to int
                                //for names there is not

                                //so let the inherent repos deal with creating objects
                                //by providing a hashmap with all the field names and their
                                //respective values, repos will return the object created

                                fieldValue = reader.getElementText();

                                //store the field name and values for the inherent repo
                                //to generate the object
                                objectProperties.put(fieldName, fieldValue);

                            }
                        }
                        break;

                    case XMLStreamReader.END_ELEMENT:

                        elementName = reader.getLocalName();

                        if (elementName.equals(className)){
                            entities.add(getObject(objectProperties));
                            objectProperties.clear();
                        }
                        break;
                }
            }

            fileInputStream.close();
            reader.close();

        } catch (Exception e){
            e.printStackTrace();
            LOGGER.log(Level.SEVERE, e.getMessage());
        }
    }

    private void createNode(XMLEventWriter eventWriter, String name,
                            String value) throws XMLStreamException {

        XMLEventFactory eventFactory = XMLEventFactory.newInstance();

        XMLEvent end = eventFactory.createDTD("\n");
        XMLEvent tab = eventFactory.createDTD("\t");

        // create Start node
        StartElement sElement = eventFactory.createStartElement("", "", name);
        eventWriter.add(tab);
        eventWriter.add(tab);
        eventWriter.add(sElement);

        // create Content
        Characters characters = eventFactory.createCharacters(value);
        eventWriter.add(characters);

        // create End node
        EndElement eElement = eventFactory.createEndElement("", "", name);
        eventWriter.add(eElement);
        eventWriter.add(end);
    }

    protected abstract E getObject(HashMap<String, String> objectProperties);

//    gets the type of the generic class
    private Type getTypeForGenericE() {
        Type classType = ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        return classType;
    }

//    gets the name of the type of the generic class
    private String getNameForGenericE() {
        String className = getTypeForGenericE().getTypeName();
        return className.toLowerCase();
    }

}
