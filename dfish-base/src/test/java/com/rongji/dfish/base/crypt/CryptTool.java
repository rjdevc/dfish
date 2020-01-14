package com.rongji.dfish.base.crypt;

import com.rongji.dfish.base.crypto.Cryptor;
import com.rongji.dfish.base.util.CryptoUtil;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class CryptTool extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage.setTitle("DFish加解密辅助工具 V0.9");

        HBox rootLayout=new HBox();
//        rootLayout.setPadding(new Insets(10));

        VBox left=new VBox();
        rootLayout.getChildren().add(left);
        left.setPadding(new Insets(10));

        Label lb1=new Label();
        lb1.setText("原文/解密");
        left.getChildren().add(lb1);

        TextArea textAreaOriginal=new TextArea();
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



        VBox middle=new VBox();
//        middle.setPadding(new Insets(10));
        middle.setPadding(new Insets(10,0,5,0));
        middle.setSpacing(5);
        middle.setMaxWidth(150);
        middle.setMinWidth(150);
        rootLayout.getChildren().add(middle);
        middle.getChildren().add(new Label("算法"));
        ComboBox alg=new ComboBox();
        middle.getChildren().add(alg);
        alg.setItems(FXCollections.observableArrayList(
                CryptoUtil.ALGORITHM_BLOWFISH,CryptoUtil.ALGORITHM_AES,CryptoUtil.ALGORITHM_DES,
                CryptoUtil.ALGORITHM_TRIPLE_DES,CryptoUtil.ALGORITHM_SM4,
                CryptoUtil.ALGORITHM_MD5,CryptoUtil.ALGORITHM_SHA1,CryptoUtil.ALGORITHM_SHA256,
                CryptoUtil.ALGORITHM_SHA256,
                "不加密"));
        alg.setValue("Blowfish");

        middle.getChildren().add(new Label("字符集"));
        ComboBox encoding=new ComboBox();
        middle.getChildren().add(encoding);
        encoding.setItems(FXCollections.observableArrayList("UTF-8","GBK"));
        encoding.setValue("UTF-8");

        middle.getChildren().add(new Label("表示方式"));
        ComboBox present=new ComboBox();
        middle.getChildren().add(present);
        present.setItems(FXCollections.observableArrayList("HEX","BASE64","BASE32","BASE64_URLSAFE","RAW"));
        present.setValue("BASE32");

        middle.getChildren().add(new Label("压缩"));
        CheckBox gzip=new CheckBox();
        middle.getChildren().add(gzip);
        gzip.setSelected(false);
        gzip.setText("文本不长时，不用开启");

        middle.getChildren().add(new Label("秘钥"));
        TextField key=new TextField();
        middle.getChildren().add(key);
        key.setText("RJ002474");

        HBox btnGroup=new HBox();
        btnGroup.setSpacing(10);
        middle.getChildren().add(btnGroup);
        btnGroup.setAlignment(Pos.CENTER);

        Button decryptBtn=new Button("<< 解密");
        Button encryptBtn=new Button("加密 >>");
        btnGroup.getChildren().add(decryptBtn);
        btnGroup.getChildren().add(encryptBtn);


        VBox right=new VBox();
        rootLayout.getChildren().add(right);
        right.setPadding(new Insets(10));

        Label lb3=new Label();
        lb3.setText("密文/加密");
        right.getChildren().add(lb3);

        TextArea textAreaEncrypted=new TextArea();
        right.getChildren().add(textAreaEncrypted);
        textAreaEncrypted.setWrapText(true);

        VBox.setVgrow( textAreaOriginal,Priority.ALWAYS);
        VBox.setVgrow( textAreaEncrypted,Priority.ALWAYS);

        //动作
        encryptBtn.setOnMouseClicked((EventHandler<MouseEvent>)(event)->{
            try {
                Cryptor cryptor=getCryptor((String)alg.getValue(),(String)encoding.getValue(),(String)present.getValue(), gzip.isSelected(),
                        key.getText());
                textAreaEncrypted.setText(cryptor.encrypt(textAreaOriginal.getText()));
            }catch (Throwable t){
                alert(t);
            }
        });
        decryptBtn.setOnMouseClicked((EventHandler<MouseEvent>)(event)->{
            try{
                Cryptor cryptor=getCryptor((String)alg.getValue(),(String)encoding.getValue(),(String)present.getValue(), gzip.isSelected(),
                        key.getText());
                textAreaOriginal.setText(cryptor.decrypt(textAreaEncrypted.getText()));
            }catch (Throwable t){
                alert(t);
            }
        });

        primaryStage.setScene(new Scene(rootLayout, 800, 600));
        primaryStage.show();
    }

    private void alert(Throwable t){
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.titleProperty().set("错误");
        alert.headerTextProperty().set(t.getClass().getName()+" : "+ t.getMessage());
        alert.showAndWait();
    }


    private Cryptor getCryptor(String alg, String encoding, String present, boolean gzip, String key){
        int intPresent=0;
        switch (present){
            case "HEX":
                intPresent=CryptoUtil.PRESENT_HEX;
                break;
            case "BASE64":
                intPresent=CryptoUtil.PRESENT_BASE64;
                break;
            case "BASE32":
                intPresent=CryptoUtil.PRESENT_BASE32;
                break;
            case "BASE64_URLSAFE":
                intPresent=CryptoUtil.PRESENT_BASE64_URLSAFE;
                break;
            case "RAW":
                intPresent=CryptoUtil.PRESENT_RAW;
                break;
            default:
        }
        if("不加密".equals(alg)){
            alg=CryptoUtil.ALGORITHM_NONE;
        }
        return CryptoUtil.prepareCryptor(alg,key).present(intPresent).encoding(encoding).gzip(gzip).build();
    }
}
