package features.justsimple.inject;

import org.mutantcat.justsimple.annotation.Inject;
import org.mutantcat.justsimple.annotation.Managed;

/**
 * @author noear 2024/12/17 created
 */
@Managed
public class ComA {
    @Inject
    public IA ia;
}
