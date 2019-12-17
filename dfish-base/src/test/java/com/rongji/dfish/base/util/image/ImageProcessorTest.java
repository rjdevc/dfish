package com.rongji.dfish.base.util.image;

import org.junit.Test;

import java.util.regex.Pattern;

/**
 * @author lamontYu
 * @create 2019-12-17
 * @since
 */
public class ImageProcessorTest {

    private static final Pattern PATTERN_ALIAS = Pattern.compile("\\{ALIAS\\}");

    @Test
    public void pattern() {
        String str = "{ALIAS}/test";
        String result = PATTERN_ALIAS.matcher(str).replaceAll("AAA");
        System.out.println(result);

    }

    @Test
    public void execute() {

    }

}
