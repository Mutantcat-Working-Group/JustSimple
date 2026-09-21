package org.mutantcat.justsimple.web;

import io.netty.buffer.ByteBuf;
import io.netty.handler.codec.http.multipart.FileUpload;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileUploadHandler {

    private FileUploadHandler() {
    }

    public static class TempFile {
        private final FileUpload fileUpload;
        private final ByteBuf byteBuf;
        private final String fileName;

        public TempFile(FileUpload fileUpload, ByteBuf byteBuf) {
            this.fileUpload = fileUpload;
            // 直接持有引用,由调用方负责释放;copy 会双倍占用堆外内存
            this.byteBuf = byteBuf;
            this.fileName = fileUpload.getFilename();
        }

        public FileUpload getFileUpload() {
            return fileUpload;
        }

        public ByteBuf getByteBuf() {
            return byteBuf;
        }

        public String getFileName() {
            return fileName;
        }
    }

    public static TempFile saveToTemporaryFile(FileUpload fileUpload, ByteBuf byteBuf) {
        return new TempFile(fileUpload, byteBuf);
    }

    public static void saveByteBufToFile(ByteBuf byteBuf, String filePath) {
        if (byteBuf == null || filePath == null) {
            return;
        }
        try (FileOutputStream fos = new FileOutputStream(filePath);
             FileChannel fileChannel = fos.getChannel()) {
            // 将 ByteBuf 数据写入文件
            byteBuf.readBytes(fileChannel, byteBuf.readableBytes());
            System.out.println("File saved successfully to: " + filePath);
        } catch (IOException e) {
            System.err.println("Failed to save the binary data to file " + filePath + ": " + e.getMessage());
        } finally {
            // 确保释放 ByteBuf
            byteBuf.release();
        }
    }
}