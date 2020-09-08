package com.rongji.dfish.framework.plugin.qrcode.controller;

import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.framework.mvc.controller.FrameworkController;
import com.rongji.dfish.framework.util.ServletUtil;
import com.rongji.dfish.misc.qrcode.MatrixToImageConfig;
import com.rongji.dfish.misc.qrcode.MatrixToImageWriter;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 二维码图标图片生成
 *
 * @author lamontYu
 *
 * @since DFish3.0
 */
@RequestMapping("/qrCode")
public class QRCodeController extends FrameworkController {
    /**
     * 生成图片
     * @param request
     * @param response
     * @throws Exception
     */
    @RequestMapping("/image")
    public void image(HttpServletRequest request, HttpServletResponse response) throws Exception {
        String content = ServletUtil.getParameter(request, "content");
        String sizeStr = request.getParameter("size");
        int size = 100;
        if (Utils.notEmpty(sizeStr)) {
            try {
                size = Integer.parseInt(sizeStr);
            } catch (Exception e) {
            }
        }
        String format = request.getParameter("format");
        format = Utils.isEmpty(format) ? "png" : format;

        String onColor = request.getParameter("onColor");
        String offColor = request.getParameter("offColor");
        MatrixToImageWriter.writeToStream(MatrixToImageWriter.toBitMatrix(content, size), format, response.getOutputStream(), MatrixToImageConfig.of(onColor, offColor));
    }

}
