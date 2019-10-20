package co.jp.wever.graphql.application.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import co.jp.wever.graphql.domain.service.GraphQLService;
import graphql.ExecutionInput;
import graphql.ExecutionResult;

@RequestMapping("/")
@RestController
public class ApplicationController {

    @Autowired
    GraphQLService graphQLService;

    @PostMapping
        public ResponseEntity<Object> getPlans(@RequestHeader(value = "AuthorizationToken", required = false) String token, @RequestBody String query) {

        ExecutionInput executionInput = ExecutionInput.newExecutionInput()
                                                      .query(query)
                                                      .context(token)
                                                      .build();

        ExecutionResult execute = graphQLService.getGraphQL().execute(executionInput);

        return new ResponseEntity<>(execute, HttpStatus.OK);
    }
}
