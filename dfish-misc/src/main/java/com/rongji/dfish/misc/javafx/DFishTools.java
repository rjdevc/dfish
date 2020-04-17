package com.rongji.dfish.misc.javafx;

import com.rongji.dfish.base.crypto.Cryptor;
import com.rongji.dfish.base.exception.MarkedRuntimeException;
import com.rongji.dfish.base.img.ImageProcessConfig;
import com.rongji.dfish.base.img.ImageProcessorGroup;
import com.rongji.dfish.base.util.CryptoUtil;
import com.rongji.dfish.base.util.Utils;
import com.rongji.dfish.misc.chinese.PinyinConverter;
import com.rongji.dfish.misc.chinese.Trad2SimpConverter;
import com.rongji.dfish.misc.qrcode.MatrixToImageWriter;
import com.rongji.dfish.misc.senswords.SensitiveWordFilter;
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
import javafx.scene.text.Text;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * DFish辅助工具包桌面程序
 *
 * @author LinLW
 * @version 1.1 lamontYu 将工具包移到misc下,完成二维码功能,其他功能待补充
 * @date 2020-04-03
 * @since 5.0
 */
public class DFishTools extends Application {

    private static final Map<String, String> IMAGE_WAYS = new HashMap<>(3);

    static {
        IMAGE_WAYS.put("压缩", ImageProcessConfig.WAY_ZOOM);
        IMAGE_WAYS.put("剪切", ImageProcessConfig.WAY_CUT);
        IMAGE_WAYS.put("指定尺寸", ImageProcessConfig.WAY_RESIZE);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("DFish辅助工具v1.0");

        TabPane tabPane = new TabPane();
        tabPane.setSide(Side.TOP);
        tabPane.getTabs().add(getTab("加/解密", getCryptoLayout()));
        tabPane.getTabs().add(getTab("二维码生成", getQRCodeLayout()));
        tabPane.getTabs().add(getTab("中文处理", getChineseLayout()));
        tabPane.getTabs().add(getTab("敏感词加密", getSensitiveWordsLayout()));
        tabPane.getTabs().add(getTab("图片处理", getImageLayout(primaryStage)));
        tabPane.getTabs().add(getTab("关于", getCopyrightLayout()));

        primaryStage.setScene(new Scene(tabPane, 800, 600));
        primaryStage.show();
    }

    /**
     * 创建Tab标签
     *
     * @param text 文本
     * @param node 子节点
     * @return Tab
     */
    private Tab getTab(String text, Node node) {
        return getTab(text, node, false);
    }

    /**
     * @param text     文本
     * @param node     子节点
     * @param closable 是否可关闭
     * @return Tab
     */
    private Tab getTab(String text, Node node, boolean closable) {
        Tab tab = new Tab(text, node);
        tab.setClosable(closable);
        return tab;
    }

    /**
     * 版本信息
     *
     * @return Parent
     */
    private Parent getCopyrightLayout() {
        VBox root = new VBox();
        root.setPadding(PADDING);

        Text content = new Text("本工具由DFish开发团队提供，展示DFish后台框架积累的工具类，本工具上调用源码可参考com.rongji.dfish.misc.javafx.DFishTools");
        root.getChildren().add(content);

        return root;
    }

    /**
     * 敬请期待
     *
     * @return Parent
     */
    private Parent getComingSoonLayout() {
        VBox root = new VBox();
        root.setPadding(PADDING);
        root.getChildren().add(new Label("敬请期待"));
        return root;
    }

    /**
     * 加/解密工具
     *
     * @return Parent
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
        left.getChildren().add(textAreaOriginal);
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
        textAreaOriginal.setWrapText(true);

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

        middle.getChildren().add(new Label("压缩(文本不长可不开启)"));
        CheckBox gzip = new CheckBox();
        middle.getChildren().add(gzip);
        gzip.setSelected(false);
        gzip.setText("开启");

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
     *
     * @param t
     */
    private void error(Throwable t) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        String msg = null;
        if (t instanceof MarkedRuntimeException) {
            msg = t.getMessage();
        } else {
            msg = t.getClass().getName() + " : " + t.getMessage();
        }
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * 警告
     *
     * @param msg
     */
    private void warn(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * 提示
     *
     * @param msg
     */
    private void info(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    /**
     * 获取加密器
     *
     * @param alg      算法
     * @param encoding 编码
     * @param present  呈现方式
     * @param gzip     是否压缩
     * @param key      秘钥
     * @return Cryptor
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
     *
     * @return Parent
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
        originalText.setText("http://dfish.rongji.com");
        originalText.setWrapText(true);

        VBox center = new VBox();
        layout.getChildren().add(center);
        center.setPadding(PADDING);
        center.setMinWidth(120);
        center.setMaxWidth(120);
        center.setAlignment(Pos.CENTER);

        Button btn = new Button("生成二维码 >>");
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
                String source = originalText.getText();
                if (Utils.notEmpty(source)) {
                    int size = 240;
                    String format = "png";

                    MatrixToImageWriter.writeToStream(MatrixToImageWriter.toBitMatrix(source, size), format, baos);
                    bais = new ByteArrayInputStream(baos.toByteArray());
                    Image image = new Image(bais);
                    imageView.setImage(image);
                } else {
                    warn("请在左侧输入需要转码内容");
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
//
//    private static class Option {
//        private Object value;
//        private String text;
//
//        public Option(Object value) {
//            this.value = value;
//        }
//
//        public Option(Object value, String text) {
//            this.value = value;
//            this.text = text;
//        }
//
//        public Object getValue() {
//            return value;
//        }
//
//        public void setValue(Object value) {
//            this.value = value;
//        }
//
//        public String getText() {
//            return text;
//        }
//
//        public void setText(String text) {
//            this.text = text;
//        }
//    }

    /**
     * 中文处理
     *
     * @return Parent
     */
    private Parent getChineseLayout() {
        HBox layout = new HBox();

        VBox left = new VBox();
        layout.getChildren().add(left);
        left.setPadding(PADDING);
        left.getChildren().add(new Label("原文"));
        TextArea originalText = new TextArea();
        left.getChildren().add(originalText);
        originalText.setText("开发框架");
        originalText.setWrapText(true);
        VBox.setVgrow(originalText, Priority.ALWAYS);

        VBox center = new VBox();
        layout.getChildren().add(center);
        center.setMinWidth(150);
        center.setMaxWidth(150);
        center.setSpacing(10);
        center.setPadding(new Insets(10, 5, 10, 5));

//        FXCollections.

        VBox pinyinBox = new VBox();
        center.getChildren().add(pinyinBox);
        pinyinBox.setSpacing(5);

        pinyinBox.getChildren().add(new Label("拼音库"));
        ComboBox pinyinMode = new ComboBox();
        pinyinBox.getChildren().add(pinyinMode);
        // FIXME 不知道怎么做选项键值对,只能先采用这猥琐方案
        Map<String, Integer> modes = new HashMap<>(2);
        modes.put("通用库", PinyinConverter.MODE_COMMON);
        modes.put("人名库", PinyinConverter.MODE_PERSON_NAME);

        pinyinMode.setItems(FXCollections.observableArrayList("通用库", "人名库"));
        pinyinMode.setValue("通用库");

        pinyinBox.getChildren().add(new Label("拼音格式"));
        ComboBox pinyinFormat = new ComboBox();
        pinyinBox.getChildren().add(pinyinFormat);

        Map<String, Integer> formats = new HashMap<>(3);
        formats.put("不使用音调", PinyinConverter.FORMAT_WITHOUT_TONE);
        formats.put("数字音调", PinyinConverter.FORMAT_WITH_TONE_NUMBER);
        formats.put("正常音调", PinyinConverter.FORMAT_WITH_TONE_MARK);

        pinyinFormat.setItems(FXCollections.observableArrayList("不使用音调", "数字音调", "正常音调"));
        pinyinFormat.setValue("不使用音调");

        CheckBox pinyinSeparator = new CheckBox("是否需要分隔符");
        pinyinBox.getChildren().add(pinyinSeparator);

        Button pinyinBtn = new Button("生成拼音 >>");
        pinyinBox.getChildren().add(pinyinBtn);

        Button simplified2TraditionalBtn = new Button("简体->繁体");
        center.getChildren().add(simplified2TraditionalBtn);
        Button traditional2SimplifiedBtn = new Button("繁体->简体");
        center.getChildren().add(traditional2SimplifiedBtn);

        VBox right = new VBox();
        layout.getChildren().add(right);
        right.setPadding(PADDING);

        right.getChildren().add(new Label("结果"));
        TextArea resultText = new TextArea();
        right.getChildren().add(resultText);
        resultText.setWrapText(true);
        VBox.setVgrow(resultText, Priority.ALWAYS);

        pinyinBtn.setOnMouseClicked((event) -> {
            String source = originalText.getText();
            Integer mode = modes.get(pinyinMode.getValue());
            mode = mode == null ? PinyinConverter.MODE_COMMON : mode;
            Integer format = formats.get(pinyinFormat.getValue());
            format = format == null ? PinyinConverter.FORMAT_WITHOUT_TONE : format;
            String separator = pinyinSeparator.isSelected() ? " " : "";
            String result = new PinyinConverter(mode).convert(source, separator, format);
            resultText.setText(result);
        });

        simplified2TraditionalBtn.setOnMouseClicked((event) -> {
            String source = originalText.getText();
            String result = new Trad2SimpConverter(Trad2SimpConverter.SIMPLIFIED_TO_TRADITIONAL).convert(source);
            resultText.setText(result);
        });

        traditional2SimplifiedBtn.setOnMouseClicked((event) -> {
            String source = originalText.getText();
            String result = new Trad2SimpConverter(Trad2SimpConverter.TRADITIONAL_TO_SIMPLIFIED).convert(source);
            resultText.setText(result);
        });

        return layout;
    }

    /**
     * 敏感词处理
     *
     * @return Parent
     */
    private Parent getSensitiveWordsLayout() {
        HBox layout = new HBox();

        VBox left = new VBox();
        layout.getChildren().add(left);
        left.setPadding(PADDING);
        left.getChildren().add(new Label("原文"));
        TextArea originalText = new TextArea();
        left.getChildren().add(originalText);
        VBox.setVgrow(originalText, Priority.ALWAYS);
        originalText.setText("奈何老夫没文化 一句卧槽行天下");
        originalText.setWrapText(true);

        VBox center = new VBox();
        layout.getChildren().add(center);
        center.setMinWidth(150);
        center.setMaxWidth(150);
        center.setSpacing(10);
        center.setPadding(new Insets(10, 5, 10, 5));
        center.setAlignment(Pos.CENTER);

        VBox pinyinBox = new VBox();
        center.getChildren().add(pinyinBox);
        pinyinBox.setSpacing(5);

        Button pinyinBtn = new Button("敏感词过滤 >>");
        pinyinBox.getChildren().add(pinyinBtn);

        VBox right = new VBox();
        layout.getChildren().add(right);
        right.setPadding(PADDING);

        right.getChildren().add(new Label("结果"));
        TextArea resultText = new TextArea();
        right.getChildren().add(resultText);
        resultText.setWrapText(true);
        VBox.setVgrow(resultText, Priority.ALWAYS);

        pinyinBtn.setOnMouseClicked((event) -> {
            String source = originalText.getText();
            String result = SensitiveWordFilter.getInstance().replace(source);
            resultText.setText(result);
        });

        return layout;
    }


    private Parent getImageLayout(Stage primaryStage) {

        HBox layout = new HBox();
        layout.setPadding(PADDING);
        layout.setSpacing(5);
        HBox.setHgrow(layout, Priority.ALWAYS);

//        ScrollPane scrollPane = new ScrollPane();
//        layout.getChildren().add(scrollPane);
//        scrollPane.setFitToHeight(true);
//        scrollPane.setFitToWidth(true);

        VBox sourceBox = new VBox();
        layout.getChildren().add(sourceBox);
        sourceBox.setSpacing(5);
        sourceBox.setMinWidth(400);
        sourceBox.setMaxWidth(400);
        VBox.setVgrow(sourceBox, Priority.ALWAYS);

        Button chooseBtn = new Button("选择图片");
        sourceBox.getChildren().add(chooseBtn);

        Label sourceFilePath = new Label("");
        sourceBox.getChildren().add(sourceFilePath);

        ScrollPane scrollPane = new ScrollPane();
        sourceBox.getChildren().add(scrollPane);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);

        ImageView imageView = new ImageView();
        scrollPane.setContent(imageView);

        VBox destBox = new VBox();
        destBox.setSpacing(5);
        VBox.setVgrow(destBox, Priority.ALWAYS);
        HBox.setHgrow(destBox, Priority.ALWAYS);
        layout.getChildren().add(destBox);

        Button destBtn = new Button("指定输出目录");
        destBox.getChildren().add(destBtn);

        TextField destDir = new TextField(System.getProperty("user.dir"));
        destBox.getChildren().add(destDir);

        destBox.getChildren().add(new Label("输出宽度(-1代表原始大小)"));
        Spinner widthInput = new Spinner(-1, 99999999, -1);
        widthInput.setEditable(true);
        destBox.getChildren().add(widthInput);
        destBox.getChildren().add(new Label("输出高度(-1代表原始大小)"));
        Spinner heightInput = new Spinner(-1, 99999999, -1);
        heightInput.setEditable(true);
        destBox.getChildren().add(heightInput);

        destBox.getChildren().add(new Label("加工方式"));
        ComboBox wayBox = new ComboBox();
        destBox.getChildren().add(wayBox);

        wayBox.setItems(FXCollections.observableArrayList("缩放", "剪切", "指定尺寸"));
        wayBox.setValue("缩放");

        destBox.getChildren().add(new Label("输出文件别名:{FILE_NAME}_{WIDTH}x{HEIGHT}.{EXTENSION}"));
//        destBox.getChildren().add(new Label("预计输出文件:"));
//        Label outputFileLabel = new Label("请在左侧选择需要加工的图片");
//        destBox.getChildren().add(outputFileLabel);

        Button executeBtn = new Button("开始加工");
        destBox.getChildren().add(executeBtn);
//        executeBtn.setDisable(true);

        ImageProcessConfig processConfig = new ImageProcessConfig();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("选择图片");
        fileChooser.setInitialDirectory(new File(System.getProperty("user.home")));
        fileChooser.getExtensionFilters().addAll(
                new FileChooser.ExtensionFilter("All Images", "*.jpg;*.gif;*.bmp;*.png;"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg"),
                new FileChooser.ExtensionFilter("GIF", "*.gif"),
                new FileChooser.ExtensionFilter("BMP", "*.bmp"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );

        chooseBtn.setOnAction((event) -> {
            File file = fileChooser.showOpenDialog(primaryStage);
            sourceFilePath.setText(file.getAbsolutePath());
            fileChooser.setInitialDirectory(file.getParentFile());
            try (InputStream input = new FileInputStream(file)) {
                imageView.setImage(new Image(input));
//                setProcessConfig(processConfig, outputFileLabel, sourceFilePath, destDir, widthInput, heightInput, wayBox, executeBtn);
            } catch (Exception e) {
                error(e);
            }
        });

        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("输出目录");
        dirChooser.setInitialDirectory(new File(System.getProperty("user.dir")));
        destBtn.setOnAction((event) -> {
            File file = dirChooser.showDialog(primaryStage);
            destDir.setText(file.getAbsolutePath());
            dirChooser.setInitialDirectory(file);
        });

        executeBtn.setOnMouseClicked((event) -> {
            try {
                File sourceFile = null;
                if (Utils.notEmpty(sourceFilePath.getText())) {
                    try {
                        sourceFile = new File(sourceFilePath.getText());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (sourceFile == null || !sourceFile.exists()) {
                    warn("请在左侧选择需要加工的图片");
                    return;
                }
                String destDirText = destDir.getText();
                if (Utils.isEmpty(destDirText)) {
                    warn("请选择输出目录");
                    return;
                }
                ImageProcessorGroup imageProcessorGroup = ImageProcessorGroup.of(sourceFile).setDest(destDirText).setAliasPattern("{FILE_NAME}_{ALIAS}.{EXTENSION}");
                Integer width = getIntegerValue(widthInput);
                Integer height = getIntegerValue(heightInput);
                width = width == null ? -1 : width;
                height = height == null ? -1 : height;

                String alias = width + "x" + height;

                // FIXME ComboBox选项问题不清楚怎么做,这里做法比较猥琐
                String way = IMAGE_WAYS.get(wayBox.getValue());
                way = Utils.isEmpty(way) ? ImageProcessConfig.WAY_ZOOM : way;

                imageProcessorGroup.process(width, height, way, alias);

                StringBuilder fileName = new StringBuilder(destDir.getText());
                if (!destDirText.endsWith(File.separator)) {
                    fileName.append(File.separatorChar);
                }
                String sourceFileName = sourceFile.getName();
                int dotSplit = sourceFileName.lastIndexOf(".");
                if (dotSplit >= 0) {
                    fileName.append(sourceFileName.substring(0, dotSplit));
                    fileName.append('_').append(width).append('x').append(height).append('.').append(sourceFileName.substring(dotSplit + 1));
                } else {
                    fileName.append(sourceFileName).append('_').append(width).append('x').append(height);
                }
                File destFile = new File(fileName.toString());

                if (destFile.exists()) {
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("确认信息对话框");
                    alert.setHeaderText(null);
                    alert.setContentText("文件[" + destFile.getName() + "]已存在,确认覆盖文件?");

                    Optional result = alert.showAndWait();
                    if (result.get() == ButtonType.OK) {
                        imageProcessorGroup.execute();
                        info("图片加工完成");
                    }
                } else {
                    imageProcessorGroup.execute();
                    info("图片加工完成");
                }

            } catch (Exception e) {
                error(e);
            }
        });

        return layout;
    }

    private Integer getIntegerValue(Spinner spinner) {
        String valueText = spinner.getEditor().getText();
        if (Utils.notEmpty(valueText)) {
            try {
                return Integer.parseInt(valueText);
            } catch (Exception e) {
                throw new MarkedRuntimeException("输入值不合法[" + valueText + "]");
            }
        }

        return null;
    }

//    private void setProcessConfig(ImageProcessConfig processConfig, Label outputFileLabel, Label sourceFilePath, TextField destDir, Spinner widthInput, Spinner heightInput, ComboBox wayBox, Button executeBtn) {
//        String sourceName = sourceFilePath.getText();
//        if (Utils.isEmpty(sourceName)) {
//            executeBtn.setDisable(true);
//            outputFileLabel.setText("请在左侧选择需要加工的图片");
//            throw new MarkedRuntimeException("请在左侧选择需要加工的图片");
//        }
//        String outputFileDir = destDir.getText();
//        if (Utils.isEmpty(outputFileDir)) {
//            executeBtn.setDisable(true);
//            outputFileLabel.setText("请选择输出目录");
//            throw new MarkedRuntimeException("请选择输出目录");
//        }
//        Integer width = getIntegerValue(widthInput);
//        Integer height = getIntegerValue(heightInput);
//        width = width == null ? -1 : width;
//        height = height == null ? -1 : height;
//
//        // FIXME ComboBox选项问题不清楚怎么做,这里做法比较猥琐
//        String way = IMAGE_WAYS.get(wayBox.getValue());
//        way = Utils.isEmpty(way) ? ImageProcessConfig.WAY_ZOOM : way;
//
//        executeBtn.setDisable(false);
//
//        StringBuilder outputFileName = new StringBuilder(outputFileDir);
//        if (!outputFileDir.endsWith(File.pathSeparator)) {
//            outputFileName.append(File.pathSeparatorChar);
//        }
//
//        int pathSplit = sourceName.lastIndexOf(File.pathSeparator);
//        if (pathSplit > -1) {
//            sourceName.substring(pathSplit + 1);
//        }
//        int dotSplit = sourceName.lastIndexOf(".");
//
//        String preName = dotSplit > 0 ? sourceName.substring(0, dotSplit) : sourceName;
//        String extension = dotSplit > 0 ? sourceName.substring(dotSplit + 1) : "";
//
//        // 名字根式参照上面格式要求{FILE_NAME}_{ALIAS}.{EXTENSION}
//        outputFileName.append(preName).append('_').append(width).append('x').append(height).append('.').append(extension);
//        outputFileLabel.setText(outputFileName.toString());
//
//        processConfig.setWidth(width);
//        processConfig.setHeight(height);
//        processConfig.setWay(way);
//        processConfig.setAlias(width + "x" + height);
//    }

}
