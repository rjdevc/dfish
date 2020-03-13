package com.rongji.dfish.misc.docpreview.builder;

import com.rongji.dfish.misc.docpreview.data.*;

import java.util.List;



/**
 * 转成text
 */
public class TextBuilder {
    /**
     * 构建 text
     * @param doc 文档
     * @return String
     */
    public String build(Document doc) {
        StringBuilder sb = new StringBuilder();
        build(doc, sb);
        return sb.toString();
    }

    private void build(Object from, StringBuilder sb) {
        List subs = null;
        if (from instanceof Document) {
            subs = ((Document) from).getBody();
        } else if (from instanceof Paragraph) {
            subs = ((Paragraph) from).getBody();
        } else if (from instanceof Table) {
            subs = ((Table) from).getRows();
        } else if (from instanceof TableRow) {
            subs = ((TableRow) from).getCells();
        } else if (from instanceof TableCell) {
            subs = ((TableCell) from).getBody();
        }
        if (subs != null) {
            for (Object sub : subs) {
                build(sub, sb);
            }
        }
        if (from instanceof CharacterRun) {
            CharacterRun cr = (CharacterRun) from;
            sb.append(cr.getText());
        } else if (from instanceof Paragraph) {
            sb.append("\r\n");
        }

    }
}
