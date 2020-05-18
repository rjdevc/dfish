package com.rongji.dfish.ui.json;

import com.rongji.dfish.ui.HasText;
import com.rongji.dfish.ui.Node;
import com.rongji.dfish.ui.form.FormElement;

import java.util.Collection;

/**
 * 用于记录构建JSON过程中的路径
 *
 * @author Dfish team
 */
public class PathInfo {
    /**
     * 完整构造函数
     *
     * @param propertyName  String
     * @param propertyValue Object
     */
    public PathInfo(String propertyName, Object propertyValue) {
        this.propertyName = propertyName;
        this.propertyValue = propertyValue;
    }

    private String propertyName;
    private Object propertyValue;

    /**
     * 属性名
     *
     * @return String
     */
    public String getPropertyName() {
        return propertyName;
    }

    /**
     * 属性名
     *
     * @param propertyName 属性名
     */
    public void setPropertyName(String propertyName) {
        this.propertyName = propertyName;
    }

    /**
     * 属性值
     *
     * @return Object
     */
    public Object getPropertyValue() {
        return propertyValue;
    }

    /**
     * 属性值
     *
     * @param propertyValue Object
     */
    public void setPropertyValue(Object propertyValue) {
        this.propertyValue = propertyValue;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("{\"");
        if (propertyName != null) {
            sb.append("path\":\"").append(propertyName).append("\", \"");
        }
        sb.append("obj\":");
        if (propertyValue == null) {
            sb.append("null");
        } else if (propertyValue instanceof Collection) {
            sb.append("\"Arr[size=");
            sb.append(((Collection) propertyValue).size());
            sb.append("]\"");
        } else if (propertyValue instanceof Object[]) {
            sb.append("\"Arr[leng=");
            sb.append(((Object[]) propertyValue).length);
            sb.append("]\"");
        } else if (propertyValue instanceof Node) {
            sb.append("\"Widget[type=");
            sb.append(((Node) propertyValue).getType());
            boolean showed = false;
            if (!showed) {
                if (propertyValue instanceof Node) {
                    String id = ((Node) propertyValue).getId();
                    if (id != null && !"".equals(id)) {
                        showed = true;
                        sb.append(",id=");
                        sb.append(id);
                    }
                }
            }
            if (!showed) {
                if (propertyValue instanceof FormElement) {
                    String name = ((FormElement) propertyValue).getName();
                    if (name != null && !"".equals(name)) {
                        showed = true;
                        sb.append(",name=");
                        sb.append(name);
                    }
                }
            }
            if (!showed) {
                if (propertyValue instanceof HasText) {
                    String text = ((HasText) propertyValue).getText();
                    if (text != null && !"".equals(text)) {
                        showed = true;
                        sb.append(",text=");
                        sb.append(text);
                    }
                }
            }
            sb.append("]\"");
        }
        sb.append('}');
        return sb.toString();
    }
}
