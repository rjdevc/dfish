package com.rongji.dfish.misc;

import com.rongji.dfish.base.crypto.Cryptor;
import com.rongji.dfish.base.util.CryptoUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.misc.qrcode.MatrixToImageWriter;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * DFish辅助工具包桌面程序
 * @since 5.0
 * @date 2020-04-03
 * @version 1.0 LinLW 完成加解密初版功能
 * @version 1.1 lamontYu 将工具包移到misc下,完成二维码功能,其他功能待补充
 * @author lamontYu
 */
public class DFishTools extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("DFish辅助工具V0.9");

        TabPane tabPane = new TabPane();
        tabPane.setSide(Side.TOP);
        tabPane.getTabs().add(getTab("加/解密", getCryptoLayout()));
        tabPane.getTabs().add(getTab("二维码", getQRCodeLayout()));
        tabPane.getTabs().add(getTab("中文处理", getComingSoonLayout(), true));
        tabPane.getTabs().add(getTab("敏感词加密", getComingSoonLayout(), true));
        tabPane.getTabs().add(getTab("图片处理", getComingSoonLayout(), true));

        primaryStage.setScene(new Scene(tabPane, 800, 600));
        primaryStage.show();
    }

    private Tab getTab(String text, Node node) {
        return getTab(text, node, false);
    }

    private Tab getTab(String text, Node node, boolean closable) {
        Tab tab = new Tab(text, node);
        tab.setClosable(closable);
        return tab;
    }

    private Parent getComingSoonLayout() {
        VBox root = new VBox();
        root.setPadding(PADDING);
        root.getChildren().add(new Label("敬请期待"));
        return root;
    }

    /**
     * 加/解密工具
     *
     * @return
     */
    private Parent getCryptoLayout() {
        HBox rootLayout = new HBox();
//        rootLayout.setPadding(new Insets(10));

        VBox left = new VBox();
        rootLayout.getChildren().add(left);
        left.setPadding(new Insets(10));

        Label lb1 = new Label();
        lb1.setText("原文/解密");
        left.getChildren().add(lb1);

        TextArea textAreaOriginal = new TextArea();
        textAreaOriginal.setText("君不见，黄河之水天上来⑵，奔流到海不复回。\n" +
                "君不见，高堂明镜悲白发，朝如青丝暮成雪⑶。\n" +
                "人生得意须尽欢⑷，莫使金樽空对月。\n" +
                "天生我材必有用，千金散尽还复来。\n" +
                "烹羊宰牛且为乐，会须一饮三百杯⑸。\n" +
                "岑夫子，丹丘生⑹，将进酒，杯莫停⑺。\n" +
                "与君歌一曲⑻，请君为我倾耳听⑼。\n" +
                "钟鼓馔玉不足贵⑽，但愿长醉不复醒⑾。\n" +
                "古来圣贤皆寂寞，惟有饮者留其名。\n" +
                "陈王昔时宴平乐，斗酒十千恣欢谑⑿。\n" +
                "主人何为言少钱⒀，径须沽取对君酌⒁。\n" +
                "五花马⒂，千金裘，呼儿将出换美酒，与尔同销万古愁⒃。 ");
        left.getChildren().add(textAreaOriginal);


        VBox middle = new VBox();
//        middle.setPadding(new Insets(10));
        middle.setPadding(new Insets(10, 0, 5, 0));
        middle.setSpacing(5);
        middle.setMaxWidth(150);
        middle.setMinWidth(150);
        rootLayout.getChildren().add(middle);
        middle.getChildren().add(new Label("算法"));
        ComboBox alg = new ComboBox();
        middle.getChildren().add(alg);
        alg.setItems(FXCollections.observableArrayList(
                CryptoUtil.ALGORITHM_BLOWFISH, CryptoUtil.ALGORITHM_AES, CryptoUtil.ALGORITHM_DES,
                CryptoUtil.ALGORITHM_TRIPLE_DES, CryptoUtil.ALGORITHM_SM4,
                CryptoUtil.ALGORITHM_MD5, CryptoUtil.ALGORITHM_SHA1, CryptoUtil.ALGORITHM_SHA256,
                CryptoUtil.ALGORITHM_SHA256, "不加密"));
        alg.setValue("Blowfish");

        middle.getChildren().add(new Label("字符集"));
        ComboBox encoding = new ComboBox();
        middle.getChildren().add(encoding);
        encoding.setItems(FXCollections.observableArrayList("UTF-8", "GBK"));
        encoding.setValue("UTF-8");

        middle.getChildren().add(new Label("表示方式"));
        ComboBox present = new ComboBox();
        middle.getChildren().add(present);
        present.setItems(FXCollections.observableArrayList("HEX", "BASE64", "BASE32", "BASE64_URLSAFE", "RAW"));
        present.setValue("BASE32");

        middle.getChildren().add(new Label("压缩"));
        CheckBox gzip = new CheckBox();
        middle.getChildren().add(gzip);
        gzip.setSelected(false);
        gzip.setText("文本不长时，不用开启");

        middle.getChildren().add(new Label("秘钥"));
        TextField key = new TextField();
        middle.getChildren().add(key);
        key.setText("RJ002474");

        HBox btnGroup = new HBox();
        btnGroup.setSpacing(10);
        middle.getChildren().add(btnGroup);
        btnGroup.setAlignment(Pos.CENTER);

        Button decryptBtn = new Button("<< 解密");
        Button encryptBtn = new Button("加密 >>");
        btnGroup.getChildren().add(decryptBtn);
        btnGroup.getChildren().add(encryptBtn);

        VBox right = new VBox();
        rootLayout.getChildren().add(right);
        right.setPadding(new Insets(10));

        Label lb3 = new Label();
        lb3.setText("密文/加密");
        right.getChildren().add(lb3);

        TextArea textAreaEncrypted = new TextArea();
        right.getChildren().add(textAreaEncrypted);
        textAreaEncrypted.setWrapText(true);

        VBox.setVgrow(textAreaOriginal, Priority.ALWAYS);
        VBox.setVgrow(textAreaEncrypted, Priority.ALWAYS);

        //动作
        encryptBtn.setOnMouseClicked((event) -> {
            try {
                Cryptor cryptor = getCryptor((String) alg.getValue(), (String) encoding.getValue(), (String) present.getValue(), gzip.isSelected(),
                        key.getText());
                textAreaEncrypted.setText(cryptor.encrypt(textAreaOriginal.getText()));
            } catch (Throwable t) {
                error(t);
            }
        });
        decryptBtn.setOnMouseClicked((event) -> {
            try {
                Cryptor cryptor = getCryptor((String) alg.getValue(), (String) encoding.getValue(), (String) present.getValue(), gzip.isSelected(),
                        key.getText());
                textAreaOriginal.setText(cryptor.decrypt(textAreaEncrypted.getText()));
            } catch (Throwable t) {
                error(t);
            }
        });
        return rootLayout;
    }

    /**
     * 错误
     * @param t
     */
    private void error(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText(t.getClass().getName() + " : " + t.getMessage());
        alert.showAndWait();
    }

    /**
     * 警告
     * @param msg
     */
    private void warn(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(msg);
        alert.showAndWait();
    }

    /**
     * 提示
     * @param msg
     */
    private void info(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(msg);
        alert.showAndWait();
    }

    /**
     * 获取加密器
     * @param alg
     * @param encoding
     * @param present
     * @param gzip
     * @param key
     * @return
     */
    private Cryptor getCryptor(String alg, String encoding, String present, boolean gzip, String key) {
        int intPresent = 0;
        switch (present) {
            case "HEX":
                intPresent = CryptoUtil.PRESENT_HEX;
                break;
            case "BASE64":
                intPresent = CryptoUtil.PRESENT_BASE64;
                break;
            case "BASE32":
                intPresent = CryptoUtil.PRESENT_BASE32;
                break;
            case "BASE64_URLSAFE":
                intPresent = CryptoUtil.PRESENT_BASE64_URLSAFE;
                break;
            case "RAW":
                intPresent = CryptoUtil.PRESENT_RAW;
                break;
            default:
        }
        if ("不加密".equals(alg)) {
            alg = CryptoUtil.ALGORITHM_NONE;
        }
        return CryptoUtil.prepareCryptor(alg, key).present(intPresent).encoding(encoding).gzip(gzip).build();
    }

    private static final Insets PADDING = new Insets(10);

    /**
     * 二维码
     * @return
     */
    private Parent getQRCodeLayout() {
        HBox layout = new HBox();

        VBox left = new VBox();
        layout.getChildren().add(left);
        left.setPadding(PADDING);
        left.setMaxWidth(400);
        TextArea originalText = new TextArea();
        left.getChildren().add(originalText);
        VBox.setVgrow(originalText, Priority.ALWAYS);

        VBox center = new VBox();
        layout.getChildren().add(center);
        center.setPadding(PADDING);
        center.setMinWidth(120);
        center.setMaxWidth(120);
        center.setAlignment(Pos.CENTER);

        Button btn = new Button("生成二维码>>");
        center.getChildren().add(btn);

        VBox right = new VBox();
        layout.getChildren().add(right);
        right.setPadding(PADDING);
        right.setAlignment(Pos.CENTER);

        ImageView imageView = new ImageView();
//        Image image = new Image("");
//        imageView.setImage(image);
        right.getChildren().add(imageView);

        btn.setOnMouseClicked((event) -> {
            ByteArrayInputStream bais = null;
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                String content = originalText.getText();
                if (Utils.isEmpty(content)) {
                    warn("请在左侧输入文本");
                } else {
                    int size = 240;
                    String format = "png";

                    MatrixToImageWriter.writeToStream(MatrixToImageWriter.toBitMatrix(content, size), format, baos);
                    bais = new ByteArrayInputStream(baos.toByteArray());
                    Image image = new Image(bais);
                    imageView.setImage(image);
                }
            } catch (Throwable t) {
                error(t);
            } finally {
                if (bais != null) {
                    try {
                        bais.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        return layout;
    }
}
