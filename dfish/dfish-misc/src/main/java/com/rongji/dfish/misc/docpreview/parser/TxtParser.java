package com.rongji.dfish.misc.docpreview.parser;

import com.rongji.dfish.misc.docpreview.DocumentParser;
import com.rongji.dfish.misc.docpreview.data.CharacterRun;
import com.rongji.dfish.misc.docpreview.data.Document;
import com.rongji.dfish.misc.docpreview.data.DocumentElement;
import com.rongji.dfish.misc.docpreview.data.Paragraph;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class TxtParser extends DocumentParser {
    @Override
    public Document parse(InputStream is) {
        List<DocumentElement> contents = new ArrayList<>();
        try {
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            byte[]buff =new byte[8192];
            int readed=-1;
            while ((readed=is.read(buff))>=0){
                baos.write(buff,0,readed);
            }
            byte[]ret=baos.toByteArray();
            String charset=com.rongji.dfish.base.util.StringUtil.detCharset(ret);
            String content=new String(ret,charset);
            BufferedReader reader=new BufferedReader(new StringReader(content));
            String line;
            while ((line = reader.readLine()) != null) {
                Paragraph p = new Paragraph();
                CharacterRun run = new CharacterRun();
                run.setText(line);
                p.getBody().add(run);
                contents.add(p);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Document doc=new Document();
        doc.setBody(contents);
        return doc;
    }
}
