package com.function;

import com.azure.messaging.servicebus.ServiceBusClientBuilder;
import com.azure.messaging.servicebus.ServiceBusMessage;
import com.azure.messaging.servicebus.ServiceBusSenderClient;
import com.microsoft.azure.functions.ExecutionContext;
import com.microsoft.azure.functions.HttpMethod;
import com.microsoft.azure.functions.HttpRequestMessage;
import com.microsoft.azure.functions.HttpResponseMessage;
import com.microsoft.azure.functions.HttpStatus;
import com.microsoft.azure.functions.annotation.AuthorizationLevel;
import com.microsoft.azure.functions.annotation.FunctionName;
import com.microsoft.azure.functions.annotation.HttpTrigger;
import com.microsoft.azure.functions.annotation.ServiceBusQueueOutput;
import com.microsoft.azure.functions.annotation.ServiceBusQueueTrigger;

import java.util.Map;
import java.util.Optional;
import java.util.Map.Entry;

/**
 * Azure Functions with HTTP Trigger.
 */
public class Function {

    @FunctionName("sbprocessor")
    public void sbprocessor(
        @ServiceBusQueueTrigger(name = "msg",
        queueName = "dev.audryus.test.fila.one/$DeadLetterQueue", 
        connection = "myconnvarname") String message,
        final ExecutionContext context) {

            String getenv = System.getenv("CUSTOMCONNSTR_myconnvarname");

            ServiceBusSenderClient senderClient = new ServiceBusClientBuilder()
                .connectionString(getenv)
                .sender()
                .queueName("dev.audryus.test.fila.one")
                .buildClient();

            senderClient.sendMessage(new ServiceBusMessage(message));

    }


    /**
     * This function listens at endpoint "/api/HttpExample". Two ways to invoke it using "curl" command in bash:
     * 1. curl -d "HTTP Body" {your host}/api/HttpExample
     * 2. curl "{your host}/api/HttpExample?name=HTTP%20Query"
     */
    @FunctionName("HttpExample2")
    public HttpResponseMessage run(
            @HttpTrigger(
                name = "req",
                methods = {HttpMethod.GET, HttpMethod.POST},
                authLevel = AuthorizationLevel.ANONYMOUS)
                HttpRequestMessage<Optional<String>> request,
            final ExecutionContext context) {
        context.getLogger().info("Java HTTP trigger processed a request.");

        // Parse query parameter
        final String query = request.getQueryParameters().get("name");
        final String name = request.getBody().orElse(query);

        if (name == null) {
            return request.createResponseBuilder(HttpStatus.BAD_REQUEST).body("Please pass a something on the query string or in the request body").build();
        } else {
            return request.createResponseBuilder(HttpStatus.OK).body("Hello, " + name).build();
        }
    }
}
