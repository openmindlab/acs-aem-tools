package com.adobe.acs.tools.tag_maker.tagdataconverters.impl;

import com.adobe.acs.tools.tag_maker.TagData;
import com.adobe.acs.tools.tag_maker.tagdataconverters.TagDataConverter;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by stefano.messi
 */
@Component(
        label = "ACS AEM Tools - Tag Maker - Title Node Name and description Converter"
)
@Properties({
        @Property(
                label = "Name",
                name = TagDataConverter.PROP_NAME,
                value = TitleNodeAndDescriptionConverterImpl.NAME,
                propertyPrivate = true
        ),
        @Property(
                label = "Label",
                name = TagDataConverter.PROP_LABEL,
                value = TitleNodeAndDescriptionConverterImpl.LABEL,
                propertyPrivate = true
        )
})
@Service
public class TitleNodeAndDescriptionConverterImpl implements TagDataConverter {

    public static final String NAME = "acs-commons-title-node-name-and-description";

    public static final String LABEL = "Title {{ node-name | description}}";

    private static final Pattern ACCEPT_PATTERN = Pattern.compile(".+\\{\\{(.+)}}|.+\\{\\{(.+)}}\\{\\{(.+)}}$");

    private static final Pattern PATTERN = Pattern.compile("\\{\\{(.+?)}}$");


    @Override
    public final String getLabel() {
        return LABEL;
    }

    @Override
    public final TagData convert(String data) {
        data = StringUtils.stripToEmpty(data);

        String name;
        String description = StringUtils.EMPTY;

        final Matcher matcher = PATTERN.matcher(data);

        if (matcher.find() && matcher.groupCount() >= 1) {
            String[] test = StringUtils.split(StringUtils.stripToEmpty(matcher.group(1)), "|");
            name = StringUtils.trimToEmpty(test[0]);
            name = Normalizer.normalize(name, Normalizer.Form.NFD).replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
            name = name.replaceAll("[^A-Za-z0-9-]", "");
            name = StringUtils.replace(name, " ", "-").toLowerCase();

            if (ArrayUtils.getLength(test) == 2) {
                description = StringUtils.trimToEmpty(test[1]);
            }
        } else {
            return TagData.EMPTY;
        }

        String title = PATTERN.matcher(data).replaceAll(StringUtils.EMPTY);
        title = StringUtils.defaultIfEmpty(StringUtils.trimToEmpty(title), name);

        final TagData tagData = new TagData(name);
        tagData.setTitle(title);
        tagData.setDescription(description);

        return tagData;
    }

    @Override
    public boolean accepts(String data) {
        final Matcher matcher = ACCEPT_PATTERN.matcher(data);
        return matcher.matches();
    }
}
