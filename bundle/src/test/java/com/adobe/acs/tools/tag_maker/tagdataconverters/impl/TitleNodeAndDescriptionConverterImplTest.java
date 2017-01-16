package com.adobe.acs.tools.tag_maker.tagdataconverters.impl;

import com.adobe.acs.tools.tag_maker.TagData;
import com.adobe.acs.tools.tag_maker.tagdataconverters.TagDataConverter;
import junit.framework.TestCase;
import org.apache.commons.lang.StringUtils;

public class TitleNodeAndDescriptionConverterImplTest extends TestCase {

    TagDataConverter converter = new TitleNodeAndDescriptionConverterImpl();

    public void testConvert() throws Exception {

        String expectedTitle = "Hello World";
        String expectedName = "hello-world";
        String expectedDescription = "my-description, my-description2, my-description3";

        TagData actual = converter.convert("Hello World {{ hello-world }}");

        assertEquals(expectedTitle, actual.getTitle());
        assertEquals(expectedName, actual.getName());

        actual = converter.convert(StringUtils.join(new String[]{"{{ ", expectedName, "}}"}));

        assertEquals(expectedName, actual.getTitle());
        assertEquals(expectedName, actual.getName());

        actual = converter.convert(StringUtils.join(new String[]{"Hello World {{ ", expectedName, "    |    ", expectedDescription, "}}"}));

        assertEquals(expectedTitle, actual.getTitle());
        assertEquals(expectedName, actual.getName());
        assertEquals(expectedDescription, actual.getDescription());


        actual = converter.convert(StringUtils.join(new String[]{"{{ ", expectedName, "    |    ", expectedDescription, "}}"}));

        assertEquals(expectedName, actual.getTitle());
        assertEquals(expectedName, actual.getName());
        assertEquals(expectedDescription, actual.getDescription());



        actual = converter.convert(StringUtils.join(new String[]{"álcool {{ álcool    |   álcool  }}"}));

        assertEquals("álcool", actual.getTitle());
        assertEquals("alcool", actual.getName());
        assertEquals("álcool", actual.getDescription());
    }

}