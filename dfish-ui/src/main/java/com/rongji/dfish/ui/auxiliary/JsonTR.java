package com.rongji.dfish.ui.auxiliary;

/**
 * 和javascript端是对应的TR模型。
 * json中如果tr没有cls等额外属性，可能会简化显示它的data部分
 * 所以Tr默认不能显示按封装类格式。这时候json中的原型将有可能还是这个JsonTR的格式
 * 也有可能是Map格式。
 *
 * @author DFish team
 */
class JsonTR extends AbstractTR<JsonTR> {
    private static final long serialVersionUID = -1034767067781605568L;

    @Override
    public String getType() {
        return "TR";
    }

}
