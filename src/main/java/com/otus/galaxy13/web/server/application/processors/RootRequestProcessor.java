package com.otus.galaxy13.web.server.application.processors;

import com.otus.galaxy13.web.server.http.ddo.HTTPRequest;
import com.otus.galaxy13.web.server.http.ddo.Response;
import com.otus.galaxy13.web.server.http.exceptions.HTTPError;

public class RootRequestProcessor extends Processor {
    @Override
    public Response execute(HTTPRequest httpRequest) throws HTTPError {
        super.logger.debug("Start page processor executed");
        if (httpRequest.getRequestType().equals("html")) {
            return new Response(200,
                    "OK",
                    """
                            <html lang="en">
                            <head>
                                <meta charset="UTF-8">
                                <meta http-equiv="X-UA-Compatible" content="IE=edge">
                                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                                <title>REST API Methods</title>
                            </head>
                            <body>
                                <h1>REST API Methods</h1>
                                <ul>
                                    <li>
                                        <h3>GET / - Welcome Page [HTML]</h3>
                                    </li>
                                    <li>
                                        <h3>GET /items - get all items from storage [HTML, JSON]</h3>
                                        <p>Example JSON Response:</p>
                                        <pre>
                                {"uuid": 1, "title": "Item 1", "price":100}
                            </pre>
                                    </li>
                                    <li>
                                        <h3>GET /{filename} - get file content from 'static' folder [HTML, JSON]</h3>
                                        <p>Example JSON Response:</p>
                                        <pre>{"content": "File content goes here"}</pre>
                                    </li>
                                    <li>
                                        <h3>GET /items/{uuid} - get item by uuid</h3>
                                        <p>Example JSON Response:</p>
                                        <pre>{"uuid": 123, "title": "Item Name", "price":99}</pre>
                                    </li>
                                    <li>
                                        <h3>POST /items?price={price}&title={title} or JSON {"title": "title", "price":100}</h3>
                                        <p>Example JSON Response:</p>
                                        <pre>{"uuid": 123, "title": "Item Name", "price":99}</pre>
                                    </li>
                                    <li>
                                        <h3>DELETE /items/{uuid}</h3>
                                        <p>Example JSON Response:</p>
                                        <pre>{"uuid": 123, "title": "Item Name", "price":99}</pre>
                                    </li>
                                    <li>
                                        <h3>PUT /items?price={price}&title={title} or JSON {"title": "title", "price":99} (1 parameter could be passed)</h3>
                                        <p>Example JSON Response:</p>
                                        <pre>{"uuid": 123, "title": "Item Name", "price":99}</pre>
                                    </li>
                                </ul>
                            </body>
                            </html>
                                    \s""");
        } else {
            return new Response(200, "OK", """
                    {"message": "Use HTML to view docs"}""");
        }
    }
}
