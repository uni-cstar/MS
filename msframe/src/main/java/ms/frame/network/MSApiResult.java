package ms.frame.network;

public class MSApiResult<T> {

    /// <summary>
    /// 返回的数据
    /// </summary>
    private T data;

    /**
     * 返回结果（0-成功；>0-失败）
     * ret=0 成功返回/ret=1 参数错误/ret=2 频率受限 /ret=3 Token无效 /ret=4 服务器内部错误 /ret=5 用户操作错误
     */
    private int ret;

    /**
     * 错误代码，因功能而异
     */
    private int errCode;

    /**
     * 返回信息
     */
    private String msg;

    /**
     * API附加信息
     */
    private String infoMsg;

    /**
     * 请求序列
     */
    private long seqId;

    /**
     * @param ret
     * @param errCode
     * @param msg
     */
    public MSApiResult(int ret, int errCode, String msg) {
        this.ret = ret;
        this.errCode = errCode;
        this.msg = msg;
        infoMsg = "";
    }

    /**
     *
     */
    public MSApiResult() {
    }


    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public int getErrCode() {
        return errCode;
    }

    public void setErrCode(int errCode) {
        this.errCode = errCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getInfoMsg() {
        return infoMsg;
    }

    public void setInfoMsg(String infoMsg) {
        this.infoMsg = infoMsg;
    }

    public long getSeqId() {
        return seqId;
    }

    public void setSeqId(long seqId) {
        this.seqId = seqId;
    }

}
