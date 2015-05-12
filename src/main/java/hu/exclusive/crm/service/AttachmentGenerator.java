package hu.exclusive.crm.service;

import hu.exclusive.utils.ObjectUtils;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

public class AttachmentGenerator {

    public static final String MIME_PDF = "application/pdf";
    public static final String MIME_PNG = "image/png";
    public static final String MIME_JPG = "image/jpg";
    public static final String MIME_TEXT = "text/plain";

    public static final String MIME_DOC = "application/vnd.ms-word.document.macroEnabled.12";
    public static final String MIME_DOCX = "application/vnd.openxmlformats-officedocument.wordprocessingml.document";
    public static final String MIME_XLSX = "application/vnd.openxmlformats-officedocument.spreadsheetml.worksheet+xml";
    public static final String MIME_XLS = "application/vnd.ms-excel";

    public static final String PLAYER_PDF = "pdf";
    public static final String PLAYER_FLASH = "flash";
    public static final String PLAYER_MOV = "quicktime";
    public static final String PLAYER_MP3 = "quicktime";

    private byte[] content;

    private String mimeType;
    private String attachmentName;
    private String attachmentPath;

    public StreamedContent generateStream() throws Exception {

        if (content == null) {
            System.err.println("!!!!AttachmentGenerator.generateStream() is null!!!");
            return new DefaultStreamedContent(new ByteArrayInputStream(new byte[0]));
        } else {

            InputStream stream = new ByteArrayInputStream(content);
            // if (ObjectUtils.isZippedStream(stream)) {
            // System.err.println("!!!!AttachmentGenerator.generateStream() zipped bytes OK !!!");
            // // stream = new ByteArrayInputStream(ObjectUtils.unzip(content)); // a docx alapbol zip, így egyelőre nem kezelünk
            // // zippelést
            // } else {
            // System.err.println("!!!!AttachmentGenerator.generateStream() bytes OK !!!");
            // }

            System.err.println("!!!!AttachmentGenerator.generateStream() bytes[" + stream.available() + "] OK !!!");
            return new DefaultStreamedContent(stream, mimeType, attachmentName);
        }

    };

    public StreamedContent getAttachment(Integer id) throws Exception {
        setContent(("" + id).getBytes());
        return generateStream();
    };

    public boolean isImage() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isMedia() {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isObject() {
        // TODO Auto-generated method stub
        return false;
    }

    public static String calulateMimeType(String filename) {
        if (!ObjectUtils.isEmpty(filename)) {
            if (filename.toLowerCase().endsWith(".doc"))
                return MIME_DOC;
            if (filename.toLowerCase().endsWith(".docx"))
                return MIME_DOCX;
            if (filename.toLowerCase().endsWith(".xls"))
                return MIME_XLS;
            if (filename.toLowerCase().endsWith(".xlsx"))
                return MIME_XLSX;
            if (filename.toLowerCase().endsWith(".pdf"))
                return MIME_PDF;
            if (filename.toLowerCase().endsWith(".jpg"))
                return MIME_JPG;
            if (filename.toLowerCase().endsWith(".jpeg"))
                return MIME_JPG;
            if (filename.toLowerCase().endsWith(".png"))
                return MIME_PNG;
        }
        return MIME_TEXT;
    }

    public byte[] getContent() {
        return content;
    }

    public AttachmentGenerator setContent(byte[] content) {
        this.content = content;
        return this;
    }

    public String getMimeType() {
        return mimeType;
    }

    public AttachmentGenerator setMimeType(String mimeType) {
        this.mimeType = mimeType;
        return this;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public AttachmentGenerator setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
        return this;
    }

    public String getAttachmentPath() {
        return attachmentPath;
    }

    public AttachmentGenerator setAttachmentPath(String attachmentPath) {
        this.attachmentPath = attachmentPath;
        return this;
    }

}
