package com.rongji.dfish.ui.auxiliary;

import com.rongji.dfish.ui.HasText;

/**
 * 和javascript端是对应的TD模型。
 * json中如果td没有cls等额外属性，可能会简化显示它的node
 * 如果这个node还是文本，可能会进一步简化显示成文本。
 * 所以Td默认不能显示按封装类格式。这时候json中的原型将有可能还是这个JsonTd的格式
 * 也有可能是Widget格式或者是text格式。
 *
 * @author DfishTeam
 */
class JsonTD extends AbstractTD<JsonTD>  {
    /**
     *
     */
    private static final long serialVersionUID = -5125782398657967546L;
    private String text;

    @Override
    public String getType() {
        return "TD";
    }


}
