package com.ontimize.plaf.utils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CSSParser {

    private static final Logger logger = LoggerFactory.getLogger(CSSParser.class);

    public static class CSSDocument {

        protected ArrayList<CSSStyle> styles = new ArrayList<CSSParser.CSSStyle>();

        protected ArrayList<String> names = new ArrayList<String>();

        public void add(CSSStyle style) {
            String name = style.getName();
            int index = this.names.indexOf(name);

            if (index >= 0) {
                // It means that there was an old style configured...
                this.names.remove(index);
                CSSStyle oldStyle = this.styles.remove(index);
                // Because of @import, it is necessary to override new attributes
                oldStyle.putAll(style); // Overriding
                style.putAll(oldStyle); // style with old properties and the new ones...
            }
            this.names.add(name);
            this.styles.add(style);
        }

        public int indexOf(CSSStyle style) {
            String name = style.getName();
            return this.names.indexOf(name);
        }

        public int indexOf(String name) {
            return this.names.indexOf(name);
        }

        public CSSStyle get(int index) {
            return this.styles.get(index);
        }

    }

    public static class CSSStyle extends HashMap<String, String> {

        protected String name;

        public CSSStyle(String name) {
            super();
            this.name = name;
        }

        public String getName() {
            return this.name;
        }

        public void setName(String name) {
            this.name = name;
        }

    }

    protected static CSSParser parser;

    public static final String OPEN_BRACKET_TAG = "{";

    public static final String CLOSE_BRACKET_TAG = "}";

    public static final String OPEN_COMMENT = "/*";

    public static final String CLOSE_COMMENT = "*/";

    public static final String SEMICOLON_TAG = ";";

    public static final String COLON_TAG = ":";

    public static final String IMPORT = "@import";

    public static CSSParser getInstance() {
        if (CSSParser.parser == null) {
            CSSParser.parser = new CSSParser();
        }
        return CSSParser.parser;
    }

    public CSSDocument parse(InputStream inputStream) {
        try {
            ByteArrayOutputStream output = new ByteArrayOutputStream();
            byte[] read = new byte[1024];

            int size = inputStream.read(read);
            while (size > 0) {
                output.write(read, 0, size);
                size = inputStream.read(read);
            }
            String source = new String(output.toByteArray());
            CSSDocument document = new CSSDocument();
            if (!this.isValid(source)) {
                return document;
            }

            // remove comments
            StringBuffer withOutComments = new StringBuffer();

            int open = 0;
            int index = 0;

            int commentIndex = source.indexOf(CSSParser.OPEN_COMMENT, 0);
            if (commentIndex == -1) {
                // there in no comments into the file...
                withOutComments.append(source);
                index = -1;
            }

            while (index >= 0) {
                int openIndex = source.indexOf(CSSParser.OPEN_COMMENT, index);
                if ((openIndex >= 0) && (open == 0)) {
                    withOutComments.append(source.substring(index, openIndex));
                    index = openIndex + 2;
                    open++;
                } else {
                    int closeIndex = source.indexOf(CSSParser.CLOSE_COMMENT, index);
                    if ((openIndex < 0) && (closeIndex >= 0)) {
                        open--;
                        if (open == 0) {
                            withOutComments.append(source.substring(closeIndex + 2));
                            index = -1;
                        } else {
                            index = closeIndex + 2;
                        }
                    } else if (closeIndex < openIndex) {
                        open--;
                        if (open == 0) {
                            withOutComments.append(source.substring(closeIndex + 2, openIndex));
                            index = openIndex + 2;
                            open++;
                        }
                    } else {
                        open++;
                        index = openIndex + 2;
                    }
                }
            }

            String noComments = withOutComments.toString();
            if (noComments.contains(CSSParser.IMPORT)) {
                document = this.processImport(document, noComments);
                // Removing @import declaration
                String imp_ = this.retrieveImportToken(noComments);
                if (imp_ != null) {
                    noComments = noComments.replace(imp_ + CSSParser.SEMICOLON_TAG, "");
                }
            }

            StringTokenizer fragments = new StringTokenizer(noComments, CSSParser.CLOSE_BRACKET_TAG);
            while (fragments.hasMoreElements()) {
                String fragment = fragments.nextToken();
                try {
                    this.processStyle(document, fragment);
                } catch (Exception e) {
                    CSSParser.logger.error("Error processing: {}", fragment, e);
                }
            }
            return document;
        } catch (Exception e) {
            CSSParser.logger.error("", e);
        }

        return null;
    }

    protected boolean isValid(String source) {
        if ((source == null) || (source.length() == 0)) {
            return false;
        }
        if (!source.contains("{")) {
            return false;
        }
        if (!source.contains("}")) {
            return false;
        }
        return true;
    }

    protected CSSDocument processImport(CSSDocument document, String css) {
        String imp_ = this.retrieveImportToken(css);
        if (imp_ == null) {
            logger.warn("Invalid '@import' declaration. It has been impossible to retrieve inherited document.");
            return document;
        }
        String path = imp_.substring(imp_.indexOf("\'") + 1, imp_.lastIndexOf("\'"));

        InputStream input = CSSParser.class.getClassLoader().getResourceAsStream(path);
        if (input != null) {
            document = CSSParser.getInstance().parse(input);
        }
        return document;
    }

    protected String retrieveImportToken(String css) {
        if ((css != null) && css.startsWith(CSSParser.IMPORT)) {
            StringTokenizer importToken = new StringTokenizer(css, CSSParser.SEMICOLON_TAG);
            String imp_ = importToken.nextToken();// Just take first token.
            return imp_;
        }
        return null;
    }

    protected void processStyle(CSSDocument document, String fragment) throws Exception {
        int index = fragment.indexOf(CSSParser.OPEN_BRACKET_TAG);
        if (index > 0) {
            String tag = fragment.substring(0, index).trim();
            CSSStyle style = new CSSStyle(tag);
            String attributes = fragment.substring(index + 1).trim();
            this.processAttributes(style, attributes);
            document.add(style);
        }
    }

    protected void processAttributes(CSSStyle style, String attributes) throws Exception {
        StringTokenizer attributeTokens = new StringTokenizer(attributes, CSSParser.SEMICOLON_TAG);
        while (attributeTokens.hasMoreElements()) {
            String attribute = attributeTokens.nextToken();
            int separator = attribute.indexOf(CSSParser.COLON_TAG);
            if (separator < 0) {
                throw new Exception("Error processing " + attributes);
            }
            String attr = attribute.substring(0, separator).trim();
            String value = attribute.substring(separator + 1).trim();
            style.put(attr, value);
        }
    }

    public static void main(String[] args) {
        InputStream input = CSSParser.class.getClassLoader().getResourceAsStream("com/ontimize/plaf/utils/demo.css");
        CSSDocument document = CSSParser.getInstance().parse(input);
    }

}
