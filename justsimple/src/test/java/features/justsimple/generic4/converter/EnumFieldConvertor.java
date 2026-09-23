package features.justsimple.generic4.converter;

import features.justsimple.generic4.FieldConvertor;
import features.justsimple.generic4.FieldMeta;

/**
 * @author noear 2025/3/19 created
 */
public class EnumFieldConvertor implements FieldConvertor.BFieldConvertor{
    @Override
    public boolean supports(FieldMeta meta, Class<?> valueType) {
        return false;
    }
}
