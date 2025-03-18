package org.mutantcat.justsimple.web;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.multipart.FileUpload;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUploadHandler {

    public static class TempFile {
        private FileUpload fileUpload;
        private ByteBuf byteBuf;

        public TempFile(FileUpload fileUpload, ByteBuf byteBuf) {
            this.fileUpload = fileUpload;
            this.byteBuf = byteBuf.copy();
        }

        public FileUpload getFileUpload() {
            return fileUpload;
        }

        public ByteBuf getByteBuf() {
            return byteBuf;
        }
    }

    public static TempFile saveToTemporaryFile(FileUpload fileUpload,ByteBuf byteBuf) {
        return new TempFile(fileUpload, byteBuf);
    }

    public static void saveByteBufToFile(ByteBuf byteBuf, String filePath) {
        try (FileOutputStream fos = new FileOutputStream(filePath);
             FileChannel fileChannel = fos.getChannel()) {

            // 将 ByteBuf 数据写入文件
            byteBuf.readBytes(fileChannel, byteBuf.readableBytes());

            System.out.println("File saved successfully to: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Failed to save the binary data to file.");
        } finally {
            // 确保释放 ByteBuf
            byteBuf.release();
        }
    }
}

