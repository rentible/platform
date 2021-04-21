package org.wallride.support;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.wallride.model.TreeNode;

import java.io.IOException;

public class StringValueDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(final JsonParser jsonParser, final DeserializationContext ctxt) throws IOException {
        final TreeNode tree = jsonParser.getCodec().readTree(jsonParser);
        return tree.toString();
    }

}