package ms.frame.network;

import java.io.File;
import java.net.FileNameMap;
import java.net.URLConnection;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by SupLuo on 2016/6/27.
 */
public class MSFileUploader {

    /**
     * create the request for okhttp3.0
     *
     * @param fileName
     * @param content
     * @return
     */
    public static Request createRequest(String url, String fileName, File content) {
        RequestBody body = RequestBody.create(MediaType.parse(getGuessMimeType(fileName)), content);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart(fileName, fileName, body).build();
        return new Request.Builder().url(url).post(requestBody).build();
    }


    /**
     * create the request for okhttp3.0
     *
     * @param fileName
     * @param content
     * @return
     */
    public static Request createRequest(String url, String fileName, String content) {
        RequestBody body = RequestBody.create(MediaType.parse(getGuessMimeType(fileName)), content);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart(fileName, fileName, body).build();
        return new Request.Builder().url(url).post(requestBody).build();
    }

    /**
     * create the request for okhttp3.0
     *
     * @param fileName
     * @param content
     * @return
     */
    public static Request createRequest(String url, String fileName, byte[] content) {
        RequestBody body = RequestBody.create(MediaType.parse(getGuessMimeType(fileName)), content);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart(fileName, fileName, body).build();
        return new Request.Builder().url(url).post(requestBody).build();
    }

    /**
     * create the request for okhttp3.0
     *
     * @param fileName  the file name
     * @param content   the content
     * @param offset    the offset for content
     * @param byteCount the request count for the content
     * @return
     */
    public static Request createRequest(String url, String fileName, byte[] content, int offset, int byteCount) {
        RequestBody body = RequestBody.create(MediaType.parse(getGuessMimeType(fileName)), content, offset, byteCount);
        RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart(fileName, fileName, body).build();
        return new Request.Builder().url(url).post(requestBody).build();
    }


    /**
     * create the request body for retrofit 2.0
     *
     * @param fileName
     * @param content
     * @return
     */
    public static MultipartBody.Part createRequestBodyForRetrofit(String fileName, File content) {
        RequestBody requestBody = RequestBody.create(MediaType.parse(getGuessMimeType(fileName)), content);
        return MultipartBody.Part.createFormData(fileName, fileName, requestBody);
    }


    /**
     * create the request body for retrofit 2.0
     *
     * @param fileName
     * @param content
     * @return
     */
    public static MultipartBody.Part createRequestBodyForRetrofit(String fileName, String content) {
        RequestBody requestBody = RequestBody.create(MediaType.parse(getGuessMimeType(fileName)), content);
        return MultipartBody.Part.createFormData(fileName, fileName, requestBody);
    }

    /**
     * create the request body for retrofit 2.0
     *
     * @param fileName
     * @param content
     * @return
     */
    public static MultipartBody.Part createRequestBodyForRetrofit(String fileName, byte[] content) {
        RequestBody requestBody = RequestBody.create(MediaType.parse(getGuessMimeType(fileName)), content);
        return MultipartBody.Part.createFormData(fileName, fileName, requestBody);
    }

    /**
     * create the request body for retrofit 2.0
     *
     * @param fileName  the file name
     * @param content   the content
     * @param offset    the offset for content
     * @param byteCount the request count for the content
     * @return
     */
    public static MultipartBody.Part createRequestBodyForRetrofit(String fileName, byte[] content, int offset, int byteCount) {
        RequestBody requestBody = RequestBody.create(MediaType.parse(getGuessMimeType(fileName)), content, offset, byteCount);
        return MultipartBody.Part.createFormData(fileName, fileName, requestBody);
    }

    /**
     * get the possible MimeType from the path
     *
     * @param path
     * @return
     */
    private static String getGuessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }
}
