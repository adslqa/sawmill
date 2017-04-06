package io.logz.sawmill;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static io.logz.sawmill.utils.DocUtils.createDoc;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

public class DocTest {

    @Test
    public void testAddGetAndRemoveFieldValue() {
        Doc doc = createDoc("message", "hola", "name", "test");

        String value = "shalom";
        String path = "object.nestedField";

        doc.addField(path, value);

        assertThat((String) doc.getField(path)).isEqualTo(value);

        assertThat(doc.removeField(path)).isTrue();

        assertThatThrownBy(() -> doc.getField(path)).isInstanceOf(IllegalStateException.class);
        assertThat(doc.removeField(path)).isFalse();
    }

    @Test
    public void testAddGetAndRemoveListOfFieldValues() {
        Doc doc = createDoc("message", "hola", "name", "test");

        String value1 = "shalom1";
        String path1 = "object.nestedField1";

        String value2 = "shalom2";
        String path2 = "object.nestedField2";
        String[] paths = new String[] { path1, path2 };

        doc.addField(path1, value1);
        doc.addField(path2, value2);

        assertThat((String) doc.getField(path1)).isEqualTo(value1);
        assertThat((String) doc.getField(path2)).isEqualTo(value2);

        assertThat(doc.removeField(paths)).isTrue();

        assertThatThrownBy(() -> doc.getField(path1)).isInstanceOf(IllegalStateException.class);
        assertThat(doc.removeField(path1)).isFalse();

        assertThatThrownBy(() -> doc.getField(path2)).isInstanceOf(IllegalStateException.class);
        assertThat(doc.removeField(path2)).isFalse();
    }

    @Test
    public void testAppendAndRemoveFromList() {
        Doc doc = createDoc("message", "hola", "name", "test");

        String path = "list";
        List<String> value = Arrays.asList("value1", "value2");

        assertThat(doc.removeFromList(path,value)).isFalse();

        doc.appendList(path, value);

        for (String item : value) {
            assertThat(((List) doc.getField("list")).contains(item)).isTrue();
        }

        assertThat(doc.removeFromList(path, value)).isTrue();

        for (String item : value) {
            assertThat(((List) doc.getField("list")).contains(item)).isFalse();
        }
    }

    @Test
    public void testHasField() {
        Doc doc = createDoc("int", 15, "String", "test");

        assertThat(doc.hasField("int")).isTrue();
        assertThat(doc.hasField("notExists")).isFalse();
        assertThat(doc.hasField("int", Integer.class)).isTrue();
        assertThat(doc.hasField("int", String.class)).isFalse();
        assertThat(doc.hasField("String", String.class)).isTrue();
        assertThat(doc.hasField("String", Integer.class)).isFalse();
    }
}
