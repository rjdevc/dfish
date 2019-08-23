package com.rongji.dfish.misc.docpreview.builder;

import com.rongji.dfish.misc.docpreview.data.*;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

public class TextBuilder {
    public String build(Document doc) {
        StringBuilder sb = new StringBuilder();
        build(doc, sb);
        return sb.toString();
    }

    private static final HashMap<Class, String> REF_MAP = new HashMap<>();

    static {
        Object[][] types = new Object[][]{
                {Document.class, "Body"},
                {Paragraph.class, "Body"},
                {Table.class, "Rows"},
                {TableRow.class, "Cells"},
                {TableCell.class, "Body"},
        };
        for (Object[] row : types) {
            REF_MAP.put((Class) row[0], (String) row[1]);
        }
    }

    private static final Class[] NO_PARAM = new Class[0];

    private void build(Object from, StringBuilder sb) {
        Class<?> clz = from.getClass();
        String subName = REF_MAP.get(clz);
        if (subName != null) {
            try {
                Method getter = clz.getMethod("get" + subName, NO_PARAM);
                List fromSubs = (List) getter.invoke(from);
                for (Object fromSub : fromSubs) {
                    build(fromSub, sb);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (clz == CharacterRun.class) {
            CharacterRun cr = (CharacterRun) from;
            sb.append(cr.getText());
        }
        if (clz == Paragraph.class) {
            sb.append("\r\n");
        }

    }
}
