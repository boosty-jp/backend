package co.jp.wever.graphql.infrastructure.converter.common;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

@Component
public class VertexConverter {
    private static Boolean hasKey(String key, Map<Object, Object> target) {
        if (Objects.isNull(target.get((key)))) {
            return false;
        }

        if (!(target.get(key) instanceof List)) {
            return false;
        }

        return ((List<Object>) target.get(key)).isEmpty() ? false : true;
    }

    public static String toString(String key, Map<Object, Object> target) {
        if (!hasKey(key, target)) {
            return "";
        }

        return ((List<Object>) target.get(key)).get(0).toString();
    }

    public static long toLong(String key, Map<Object, Object> target) {
        try {
            return Long.parseLong(toString(key, target));
        } catch (NumberFormatException e) {
            //TODO: ログ
            return 0;
        }
    }

    public static int toInt(String key, Map<Object, Object> target) {
        try {
            return Integer.parseInt(toString(key, target));
        } catch (NumberFormatException e) {
            //TODO: ログ
            return 0;
        }
    }

    public static String toId(Map<Object, Object> target) {
        try {

            Set<Object> keys = target.keySet();
            for (Object key : keys) {
                if (key.toString().equals("id")) {
                    return (target.get(key).toString());
                }
            }
            // TODO: Exception発生させる
            return "";
        } catch (NumberFormatException e) {
            //TODO: Exception発生させる
            return "";
        }
    }

    public static String toLabel(Map<Object, Object> target) {
        try {

            Set<Object> keys = target.keySet();
            for (Object key : keys) {
                if (key.toString().equals("label")) {
                    return (target.get(key).toString());
                }
            }
            // TODO: Exception発生させる
            return "";
        } catch (NumberFormatException e) {
            //TODO: Exception発生させる
            return "";
        }
    }
}

