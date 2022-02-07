package cn.master.tsim.common;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author by 11's papa on 2022年02月07日
 * @version 1.0.0
 */
@Data
@AllArgsConstructor
public class UploadFileResponse {
    private String fileName;
    private String fileType;
    private long fileSize;
    private String fileNewName;
    private String fileStorageUrl;
    private String docFlag;
}
