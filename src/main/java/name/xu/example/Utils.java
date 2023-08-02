package name.xu.example;

import org.apache.batik.transcoder.Transcoder;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.TranscoderOutput;
import org.apache.batik.transcoder.image.PNGTranscoder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;

public class Utils {
    public static boolean checkGoogle() {
        try {
            InetAddress address = InetAddress.getByName("www.google.com");
            boolean isConnected = address.isReachable(5000); // 5000毫秒的超时时间
            return isConnected;

        } catch (UnknownHostException e) {
            System.out.println("无法解析主机名");
            return false;
        } catch (Exception e) {
            System.out.println("发生异常：" + e.getMessage());
            return false;
        }
    }

    public static void svg2Png(File src, File outDir) {
        if (!outDir.exists()) outDir.mkdir();
        int iconSize = 16;
        while (iconSize <= 1024) {
            convert(iconSize, src, outDir);
            iconSize *= 2;
        }
    }

    private static void convert(float iconSize, File src, File outDir) {
        Transcoder transcoder = new PNGTranscoder();
        //设置png图片的宽和长
        transcoder.addTranscodingHint(PNGTranscoder.KEY_WIDTH, iconSize);
        transcoder.addTranscodingHint(PNGTranscoder.KEY_HEIGHT, iconSize);
        try (
                FileInputStream in = new FileInputStream(src)
        ) {
            TranscoderInput input = new TranscoderInput(in);
            String fileName = src.getName().replace(".svg", "");
            FileOutputStream fileOutputStream = new FileOutputStream(outDir.toPath().resolve(fileName + "-" + (int) iconSize + ".png").toFile());
            TranscoderOutput output = new TranscoderOutput(fileOutputStream);
            transcoder.transcode(input, output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
