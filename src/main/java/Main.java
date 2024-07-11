import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.bean.ColumnPositionMappingStrategy;
import au.com.bytecode.opencsv.bean.CsvToBean;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.exceptions.CsvValidationException;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Main {
    private String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
    private String fileName = "data.csv";
    private List<Employee> list = parseCSV(columnMapping,fileName);
    private static String json = listToJson(list);
    private  String jsonFilename = "data.json";
    String xmlFileName = "data.xml";
    List<Employee> list2 = parseXML(xmlFileName);
    String json2 = listToJson(list2);
    String jsonFilename2 = "data2.json";
    writeString(json2, jsonFilename2);


    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        List<Employee> employees = null;
        ColumnPositionMappingStrategy<Employee> cpmStrategy = new ColumnPositionMappingStrategy<>();
        cpmStrategy.setType(Employee.class);
        cpmStrategy.setColumnMapping(columnMapping);

        try (CSVReader reader = new CSVReader(new FileReader(fileName))) {
            String[] nextLine;
            while ((nextLine = reader.readNext()) != null){
                System.out.println(Arrays.toString(nextLine));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return employees;
    }

    public static String listToJson(List<Employee> list) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(list);
    }

    public static void writeString(String json) {
        try (FileWriter fileWriter = new FileWriter("data.json")) {
            fileWriter.write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static List<Employee> parseXML(String xmlFilename) throws ParserConfigurationException, IOException, SAXException {
        List<String> elements = new ArrayList<>();
        List<Employee> list = new ArrayList<>();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse(new File(xmlFilename));
        Node root = doc.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeName().equals("employee")) {
                NodeList nodeList1 = node.getChildNodes();
                for (int j = 0; j < nodeList1.getLength(); j++) {
                    Node node_ = nodeList1.item(j);
                    if (Node.ELEMENT_NODE == node_.getNodeType()) {
                        elements.add(node_.getTextContent());
                    }
                }
                list.add(new Employee(
                        Long.parseLong(elements.get(0)),
                        elements.get(1),
                        elements.get(2),
                        elements.get(3),
                        Integer.parseInt(elements.get(4))));
                elements.clear();
            }
        }
        return list;

        private static List<Employee> jsonToList(String json) {
            List<Employee> list2 = new ArrayList<>();
            GsonBuilder builder2 = new GsonBuilder();
            Gson gson = builder2.create();
            try {
                Object obj = new JSONParser().parse(json);
                JSONArray array = (JSONArray) obj;
                for (Object a : array) {
                    list.add(gson.fromJson(a.toString(), Employee.class));
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return list;
        }
    }
}
