package co.jp.wever.graphql.infrastructure.converter;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import co.jp.wever.graphql.infrastructure.datamodel.Plan;

@Component
public class PlanConverter {
    public Plan toPlan(Map<Object, Object> result) {

        return Plan.builder().title(convertToString("title", result)).description(convertToString("description", result)).image(convertToString("image", result)).build();
    }

    private Boolean hasKey(String key, Map<Object, Object> target) {
        if (Objects.isNull(target.get((key)))) {
            return false;
        }

        if (!(target.get(key) instanceof List)) {
            return false;
        }

        return ((List<Object>) target.get(key)).isEmpty() ? false : true;
    }

    private String convertToString(String key, Map<Object, Object> target) {
        if (!hasKey(key, target)) {
            return "";
        }

        return ((List<Object>) target.get(key)).get(0).toString();
    }

    private int convertToInt(String key, Map<Object, Object> target) {
        try {
            return Integer.parseInt(convertToString(key, target));
        } catch (NumberFormatException e) {
            //TODO: ログ
            return 0;
        }
    }
}
