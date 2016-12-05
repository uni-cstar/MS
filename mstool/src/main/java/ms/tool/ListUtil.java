package ms.tool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lucio on 16/8/12.
 */
public class ListUtil {

    /**
     * 列表是否为空
     *
     * @param lst
     * @return
     */
    public static boolean isNullOrEmpty(List lst) {
        return lst == null || lst.size() == 0;
    }

    /**
     * change list to super list
     *
     * @param datas
     * @param <T>
     * @return
     */
    public static <T> List<T> changeToSuperList(Class<T> tClass, List<? extends T> datas) {
        if (datas == null)
            return null;

        if (datas.size() == 0)
            return new ArrayList<>();

        List<T> result = new ArrayList<>();
        for (T item : datas) {
            result.add(item);
        }
        return result;
    }
}
