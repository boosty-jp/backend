package jp.boosty.graphql.infrastructure.util;

public class EdgeIdCreator {
    public static String createId(String sourceId, String targetId, String label){
        return sourceId + "-" + label + "-" + targetId;
    }
}
