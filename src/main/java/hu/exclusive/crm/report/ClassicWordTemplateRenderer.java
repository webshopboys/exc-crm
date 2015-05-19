package hu.exclusive.crm.report;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * / The class can be used, to create a new word file out of an existing template.
 * / Placeholders must be of format ${ENTRY_NAME} and must be inserted as plain text to a classic "doc"-file
 **/
public class ClassicWordTemplateRenderer {

    private static final Pattern PLACEHOLDER_PATTERN = Pattern.compile("\\$\\{([^\\}]*)\\}");
    private final Logger logger = Logger.getLogger(ClassicWordTemplateRenderer.class);

    public byte[] execute(File templateFile, ReportParameterProvider someDAO) throws IOException {
        // Temporarily created file input stream will be closed inside POIFSFileSystem
        POIFSFileSystem fileSystem = new POIFSFileSystem(new FileInputStream(templateFile));
        HWPFDocument template = new HWPFDocument(fileSystem);

        List<String> placeholders = findPlaceholders(template);
        if (placeholders.size() == 0) {
            // Just copy the file if there are no placeholders in it
            return org.apache.commons.io.FileUtils.readFileToByteArray(templateFile);
        }

        ByteArrayInputStream templateCopy = copyTemplateToMemory(templateFile);
        fileSystem = new POIFSFileSystem(templateCopy);
        HWPFDocument targetDocument = new HWPFDocument(fileSystem);
        Range range = targetDocument.getRange();

        Map<String, String> placeholdersToValues = mapPlaceholdersToValues(placeholders, someDAO);
        for (String placeholder : placeholdersToValues.keySet()) {
            range.replaceText(placeholder, placeholdersToValues.get(placeholder));
        }

        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            targetDocument.write(outputStream);
        } finally {
            outputStream.close();
        }
        return outputStream.toByteArray();
    }

    private Map<String, String> mapPlaceholdersToValues(List<String> placeholders, ReportParameterProvider someDAO) {
        Map<String, String> result = new HashMap<String, String>();
        for (String placeholder : placeholders) {
            // Trim the leading ${ and the trailing } characters
            String barePlaceholder = placeholder.substring(2, placeholder.length() - 2);
            String value = someDAO.getValue(barePlaceholder);
            if (value == null) {
                logger.info("Did not find any value for placeholder ${placeholder}. Skip it!");
            } else {
                result.put(placeholder, value);
            }
        }
        return result;
    }

    private List<String> findPlaceholders(HWPFDocument document) {
        org.apache.poi.hwpf.usermodel.Range range = document.getRange();
        String documentContent = range.text();

        Matcher matcher = PLACEHOLDER_PATTERN.matcher(documentContent);
        List<String> placeholders = new ArrayList<String>();
        while (matcher.find()) {
            placeholders.add(matcher.group(0));
        }
        return placeholders;
    }

    private ByteArrayInputStream copyTemplateToMemory(File templateFile) throws IOException {
        return new ByteArrayInputStream(org.apache.commons.io.FileUtils.readFileToByteArray(templateFile));
    }
}