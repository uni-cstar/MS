package ms.frame.network;

public class ApiTResult<T> extends ApiResult {

    /// <summary>
    /// 返回的数据
    /// </summary>
    public T data ;

    public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}

	/// <summary>
    /// ApiResult
    /// </summary>
    /// <param name="ret">返回结果</param>
    /// <param name="errCode">错误代码</param>
    /// <param name="msg">返回信息</param>
    /// <param name="data">返回的数据</param>
    /// <param name="infoMsg">提示的数据</param>
    public ApiTResult(int ret, int errCode, String msg, T data, String infoMsg)
    {
    	super(ret, errCode, msg);
    	this.data = data;
    	setInfoMsg(infoMsg);
    }

    /// <summary>
    /// ApiResult
    /// </summary>
    /// <param name="data">返回的数据</param>
    public ApiTResult(T data)
    {
    	super();
        this.data = data;
    }

    /// <summary>
    /// ApiResult
    /// </summary>
    public ApiTResult()
    {
    	super();
    }
}
