package ms.doc;

/**
 * Created by Lucio on 16/12/12.
 */
public class Ms2016 {
    private static Ms2016 ourInstance = new Ms2016();

    public static Ms2016 getInstance() {
        return ourInstance;
    }

    private Ms2016() {
    }

    public String mainResult(){
        /**
         * 1.可以自定义微信的回调操作activity(PS:优信大写包名,清单文件小写,导致微信配置的回调activity无法回调)
         * 2.类似@xxx在编辑框中的实现.(PS:个人尝试出的也许是非正统实现的一种方式,主要禁止用户能够将光标点击进入@
         * xx之间以及能删掉单个字符时删除整个文本)
         */
        return "";
    }
}
