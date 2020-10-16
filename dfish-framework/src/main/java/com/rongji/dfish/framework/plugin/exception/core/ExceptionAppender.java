package com.rongji.dfish.framework.plugin.exception.core;

import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.PatternLayout;

import java.io.Serializable;

@Plugin(name = "ExceptionAppender", category = "Core", elementType = "appender", printObject = true)
public class ExceptionAppender extends AbstractAppender {
    private String fileName;

    /* 构造函数 */
    public ExceptionAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions) {
        super( name, filter, layout, ignoreExceptions, Property.EMPTY_ARRAY);
        this.fileName = fileName;
    }

    @Override
    public void append(LogEvent event) {
//        final byte[] bytes = getLayout().toByteArray(event);
//        System.out.println(event.getMessage());
        ExceptionManager.getInstance().save(event.getThrown());

        //
    }

    /*  接收配置文件中的参数 */
    @PluginFactory
    public static ExceptionAppender createAppender(@PluginAttribute("name") String name,
                                                   @PluginElement("Filter") final Filter filter,
                                                   @PluginElement("Layout") Layout<? extends Serializable> layout,
                                                   @PluginAttribute("ignoreExceptions") boolean ignoreExceptions) {
        if (name == null) {
            LOGGER.error("no name defined in conf.");
            return null;
        }
        if (layout == null) {
            layout = PatternLayout.createDefaultLayout();
        }
        //FIXME 建表
//        if (!createFile(fileName)) {
//            return null;
//        }
        return new ExceptionAppender(name, filter, layout, ignoreExceptions);
    }



}