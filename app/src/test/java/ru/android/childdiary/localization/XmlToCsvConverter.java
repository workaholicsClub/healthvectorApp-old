package ru.android.childdiary.localization;

import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * <p>
 * Данный класс предназначен для конвертации набора файлов strings*.xml в один файл *.csv,
 * предназначенный для переводчика. После перевода (заполнения таблицы английскими строками)
 * предполагается с помощью средств Excel (Calc) сгенерировать строки для английской локали
 * и вставить их в соответствующие файлы strings*.xml. После перевода необходимо проверить
 * 1) параметры: их типы и количество, 2) экранированные, специальные символы.
 * </p>
 * <li>Файлы обрабатываются в порядке, заданном константой, строки (strings, string-array, plurals)
 * обрабатываются в порядке появления в файле.</li>
 * <li>Строки не должны использовать CDATA-section (при необходимости надо доработать эту утилиту
 * для обработки типа узла CDATA_SECTION_NODE или заменить символы, экранируемые CDATA, на &lt;
 * и т.д.).</li>
 * <li>Комментарий перед строкой, массивом, количественной строкой используется как значение для
 * третьей колонки. Таким образом можно составлять комментарии для переводчика.</li>
 * <li>Файл strings-format.xml не предоставляется заказчикам для перевода, чтобы не пугать их, т.к.
 * не представляет особого труда перевести его на английский. Для других языков может понадобиться
 * изменять и этот файл (другой порядок слов и т.п.).</li>
 */
public class XmlToCsvConverter {
    private static final String[] INPUT_FILE_NAMES = new String[]{
            "strings.xml", "strings-arrays.xml", "strings-plurals.xml",
            "strings-test-doman.xml", "strings-test-doman-mental.xml", "strings-test-doman-physical.xml",
            "strings-test-newborn.xml", "strings-test-autism.xml", "strings-dictionaries.xml"};
    private static final String OUTPUT_FILE_NAME = "Вектор развития ru-en.csv";
    private static final String[] COMMENTS = new String[]{
            "Примечания для переводчика:",
            "1. Не изменять содержимое столбца A.",
            "2. Не удалять пустые строки.",
            "3. Не изменять значения столбца C. При необходимости добавить комментарий добавить его в столбец E.",
            "4. В комментариях указаны значения и смысл параметров, специальных символов.",
            "5. Текст на английском языке должен содержать те же параметры, что и текст на русском языке. Но в тексте на английском языке порядок параметров может отличаться в соответствии с грамматикой языка.",
            "6. При изменении значений столбца B помечать их цветом."};

    private final Set<String> values = new HashSet<>();

    @Test
    public void convert() {
        List<Row> resultRows = new ArrayList<>();
        resultRows.add(new Row("ИД (не изменять)", "Русский текст", "Комментарий", "Английский текст"));
        for (String fileName : INPUT_FILE_NAMES) {
            List<Row> rows = readXml(fileName);
            resultRows.addAll(rows);
        }
        Assert.assertTrue(resultRows.size() > COMMENTS.length);
        for (int i = 0; i < COMMENTS.length; ++i) {
            Row row = resultRows.get(i);
            Assert.assertTrue(row.getColumnsCount() < 5);
            for (int j = row.getColumnsCount(); j < 5; ++j) {
                resultRows.get(i).addColumns((String) null);
            }
            resultRows.get(i).addColumns(COMMENTS[i]);
        }
        writeCsv(OUTPUT_FILE_NAME, resultRows);
    }

    private String getResourcesParentPath() {
        return new File(".").getAbsolutePath() + "\\app\\src\\main\\res\\values\\";
    }

    private void writeCsv(String fileName, List<Row> rows) {
        File file = new File(fileName);
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), "UTF8"));
            System.out.println("Start write file: '" + file.getAbsolutePath() + "'");
            for (Row row : rows) {
                writer.write(row.toString());
            }
            System.out.println("Finish write file: '" + file.getAbsolutePath() + "'");
        } catch (FileNotFoundException e) {
            System.err.println("File not found: '" + file.getAbsolutePath() + "'");
        } catch (IOException e) {
            System.err.println("Read error: '" + file.getAbsolutePath() + "'");
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println("Failed to close file: '" + file.getAbsolutePath() + "'");
                }
            }
        }
    }

    private List<Row> readXml(String fileName) {
        List<Row> rows = new ArrayList<>();
        File file = new File(getResourcesParentPath() + fileName);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        try {
            System.out.println("Start read file: '" + file.getAbsolutePath() + "'");
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.parse(file);
            document.getDocumentElement().normalize();
            rows.add(new Row(fileName));
            NodeList childNodes = document.getFirstChild().getChildNodes();
            processNodes(childNodes, rows);
            System.out.println("Finish read file: '" + file.getAbsolutePath() + "'");
            return rows;
        } catch (FileNotFoundException e) {
            System.err.println("File not found: '" + file.getAbsolutePath() + "'");
        } catch (IOException e) {
            System.err.println("Read error: '" + file.getAbsolutePath() + "'");
        } catch (SAXException | ParserConfigurationException e) {
            System.err.println("Parse error: '" + file.getAbsolutePath() + "'");
            e.printStackTrace();
        }
        return Collections.emptyList();
    }

    private void processNodes(NodeList nodes, List<Row> rows) {
        String comment = null;
        for (int i = 0; i < nodes.getLength(); ++i) {
            Node node = nodes.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE && "string".equals(node.getNodeName())) {
                processStringNode(node, rows, comment);
                comment = null;
            } else if (node.getNodeType() == Node.ELEMENT_NODE && "string-array".equals(node.getNodeName())) {
                processStringArrayNode(node, rows, comment);
                comment = null;
            } else if (node.getNodeType() == Node.ELEMENT_NODE && "plurals".equals(node.getNodeName())) {
                processPluralNode(node, rows, comment);
                comment = null;
            } else if (node.getNodeType() == Node.COMMENT_NODE) {
                comment = getComment(node);
            }
        }
    }

    private String getComment(Node node) {
        return node.getTextContent();
    }

    private Row addComment(Row row, String comment) {
        if (comment != null && !comment.isEmpty()) {
            row.addColumns(comment);
        }
        return row;
    }

    private void processStringNode(Node node, List<Row> rows, String comment) {
        Element element = (Element) node;
        String key = element.getAttribute("name");
        String value = element.getTextContent();
        value = preprocess(value);
        if (values.contains(value)) {
            System.err.println("duplicate: key = '" + key + "'; value = '" + value + "'");
        } else {
            values.add(value);
        }
        rows.add(addComment(new Row(key, value), comment));
    }

    private void processStringArrayNode(Node node, List<Row> rows, String comment) {
        Element element = (Element) node;
        String key = element.getAttribute("name");
        rows.add(addComment(new Row(key, null), comment));
        comment = null;
        NodeList childNodes = node.getChildNodes();
        for (int j = 0; j < childNodes.getLength(); ++j) {
            Node childNode = childNodes.item(j);
            if (childNode.getNodeType() == Node.ELEMENT_NODE && "item".equals(childNode.getNodeName())) {
                Element childElement = (Element) childNode;
                String value = childElement.getTextContent();
                value = preprocess(value);
                rows.add(addComment(new Row("item", value), comment));
                comment = null;
            } else if (childNode.getNodeType() == Node.COMMENT_NODE) {
                comment = getComment(childNode);
            }
        }
    }

    private void processPluralNode(Node node, List<Row> rows, String comment) {
        Element element = (Element) node;
        String key = element.getAttribute("name");
        rows.add(addComment(new Row(key, null), comment));
        comment = null;
        NodeList childNodes = node.getChildNodes();
        for (int j = 0; j < childNodes.getLength(); ++j) {
            Node childNode = childNodes.item(j);
            if (childNode.getNodeType() == Node.ELEMENT_NODE && "item".equals(childNode.getNodeName())) {
                Element childElement = (Element) childNode;
                String quantity = childElement.getAttribute("quantity");
                String value = childElement.getTextContent();
                value = preprocess(value);
                rows.add(addComment(new Row(quantity, value), comment));
                comment = null;
            } else if (childNode.getNodeType() == Node.COMMENT_NODE) {
                comment = getComment(childNode);
            }
        }
    }

    private String preprocess(String value) {
        if (value.startsWith("\"") && value.endsWith("\"")) {
            value = value.substring(1, value.length() - 1);
        }
        value = value.replace("<", "&lt;");
        value = value.replace(">", "&gt;");
        value = value.replace("…", "&#8230;");
        return value;
    }

    private static class Row {
        private final List<String> columns = new ArrayList<>();

        public Row(String... columns) {
            addColumns(columns);
        }

        public void addColumns(String... strings) {
            columns.addAll(Arrays.asList(strings));
        }

        public int getColumnsCount() {
            return columns.size();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < columns.size(); ++i) {
                sb.append("\"");
                if (columns.get(i) != null) {
                    sb.append(columns.get(i).replace("\"", "\"\""));
                }
                sb.append("\"");
                sb.append(";");
            }
            sb.append("\n");
            return sb.toString();
        }
    }
}
