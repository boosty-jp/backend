package jp.boosty.graphql.application.controller;

import jp.boosty.graphql.domain.service.GraphQLService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

import graphql.ExecutionInput;
import graphql.ExecutionResult;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@CrossOrigin(origins = "*", methods = {RequestMethod.POST})
@RequestMapping("/")
@RestController
public class ApplicationController {

    @Autowired
    GraphQLService graphQLService;

    @GetMapping
    public ResponseEntity<Object> execute() {
        // ヘルスチェック用のエンドポイント
        return new ResponseEntity<>("", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Object> execute(
        @RequestHeader(value = "AuthorizationToken", required = false) String token, @RequestBody Map body) {
        String queryString = (String) body.get("query");
        Map<String, Object> variables = (Map<String, Object>) body.get("variables");
        if (variables == null) {
            variables = new LinkedHashMap<>();
        }

        ExecutionInput executionInput =
            ExecutionInput.newExecutionInput().query(queryString).variables(variables).context(token).build();

        ExecutionResult execute = graphQLService.getGraphQL().execute(executionInput);

        return new ResponseEntity<>(execute, HttpStatus.OK);
    }
}
