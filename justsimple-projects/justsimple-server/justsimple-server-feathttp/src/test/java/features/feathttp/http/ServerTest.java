package features.feathttp.http;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.mutantcat.justsimple.JustSimple;
import org.mutantcat.justsimple.core.util.MimeType;
import org.mutantcat.justsimple.test.HttpTester;
import org.mutantcat.justsimple.test.JustSimpleTest;

import java.util.stream.Collectors;
import java.util.stream.IntStream;

@JustSimpleTest(App.class)
public class ServerTest extends HttpTester {
    @AfterAll
    public static void aftAll() {
        if (JustSimple.app() != null) {
            JustSimple.stopBlock();
        }
    }

    @Test
    public void test() throws Exception {
        assert "hello".equals(path("/hello").get());
    }

    @Test
    public void async() throws Exception {
        assert "async".equals(path("/async").get());
    }

    @Test
    public void async_timeout() throws Exception {
        assert 500 == path("/async_timeout").head();
    }

    @Test
    public void ct0() {
        assert path("/ct0").exec("GET").contentType() == null;
    }

    @Test
    public void ct1() {
        assert path("/hello").exec("GET").contentType()
                .startsWith(MimeType.TEXT_PLAIN_VALUE);
    }

    @Test
    public void multipart_text_larger_than_read_buffer() throws Exception {
        String text = textOfSize(32 * 1024);

        assert text.equals(path("/multipart/text")
                .data("text", text)
                .post(true));
    }

    @Test
    public void multipart_form_content_size_limit() throws Exception {
        assert 413 == path("/multipart/count")
                .data("text1", textOfSize(24 * 1024 + 1))
                .data("text2", textOfSize(24 * 1024))
                .multipart(true)
                .execAsCode("POST");
    }

    @Test
    public void multipart_part_count_limit() throws Exception {
        assert 4 == Integer.parseInt(path("/multipart/count")
                .data("p1", "1")
                .data("p2", "2")
                .data("p3", "3")
                .data("p4", "4")
                .post(true));

        assert 413 == path("/multipart/count")
                .data("p1", "1")
                .data("p2", "2")
                .data("p3", "3")
                .data("p4", "4")
                .data("p5", "5")
                .multipart(true)
                .execAsCode("POST");
    }

    private String textOfSize(int size) {
        return IntStream.range(0, size)
                .mapToObj(i -> String.valueOf((char) ('a' + i % 26)))
                .collect(Collectors.joining());
    }
}
