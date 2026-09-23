package demo.security.web;

import org.mutantcat.justsimple.annotation.Managed;
import org.mutantcat.justsimple.annotation.Configuration;
import org.mutantcat.justsimple.security.web.SecurityFilter;
import org.mutantcat.justsimple.security.web.header.XContentTypeOptionsHeaderHandler;
import org.mutantcat.justsimple.security.web.header.XXssProtectionHeaderHandler;


@Configuration
public class DemoFilter {
    @Managed(index = -99)
    public SecurityFilter securityFilter() {
        return new SecurityFilter(
                new XContentTypeOptionsHeaderHandler(),
                new XXssProtectionHeaderHandler()
        );
    }
}
