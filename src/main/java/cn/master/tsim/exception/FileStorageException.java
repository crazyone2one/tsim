package cn.master.tsim.exception;

/**
 * 文件存储异常
 *
 * @author by 11's papa on 2022年02月07日
 * @version 1.0.0
 */
public class FileStorageException extends RuntimeException {
    public FileStorageException(String message) {
        super(message);
    }

    public FileStorageException(String message, Throwable cause) {
        super(message,cause);
    }
}
