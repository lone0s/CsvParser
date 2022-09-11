package CsvParser;

import java.util.List;

public class LineObject {
    private final String[] header;
    private final String[] lineContent;
    private boolean isInitialized = false;

    public LineObject(String[] headerData, String[] lineData) {
        header = headerData.clone();
        lineContent = lineData.clone();
        isInitialized = true;
    }

    public String[] getHeader() {
        if (isInitialized)
            return header;
        return null;
    }
    public String[] getLineContent() {
        if (isInitialized)
            return lineContent;
        return null;
    }
    public String getLineContent(int i) {
        if (isInitialized) {
            if (i >= 0 && i <= lineContent.length) {
                return lineContent[i];
            }
        }
        return null;
    }

    public String getHeader(int i) {
        if (isInitialized) {
            if (i >= 0 && i <= header.length) {
                return header[i];
            }
        }
        return null;
    }
}
