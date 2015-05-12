package hu.exclusive.crm.report;


public class XMLWordTemplateRenderer {
    // Merge fields can either be inside w:fldSimple tags or can be "complex fields" (with a special rsid-definition).
    // In the later case the field definition should be within a "instrText" tag
    // private static final String XPATH =
    // "//w:instrText[starts-with(text(),\" MERGEFIELD \")] | //w:fldSimple[starts-with(@w:instr, \" MERGEFIELD \")]";
    // private static final Pattern MERGEFIELD_PATTERN = Pattern.compile(' MERGEFIELD ([^\\\\]*)');
    //
    // private final Logger logger = Logger.getLogger(XMLWordTemplateRenderer.class)
    //
    // @Override
    // public byte[] execute(File templateFile, SomeDAOClass someDAO) {
    // LoadFromZipNG loader = new LoadFromZipNG();
    // WordprocessingMLPackage wordprocessingMLPackage = (WordprocessingMLPackage)loader.get(new FileInputStream(templateFile));
    // MainDocumentPart documentPart = wordprocessingMLPackage.getMainDocumentPart();
    //
    // List allMergeFieldNames = []
    // allMergeFieldNames.addAll(detectMergeFieldsWithinHeaderAndFooter(documentPart, wordprocessingMLPackage.getDocumentModel()))
    // allMergeFieldNames.addAll(detectMainDocumentMergeFields(documentPart))
    // Map<DataFieldName, String> mergeFieldsToValues = [:]
    // for (String mergeFieldName : allMergeFieldNames) {
    // String value = someDAO.getValue(mergeFieldName.trim())
    // if (value) {
    // mergeFieldsToValues.put(new DataFieldName(mergeFieldName), value)
    // } else {
    // mergeFieldsToValues.put(new DataFieldName(mergeFieldName), "UNDEFINED!")
    // }
    // }
    // MailMerger.performMerge(wordprocessingMLPackage, mergeFieldsToValues, true)
    //
    // SaveToZipFile saver = new SaveToZipFile(wordprocessingMLPackage)
    // ByteArrayOutputStream result = new ByteArrayOutputStream()
    // try {
    // saver.save(result)
    // } finally {
    // result.close()
    // }
    // return result.toByteArray()
    // }
    //
    // private List detectMainDocumentMergeFields(MainDocumentPart mainDocumentPart) {
    // List mergeFieldNames = new ArrayList();
    //
    // for (Object jaxbNode : mainDocumentPart.getJAXBNodesViaXPath(XPATH, false)) {
    // String mergeFieldName = detectMergeFieldName(jaxbNode);
    // if (mergeFieldName != null) {
    // mergeFieldNames.add(mergeFieldName);
    // }
    // }
    // return mergeFieldNames;
    // }
    //
    // private List detectMergeFieldsWithinHeaderAndFooter(MainDocumentPart mainDocumentPart, DocumentModel documentModel) {
    // if (documentModel.getSections().size() == 0) {
    // return new ArrayList();
    // }
    // List mergeFieldNames = new ArrayList();
    //
    // SectionWrapper sectionWrapper = documentModel.getSections().get(0);
    // SectPr sectPr = sectionWrapper.getSectPr();
    // for (CTRel rel : sectPr.getEGHdrFtrReferences()) {
    // String relId = rel.getId();
    // JaxbXmlPart part = (JaxbXmlPart) mainDocumentPart.getRelationshipsPart().getPart(relId);
    // FieldsPreprocessor.complexifyFields(part);
    // if (part instanceof JaxbXmlPartXPathAware) {
    // for (Object jaxbNode : part.getJAXBNodesViaXPath(XPATH, false)) {
    // String mergeFieldName = detectMergeFieldName(jaxbNode);
    // if (mergeFieldName) {
    // mergeFieldNames.add(mergeFieldName);
    // }
    // }
    // }
    // }
    // return mergeFieldNames;
    // }
    //
    // private String detectMergeFieldName(Object jaxbNode) {
    // String mergeFieldName = null;
    // String textToParse = null;
    // if (jaxbNode instanceof Text) {
    // textToParse = ((Text) jaxbNode).getValue();
    // }
    // if (jaxbNode instanceof CTSimpleField) {
    // CTSimpleField simpleField = (CTSimpleField) jaxbNode;
    // textToParse = ((CTSimpleField) simpleField).getInstr();
    // }
    // if (textToParse != null) {
    // Matcher matcher = MERGEFIELD_PATTERN.matcher(textToParse);
    // if (matcher.find() && matcher.groupCount() == 1) {
    // mergeFieldName = matcher.group(1);
    // mergeFieldName = removeTrailingOrLeadingWhitespaces(mergeFieldName);
    // mergeFieldName = handleWordsWithInnerWhitespaces(mergeFieldName);
    // }
    // }
    // return mergeFieldName;
    // }
    //
    // private String removeTrailingOrLeadingWhitespaces(String mergeFieldName) {
    // return mergeFieldName.trim()
    // }
    //
    // private String handleWordsWithInnerWhitespaces(String mergeFieldName) {
    // int words = mergeFieldName.split(" ").findAll { it.length() > 0 }.size();
    // if (words == 0 || words == 1) {
    // return mergeFieldName;
    // }
    // // Remove leading / trailing "-character
    // mergeFieldName = mergeFieldName.replaceFirst("\"", "");
    // mergeFieldName = mergeFieldName.reverse().replaceFirst("\"", "").reverse();
    // return mergeFieldName;
    // }
}