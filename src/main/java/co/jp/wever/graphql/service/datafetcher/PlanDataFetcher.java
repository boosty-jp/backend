package co.jp.wever.graphql.service.datafetcher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import co.jp.wever.graphql.model.Plan;
import co.jp.wever.graphql.repository.PlanRepository;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;

@Component
public class PlanDataFetcher implements DataFetcher<Plan> {
//    @Autowired
//    PlanRepository planRepository;

    @Override
    public Plan get(DataFetchingEnvironment dataFetchingEnvironment){

        String id = dataFetchingEnvironment.getArgument("id");

        return Plan.builder().id(1L).image("image.png").name("hoge").price(100).description("fuga").publish(false).deleted(true).build();
        //        return new Plan(1L, 100, "", "", "");
    }
}
